package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class C1_9mm extends GunWeapon {
    {
        image = ItemSpriteSheet.C1;
        hitSound = Assets.Sounds.HIT_GUN;
        hitSoundPitch = 0.9f;


        FIRE_DELAY_MULT = 0.66f;
        bulletMax = 34;
        MIN_RANGE = 1;
        MAX_RANGE = 4;

        usesTargeting = true;

        defaultAction = AC_ZAP;

        tier = 3;
    }

    @Override
    public float getFireAcc(int from, int to) {
        int distance = getDistance(from, to);

        // 최대 사거리 7. 유효 사거리 1-4, 보정 없음.
        if (isWithinRange(distance)) {
            return 1f;
        } else if (distance > getMaxRange()) {
            return Math.max(0f, 1f - 0.25f * (distance - getMaxRange()));
        }

        return 1f;
    }
    @Override
    protected void specialFire(Char ch) {
        Buff.affect(ch, Slow.class, 2f);
    }
}
