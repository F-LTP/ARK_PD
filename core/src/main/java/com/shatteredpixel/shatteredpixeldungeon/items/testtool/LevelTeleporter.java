package com.shatteredpixel.shatteredpixeldungeon.items.testtool;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;

import java.util.ArrayList;

public class LevelTeleporter extends ChallengeItem{
    {
        image = ItemSpriteSheet.ARTIFACT_TALISMAN;
        defaultAction = AC_DESCEND;
        changeDefAct = true;
    }
    private static final String AC_DESCEND = "descend";
    private static final String AC_ASCEND = "ascend";
    private static final String AC_VIEW = "view";
    private static final String AC_TP = "tp";
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_ASCEND );
        actions.add(AC_DESCEND);
        actions.add(AC_VIEW);
        actions.add(AC_TP);
        return actions;
    }
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);
        if(action.equals(AC_DESCEND)){
            if(Dungeon.level.locked || Dungeon.depth==26 || Dungeon.depth==40) {
            GLog.w(Messages.get(this,"cannot_send"));
            return;
        }
            Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();
            buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (buff != null) buff.detach();

            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene( InterlevelScene.class );
            }
        else if(action.equals(AC_ASCEND)){
            if(Dungeon.level.locked) {
                GLog.w(Messages.get(this,"cannot_send"));
                return;
            }
            Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();
            buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (buff != null) buff.detach();
            if(Dungeon.depth==1)
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND_27;
            else if (Dungeon.depth==27)
                InterlevelScene.mode = InterlevelScene.Mode.ASCEND_27;
            else InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
            InterlevelScene.returnPos = -1;
            Game.switchScene( InterlevelScene.class );
        } else if(action.equals(AC_VIEW)){
            Buff.affect( hero, MindVision.class, MindVision.DURATION );
            Dungeon.observe();
            ScrollOfMagicMapping som = new ScrollOfMagicMapping();
            som.doRead();
        }else if(action.equals(AC_TP)){
            empoweredRead();
        }
        }

    public void empoweredRead() {

        GameScene.selectCell(new CellSelector.Listener() {
            @Override
            public void onSelect(Integer target) {
                if (target != null) {
                    //time isn't spent
                    ((HeroSprite)curUser.sprite).read();
                    teleportToLocation(curUser, target);

                }
            }

            @Override
            public String prompt() {
                return Messages.get(ScrollOfTeleportation.class, "prompt");
            }
        });
    }

    public static void teleportToLocation(Hero hero, int pos){
        if (Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                || Actor.findChar(pos) != null){
            GLog.w( Messages.get(ScrollOfTeleportation.class, "cant_reach") );
            return;
        }

        appear( hero, pos );
        Dungeon.level.occupyCell(hero );
        Dungeon.observe();
        GameScene.updateFog();

    }

    public static void appear(Char ch, int pos ) {

        ch.sprite.interruptMotion();

        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[ch.pos]){
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        }

        ch.move( pos );
        if (ch.pos == pos) ch.sprite.place( pos );

        if (ch.invisible == 0) {
            ch.sprite.alpha( 0 );
            ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
        }

        if (Dungeon.level.heroFOV[pos] || ch == Dungeon.hero ) {
            ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
        }
    }
    }
