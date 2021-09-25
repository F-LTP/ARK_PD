package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ArmorUpKit extends Item {
    private static final String AC_UPGRADE = "UPGRADE";

    {
        image = ItemSpriteSheet.KIT;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_UPGRADE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_UPGRADE)) {
            curUser = hero;
            ArmorUpgard(hero);
            this.detach(hero.belongings.backpack);
        }
    }

    public void ArmorUpgard(Hero hero) {
        if (hero.belongings.armor != null) {
            Armor HeroArmor = hero.belongings.armor;

            if (HeroArmor.tier < 3) {
                ScaleArmor armor = new ScaleArmor();
                int level = HeroArmor.level();
                if (HeroArmor.curseInfusionBonus) level--;
                if (level > 0) {
                    armor.upgrade( level );
                } else if (level < 0) {
                    armor.degrade( -level );
                }

                armor.glyph = HeroArmor.glyph;
                armor.curseInfusionBonus = HeroArmor.curseInfusionBonus;
                armor.levelKnown = HeroArmor.levelKnown;
                armor.cursedKnown = HeroArmor.cursedKnown;
                armor.cursed = HeroArmor.cursed;
                armor.augment = HeroArmor.augment;

                Dungeon.hero.belongings.armor = armor;
                Dungeon.hero.belongings.armor.upgrade();
            }
            else if (HeroArmor.tier < 5) {
                PlateArmor armor = new PlateArmor();
                int level = HeroArmor.level();
                if (HeroArmor.curseInfusionBonus) level--;
                if (level > 0) {
                    armor.upgrade( level );
                } else if (level < 0) {
                    armor.degrade( -level );
                }

                armor.glyph = HeroArmor.glyph;
                armor.curseInfusionBonus = HeroArmor.curseInfusionBonus;
                armor.levelKnown = HeroArmor.levelKnown;
                armor.cursedKnown = HeroArmor.cursedKnown;
                armor.cursed = HeroArmor.cursed;
                armor.augment = HeroArmor.augment;

                Dungeon.hero.belongings.armor = armor;
                Dungeon.hero.belongings.armor.upgrade();
            }
            else Dungeon.hero.belongings.armor.upgrade();
            curUser.sprite.operate( curUser.pos );
            Sample.INSTANCE.play( Assets.Sounds.EVOKE );
        }
        else {
            new ArmorUpKit().collect();
        }
    }


    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }
}
