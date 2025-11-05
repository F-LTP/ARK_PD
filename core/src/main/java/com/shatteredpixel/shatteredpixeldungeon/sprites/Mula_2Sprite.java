package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class Mula_2Sprite extends MobSprite {

    public Mula_2Sprite() {
        super();

        texture( Assets.Sprites.MULA_BODY );

<<<<<<<< HEAD:Arrrknights/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/sprites/Tiacauh_lancerSprite.java
        TextureFilm frames = new TextureFilm( texture, 56, 46 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 1, 0 );

        run = new Animation( 8, true );
        run.frames( frames, 3, 4, 5, 6, 7, 8, 9, 10 );
========
        TextureFilm frames = new TextureFilm( texture, 92, 60 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0);

        run = new Animation( 10, true );
        run.frames( frames, 0 );
>>>>>>>> upstream/main:core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/sprites/Mula_2Sprite.java

        attack = new Animation( 20, false );
        attack.frames( frames, 3, 4, 5, 6, 7, 8, 9, 10  );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        play( idle );
    }
}