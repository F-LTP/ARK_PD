package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class Endspeaker1Sprite extends MobSprite {

    public Endspeaker1Sprite() {
        super();

        texture( Assets.Sprites.ENDSPEAKER1 );

        TextureFilm frames = new TextureFilm( texture, 48, 26 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7, 8 );

        run = new Animation( 12, true );
        run.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7, 8 );

        attack = new Animation( 12, false );
        attack.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7, 8 );

        die = new Animation( 5, false );
        die.frames( frames, 0 );

        play( idle );
    }

}