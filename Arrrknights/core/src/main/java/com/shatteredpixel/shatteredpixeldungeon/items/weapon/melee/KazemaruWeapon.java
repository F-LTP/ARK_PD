package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hallucination;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BeeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BreakerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class KazemaruWeapon extends MeleeWeapon {

    {
        image = ItemSpriteSheet.POMBBAY;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.11f;

        tier = 4;
    }

    @Override
    public int max(int lvl) {
        return  5*(tier) +
                lvl*(tier+1);
    }

    public String statsInfo() {
		if (isIdentified()) {
            return Messages.get(this, "stats_desc", 1+buffedLvl(), 20+buffedLvl()*5);//change from budding
        } else {
            return Messages.get(this, "typical_stats_desc", 1, 20);
        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Random.Int(10) == 0) {
            ArrayList<Integer> respawnPoints = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = defender.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add(p);
                }
            }
            int spawnd = 0;
            while (respawnPoints.size() > 0 && spawnd == 0) {
                int index = Random.index(respawnPoints);

                KazemaruSummon summon = new KazemaruSummon();
                summon.GetWeaponLvl(buffedLvl());
                summon.GetTarget(defender);
                if (attacker instanceof Hero || attacker.alignment == Char.Alignment.ALLY) summon.GetAlignment(true);//change from budding
                GameScene.add(summon);
                ScrollOfTeleportation.appear(summon, respawnPoints.get(index));

                respawnPoints.remove(index);
                spawnd++;
            }
        }

        return super.proc(attacker, defender, damage);
    }

    public static class KazemaruSummon extends Mob {
        {
            HP = HT = 1;

            spriteClass = BreakerSprite.class;

            flying = true;
            state = WANDERING;

        }

        @Override
        public void onAttackComplete() {
            attack( enemy );
            next();
            die(this);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(maxLvl + 1, 20 + (maxLvl * 5));
        }
        @Override//change from budding
        public int attackSkill( Char target ){return (alignment == Alignment.ALLY)?(Dungeon.hero.attackSkill(target)):(9+Dungeon.depth);}//change from budding
        public void GetWeaponLvl(int wlvl) {
            maxLvl = wlvl;
        }
        public void GetTarget(Char t) {target = t.pos;}//change from budding
        public void GetAlignment(boolean isally){if (isally) alignment = Alignment.ALLY;}//change from budding
    }}
