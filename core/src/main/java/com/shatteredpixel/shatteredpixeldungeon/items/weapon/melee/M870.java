package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class M870 extends ShotgunWeapon {

    {
        image = ItemSpriteSheet.M870;
        hitSound = Assets.Sounds.HIT_SHOTGUN;
        hitSoundPitch = 0.8f;

        FIRE_DELAY_MULT = 1.0f;
        bulletMax = 8;
        bullet = bulletMax;
        MAX_RANGE = 5;

        PELLET_COUNT = 7;
        CONE_DEGREES = 75f;
        EXTRA_PELLET_MULT = 0.25f;

        usesTargeting = true;
        defaultAction = AC_ZAP;
        tier = 5;
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
        Buff.affect(ch, Burning.class).reignite(ch, 4f);
    }
}