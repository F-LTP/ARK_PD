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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class ChallengesList extends ScrollPane {

    private ArrayList<ListItem> items = new ArrayList<>();

    public ChallengesList() {
        super( new Component() );

        for (int i = 0; i < Challenges.NAME_IDS.length; i++) {

            final String challenge = Challenges.NAME_IDS[i];

            ListItem item = new ListItem( challenge, (Dungeon.challenges & Challenges.MASKS[i]) != 0 );
            content.add( item );
            items.add( item );
        }
    }

    @Override
    protected void layout() {

        float pos = 0;

        int size = items.size();
        for (int i=0; i < size; i++) {
            items.get( i ).setRect( 0, pos, width, ListItem.HEIGHT );
            pos += ListItem.HEIGHT;
        }

        content.setSize( width, pos );

        super.layout();
    }

    @Override
    public void onClick( float x, float y ) {
        int size = items.size();
        for (int i=0; i < size; i++) {
            if (items.get( i ).onClick( x, y )) {
                break;
            }
        }
    }

    private class ListItem extends Component {

        private static final int WIDTH		= 120;

        private static final float HEIGHT	= 16;

        private CheckBox checkBox;
        private String challenge;

        public ListItem( String challenge, boolean isChecked ) {
            super();

            this.challenge = challenge;
            checkBox.text(Messages.titleCase(Messages.get(Challenges.class, challenge)));
            checkBox.checked(isChecked);
        }

        @Override
        protected void createChildren() {
            checkBox = new CheckBox("");
            checkBox.setSize(WIDTH - 8, HEIGHT);
            checkBox.checked(false);
            checkBox.active = false;
            add( checkBox );
        }

        @Override
        protected void layout() {
            checkBox.setPos(x, y);
            PixelScene.align(checkBox);
        }

        public boolean onClick( float x, float y ) {
            if (inside( x, y )) {
                Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
                TomorrowRogueNight.scene().add(
                        new WndMessage(Messages.get(Challenges.class, challenge+"_desc"))
                );
                return true;
            } else {
                return false;
            }
        }
    }
}