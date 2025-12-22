package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Sig553 extends GunWeapon {
    {
        image = ItemSpriteSheet.SIG553;
        hitSound = Assets.Sounds.HIT_GUN;
        hitSoundPitch = 0.9f;

        FIRE_DELAY_MULT = 0.75f;

        bulletMax = 31;
        MIN_RANGE = 2;
        MAX_RANGE = 6;

        usesTargeting = true;

        defaultAction = AC_ZAP;

        tier = 3;
    }

    @Override
    public int fireMax() {
        return (int) 2
                + tier * 2
                + bulletTier * 3
                + level() * tier
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) * 2;
    }

    @Override
    public float getFireAcc(int from, int to) {
        int distance = getDistance(from, to);

        // 최대 사거리 10, 최소사거리 2, 유효 사거리 2-6, +50% 보정
        if (isWithinRange(distance)) {
            return 1.5f;
        } else if (distance > getMaxRange()) {
            return Math.max(0f, 1f - 0.2f * (distance - getMaxRange()));
        } else if (distance < getMinRange()) {
            return 0.67f;
        }
        return 1f;
    }
    @Override
    protected void specialFire(Char ch) {
        Buff.affect(ch, Silence.class, 2f);
    }
}
