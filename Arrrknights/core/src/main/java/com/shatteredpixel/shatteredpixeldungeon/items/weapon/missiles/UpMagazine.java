package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class UpMagazine extends MissileWeapon {
    {
        image = ItemSpriteSheet.STONE_HOLDER;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1f;

        tier = 4;
        baseUses = 1;
    }

    @Override
    public int value() {
        return super.value() / 2; //half normal value
    }
}
