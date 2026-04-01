package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SG_CQB extends ShotgunWeapon {

    {
        image = ItemSpriteSheet.SG_CQB;
        hitSound = Assets.Sounds.HIT_SHOTGUN;
        hitSoundPitch = 0.9f;

        FIRE_DELAY_MULT = 1.5f;
        bulletMax = 7;
        bullet = bulletMax;
        MAX_RANGE = 4;

        PELLET_COUNT = 5;
        CONE_DEGREES = 60f;
        EXTRA_PELLET_MULT = 0.33f;

        usesTargeting = true;
        defaultAction = AC_ZAP;
        tier = 4;
    }

    @Override
    public int fireMin() {
        return (tier - 2) + bulletTier + level() / 2
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public int fireMax() {
        return tier * 2 + bulletTier * 2 + level() * (tier - 1)
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) * 2;
    }

    @Override
    protected void specialFire(Char ch) {
        Ballistica trajectory = new Ballistica(Dungeon.hero.pos, ch.pos, Ballistica.STOP_TARGET);
        trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
        WandOfBlastWave.throwChar(ch, trajectory, 2);
    }
}