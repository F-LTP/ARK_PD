package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class Endspeaker4Sprite extends MobSprite {

    public Endspeaker4Sprite() {
        super();

        texture( Assets.Sprites.ENDSPEAKER4 );

        TextureFilm frames = new TextureFilm( texture, 92, 58 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 1, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7, 8, 9 );

        attack = new Animation( 18, false );
        attack.frames( frames, 2, 3, 4, 5, 6, 7, 8, 9 );

        die = new Animation( 5, false );
        die.frames( frames, 0 );

        play( idle );
    }

}
