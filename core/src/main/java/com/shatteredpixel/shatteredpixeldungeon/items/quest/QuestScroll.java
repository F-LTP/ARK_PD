package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class QuestScroll extends Item {

    public static final String AC_READ = "READ";

    private static final float TIME_TO_READ = 1f;

    {
        image = ItemSpriteSheet.QUEST_SCROLL;
        unique = true;
        stackable = false;
        defaultAction = AC_READ;
        keptThoughLostInvent = true;
    }

    public enum QuestObjective {
        SLAY_ENEMIES,
        REACH_DEPTH,
        EXPLORE_FLOORS;

        public boolean checkComplete( QuestScroll scroll ) {
            switch (this) {
                case SLAY_ENEMIES:
                    return (Statistics.enemiesSlain - scroll.snapshotValue) >= scroll.targetValue;
                case REACH_DEPTH:
                    return (Statistics.deepestFloor - scroll.snapshotValue) >= scroll.targetValue;
                case EXPLORE_FLOORS:
                    return (countExplored() - scroll.snapshotValue) >= scroll.targetValue;
                default:
                    return false;
            }
        }

        public String progressText( QuestScroll scroll ) {
            int current;
            switch (this) {
                case SLAY_ENEMIES:
                    current = Math.max(0, Statistics.enemiesSlain - scroll.snapshotValue);
                    break;
                case REACH_DEPTH:
                    current = Math.max(0, Statistics.deepestFloor - scroll.snapshotValue);
                    break;
                case EXPLORE_FLOORS:
                    current = Math.max(0, countExplored() - scroll.snapshotValue);
                    break;
                default:
                    current = 0;
            }
            return Math.min(current, scroll.targetValue) + "/" + scroll.targetValue;
        }

        public int rollTarget() {
            switch (this) {
                case SLAY_ENEMIES:
                    return Random.IntRange(10, 20);
                case REACH_DEPTH:
                    int depthCap = Math.max(1, 40 - Statistics.deepestFloor);
                    return Math.min(Random.IntRange(3, 8), depthCap);
                case EXPLORE_FLOORS:
                    int exploreCap = Math.max(1, 40 - countExplored());
                    return Math.min(Random.IntRange(3, 8), exploreCap);
                default:
                    return 10;
            }
        }

        public int snapshotCurrent() {
            switch (this) {
                case SLAY_ENEMIES:
                    return Statistics.enemiesSlain;
                case REACH_DEPTH:
                    return Statistics.deepestFloor;
                case EXPLORE_FLOORS:
                    return countExplored();
                default:
                    return 0;
            }
        }

        private static int countExplored() {
            int count = 0;
            for (int i = 1; i <= 40; i++) {
                if (Statistics.floorsExplored.get(i, 0f) > 0) count++;
            }
            return count;
        }
    }

    public QuestObjective objective;
    public int targetValue;
    public int snapshotValue;
    public int rewardType;
    public boolean completed = false;

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);

        if (action.equals(AC_READ)) {
            if (objective == null) {
                GLog.w(Messages.get(this, "invalid"));
                return;
            }

            if (objective.checkComplete(this)) {
                completed = true;
                GLog.p(Messages.get(this, "complete"));
                grantRewards(hero);
                detach(hero.belongings.backpack);
            } else {
                GLog.i(Messages.get(this, "progress", objective.progressText(this)));
            }

            hero.sprite.operate(hero.pos);
            hero.spendAndNext(TIME_TO_READ);
        }
    }

    private void grantRewards( Hero hero ) {
        int goldAmount = targetValue * 50;
        Gold gold = new Gold(goldAmount);
        if (!gold.doPickUp(hero)) {
            Dungeon.level.drop(gold, hero.pos).sprite.drop();
        }
    }

    @Override
    public String desc() {
        if (objective == null) return Messages.get(this, "desc");

        String objDesc = Messages.get(this, "obj_" + objective.name().toLowerCase());
        String progress = objective.progressText(this);
        return objDesc + "\n\n" + Messages.get(this, "progress_label", progress);
    }

    private static final String OBJECTIVE  = "objective";
    private static final String TARGET     = "target";
    private static final String SNAPSHOT   = "snapshot";
    private static final String REWARD     = "reward";
    private static final String COMPLETED  = "completed";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        if (objective != null) {
            bundle.put(OBJECTIVE, objective.name());
        }
        bundle.put(TARGET, targetValue);
        bundle.put(SNAPSHOT, snapshotValue);
        bundle.put(REWARD, rewardType);
        bundle.put(COMPLETED, completed);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        String objName = bundle.getString(OBJECTIVE);
        if (objName != null && !objName.isEmpty()) {
            try {
                objective = QuestObjective.valueOf(objName);
            } catch (IllegalArgumentException e) {
                objective = null;
            }
        }
        targetValue = bundle.getInt(TARGET);
        snapshotValue = bundle.getInt(SNAPSHOT);
        rewardType = bundle.getInt(REWARD);
        completed = bundle.getBoolean(COMPLETED);
    }

    public static QuestScroll createRandom() {
        QuestScroll scroll = new QuestScroll();
        QuestObjective[] objectives = QuestObjective.values();
        scroll.objective = objectives[Random.Int(objectives.length)];
        scroll.targetValue = scroll.objective.rollTarget();
        scroll.snapshotValue = scroll.objective.snapshotCurrent();
        scroll.rewardType = 0;
        return scroll;
    }
}