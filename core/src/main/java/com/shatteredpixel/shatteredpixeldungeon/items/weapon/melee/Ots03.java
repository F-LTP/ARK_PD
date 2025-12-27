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

public class Ots03 extends GunWeapon {
    {
        image = ItemSpriteSheet.OTS03;
        hitSound = Assets.Sounds.HIT_PISTOL;
        hitSoundPitch = 0.9f;

        FIRE_DELAY_MULT = 2f;
        bulletMax = 16;
        MIN_RANGE = 3;
        MAX_RANGE = 999;

        usesTargeting = true;

        defaultAction = AC_ZAP;
        tier = 4;
    }

    @Override
    public int fireMin() {
        return (int) (4 + bulletTier * 5 + level() * 2)
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }

    @Override
    public int fireMax() {
        return (int) 20
                + tier * 3
                + bulletTier * 6
                + level() *  (tier + 1)
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) * 2;
    }

    @Override
    public float getFireAcc(int from, int to) {
        int distance = getDistance(from, to);

        float accuracy = 2f;
        // 최소사거리 3, 유효 사거리 3-inf
        if (isWithinRange(distance)) {
            accuracy = accuracy + (distance - getMinRange()) * 3f;
        } else if (distance < getMinRange()) {
            accuracy = 0.33f * (distance);
        }

        if (mobsAdjacent(from)) {
            accuracy = accuracy * 0.5f;
        }
        return accuracy;
    }

    private boolean mobsAdjacent(int pos) {
        for (Char ch : Dungeon.level.mobs) {
            if (ch != null && ch.isAlive() && Dungeon.level.adjacent(pos, ch.pos)) {
                return true;
            }
        }
        return false;
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
        Sample.INSTANCE.play( this.hitSound );
    }
}
