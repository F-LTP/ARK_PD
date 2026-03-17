package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Sea_LeefSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SeaLeef extends Mob {
    {
        spriteClass = Sea_LeefSprite.class;

        HP = HT = 135;
        EXP = 18;
        maxLvl = 37;

        defenseSkill = 15;

        loot = Gold.class;
        lootChance = 0.28f;

        properties.add(Property.SEA);
    }

    @Override
    public int damageRoll() {
        int bonus = 0;
        DamageRampUp ramp = buff(DamageRampUp.class);
        if (ramp != null) bonus = ramp.getBonus();
        return Random.NormalIntRange(16 + (bonus / 2), 24 + bonus);
    }

    @Override
    public int attackSkill( Char target ) {
        return 37;
    }

    @Override
    protected float attackDelay() {
        return super.attackDelay() * 0.5f;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.affect(this, DamageRampUp.class).addBonus();
        return super.attackProc(enemy, damage);
    }

    @Override
    public void activateSeaTerror() {
        if (this.buff(Camouflage.class) == null) {
            Buff.affect(this, Camouflage.class, 1f);
        } else if (this.buff(Camouflage.class) != null) {
            Buff.prolong(this, Camouflage.class, 1f);
        }
    }

    public static class DamageRampUp extends Buff {

        private int bonus = 0;
        private int turnsWithoutAttack = 0;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public void addBonus() {
            bonus = Math.min(bonus + 3, 60);
            turnsWithoutAttack = 0;
        }

        public int getBonus() {
            return bonus;
        }

        private void removeBonus() {
            bonus -= 6; // 2 stacks worth
            if (bonus <= 0) {
                detach();
            } else {
                spend(TICK);
            }
        }

        @Override
        public boolean act() {
            turnsWithoutAttack++;
            if (turnsWithoutAttack >= 3) {
                removeBonus();
            } else {
                spend(TICK);
            }
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.2f, 1.5f, 0.5f);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", bonus);
        }

        private static final String BONUS = "bonus";
        private static final String TURNS_NO_ATK = "turnsWithoutAttack";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BONUS, bonus);
            bundle.put(TURNS_NO_ATK, turnsWithoutAttack);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            bonus = bundle.getInt(BONUS);
            turnsWithoutAttack = bundle.getInt(TURNS_NO_ATK);
        }
    }
}
