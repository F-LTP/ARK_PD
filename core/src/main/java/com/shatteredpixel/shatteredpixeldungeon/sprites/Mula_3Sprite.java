package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class Mula_3Sprite extends MobSprite {

    public Mula_3Sprite() {
        super();

        texture( Assets.Sprites.MULA_TAIL );

<<<<<<<< HEAD:Arrrknights/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/sprites/Tiacauh_fanaticSprite.java
        TextureFilm frames = new TextureFilm( texture, 56, 46 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 1, 0 );

        run = new Animation( 8, true );
        run.frames( frames, 3, 4, 5, 6, 7, 8, 9, 10 );
========
        TextureFilm frames = new TextureFilm( texture, 72, 50 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0 );

        run = new Animation( 10, true );
        run.frames( frames, 0 );
>>>>>>>> upstream/main:core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/sprites/Mula_3Sprite.java

        attack = new Animation( 20, false );
        attack.frames( frames, 3, 4, 5, 6, 7, 8, 9, 10  );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        play( idle );
    }
}