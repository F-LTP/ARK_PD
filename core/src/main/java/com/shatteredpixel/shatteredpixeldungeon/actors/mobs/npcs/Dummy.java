package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
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
        //properties.add(Property.IMMOVABLE);

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
        if (Camouflage.CamoFlageEnemy(this)) Buff.affect(this, Camouflage.class, 10f);
        int theheal = 50;
        HP = Math.min(HP+theheal, HT);
        return super.act();
    }

    public static void spawn(Level level, int poss) {
        Dummy WhatYourName = new Dummy();
        do {
            WhatYourName.pos = poss;
        } while (WhatYourName.pos == -1);
        level.mobs.add(WhatYourName);
    }

    {
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
        immunities.add(Corruption.class);//change from budding
    }
}
