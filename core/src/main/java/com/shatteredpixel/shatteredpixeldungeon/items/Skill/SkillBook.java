package com.shatteredpixel.shatteredpixeldungeon.items.Skill;


import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.items.Pombbay;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.Nervous;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSunLight;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.TEST;

public class SkillBook extends Item {
    {
        image = ItemSpriteSheet.SKILL_BOOK;
        stackable = false;
        bones = false;
        unique = true;
        defaultAction = AC_ACT;
    }

    private static final String AC_ACT = "ACT";
    public int charge = 30;
    public int chargeCap = 150;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ACT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);


        if (action.equals(AC_ACT)) {

            float spBonus = RingOfSunLight.SPBonus(Dungeon.hero);
            DecimalFormat fmt = new DecimalFormat("#");
            float cost1 = Math.max(5,  30f  / spBonus);
            float cost2 = Math.max(10, 60f  / spBonus);
            float cost3 = Math.max(15, 100f / spBonus);

            GameScene.show(
                    new WndOptions(Messages.get(this, "name"),
                            Messages.get(this, "wnddesc") + infoWnd(),
                            Messages.get(this, "ac_skl1", fmt.format(cost1)),
                            Messages.get(this, "ac_skl2", fmt.format(cost2)),
                            Messages.get(this, "ac_skl3", fmt.format(cost3))) {

                        @Override
                        protected void onSelect(int index) {
                            Skill skill = null;
                            int cost = 0;
                            int minCost = 0;
                            if (index == 0) {
                                skill = hero.SK1;
                                cost = 30;
                                minCost = 5;
                            } else if (index == 1) {
                                skill = hero.SK2;
                                cost = 60;
                                minCost = 10;
                            } else if (index == 2) {
                                skill = hero.SK3;
                                cost = 100;
                                minCost = 15;
                            }
                            if (skill == null) {
                                GLog.w(Messages.get(SkillBook.class, "no_skill"));
                                return;
                            }

                            float chargeDown = cost / (RingOfSunLight.SPBonus(Dungeon.hero));
                            if (chargeDown < minCost) chargeDown = minCost;

                            if (charge < chargeDown && !Dungeon.isChallenged(TEST)) {
                                GLog.w(Messages.get(SkillBook.class, "low_charge"));
                                return;
                            }
                            charge -= chargeDown;
                            updateQuickslot();

                            skill.doSkill();
                            Talent.onSkillUsed(Dungeon.hero);
                        }
                    });
        }
    }

    @Override
    public String info() {
        String info = desc();

        curUser = Dungeon.hero;

        info += "\n\n" + Messages.get(this, "spcharge", Math.round(charge));

        if (curUser.SK1 != null) {
            info += "\n\n" + curUser.SK1.name();
            info += " " + curUser.SK1.desc();
        }

        if (curUser.SK2 != null) {
            info += "\n\n" + curUser.SK2.name();
            info += " " + curUser.SK2.desc();
        }

        if (curUser.SK3 != null) {
            info += "\n\n" + curUser.SK3.name();
            info += " " + curUser.SK3.desc();
        }

        return info;
    }


    public String infoWnd() {
        String infoWnd = "";
        if (curUser.SK1 != null) {
            infoWnd += "\n\n" + curUser.SK1.name();
            infoWnd += " " + curUser.SK1.desc_wnd();
        }

        if (curUser.SK2 != null) {
            infoWnd += "\n\n" + curUser.SK2.name();
            infoWnd += " " + curUser.SK2.desc_wnd();
        }

        if (curUser.SK3 != null) {
            infoWnd += "\n\n" + curUser.SK3.name();
            infoWnd += " " + curUser.SK3.desc_wnd();
        }

        return infoWnd;
    }

    public void onHeroGainExp(float levelPercent, Hero hero) {
        super.onHeroGainExp(levelPercent, hero);

        float chargepur = 10 + (Dungeon.hero.lvl * 6) - 72;
        if (chargepur < 10) chargepur = 10;
        if (chargepur > 100) chargepur = 100;

        if (charge > 100) chargepur /= 2;

        charge += chargepur * levelPercent;
        if (charge > 150) charge = 150;
        updateQuickslot();
    }

    public void SetCharge(int cha)
    {
        charge += cha;
        if (charge > 150) charge = 150;
        updateQuickslot();
    }

    private static final String CHARGE = "charge";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (chargeCap > 0) charge = Math.min(chargeCap, bundle.getInt(CHARGE));
        else charge = bundle.getInt(CHARGE);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String status() {

        //if the artifact isn't IDed, or is cursed, don't display anything
        if (!isIdentified() || cursed) {
            return null;
        }
        //display as percent
        if (chargeCap == 150)
            return Messages.format("%d%%", charge);


        //otherwise, if there's no charge, return null.
        return null;
    }
}
