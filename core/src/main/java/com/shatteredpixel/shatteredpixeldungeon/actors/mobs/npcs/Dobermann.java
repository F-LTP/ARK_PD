package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestScroll;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DobermannSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Dobermann extends NPC {
    {
        spriteClass = DobermannSprite.class;
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Property.NPC);
    }

    private boolean questGiven = false;
    private QuestScroll pendingQuest = null;

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return super.interact(c);
        }

        if (questGiven || Dungeon.hero.belongings.getItem(QuestScroll.class) != null) {
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "has_quest"));
        } else {
            if (pendingQuest == null) {
                pendingQuest = QuestScroll.createRandom();
            }
            final QuestScroll scroll = pendingQuest;
            String objKey = "offer_" + scroll.objective.name().toLowerCase();
            final String offerText = Messages.get(this, objKey, scroll.targetValue);

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                            Messages.get(Dobermann.class, "name"),
                            offerText,
                            Messages.get(Dobermann.class, "accept"),
                            Messages.get(Dobermann.class, "decline")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                questGiven = true;
                                pendingQuest = null;
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

    private static final String QUEST_GIVEN = "quest_given";
    private static final String PENDING_QUEST = "pending_quest";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(QUEST_GIVEN, questGiven);
        if (pendingQuest != null) {
            bundle.put(PENDING_QUEST, pendingQuest);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        questGiven = bundle.getBoolean(QUEST_GIVEN);
        if (bundle.contains(PENDING_QUEST)) {
            pendingQuest = (QuestScroll) bundle.get(PENDING_QUEST);
        }
    }

    public static void spawn(Level level, int ppos) {
        Dobermann perro = new Dobermann();
        perro.pos = ppos;
        level.mobs.add(perro);
    }
}