package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DP27 extends GunWeapon {
    {
        image = ItemSpriteSheet.DP27;
        hitSound = Assets.Sounds.HIT_GUN2;
        hitSoundPitch = 0.9f;

        FIRE_DELAY_MULT = 0.5f;

        bulletMax = 47;
        MIN_RANGE = 1;
        MAX_RANGE = 4;

        usesTargeting = true;

        defaultAction = AC_ZAP;
        tier = 4;
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
    public float getFireAcc(int from, int to) {
        int distance = getDistance(from, to);

        // 최대 사거리 6, 유효 사거리 4
        if (isWithinRange(distance)) {
            return 0.8f;
        } else if (distance > getMaxRange()) {
            return Math.max(0f, 0.9f - 0.3f * (distance - getMaxRange()));
        }

        return 0.8f;
    }

    @Override
    protected void specialFire(Char ch) {
        Buff.affect(ch, Burning.class).reignite(ch);
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback ) {
        int a = 0;
        if (specialFire) a = 1;
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.GUN_SHOT+a,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( this.hitSound  );
    }
}
