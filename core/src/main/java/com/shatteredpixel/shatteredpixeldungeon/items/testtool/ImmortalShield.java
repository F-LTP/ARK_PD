package com.shatteredpixel.shatteredpixeldungeon.items.testtool;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
//import com.shatteredpixel.shatteredpixeldungeon.Badges;//for sixfa
import java.util.ArrayList;



public class ImmortalShield extends ChallengeItem
{
    {
        image = ItemSpriteSheet.ARTIFACT_CHALICE3;
        defaultAction = AC_SWITCH;
    }
    private static final String AC_SWITCH = "switch";
    //private static boolean sixfa = false;
    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SWITCH);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);
        if(action.equals(AC_SWITCH)){
            /*if (!sixfa){
                sixfa =true;
                Badges.validateskadiskin();
                Badges.validategrnskin();
                Badges.validatejessiskin();
                Badges.validatelappyskin();
                Badges.validatetaluskin();
                Badges.validatenovaskin();
                Badges.validatesusuuskin();
                Badges.validateleafskin();
                Badges.validateRockskin();
                Badges.validateAstesiaskin();
                Badges.validatesameskin();
                Badges.validateschwazrskin();
                Badges.validatearchskin();
                Badges.validatetomimiskin();
                Badges.validatefrankaskin();
            }*/
            if(isImmortal(hero)){
                Buff.detach(hero, ImmortalShielded.class);
            }else{
                Buff.affect(hero, ImmortalShielded.class);
            }
        }
    }

    public static boolean isImmortal(Char target){
        return target.buff(ImmortalShielded.class)!=null;
    }

    public static class ImmortalShielded extends Buff{
        {
            type = buffType.NEUTRAL;
            announced = false;
        }

        @Override
        public boolean act(){
            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.SHIELDED);
            else target.sprite.remove(CharSprite.State.SHIELDED);
        }
    }
}
