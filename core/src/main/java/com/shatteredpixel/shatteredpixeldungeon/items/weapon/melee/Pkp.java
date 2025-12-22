package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Pkp extends GunWeapon {
    {
        image = ItemSpriteSheet.PKP;
        hitSound = Assets.Sounds.HIT_AR;
        hitSoundPitch = 0.9f;

        FIRE_DELAY_MULT = 0.5f;

        bulletMax = 101;
        MIN_RANGE = 1;
        MAX_RANGE = 4;

        usesTargeting = true;

        defaultAction = AC_ZAP;
        tier = 5;
    }

    @Override
    public float getFireAcc(int from, int to) {
        int distance = getDistance(from, to);

        // 최대 사거리 6, 유효 사거리 4
        if (isWithinRange(distance)) {
            return 0.8f;
        } else if (distance > getMaxRange()) {
            return Math.max(0f, 0.9f - 0.6f * (distance - getMaxRange()));
        }

        return 0.8f;
    }

    @Override
    public int fireMin() {
        return (int) 2 + (tier + bulletTier + level())
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }
    @Override
    public int fireMax() {
        return (int) 3
                + tier * 3
                + bulletTier
                + level() * (tier)
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) * 2;
    }

    @Override
    protected void specialFire(Char ch) {
        Buff.affect(ch, Slow.class, 2f);
    }
}
