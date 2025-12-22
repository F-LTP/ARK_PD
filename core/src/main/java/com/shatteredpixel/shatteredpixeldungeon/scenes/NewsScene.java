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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.RectF;

public class NewsScene extends PixelScene {

	boolean displayingNoArticles = false;

	private static final int BTN_HEIGHT = 44;
	private static final int BTN_WIDTH = 100;

	@Override
	public void create() {
		super.create();

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

        RectF insets = getCommonInsets();

        w -= insets.left + insets.right;
        h -= insets.top + insets.bottom;

		int fullWidth = PixelScene.landscape() ? 202 : 100;
        int left = (int)insets.left + (w - fullWidth)/2;

		Archs archs = new Archs();
		archs.setSize(w, h);
		add(archs);

		ExitButton btnExit = new ExitButton();
        btnExit.setPos(insets.left + w - btnExit.width(), insets.top);
		add(btnExit);

		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
                insets.left + (w - title.width()) / 2f,
                insets.top + (20 - title.height()) / 2f
		);
		align(title);
		add(title);

		float top = 18+ insets.top;

		StyledButton btnSite = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "read_more1")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://www.pixiv.net/users/14086167";
				//tracking codes, so that the website knows where this pageview came from
				DeviceCompat.openURI(link);
			}
		};
		StyledButton btnSite2 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "read_more2")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://github.com/tomorrows-ark-pd/ARK_PD/";
				//tracking codes, so that the website knows where this pageview came from
				DeviceCompat.openURI(link);
			}
		};
		StyledButton btnSite3 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "read_more3")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://docs.google.com/forms/d/19Lpo9rxfD3MNvfFKqYoRQhi-3HPxcitiCZDvkI2aokA/edit";
				//tracking codes, so that the website knows where this pageview came from
				DeviceCompat.openURI(link);
			}
		};
		btnSite.icon(Icons.get(Icons.NAMSEK));
		btnSite.textColor(Window.TITLE_COLOR);
        btnSite.setRect(left, top + 4, fullWidth, BTN_HEIGHT);
		add(btnSite);

		btnSite2.icon(Icons.get(Icons.DANWOO));
		btnSite2.textColor(Window.TITLE_COLOR);
        btnSite2.setRect(left, btnSite.bottom() + 4, fullWidth, BTN_HEIGHT);
		add(btnSite2);

		btnSite3.textColor(Window.TITLE_COLOR);
        btnSite3.setRect(left, btnSite2.bottom() + 4, fullWidth, BTN_HEIGHT);
		add(btnSite3);

	}

	@Override
	protected void onBackPressed() {
		TomorrowRogueNight.switchNoFade( TitleScene.class );
	}

	@Override
	public void update() {
		if (displayingNoArticles && News.articlesAvailable()){
			TomorrowRogueNight.seamlessResetScene();
		}
		super.update();
	}

}
