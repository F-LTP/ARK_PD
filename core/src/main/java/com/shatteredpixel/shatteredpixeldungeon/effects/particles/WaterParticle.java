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

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class WaterParticle extends PixelParticle {

    public static final Factory FALLING = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((WaterParticle)emitter.recycle( WaterParticle.class )).resetFalling( x,  y );
        }
    };

    public static final Factory SPLASHING = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((WaterParticle)emitter.recycle( WaterParticle.class )).resetSplashing( x,  y );
        }
    };

    public WaterParticle() {
        super();

        color( ColorMath.random( 0x064ea7, 0x021d3e ) );
        angle = Random.Float( -30, 30 );
    }

    public void reset( float x, float y ) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan = 0.5f;
        size = 16;
    }

    public void resetFalling( float x, float y ) {
        reset(x, y);

        left = lifespan = 1f;
        size = 8;

        acc.y = 30;
        speed.y = -5;
        angularSpeed = Random.Float(-90, 90);
    }

    public void resetSplashing( float x, float y ) {
        reset(x, y);

        angularSpeed = Random.Float(-90, 90);
        size = 24;
        acc.y = 30;
        speed.y = 32;
        this.y -= speed.y * lifespan;
    }

    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        size( (p < 0.5f ? p : 1 - p) * size );
    }
}