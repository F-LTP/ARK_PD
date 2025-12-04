/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dario;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.ui.Component;

public class WndDario extends Window {

    protected static final int WIDTH_MIN    = 120;
    protected static final int WIDTH_MAX    = 220;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP        = 4;

    public WndDario(Dario dario, String message ) {
        super();

        int width = WIDTH_MIN;

        Component titlebar = new IconTitle( dario.sprite(), Messages.titleCase( dario.name() ));
        titlebar.setRect( 0, 0, width, 0 );
        add(titlebar);

        RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
        text.text( message, width );
        text.setPos( titlebar.left(), titlebar.bottom() + 2*GAP );
        add( text );

        while (PixelScene.landscape()
                && text.bottom() > (PixelScene.MIN_HEIGHT_L - 10)
                && width < WIDTH_MAX){
            width += 20;
            text.maxWidth(width);
        }

        RedButton btnReward = new RedButton( Messages.get(dario, "reward") ) {
            @Override
            protected void onClick() {
                completeQuest(dario);
            }
        };
        btnReward.setRect( 0, text.top() + text.height() + GAP, width, BTN_HEIGHT );
        add( btnReward );

        resize( width, (int)btnReward.bottom() );
    }

    private void completeQuest(Dario dario) {

        hide();

        Dario.Quest.dropReward(dario);
        GLog.p(Messages.get(dario, "success")); // 접촉시 고마워 대사 출력

        dario.destroy(); // 삭제
        dario.sprite.die();

    }
}