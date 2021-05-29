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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class AirborneSprite extends MobSprite {

    public AirborneSprite() {
        super();

        texture( Assets.Sprites.AIRBORNE );

        TextureFilm frames = new TextureFilm( texture, 34, 32 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0 );

        run = new Animation( 13, true );
        run.frames( frames, 1, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 12, false );
        attack.frames( frames, 1, 2, 3, 4, 5, 6, 7 );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        play( idle );
    }
}