package com.shatteredpixel.shatteredpixeldungeon.items.testtool;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class MyOrder extends ChallengeItem {
    {
        image = ItemSpriteSheet.ROT_DART;
        defaultAction=AC_CHARM;
    }
    private static final String AC_CHARM = "charm";
    private static final String AC_ATTACK = "attack";

    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_CHARM );
        actions.add(AC_ATTACK);
        return actions;
    }
    private int nowID=-1;

    private static final String NOWID		= "nowid";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(NOWID,nowID);
    }
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(NOWID)){
            nowID=bundle.getInt(NOWID);
        }else nowID=-1;
    }
    private CellSelector.Listener selectorCharm = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if(cell == null) return;
            Char ch = Actor.findChar(cell);
            if(ch == null){
                GLog.w(Messages.get(MyOrder.class, "no_char"));
            }else{
                nowID=ch.id();
                ch.alignment= Char.Alignment.ALLY;
                Buff.append(ch,toCharm.class);
                defaultAction=AC_ATTACK;
                CellEmitter.center(cell).start( Speck.factory( Speck.HEART ), 0.2f, 5 );
            }
        }

        @Override
        public String prompt() {
            return M.L(MyOrder.class, "selectcharm");
        }
    };
    private CellSelector.Listener selectorAttack = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if(cell == null) return;
            Char ch = Actor.findChar(cell);
            if(ch == null || ch.alignment== Char.Alignment.ALLY){
                GLog.w(Messages.get(MyOrder.class, "no_char"));
            }else{
                Char ordered=nowControl();
                if (ordered==null){
                    GLog.w(Messages.get(MyOrder.class,"no_control"));
                    return;
                }
                ((Mob)ordered).aggro(ch);
                ((Mob) ordered).beckon(ch.pos);
                CellEmitter.center(cell).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
                CellEmitter.center(ordered.pos).start( Speck.factory( Speck.BUBBLE ), 0.3f, 5 );
                if (ordered.buff(toAttack.class)!=null) Buff.detach(ordered,toAttack.class);
                Buff.append(ordered,toAttack.class).set(ch.id());
            }
        }

        @Override
        public String prompt() {
            return M.L(MyOrder.class, "attack");
        }
    };
    public void execute(Hero hero, String action ) {
        super.execute(hero,action);
        if (action.equals(AC_CHARM)){
            GameScene.selectCell(selectorCharm);
        }else if (action.equals(AC_ATTACK)){
            GameScene.selectCell(selectorAttack);
        }
    }
    @Override
    public String info(){
        String d=this.desc();
        Char ch=nowControl();
        if (ch!=null)
        {
            d+="\n";
            d+=Messages.get(this,"control",(Messages.get(ch.getClass(),"name")));
        }
        return d;
    }
    public Char nowControl(){
        return (Char)Actor.findById(nowID);
    }
    public static class toCharm extends Buff{

        @Override
        public boolean attachTo(Char target) {
            target.alignment= Char.Alignment.ALLY;

            return super.attachTo(target);
        }
        @Override
        public boolean act(){
            CellEmitter.center(target.pos).start( Speck.factory( Speck.HEART ), 0.2f, 5 );
            spend(TICK);
            return true;
        }
    }
    public static class toAttack extends Buff{
        int myTargetID;

        private static final String CHAR_ID = "char_id";
        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            myTargetID = bundle.getInt(CHAR_ID);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(CHAR_ID, myTargetID);
        }
        @Override
        public boolean act(){
            Char ch=(Char)Actor.findById(myTargetID);
            if (ch==null) Buff.detach(target,toAttack.class);
            else {
                ((Mob)target).aggro(ch);
                ((Mob)target).beckon(ch.pos);
                spend(TICK);
            }
            return true;
        }
        public void set(int id){
            myTargetID=id;
        }
    }
}
