/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BombtailSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Skeleton extends Mob {

    {
        spriteClass = BombtailSprite.class;

        HP = HT = 12;
        defenseSkill = 2;
        baseSpeed = 0.5f;

        EXP = 5;
        maxLvl = 10;

        flying = true;

        loot = Generator.Category.WEAPON;
        lootChance = 0.2f; //by default, see rollToDropLoot()

        properties.add(Property.INORGANIC);
        properties.add(Property.DRONE);
        immunities.add(Silence.class);
    }

    private boolean hasPrimed = false;
    private boolean explodeNextTurn = false;
    private Object primeCause = null;

    public boolean isPrimed() {
        return explodeNextTurn;
    }

    @Override
    public CharSprite sprite() {
        CharSprite s = super.sprite();
        if (explodeNextTurn) s.tint(0xFF0000, 0.5f);
        return s;
    }

    private static final String HAS_PRIMED = "has_primed";
    private static final String EXPLODE_NEXT_TURN = "explode_next_turn";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(HAS_PRIMED, hasPrimed);
        bundle.put(EXPLODE_NEXT_TURN, explodeNextTurn);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        hasPrimed = bundle.getBoolean(HAS_PRIMED);
        explodeNextTurn = bundle.getBoolean(EXPLODE_NEXT_TURN);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 1);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    public void damage(int dmg, Object src) {
        //stash source so isAlive()'s prime trigger can attribute the kill
        primeCause = src;
        super.damage(dmg, src);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        //primed skeleton ignores further damage; explosion is locked in
        return explodeNextTurn || super.isInvulnerable(effect);
    }

    @Override
    public boolean isAlive() {
        if (HP > 0) {
            return true;
        }
        if (!hasPrimed) {
            triggerExplosionPrime();
        }
        return explodeNextTurn;
    }

    protected void triggerExplosionPrime() {
        hasPrimed = true;
        explodeNextTurn = true;
        if (sprite != null) sprite.tint(0xFF0000, 0.5f);
        if (Dungeon.level.heroFOV[pos]) {
            GLog.w(Messages.get(this, "about_to_explode"));
            Dungeon.hero.interrupt();
        }
        //turn gap: skeleton waits one tick, giving the player a chance to move away
        spend(TICK);
    }

    @Override
    protected boolean act() {
        if (explodeNextTurn) {
            explode();
            return true;
        }
        return super.act();
    }

    @Override
    public void die(Object cause) {
        if (cause == Chasm.class) {
            super.die(cause);
            return;
        }
        if (!hasPrimed) {
            //direct die() (Grim, Doom, Necromancer death, etc.) — route through priming
            primeCause = cause;
            HP = 0;
            triggerExplosionPrime();
            return;
        }
        if (!explodeNextTurn) {
            //called by explode() after the blast — actually die now
            super.die(cause);
        }
        //else: primed but not yet detonated, do nothing (let act() explode)
    }

    private void explode() {
        boolean heroKilled = false;
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
            if (ch != null && ch.isAlive()) {
                int damage = Random.NormalIntRange(18, 25);
                damage = Math.max(0, damage - ch.drRoll());
                ch.damage(damage, this);
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }

                if (ch.isAlive() && Dungeon.isChallenged(Challenges.TACTICAL_UPGRADE) && !(ch instanceof Necromancer)) {
                    Buff.affect(ch, Burning.class).reignite(ch);
                }
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BONES);
        }

        explodeNextTurn = false;
        die(primeCause != null ? primeCause : this);

        if (heroKilled) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(this, "explo_kill"));
        }
    }

    @Override
    public void rollToDropLoot() {
        //each drop makes future drops 1/2 as likely
        // so loot chance looks like: 1/6, 1/12, 1/24, 1/48, etc.
        lootChance *= Math.pow(1 / 2f, Dungeon.LimitedDrops.SKELE_WEP.count);
        super.rollToDropLoot();
    }

    @Override
    protected Item createLoot() {
        Dungeon.LimitedDrops.SKELE_WEP.count++;
        return super.createLoot();
    }

}