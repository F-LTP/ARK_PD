package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.AnnihilationGear;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class ChaosCatalyst extends InventorySpell{
    private static ItemSprite.Glowing COL = new ItemSprite.Glowing( 0xE6E6FA );
    {
        image = ItemSpriteSheet.FORCE_CATALYST;
        mode = WndBag.Mode.UPGRADEABLE;
        stackable = true;
    }

    public ItemSprite.Glowing glowing() {
        return COL;
    }

    @Override
    protected void onItemSelected(Item item) {
        Item result;
        if (item instanceof MeleeWeapon || item instanceof MissileWeapon) {
            result = upWeapon( (Weapon)item );
        }else if(item instanceof Armor){
            result = upArmor((Armor)item);
        }
        else if (item instanceof Ring) {
            result = upRing( (Ring)item );
        } else if (item instanceof Wand) {
            result = upWand( (Wand)item );
        }else if (item instanceof AnnihilationGear){
            result = upGear((AnnihilationGear)item);
        }
        else {
            result = upOther(item);
        }
        if (result == null){
            //This shouldn't ever trigger
            GLog.n( Messages.get(this, "nothing") );
            curItem.collect( curUser.belongings.backpack );
        } else {
            if (item.isEquipped(Dungeon.hero)) {
                boolean record_curse=item.cursed;//TODO new instance instead of record curse
                item.cursed = false; //to allow it to be unequipped
                ((EquipableItem) item).doUnequip(Dungeon.hero, false);
                result.cursed=record_curse;
                ((EquipableItem) result).doEquip(Dungeon.hero);
                Dungeon.hero.spend(-Dungeon.hero.cooldown());
            } else {
                item.detach(Dungeon.hero.belongings.backpack);
                if (!result.collect()) {
                    Dungeon.level.drop(result, curUser.pos).sprite.drop();
                }
            }
        //item.level(1);
        Sample.INSTANCE.play(Assets.Sounds.EVOKE);
        //Dungeon.hero.spendAndNext(1f);
        updateQuickslot();
        }
    }
    private  Weapon upWeapon(Weapon w){
        Weapon n= (Weapon) Generator.random( Generator.Category.WEAPON );
        n.enchantment = w.enchantment;
        n.curseInfusionBonus = w.curseInfusionBonus;
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        n.augment = w.augment;
        int deal=1-w.level();
        if (deal>0) {
            w.upgrade(deal);
        }
        else if (deal<0){
            w.degrade(-deal);
        }
        w.enchantment = n.enchantment;
        w.curseInfusionBonus = n.curseInfusionBonus;
        w.levelKnown = n.levelKnown;
        w.cursedKnown = n.cursedKnown;
        w.cursed = n.cursed;
        w.augment = n.augment;
        return w;
    }
    private  Armor upArmor(Armor w){
        Armor n= new Armor(w.tier);
        boolean seal_exist=(w.checkSeal() != null);
        int b_level=0;
        if (seal_exist){
                b_level=w.checkSeal().level();
            }
        n.glyph = w.glyph;
        n.curseInfusionBonus = w.curseInfusionBonus;
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        n.augment = w.augment;
        int deal=1-w.level();
        if (deal>0) {
            w.upgrade(deal);
        }
        else if (deal<0){
            w.degrade(-deal);
        }
        w.glyph = n.glyph;
        w.curseInfusionBonus = n.curseInfusionBonus;
        w.levelKnown = n.levelKnown;
        w.cursedKnown = n.cursedKnown;
        w.cursed = n.cursed;
        w.augment = n.augment;
        if (seal_exist)
        w.checkSeal().level(b_level);
        return w;
    }
    private Ring upRing(Ring w){
        Ring n= new Ring();
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        int deal=1-w.level();
        if (deal>0) {
            w.upgrade(deal);
        }
        else if (deal<0){
            w.degrade(-deal);
        }
        w.levelKnown = n.levelKnown;
        w.cursedKnown = n.cursedKnown;
        w.cursed = n.cursed;
        return w;
    }
    private Wand upWand(Wand w){
        Wand n= (Wand) Generator.random( Generator.Category.WAND );
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        n.curseInfusionBonus = w.curseInfusionBonus;
        int deal=1-w.level();
        if (deal>0) {
            w.upgrade(deal);
        }
        else if (deal<0){
            w.degrade(-deal);
        }
        w.levelKnown = n.levelKnown;
        w.cursedKnown = n.cursedKnown;
        w.cursed = n.cursed;
        w.curseInfusionBonus = n.curseInfusionBonus;
        return w;
    }
    private  AnnihilationGear upGear(AnnihilationGear w){
        AnnihilationGear n= new AnnihilationGear();
        n.arts = w.arts;
        //n.curseInfusionBonus = w.curseInfusionBonus;
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        int deal=1-w.level();
        if (deal>0) {
            w.upgrade(deal);
        }
        else if (deal<0){
            w.degrade(-deal);
        }
        w.arts = n.arts;
        //w.curseInfusionBonus = n.curseInfusionBonus;
        w.levelKnown = n.levelKnown;
        w.cursedKnown = n.cursedKnown;
        w.cursed = n.cursed;
        return w;
    }
    private Item upOther(Item w){
        Item n =new Item();
        n.levelKnown = w.levelKnown;
        n.cursedKnown = w.cursedKnown;
        n.cursed = w.cursed;
        int deal=1-w.level();
        if (deal>0) {
            w.upgrade(deal);
        }
        else if (deal<0){
            w.degrade(-deal);
        }
        w.levelKnown = n.levelKnown;
        w.cursedKnown = n.cursedKnown;
        w.cursed = n.cursed;
        return w;
    }
    @Override
    public int value() {
        //prices of ingredients, divided by output quantity
        return quantity * 25;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ForceCatalyst.class};
            inQuantity = new int[]{2};

            cost = 20;

            output = ChaosCatalyst.class;
            outQuantity = 1;
        }

    }
}
