package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class Endspeaker2Sprite extends MobSprite {

    public Endspeaker2Sprite() {
        super();

        texture( Assets.Sprites.ENDSPEAKER2 );

        TextureFilm frames = new TextureFilm( texture, 44, 30 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7 );

        run = new Animation( 12, true );
        run.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 18, false );
        attack.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7 );

        die = new Animation( 5, false );
        die.frames( frames, 0 );

        play( idle );
    }

}