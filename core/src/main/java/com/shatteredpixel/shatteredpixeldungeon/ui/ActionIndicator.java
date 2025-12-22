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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.input.GameAction;
import com.watabou.noosa.Image;

public class ActionIndicator extends Tag {

	Image icon;

	public static Action action;
	public static ActionIndicator instance;

	public ActionIndicator() {
		super( 0xFFFF4C );

		instance = this;

		setSize( SIZE, SIZE );
		visible = false;
	}
	
	@Override
	public GameAction keyAction() {
		return SPDAction.TAG_ACTION;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}
	
	@Override
	protected synchronized void layout() {
		super.layout();
		
		if (icon != null){
            if (!flipped)   icon.x = x + (SIZE - icon.width()) / 2f + 1;
            else            icon.x = x + width - (SIZE + icon.width()) / 2f - 1;
            icon.y = y + (height - icon.height()) / 2f;
			PixelScene.align(icon);
		}
	}
	
	private boolean needsRefresh = false;
	
	@Override
	public synchronized void update() {
        super.update();
        synchronized (ActionIndicator.class) {
            if (!visible && action != null) {
                visible = true;
                needsRefresh = true;
                flash();
            } else {
                visible = action != null;
            }

        if (needsRefresh) {
            if (icon != null) {
                icon.destroy();
                icon.killAndErase();
                icon = null;
            }
            if (action != null) {
                icon = action.getIcon();
                add(icon);
            }

            layout();
            needsRefresh = false;
        }
            if (!Dungeon.hero.ready) {
                if (icon != null) icon.alpha(0.5f);
            } else {
                if (icon != null) icon.alpha(1f);
            }
        }
	}

	@Override
	protected void onClick() {
        super.onClick();
        if (action != null && Dungeon.hero.ready) {
            action.doAction();
        }
	}

	public static void setAction(Action action){
        synchronized (ActionIndicator.class) {
            ActionIndicator.action = action;
            refresh();
        }
	}

    public static void clearAction(){
        clearAction(null);
    }

	public static void clearAction(Action action){
        synchronized (ActionIndicator.class) {
            if (action == null || ActionIndicator.action == action) {
                ActionIndicator.action = null;
            }
        }
	}
    public static void refresh(){
        synchronized (ActionIndicator.class) {
            if (instance != null) {
                instance.needsRefresh = true;
            }
        }
    }

    public interface Action{

		public Image getIcon();

		public void doAction();

	}

}
