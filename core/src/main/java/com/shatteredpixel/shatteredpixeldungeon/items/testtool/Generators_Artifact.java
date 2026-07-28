package com.shatteredpixel.shatteredpixeldungeon.items.testtool;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
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
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

public class Generators_Artifact extends Generators {
    {
        image = ItemSpriteSheet.ARTIFACT_HOLDER;
    }
    private int level;
    private int selected;
    private boolean cursed;
    @Override
    public ArrayList<String> actions(Hero hero) {
        return super.actions(hero);
    }
    @Override
    public void execute(Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_GIVE)){
            GameScene.show(new SettingsWindow());
        }
    }

    private static Class<? extends Artifact>[] artifactList(){
        return (Class<? extends Artifact>[]) Generator.Category.ARTIFACT.classes.clone();
    }
    private void createArtifact(){
        Artifact a = Reflection.newInstance(artifactList()[selected]);
        if(a != null){
            a.transferUpgrade(level);
            a.cursed = cursed;
            if(Challenges.isItemBlocked(a)) return;
            a.identify();
            if(a.collect()){
                GLog.i(Messages.get(this, "collect_success", a.name()));
            }else{
                a.doDrop(curUser);
            }
        }
    }
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("selected", selected);
        bundle.put("is_cursed", cursed);
        bundle.put("level", level);
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        selected = bundle.getInt("selected");
        if (selected < 0 || selected >= artifactList().length) {
            selected = 0;
        }
        cursed = bundle.getBoolean("is_cursed");
        level = bundle.getInt("level");
    }

    private class SettingsWindow extends Window {
        private static final int WIDTH = 140;
        private static final int BTN_SIZE = 16;
        private static final int GAP = 2;
        private RenderedTextBlock t_selected;
        private OptionSlider o_level;
        private CheckBox c_curse;
        private RedButton b_create;
        private ArrayList<IconButton> artifactSprites = new ArrayList<>();

        public SettingsWindow(){
            createArtifactImage();
            t_selected = PixelScene.renderTextBlock("", 6);
            t_selected.text();
            add((t_selected));

            o_level = new OptionSlider(Messages.get(this, "level"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    level = getSelectedValue();
                }
            };
            o_level.setSelectedValue(level);
            add(o_level);

            c_curse = new CheckBox(Messages.get(this, "curse")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    cursed = checked();
                }
            };
            c_curse.checked(cursed);
            add(c_curse);

            b_create = new RedButton(Messages.get(this, "create_button")) {
                @Override
                protected void onClick() {
                    createArtifact();
                }
            };
            add(b_create);

            updateText();
        }

        private void layout(){
            t_selected.setPos(0, 3*GAP + BTN_SIZE *2);
            o_level.setRect(0, t_selected.bottom() + GAP, WIDTH, 24);
            c_curse.setRect(0, o_level.bottom() + GAP, WIDTH, 18);
            b_create.setRect(0, c_curse.bottom() + GAP, WIDTH, 16);
            resize(WIDTH, (int)b_create.bottom() + GAP);
        }

        private void createArtifactImage(){
            float left;
            float top = GAP;
            int placed = 0;
            int length = artifactList().length;
            // 根据数量自动计算行列布局
            int cols = length <= 9 ? length : (length + 1) / 2;
            // 更健壮：最大每行9个，超过则换行
            int maxCols = 9;
            int rows = (length + maxCols - 1) / maxCols;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        selected = j;
                        updateText();
                        super.onClick();
                    }
                };
                Image im = new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(artifactList()[i])).image));
                im.scale.set(0.5f);
                btn.icon(im);
                int row = i / maxCols;
                int col = i % maxCols;
                float leftOffset = (WIDTH - maxCols * BTN_SIZE) / 2f;
                btn.setRect(leftOffset + col * BTN_SIZE, top + row * (BTN_SIZE + GAP), BTN_SIZE, BTN_SIZE);
                add(btn);
                placed++;
                artifactSprites.add(btn);
            }
        }

        private void updateText(){
            t_selected.text(Messages.get(Generators_Artifact.class, "selected", Messages.get(artifactList()[selected], "name")));
            layout();
        }
    }
}