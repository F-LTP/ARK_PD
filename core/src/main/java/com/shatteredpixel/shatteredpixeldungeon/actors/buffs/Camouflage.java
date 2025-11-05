package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

public class Camouflage extends Invisibility {
    {
        announced = false;
    }

    @Override
    public boolean attachTo( Char target ) {
        return super.attachTo(target);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public void detach() {
        if (target.invisible > 0)
            target.invisible--;
        super.detach();
    }


    public static void dispelCamouflage() {
        if (Dungeon.level!=null)//change from budding
            for (Mob mob :Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob.buff(Camouflage.class)!=null)
                Buff.detach(mob, Camouflage.class);
        }
    }

    public static void dispelCamouflage(Char c) {
                if (c.buff(Camouflage.class)!=null)
                    Buff.detach(c, Camouflage.class);
    }
    public static boolean CamoFlageEnemy(Char mob) {//change from budding
        boolean det=false;
        Hero h=Dungeon.hero;
        if (!(mob instanceof Hero) && mob.buff(Camouflage.class)==null){
            if (h.buff(Light.class) != null){
                det=true;
            }else if (h.buff(MindVision.class)!=null){
                det=true;
            }else {
                TalismanOfForesight.CharAwareness b=h.buff(TalismanOfForesight.CharAwareness.class);
                if (b!=null &&  b.charID == mob.id()){
                    det=true;
                }
            }
        }
        return !det;
    }
}
