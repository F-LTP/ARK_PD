package com.shatteredpixel.shatteredpixeldungeon.items.testtool;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookCamouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookChainHook;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookCrimsonCutter;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookExecutionMode;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookFate;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookFierceGlare;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookFoodPrep;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookHikari;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookHotBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK1.BookLive;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SkillBook;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Annoying;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Displacing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Exhausting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Fragile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Friendly;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Polarized;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Sacrificial;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Wayward;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.contamination;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Dragonkill;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Overeating;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Penetrate;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

public class Generators_Skill extends Generators {
    {
        image = ItemSpriteSheet.SKILL_BOOK;
    }

    private int tier = 1;
    private int id = 0;

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_GIVE)) {
            GameScene.show(new Generators_Skill.SettingsWindow());
        }
    }

    private void createSkill() {
        Item s = Reflection.newInstance(skillList(tier)[id]);
        if (s.collect()) {
            GLog.i(Messages.get(this, "collect_success", s.name()));
        } else {
            s.doDrop(curUser);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("skill_tier", tier);
        bundle.put("skill_id", id);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        tier = bundle.getInt("skill_tier");
        id = bundle.getInt("skill_id");
    }

    private Class<? extends Item>[] skillList(int tier) {
        switch (tier) {
            default:
            case 1:
                return (Class<? extends Item>[]) Generator.Category.SKL_T1.classes.clone();
            case 2:
                return (Class<? extends Item>[]) Generator.Category.SKL_T2.classes.clone();
            case 3:
                return (Class<? extends Item>[]) Generator.Category.SKL_T3.classes.clone();
        }
    }
    private int maxSlots(int t){
        switch (t){
            default:case 1:case 2:return 20;
            case 3:return 10;
        }
    }
    private class SettingsWindow extends Window {

        private static final int WIDTH = 140;
        private static final int BTN_SIZE = 16;
        private static final int GAP = 2;
        private OptionSlider o_tier;
        private OptionSlider o_id;
        private RenderedTextBlock t_infoSkill;
        private Class<? extends Item>[] all;
        private RedButton b_create;
        private void createSkillArray() {
            all = skillList(tier);
        }
        public SettingsWindow() {
            super();
            createSkillArray();
            o_tier = new OptionSlider(Messages.get(this, "tier"), "1", "3", 1, 3) {
                @Override
                protected void onChange() {
                    tier = getSelectedValue();
                    id = Math.min(maxSlots(tier)-1,id);
                    createSkillArray();
                    updateskillText();
                }
            };
            o_tier.setSelectedValue(tier);
            add(o_tier);
            o_tier.setRect(0, GAP, WIDTH, 24);
            //this is executed in layout because the pos of buttom is affected by the whole window.

            t_infoSkill = PixelScene.renderTextBlock("", 6);
            t_infoSkill.text(skillDesc());
            add(t_infoSkill);

            o_id = new OptionSlider(Messages.get(this, "skill_id"), "0", "19", 0, 19) {
                @Override
                protected void onChange() {
                    id = getSelectedValue();
                    id = Math.min(maxSlots(tier)-1,id);
                    updateskillText();
                }
            };
            o_id.setSelectedValue(id);
            add(o_id);

            b_create = new RedButton(Messages.get(this, "create_button")) {
                @Override
                protected void onClick() {
                    createSkill();
                }
            };
            add(b_create);

            layout();
        }
        private void layout() {
            o_tier.setRect(0, GAP, WIDTH, 24);
            t_infoSkill.setPos(0, GAP + o_tier.bottom());
            o_id.setRect(0, GAP + t_infoSkill.bottom(), WIDTH, 24);
            b_create.setRect(WIDTH/2f+GAP/2f, o_id.bottom(), WIDTH/2f - GAP/2f, 16);
            resize(WIDTH, (int) (b_create.bottom() + GAP));
        }
        private void updateskillText() {
            t_infoSkill.text(skillDesc());
            layout();
        }
            private String skillDesc() {
                String desc = "";
                String key = "skill_id_e" + String.valueOf(tier);
                Class<? extends Item> skl = skillList(tier)[id];
                desc += Messages.get(Generators_Skill.class, key, (skl == null ? Messages.get(Generators_Skill.class, "null_skill") : Messages.get(skl,"name")));
                return desc;
            }
        }
    }

