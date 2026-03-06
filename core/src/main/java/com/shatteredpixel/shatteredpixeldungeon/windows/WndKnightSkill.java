package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.KnightSKILL;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.KnightSkillCombo;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndKnightSkill extends Window {
    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 160;

    private static final int MARGIN  = 2;

    public WndKnightSkill( KnightSKILL combo ){
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        float pos = MARGIN;
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title")), 8);
        KnightSkillCombo counter = Dungeon.hero.buff(KnightSkillCombo.class);
        int stacks = (int) (counter != null ? counter.count() : 0);
        RenderedTextBlock stackText = PixelScene.renderTextBlock(Messages.get(this, "stack", stacks), 7);

        title.hardlight(TITLE_COLOR);
        title.setPos((width-title.width()-stackText.width())/2, pos);
        title.maxWidth(width - MARGIN * 2);
        add(title);

        stackText.hardlight(stacks == 10 ? 0xFFCC00 : 0xCCCCCC);
        stackText.setPos(title.right() + MARGIN, pos);

        add(stackText);

        pos = stackText.bottom() + 3*MARGIN;

        Image icon;
        icon = Icons.get(Icons.COMBO);

        for (KnightSKILL.ComboMove move : KnightSKILL.ComboMove.values()) {
            Image ic = new Image(icon);

            RedButton moveBtn = new RedButton(move.desc(), 6){
                @Override
                protected void onClick() {
                    super.onClick();
                    hide();
                    combo.useMove(move);
                }
            };
            ic.tint(move.tintColor);
            moveBtn.icon(ic);
            moveBtn.multiline = true;
            moveBtn.setSize(width, moveBtn.reqHeight());
            moveBtn.setRect(0, pos, width, moveBtn.reqHeight());
            moveBtn.enable(combo.canUseMove(move));
            add(moveBtn);
            pos = moveBtn.bottom() + MARGIN;
        }

        resize(width, (int)pos);

    }
}