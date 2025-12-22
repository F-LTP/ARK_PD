package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Mula_2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossMultiHealthBar;
import com.watabou.utils.Bundle;

//패턴 : 믈라의 몸통은 생존해있다면 주기적으로 머리+꼬리의 채력을 회복한다
public class IsharmlaSeabornBody extends Mob {
    {
        spriteClass = Mula_2Sprite.class;

        HP = HT = 1000;

        defenseSkill = 25;

        actPriority = MOB_PRIO-1;

        properties.add(Property.SEA);
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);

        state = new Hunting();
    }

    // 모든 믈라 파츠가 파괴되면 사망
    private boolean isDead = false;
    private int cooldown = 8;
    int healAmount = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? 50 : 40;

    @Override
    public void notice() {
        BossMultiHealthBar.assignBoss(this);
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

    // 공격불가
    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    // 몸통은 주기적으로 힐을 부여한다. 체력이 1이하가 되면 스킬사용 불가
    @Override
    protected boolean act() {

        sprite.turnTo(pos, 999999);
        rooted = true;

        if (isDead) {
            alerted = false;
            return super.act();
        }

        if (cooldown > 0) {
            cooldown--;
        } else {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if ((mob instanceof IsharmlaSeabornHead || mob instanceof IsharmlaSeabornBody || mob instanceof IsharmlaSeabornTail)
                        && mob.buff(Doom.class) == null) {
                    mob.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
                    mob.HP = Math.min(mob.HT, mob.HP + healAmount);
                }
            }
            cooldown = Dungeon.isChallenged(Challenges.DECISIVE_BATTLE) ? 6 : 9;
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

    private static final String IS_DEAD_BODY = "isDeadBody";
    private static final String SHIELD_COOLDOWN = "shieldCooldown";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(IS_DEAD_BODY, isDead);
        bundle.put(SHIELD_COOLDOWN, cooldown);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        isDead = bundle.getBoolean(IS_DEAD_BODY);
        cooldown = bundle.getInt(SHIELD_COOLDOWN);
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




