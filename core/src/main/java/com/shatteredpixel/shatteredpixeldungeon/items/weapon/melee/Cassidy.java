package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Cassidy extends ShotgunWeapon {

    {
        image = ItemSpriteSheet.CASSIDY;
        hitSound = Assets.Sounds.HIT_SHOTGUN;
        hitSoundPitch = 1.0f;

        FIRE_DELAY_MULT = 0.75f;
        bulletMax = 2;
        bullet = bulletMax;
        MAX_RANGE = 3;

        PELLET_COUNT = 5;
        CONE_DEGREES = 60f;
        EXTRA_PELLET_MULT = 0.33f;

        usesTargeting = true;
        defaultAction = AC_ZAP;
        tier = 2;
    }

    @Override
    public int fireMin() {
        return 4 + ((tier) + bulletTier + level())
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public int fireMax() {
        return 6
                + tier * 2
                + bulletTier * 3
                + level() * tier
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) * 2;
    }

    @Override
    protected void specialFire(Char ch) {
        Buff.affect(ch, Slow.class, 3f);
    }
}
