package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.Property.STATIC;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EndspeakerAspect;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NetherseaBrandguider;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SeaLeef;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Sea_Octo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.NewGameItem.Certificate;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.SeaLevel_part2;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Endspeaker1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Endspeaker2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Endspeaker3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Endspeaker4Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TheEndspeaker extends Mob {
    {
        spriteClass = Status.getSprite();

        HP = HT = Status.getMaxHp();
        defenseSkill = Status.getDefense();

        EXP = Status.getExp();
        maxLvl = 45;

        actPriority = MOB_PRIO - 1;

        state = PASSIVE;

        properties.add(Property.BOSS);
        addImmunities();
    }

    private int spellAbsorptionCooldown = 0;
    private int chargeCooldown = 5;
    private int chargePos = -1;

    @Override
    public int damageRoll() {
        int low = 20 + (Status.abilityCount / 2) * 6;
        int high = 40 + (Status.abilityCount / 2) * 8;
        int damage = Random.NormalIntRange(low, high);

        // Apply Ramp Up damage bonus (5% per stack)
        RampUpStacks rampBuff = buff(RampUpStacks.class);
        if (Status.abilityRampUp && rampBuff != null) {
            damage = Math.round(damage * (1f + (rampBuff.getStacks() * 0.05f)));
        }

        return damage;
    }

    @Override
    public int attackSkill(Char target) {
        return 48;
    }

    @Override
    public int drRoll() {
        int low = (Status.abilityCount / 2) * 9;
        int high = 17 + (Status.abilityCount / 2) * 8;
        return Random.NormalIntRange(low, high);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (Status.abilitySpellAbsorption && spellAbsorptionCooldown <= 0 && src != null && AntiMagic.RESISTS.contains(src.getClass())) {
            dmg = dmg / 4;
            Buff.affect(this, SpellAbsorptionActive.class);
            spellAbsorptionCooldown = 5;
        }
        if (Status.abilityHardening && HP < HT / 4) {
            dmg = dmg / 2;
        }
        if (state == PASSIVE) state = HUNTING;
        super.damage(dmg, src);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (Status.abilityIncreasedRange) {
            return this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 3;
        }
        return Dungeon.level.adjacent( this.pos, enemy.pos );
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        SpellAbsorptionActive spellBuff = buff(SpellAbsorptionActive.class);
        if (Status.abilitySpellAbsorption && spellBuff != null) {
            spellBuff.detach();
            return zap();
        } else {
            return super.doAttack(enemy);
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int dmg = super.attackProc(enemy, damage);

        // Ramp Up: gain stack only on successful hit
        if (dmg > 0 && Status.abilityRampUp) {
            RampUpStacks rampBuff = buff(RampUpStacks.class);
            if (rampBuff == null) {
                rampBuff = Buff.affect(this, RampUpStacks.class, 3f);
            }
            rampBuff.addStack(); // Adds stack and resets duration to 3
        }

        return dmg;
    }

    protected boolean zap() {
        if (enemy == null) return false;

        spend( 1f );

        if (hit(this, enemy, true)) {
            int dmg = this.damageRoll();
            enemy.damage( dmg, this );

            // Ramp Up: grant stack on successful zap hit
            if (dmg > 0 && Status.abilityRampUp) {
                RampUpStacks rampBuff = buff(RampUpStacks.class);
                if (rampBuff == null) {
                    rampBuff = Buff.affect(this, RampUpStacks.class, 3f);
                }
                rampBuff.addStack(); // Adds stack and resets duration to 3
            }

            if (enemy == Dungeon.hero) {

                Camera.main.shake( 2, 0.3f );

                if (!enemy.isAlive()) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(this, "kill") );
                }
            }

        } else {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }

        if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
            sprite.attack( enemy.pos );
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected boolean act() {
        if (state == PASSIVE) return super.act();

        // Execute charge if position is set
        if (Status.abilityCharge && chargePos != -1) {
            executeCharge();
            return false;
        }

        // Setup charge if conditions are met
        if (Status.abilityCharge && chargeCooldown <= 0 && enemy != null
                && !rooted && Dungeon.level.distance(pos, enemy.pos) >= 3) {
            if (setupCharge()) {
                return true;
            }
        }

        // Ramp Up cooldown is now handled by the buff's duration system

        reduceCooldown();
        return super.act();
    }

    private boolean setupCharge() {
        if (enemy == null) return false;

        Ballistica b = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

        // Check if path is clear enough
        if (b.collisionPos != enemy.pos && Dungeon.level.distance(pos, b.collisionPos) < 3) {
            return false;
        }

        int targetPos = b.collisionPos;

        // Check if we can land at target
        Char targetChar = Actor.findChar(targetPos);
        if (targetChar != null) {
            // Find landing spot next to target
            int bouncepos = -1;
            for (int i : PathFinder.NEIGHBOURS8) {
                if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, targetPos + i) < Dungeon.level.trueDistance(pos, bouncepos))
                        && Actor.findChar(targetPos + i) == null && Dungeon.level.passable[targetPos + i]) {
                    bouncepos = targetPos + i;
                }
            }
            if (bouncepos == -1) {
                return false;
            }
        }

        // Setup charge
        chargePos = targetPos;
        chargeCooldown = Random.NormalIntRange(5, 7);

        // Visual warning
        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[chargePos]) {
            GLog.w(Messages.get(this, "charge"));
            sprite.parent.addToBack(new TargetedCell(chargePos, 0xFF0000));
            Dungeon.hero.interrupt();
        }

        spend(TICK);
        return true;
    }

    private void executeCharge() {
        Ballistica b = new Ballistica(pos, chargePos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

        // If path is now blocked, cancel charge
        if (rooted || b.collisionPos != chargePos) {
            chargePos = -1;
            spend(TICK);
            next();
            return;
        }

        final Char chargeVictim = Actor.findChar(chargePos);
        final int endPos;

        // Find landing position
        if (chargeVictim != null) {
            int bouncepos = -1;
            for (int i : PathFinder.NEIGHBOURS8) {
                if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, chargePos + i) < Dungeon.level.trueDistance(pos, bouncepos))
                        && Actor.findChar(chargePos + i) == null && Dungeon.level.passable[chargePos + i]) {
                    bouncepos = chargePos + i;
                }
            }
            if (bouncepos == -1) {
                chargePos = -1;
                spend(TICK);
                next();
                return;
            }
            endPos = bouncepos;
        } else {
            endPos = chargePos;
        }

        // Do the charge with animation
        sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[chargePos] || Dungeon.level.heroFOV[endPos];
        sprite.jump(pos, chargePos, new Callback() {
            @Override
            public void call() {
                // Damage all characters along the path
                for (int cell : b.path) {
                    Char ch = Actor.findChar(cell);
                    if (ch != null && ch != TheEndspeaker.this && alignment != ch.alignment) {
                        int base = damageRoll();
                        int damage = Random.NormalIntRange(base / 2, base);
                        ch.damage(damage, TheEndspeaker.this);
                        if (ch.isAlive()) {
                            Buff.prolong(ch, Cripple.class, 3f);
                        }
                        ch.sprite.flash();
                        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                    }
                }

                // Move to end position
                if (endPos != chargePos) {
                    Actor.addDelayed(new Pushing(TheEndspeaker.this, chargePos, endPos), -1);
                }

                pos = endPos;
                chargePos = -1;
                sprite.idle();
                Dungeon.level.occupyCell(TheEndspeaker.this);
                spend(TICK);
                next();
            }
        });
    }

    private void reduceCooldown() {
        chargeCooldown--;
        spellAbsorptionCooldown--;
        // Note: rampUpResetCooldown is handled in act() based on attack state
    }

    private void addImmunities() {
        if (Status.abilityCcImmune) {
            properties.add(STATIC);
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        int lootTier = Status.abilityCount / 2;
        Ankh lootAnkh;
        switch(lootTier) {
            case 1:
                // 1단계 폼 (2 형태)
                Dungeon.level.drop(new Certificate(20), pos).sprite.drop(pos);
                dropLoot(new ScrollOfUpgrade());
                dropLoot(new PotionOfExperience());
                break;
            case 2:
                // 2딘계 폼 (3 형태)
                Dungeon.level.drop(new Certificate(30), pos).sprite.drop(pos);
                dropLoot(new ScrollOfUpgrade());
                dropLoot(new PotionOfExperience());
                lootAnkh = new Ankh();
                lootAnkh.bless();
                dropLoot(lootAnkh);
                break;
            case 3:
                // 3단계 폼 (4 형태)
                Dungeon.level.drop(new Certificate(40), pos).sprite.drop(pos);
                dropLoot(new ScrollOfUpgrade());
                dropLoot(new PotionOfExperience());
                lootAnkh = new Ankh();
                lootAnkh.bless();
                dropLoot(lootAnkh);
                break;
            case 0:
            default:
                // 기본 폼 (1 형태)
                Dungeon.level.drop(new Certificate(10), pos).sprite.drop(pos);
                dropLoot(new PotionOfExperience());
                break;

        }
    }

    private void dropLoot(Item item) {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int offset : PathFinder.NEIGHBOURS8) {
            if (Dungeon.level.passable[pos + offset]) {
                candidates.add(pos + offset);
            }
        }
        int lootPos = candidates.isEmpty() ? pos : Random.element(candidates);
        Dungeon.level.drop(item, lootPos).sprite.drop(pos);
    }

    @Override
    public String description() {
        String desc = Messages.get(this, "desc");
        desc += Messages.get(this, "desc_" + Status.abilityCount / 2);
        if (Status.abilityCount > 0) {
            desc += Messages.get(this, "desc_sp");
            if (Status.abilitySpellAbsorption) desc += Messages.get(this, "desc_sp_spellabsorption");
            if (Status.abilityIncreasedRange) desc += Messages.get(this, "desc_sp_increasedrange");
            if (Status.abilityRampUp) desc += Messages.get(this, "desc_sp_rampup");
            if (Status.abilityCharge) desc += Messages.get(this, "desc_sp_charge");
            if (Status.abilityHardening) desc += Messages.get(this, "desc_sp_hardening");
            if (Status.abilityCcImmune) desc += Messages.get(this, "desc_sp_crowdcontrolimmune");
        }
        return desc;
    }

    private static final String SPELL_ABSORPTION_CD = "spell_absorption_cooldown";
    private static final String CHARGE_CD = "charge_cooldown";
    private static final String CHARGE_POS = "charge_pos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPELL_ABSORPTION_CD, spellAbsorptionCooldown);
        bundle.put(CHARGE_CD, chargeCooldown);
        bundle.put(CHARGE_POS, chargePos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        spellAbsorptionCooldown = bundle.getInt(SPELL_ABSORPTION_CD);
        chargeCooldown = bundle.getInt(CHARGE_CD);
        chargePos = bundle.getInt(CHARGE_POS);
    }

    /**
     * Buff that tracks and displays Ramp Up stacks
     * Duration automatically manages the reset cooldown
     */
    public static class RampUpStacks extends FlavourBuff {

        private int stacks = 0;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFF9900);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", stacks, (int)(stacks * 5));
        }

        /**
         * Add a stack and reset the duration to 3 turns
         */
        public void addStack() {
            stacks++;
            spend(3f - cooldown()); // Reset to 3 turns
        }

        public int getStacks() {
            return stacks;
        }

        @Override
        public boolean act() {
            // Only tick down when the boss can't attack
            TheEndspeaker boss = (TheEndspeaker) target;
            if (boss.enemy == null || !boss.canAttack(boss.enemy)) {
                spend(TICK);
                return super.act();
            } else {
                // Boss is actively attacking, pause the countdown
                spend(TICK);
                return true;
            }
        }

        private static final String STACKS = "stacks";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(STACKS, stacks);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            stacks = bundle.getInt(STACKS);
        }
    }

    /**
     * Buff that indicates Spell Absorption counter-attack is ready
     */
    public static class SpellAbsorptionActive extends Buff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(com.watabou.noosa.Image icon) {
            icon.hardlight(0x00FFFF);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public boolean act() {
            // This buff is manually controlled, not time-based
            spend(TICK);
            return true;
        }
    }

    public static class AspectSmall extends Sea_Octo {
        {
            state = SLEEPING;
            loot = new PotionOfStrength();
            lootChance = 1f;
        }

        private boolean isEmpowered = false;

        @Override
        public boolean act() {
            EndspeakerAspect.Empowering buff = buff(EndspeakerAspect.Empowering.class);
            if (!isEmpowered && buff != null) {
                HP = HT = 250;
                defenseSkill = 16;
                loot = new ScrollOfUpgrade();
                isEmpowered = true;
            }
            return super.act();
        }

        @Override
        public int damageRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(40, 62);
            }
            return super.damageRoll();
        }

        @Override
        public int drRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(9, 27);
            }
            return super.drRoll();
        }

        @Override
        public void die( Object cause ) {
            removeAbility(this);
            empowerRemainingAspect();
            super.die(cause);
        }

        @Override
        public void destroy() {
            grantAbility(this);
            super.destroy();
        }

        private static final String IS_EMPOWERED = "is_empowered";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(IS_EMPOWERED, isEmpowered);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            isEmpowered = bundle.getBoolean(IS_EMPOWERED);
        }
    }

    public static class AspectMedium extends SeaLeef {
        {
            state = SLEEPING;
            loot = new PotionOfStrength();
            lootChance = 1f;
        }

        private boolean isEmpowered = false;

        @Override
        public boolean act() {
            EndspeakerAspect.Empowering buff = buff(EndspeakerAspect.Empowering.class);
            if (!isEmpowered && buff != null) {
                HP = HT = 270;
                defenseSkill = 20;
                loot = new ScrollOfUpgrade();
                isEmpowered = true;
            }
            return super.act();
        }

        @Override
        public int damageRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(24 + (damageBonus / 2), 36 + damageBonus);
            }
            return super.damageRoll();
        }

        @Override
        public int drRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(5, 15);
            }
            return super.drRoll();
        }

        @Override
        public void die( Object cause ) {
            removeAbility(this);
            empowerRemainingAspect();
            super.die(cause);
        }

        @Override
        public void destroy() {
            grantAbility(this);
            super.destroy();
        }

        private static final String IS_EMPOWERED = "is_empowered";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(IS_EMPOWERED, isEmpowered);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            isEmpowered = bundle.getBoolean(IS_EMPOWERED);
        }
    }

    public static class AspectLarge extends NetherseaBrandguider {
        {
            state = SLEEPING;
            loot = new PotionOfStrength();
            lootChance = 1f;
        }

        private boolean isEmpowered = false;

        @Override
        public boolean act() {
            EndspeakerAspect.Empowering buff = buff(EndspeakerAspect.Empowering.class);
            if (!isEmpowered && buff != null) {
                HP = HT = 320;
                defenseSkill = 30;
                loot = new ScrollOfUpgrade();
                isEmpowered = true;
            }
            return super.act();
        }

        @Override
        public int damageRoll() {
            if (isEmpowered) {
                return Random.NormalIntRange(47, 68);
            }
            return super.damageRoll();
        }

        @Override
        public int drRoll() {
            if (isEmpowered) {
                if (HT /2 >= HP) return Random.NormalIntRange(20, 60);
                return Random.NormalIntRange(10, 30);
            }
            return super.drRoll();
        }

        @Override
        public void die( Object cause ) {
            removeAbility(this);
            empowerRemainingAspect();
            super.die(cause);
        }

        @Override
        public void destroy() {
            grantAbility(this);
            super.destroy();
        }

        private static final String IS_EMPOWERED = "is_empowered";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(IS_EMPOWERED, isEmpowered);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            isEmpowered = bundle.getBoolean(IS_EMPOWERED);
        }
    }

    protected static void removeAbility(Mob mob) {
        EndspeakerAspect ability = null;
        for (EndspeakerAspect buff : mob.buffs(EndspeakerAspect.class)) {
            buff.detach();
        }
    }

    protected static void grantAbility(Mob mob) {
        EndspeakerAspect ability = null;
        for (EndspeakerAspect buff : mob.buffs(EndspeakerAspect.class)){
            if (buff.giveAbility()) TheEndspeaker.Status.abilityCount++;
        }
    }

    protected static void empowerRemainingAspect() {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob.isAlive() && (mob instanceof AspectSmall
                    || mob instanceof AspectMedium
                    || mob instanceof AspectLarge)) {
                Buff.affect(mob, EndspeakerAspect.Empowering.class);
            }
        }
    }

    public static class Status {
        public static boolean spawned;
        public static boolean abilitySpellAbsorption;
        public static boolean abilityHardening;
        public static boolean abilityCcImmune;
        public static boolean abilityIncreasedRange;
        public static boolean abilityRampUp;
        public static boolean abilityCharge;

        public static int abilityCount;

        public static Class<? extends MobSprite> getSprite() {
            switch (abilityCount / 2) {
                case 1:
                    return Endspeaker1Sprite.class;
                case 2:
                    return Endspeaker2Sprite.class;
                case 3:
                    return Endspeaker3Sprite.class;
                case 0:
                default:
                    return Endspeaker4Sprite.class;
            }
        }

        public static int getMaxHp() {
            return 700 + 150 * (abilityCount / 2);
        }

        public static int getDefense() {
            return 10 + 7 * (abilityCount / 2);
        }

        public static int getExp() {
            return 50 + 25 * (abilityCount / 2);
        }

        public static void spawnAspects(SeaLevel_part2 level) {
            if (Dungeon.depth > 35 && Dungeon.depth < 40) {
                switch (Dungeon.depth) {
                    case 36:
                        AspectSmall aspectRange = new AspectSmall();
                        AspectSmall aspectSpell = new AspectSmall();
                        summonMob(level, aspectSpell, EndspeakerAspect.SpellAbsorption.class);
                        summonMob(level, aspectRange, EndspeakerAspect.IncreasedRange.class);
                        break;
                    case 37:
                        AspectMedium aspectRamp = new AspectMedium();
                        AspectMedium aspectCharge = new AspectMedium();
                        summonMob(level, aspectRamp, EndspeakerAspect.RampUp.class);
                        summonMob(level, aspectCharge, EndspeakerAspect.Charge.class);
                        break;
                    case 38:
                        AspectLarge aspectHarden = new AspectLarge();
                        AspectLarge aspectCcImmune = new AspectLarge();
                        summonMob(level, aspectHarden, EndspeakerAspect.Hardening.class);
                        summonMob(level, aspectCcImmune, EndspeakerAspect.CrowdControlImmune.class);
                        break;
                    default:
                        break;
                }
            }
        }

        private static <T extends EndspeakerAspect> void summonMob(SeaLevel_part2 level, Mob mob, Class<T> ability) {
            if (spawned) {
                return;
            }
            do {
                mob.pos = level.randomRespawnCell( mob );
            } while (
                    mob.pos == -1 ||
                            level.heaps.get( mob.pos) != null ||
                            level.traps.get( mob.pos) != null ||
                            level.findMob( mob.pos ) != null ||
                            !(level.passable[mob.pos + PathFinder.CIRCLE4[0]] && level.passable[mob.pos + PathFinder.CIRCLE4[2]]) ||
                            !(level.passable[mob.pos + PathFinder.CIRCLE4[1]] && level.passable[mob.pos + PathFinder.CIRCLE4[3]])
            );
            Buff.affect(mob, ability);
            level.mobs.add(mob);
        }

        public static void activate(EndspeakerAspect.EndspeakerAbility ability) {
            if (spawned) {
                return;
            }
            switch (ability) {
                case SPELL_ABSORPTION:
                    abilitySpellAbsorption = true;
                    break;
                case INCREASED_RANGE:
                    abilityIncreasedRange = true;
                    break;
                case RAMP_UP:
                    abilityRampUp = true;
                    break;
                case CHARGE:
                    abilityCharge = true;
                    break;
                case HARDENING:
                    abilityHardening = true;
                    break;
                case CROWD_CONTROL_IMMUNE:
                    abilityCcImmune = true;
                    break;
                default:
                    break;
            }
        }

        public static void reset() {
            spawned = false;
            abilitySpellAbsorption = false;
            abilityHardening = false;
            abilityCcImmune = false;
            abilityIncreasedRange = false;
            abilityRampUp =false;
            abilityCharge = false;
            abilityCount = 0;
        }

        private static final String NODE		= "theEndspeakerStatus";

        private static final String SPAWNED		= "spawned";
        private static final String ABILITY_SPELL = "abilitySpell";
        private static final String ABILITY_HARDENING = "abilityHardening";
        private static final String ABILITY_CC = "abilityCcImmune";
        private static final String ABILITY_RANGE = "abilityRange";
        private static final String ABILITY_RAMP = "abilityRamp";
        private static final String ABILITY_CHARGE = "abilityCharge";
        private static final String ABILITY_COUNT = "abilityCount";

        public static void storeInBundle( Bundle bundle ) {
            Bundle node = new Bundle();

            node.put( SPAWNED, spawned );

            node.put( ABILITY_SPELL, abilitySpellAbsorption );
            node.put( ABILITY_HARDENING, abilityHardening );
            node.put( ABILITY_CC, abilityCcImmune );
            node.put( ABILITY_RANGE, abilityIncreasedRange );
            node.put( ABILITY_RAMP, abilityRampUp );
            node.put( ABILITY_CHARGE, abilityCharge );
            node.put( ABILITY_COUNT, abilityCount );

            bundle.put( NODE, node );
        }

        public static void restoreFromBundle( Bundle bundle ) {
            Bundle node = bundle.getBundle( NODE );

            if (!node.isNull()) {
                spawned = node.getBoolean(SPAWNED);
                abilitySpellAbsorption = node.getBoolean(ABILITY_SPELL);
                abilityHardening = node.getBoolean(ABILITY_HARDENING);
                abilityCcImmune = node.getBoolean(ABILITY_CC);
                abilityIncreasedRange = node.getBoolean(ABILITY_RANGE);
                abilityRampUp = node.getBoolean(ABILITY_RAMP);
                abilityCharge = node.getBoolean(ABILITY_CHARGE);
                abilityCount = node.getInt(ABILITY_COUNT);
            }
        }
    }
}