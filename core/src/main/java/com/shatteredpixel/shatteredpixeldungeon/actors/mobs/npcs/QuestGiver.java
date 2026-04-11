package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestScroll;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Dobermann_shadowSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class QuestGiver extends NPC {

    {
        spriteClass = Dobermann_shadowSprite.class;
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Property.NPC);
    }

    private boolean questGiven = false;

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact( Char c ) {
        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return super.interact(c);
        }

        if (questGiven || Dungeon.hero.belongings.getItem(QuestScroll.class) != null) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndMessage(Messages.get(QuestGiver.class, "has_quest")));
                }
            });
        } else {
            final QuestScroll scroll = QuestScroll.createRandom();
            String objKey = "offer_" + scroll.objective.name().toLowerCase();
            final String offerText = Messages.get(QuestGiver.class, objKey, scroll.targetValue);

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                            Messages.get(QuestGiver.class, "name"),
                            offerText,
                            Messages.get(QuestGiver.class, "accept"),
                            Messages.get(QuestGiver.class, "decline")
                    ) {
                        @Override
                        protected void onSelect( int index ) {
                            if (index == 0) {
                                questGiven = true;
                                if (scroll.doPickUp(Dungeon.hero)) {
                                    GLog.i(Messages.get(Dungeon.hero, "you_now_have", scroll.name()));
                                } else {
                                    Dungeon.level.drop(scroll, Dungeon.hero.pos).sprite.drop();
                                }
                            }
                        }
                    });
                }
            });
        }

        return true;
    }

    @Override
    protected boolean act() {
        return super.act();
    }

    private static final String QUEST_GIVEN = "quest_given";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(QUEST_GIVEN, questGiven);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        questGiven = bundle.getBoolean(QUEST_GIVEN);
    }

    public static void spawn( Level level, int ppos ) {
        QuestGiver npc = new QuestGiver();
        npc.pos = ppos;
        level.mobs.add(npc);
    }
}