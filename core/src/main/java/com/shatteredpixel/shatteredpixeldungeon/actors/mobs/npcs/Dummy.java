package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CivilianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClosureSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_dummySprite;

public class Dummy extends Mob {
    {
        spriteClass = CivilianSprite.class;
        HP=HT=1000;

        state = PASSIVE;
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }
    public static boolean kkdy=false;//change from budding
    @Override
    public void damage( int dmg, Object src ){//change from budding
        kkdy=true;//considering more change than just invincibility;protect it from blooming enchantment&battlemage,etc
        super.damage(dmg,src);
        kkdy=false;
    }

    @Override
    protected boolean act() {
        //fix corrupted saves: reset alignment and state if changed by external effects
        alignment = Alignment.ENEMY;
        state = PASSIVE;

        if (Camouflage.CamoFlageEnemy(this)) Buff.affect(this, Camouflage.class, 10f);
        if (buff(Corruption.class) != null) {
            buff(Corruption.class).detach();
        }
        int theheal = 50;
        HP = Math.min(HP+theheal, HT);
        return super.act();
    }

    public static void spawn(Level level, int poss) {
        Dummy dummy = new Dummy();
        dummy.pos = poss;
        level.mobs.add(dummy);
    }

    {
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
        immunities.add(Corruption.class);
        immunities.add(Doom.class);
        immunities.add(Charm.class);
    }
}
