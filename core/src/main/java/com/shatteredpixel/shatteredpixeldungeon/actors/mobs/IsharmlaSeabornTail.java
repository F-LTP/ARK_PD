package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Mula_3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossMultiHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//패턴 : 1) 근처에 모든 적에게 고정데미지를 입히는 스킬을 사용한다; 2) 원거리 물리공격
public class IsharmlaSeabornTail extends Mob {
    {
        spriteClass = Mula_3Sprite.class;

        HP = HT = 1000;

        defenseSkill = 25;

        actPriority = MOB_PRIO-1;

        properties.add(Property.SEA);
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
        properties.add(Property.STATIC);

        state = new Hunting();
    }

    // 모든 믈라 파츠가 파괴되면 사망
    private boolean isDead = false;

    private int cooldown = 3;

    @Override
    public void notice() {
        BossMultiHealthBar.assignBoss(this);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(25, 45);
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }

    @Override
    public int defenseSkill(Char enemy) {
        if (isDead) return INFINITE_EVASION;

        // 캐릭터가 물 밖이라면 데미지를 입지 않습니다
        if (enemy instanceof Hero && Dungeon.level.map[enemy.pos] == Terrain.EMPTY) {
            return INFINITE_EVASION;
        }

        else return 20;
    }

    // 캐릭터가 물 위라면 어디든지 공격 가능
    @Override
    protected boolean canAttack(Char enemy) {
        return !isDead && (Dungeon.level.map[enemy.pos] != Terrain.EMPTY);
    }

    @Override
    protected boolean act() {

        sprite.turnTo(pos, 999999);
        rooted = true;

        if (isDead) {
            alerted = false;
            return super.act();
        }

        if (cooldown > 0) cooldown--;
        else {
            Dungeon.hero.damage(10, this);
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.alignment == Alignment.ALLY)
                    mob.damage(10, this);
            }
            if (Dungeon.isChallenged(Challenges.DECISIVE_BATTLE)) cooldown = 3;
            else cooldown = 5;
        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {

        if (isDead) return;

        // 캐릭터가 물 밖이라면 데미지를 입지 않습니다
        if (src instanceof Hero && Dungeon.level.map[Dungeon.hero.pos] == Terrain.EMPTY) {
            return;
        }

        super.damage(dmg, src);

        if (HP < 1) {
            isDead = true;
            Buff.affect(this, Doom.class);
            Dungeon.mulaCount++;
            IsharmlaSeabornHead.triggerAnger();
        }
    }

    @Override
    public void die(Object cause) { }

    private static final String IS_DEAD_TAIL   = "isDeadTail";
    private static final String SING_COOLDOWN = "singCooldown";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( IS_DEAD_TAIL, isDead);
        bundle.put(SING_COOLDOWN, cooldown);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        isDead = bundle.getBoolean(IS_DEAD_TAIL);
        cooldown = bundle.getInt(SING_COOLDOWN);
    }

    protected class Hunting implements AiState {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                target = enemy.pos;
                return doAttack( enemy );

            } else {
                spend( TICK );
                return true;
            }
        }
    }
}




