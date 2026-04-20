package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class QuestScroll extends Item {

    public static final String AC_READ = "READ";

    private static final float TIME_TO_READ = 1f;

    private static final int MAX_FLOOR_CAP = 40;

    {
        image = ItemSpriteSheet.QUEST_SCROLL;
        unique = true;
        stackable = false;
        defaultAction = AC_READ;
    }

    public enum QuestObjective {
        SLAY_ENEMIES,
        RANGED_KILLS,
        DESCEND_RATIONLESS,
        OPEN_CHESTS,
        COLLECT_GOLD;

        public int rollTarget() {
            switch (this) {
                case SLAY_ENEMIES:
                    return Random.IntRange(5, 7) * 10;
                case RANGED_KILLS:
                    return Random.IntRange(8, 15);
                case DESCEND_RATIONLESS: {
                    int cap = Math.max(1, MAX_FLOOR_CAP - Statistics.deepestFloor);
                    return Math.min(Random.IntRange(2, 3), cap);
                }
                case OPEN_CHESTS:
                    return Random.IntRange(3, 6);
                case COLLECT_GOLD:
                    return Random.IntRange(30, 80) * 10;
                default:
                    return 10;
            }
        }

        // Gold reward = base * targetValue, scaled by quest difficulty
        public int rewardGold(int targetValue) {
            switch (this) {
                case SLAY_ENEMIES:
                    return targetValue * 7;
                case RANGED_KILLS:
                    return targetValue * 30;
                case DESCEND_RATIONLESS:
                    return targetValue * 150;
                case OPEN_CHESTS:
                    return targetValue * 50;
                case COLLECT_GOLD:
                    return Math.round(targetValue * 0.2f);
                default:
                    return targetValue * 10;
            }
        }
    }

    public QuestObjective objective;
    public int targetValue;
    public int progress;

    // Optional non-gold reward. Null means gold-only. Placeholder for future item rewards.
    public Item bonusReward;

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
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_READ)) {
            if (objective == null) {
                GLog.w(Messages.get(this, "invalid"));
                return;
            }

            if (currentProgress() >= targetValue) {
                completed = true;
                GLog.p(Messages.get(this, "complete"));
                grantRewards(hero);
                detach(hero.belongings.backpack);
            } else {
                GLog.i(Messages.get(this, "progress", progressText()));
            }

            hero.sprite.operate(hero.pos);
            hero.spendAndNext(TIME_TO_READ);
        }
    }

    public int currentProgress() {
        if (objective == null) return 0;
        // DESCEND_RATIONLESS displays progress off-by-one from the internal counter so that eating on the final descended floor still resets progress before it counts.
        if (objective == QuestObjective.DESCEND_RATIONLESS) {
            return Math.max(0, progress - 1);
        }
        return progress;
    }

    public String progressText() {
        return Math.min(currentProgress(), targetValue) + "/" + targetValue;
    }

    private void grantRewards(Hero hero) {
        int goldAmount = objective.rewardGold(targetValue);
        Dungeon.level.drop(new Gold(goldAmount), hero.pos).sprite.drop();
        if (bonusReward != null) {
            Dungeon.level.drop(bonusReward, hero.pos).sprite.drop();
            bonusReward = null;
        }
    }

    public String rewardText() {
        if (objective == null) return "";
        int goldAmount = objective.rewardGold(targetValue);
        if (bonusReward != null) {
            return Messages.get(this, "reward_with_bonus", goldAmount, bonusReward.title());
        }
        return Messages.get(this, "reward_label", goldAmount);
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");

        if (objective == null) return desc;

        String objDesc = Messages.get(this, "obj_" + objective.name().toLowerCase(), targetValue);
        return desc + "\n\n" + objDesc
                + "\n\n" + rewardText()
                + "\n\n" + Messages.get(this, "progress_label", progressText());
    }

    // event hooks
    public static QuestScroll active() {
        if (Dungeon.hero == null || Dungeon.hero.belongings == null) return null;
        //iterate belongings directly so progress keeps tracking even while LostInventory
        //hides the scroll from getItem() after an unblessed ankh resurrect
        for (Item item : Dungeon.hero.belongings) {
            if (item instanceof QuestScroll) return (QuestScroll) item;
        }
        return null;
    }

    public static void onMobKilled(Object cause) {
        QuestScroll q = active();
        if (q == null || q.completed || q.objective == null) return;
        switch (q.objective) {
            case SLAY_ENEMIES:
                if (isHeroKill(cause)) q.progress++;
                break;
            case RANGED_KILLS:
                if (isRangedCause(cause)) q.progress++;
                break;
            default:
        }
    }

    public static void onChestOpened() {
        QuestScroll q = active();
        if (q == null || q.completed || q.objective == null) return;
        if (q.objective == QuestObjective.OPEN_CHESTS) {
            q.progress++;
        }
    }

    public static void onFoodEaten(Item food) {
        QuestScroll q = active();
        if (q == null || q.completed || q.objective == null) return;
        if (q.objective == QuestObjective.DESCEND_RATIONLESS) {
            q.progress = 0;
        }
    }

    public static void onNewFloorReached() {
        QuestScroll q = active();
        if (q == null || q.completed || q.objective == null) return;
        if (q.objective == QuestObjective.DESCEND_RATIONLESS) {
            q.progress++;
        }
    }

    public static void onGoldCollected(int amount) {
        QuestScroll q = active();
        if (q == null || q.completed || q.objective == null) return;
        if (q.objective == QuestObjective.COLLECT_GOLD) {
            q.progress += amount;
        }
    }

    private static boolean isHeroKill(Object cause) {
        return cause == Dungeon.hero
                || cause instanceof Wand
                || cause instanceof MissileWeapon;
    }

    private static boolean isRangedCause(Object cause) {
        if (cause instanceof Wand) return true;
        if (cause == Dungeon.hero
                && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon) {
            return true;
        }
        return false;
    }

    private static final String OBJECTIVE = "objective";
    private static final String TARGET = "target";
    private static final String PROGRESS = "progress";
    private static final String COMPLETED = "completed";
    private static final String BONUS_REWARD = "bonus_reward";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (objective != null) {
            bundle.put(OBJECTIVE, objective.name());
        }
        bundle.put(TARGET, targetValue);
        bundle.put(PROGRESS, progress);
        bundle.put(COMPLETED, completed);
        if (bonusReward != null) {
            bundle.put(BONUS_REWARD, bonusReward);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
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
        progress = bundle.getInt(PROGRESS);
        completed = bundle.getBoolean(COMPLETED);
        if (bundle.contains(BONUS_REWARD)) {
            bonusReward = (Item) bundle.get(BONUS_REWARD);
        }
    }

    public static QuestScroll createRandom() {
        QuestScroll scroll = new QuestScroll();
        QuestObjective[] objectives = QuestObjective.values();
        scroll.objective = objectives[Random.Int(objectives.length)];
        scroll.targetValue = scroll.objective.rollTarget();
        scroll.progress = 0;
        return scroll;
    }
}