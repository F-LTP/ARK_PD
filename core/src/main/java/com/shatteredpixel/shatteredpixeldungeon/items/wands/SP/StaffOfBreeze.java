package com.shatteredpixel.shatteredpixeldungeon.items.wands.SP;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HealingGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.CorrosionParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StaffOfBreeze extends Wand {
    public static final String AC_SWITCH = "SWITCH";
    private static ItemSprite.Glowing COL = new ItemSprite.Glowing( 0xFFEFD5 );
    private static ItemSprite.Glowing COL_ALT = new ItemSprite.Glowing( 0x7B9E2A );
    {
        image = ItemSpriteSheet.WAND_CORROSION;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
    }

    private boolean altMode = false;

    @Override
    public ItemSprite.Glowing glowing() {
        return altMode ? COL_ALT : COL;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SWITCH);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_SWITCH)) {
            altMode = !altMode;
            updateQuickslot();
        }
    }

    @Override
    protected void onZap(Ballistica bolt) {
        if (altMode) {
            onZapCorrosion(bolt);
        } else {
            onZapHealing(bolt);
        }
    }

    private void onZapHealing(Ballistica bolt) {
        HealingGas gas = Blob.seed(bolt.collisionPos, 50 + 10 * (curCharges * 2), HealingGas.class);
        CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.HEALING), 10 );
        gas.setStrength(1);
        GameScene.add(gas);
        Sample.INSTANCE.play(Assets.Sounds.GAS);
        curCharges = 1;

        for (int i : PathFinder.NEIGHBOURS9) {
            Char ch = Actor.findChar(bolt.collisionPos + i);
            if (ch != null) {
                processSoulMark(ch, chargesPerCast());
            }
        }

        if (Actor.findChar(bolt.collisionPos) == null){
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    private void onZapCorrosion(Ballistica bolt) {
        //slightly less effective than WandOfCorrosion
        //volume: 40 + 8*lvl (vs 50 + 10*lvl), strength: 1 + lvl (vs 2 + lvl)
        CorrosiveGas gas = Blob.seed(bolt.collisionPos, 40 + 8 * buffedLvl(), CorrosiveGas.class);
        CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.CORROSION), 10 );
        gas.setStrength(1 + buffedLvl());
        GameScene.add(gas);
        Sample.INSTANCE.play(Assets.Sounds.GAS);

        for (int i : PathFinder.NEIGHBOURS9) {
            Char ch = Actor.findChar(bolt.collisionPos + i);
            if (ch != null) {
                processSoulMark(ch, chargesPerCast());
            }
        }

        if (Actor.findChar(bolt.collisionPos) == null){
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(
                curUser.sprite.parent,
                MagicMissile.CORROSION,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        if (altMode) {
            // slightly lower proc than WandOfCorrosion: +4 instead of +3
            // lvl 0 - 25%, lvl 1 - 40%, lvl 2 - 50%
            if (Random.Int( buffedLvl() + 4 ) >= 3) {
                Buff.affect( defender, Ooze.class ).set( Ooze.DURATION );
                CellEmitter.center(defender.pos).burst( CorrosionParticle.SPLASH, 5 );
            }
        }
    }

    protected int initialCharges() {
        return 1;
    }

    @Override
    public String status() {
        String charge = super.status();
        String mode = altMode ? "C" : "H";
        return mode + charge;
    }

    @Override
    public String statsDesc() {
        if (altMode) {
            if (levelKnown)
                return Messages.get(this, "stats_desc_alt", 1 + buffedLvl());
            else
                return Messages.get(this, "stats_desc_alt", 1);
        } else {
            return Messages.get(this, "stats_desc");
        }
    }

    private static final String ALT_MODE = "altMode";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ALT_MODE, altMode);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        altMode = bundle.getBoolean(ALT_MODE);
    }
}
