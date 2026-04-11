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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WolfMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

import java.util.ArrayList;
import java.util.Iterator;

public class ThrowingKnife extends MissileWeapon {

    public boolean duplicateDestroyed = false;
	
	{
		image = ItemSpriteSheet.THROWING_KNIFE;
		hitSound = Assets.Sounds.HIT_KNIFE;
		hitSoundPitch = 1.2f;

		unique = true;
		bones = false;
        stackable = false;
		
		tier = 1;
		baseUses = 10000;
	}
	
	@Override
	public int max(int lvl) {
		return  6 * tier +                      //6 base, up from 5
				(tier == 1 ? 2*lvl : tier*lvl); //scaling unchanged
	}

    @Override
    public boolean doPickUp(Hero hero) {
        //only reject if the hero already holds a knife — everything else (stray
        //ground copies, PinCushion leftovers, cross-floor junk) is cleaned up by
        //clearFromLevel on level entry, so pickup should always succeed otherwise.
        if (hero.belongings.getItem(ThrowingKnife.class) != null) {
            duplicateDestroyed = true;
            hero.spendAndNext(TIME_TO_PICK_UP);
            return true;
        }
        return super.doPickUp(hero);
    }

    @Override
	public int proc(Char attacker, Char defender, int damage) {
		if (defender instanceof Piranha) damage = 0;

		if (Dungeon.hero.subClass != HeroSubClass.WILD) {
			if (attacker.buff(huntcooldown.class) == null && defender.isAlive()) {
				Buff.prolong(attacker, WolfMark.class, WolfMark.DURATION).set(defender.id(), this);
			}
		}
		return super.proc(attacker, defender, damage);
	}


	public static class huntcooldown extends FlavourBuff {
		@Override
		public int icon() {
			return BuffIndicator.COOL_TIME;
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns());
		}
	}

    //wipes stray knives from the current level and gives the rogue one if they lack it
    public static void onLevelEnter() {
        Hero hero = Dungeon.hero;

        //detach WolfMark first: its detach() collects any in-flight knife back
        //into inventory, so cross-floor references never linger past a transition
        if (hero != null) {
            WolfMark wm = hero.buff(WolfMark.class);
            if (wm != null) wm.detach();
        }

        clearFromLevel(Dungeon.level);

        if (hero == null) return;
        if (hero.heroClass != HeroClass.ROGUE) return;
        if (hero.belongings.getItem(ThrowingKnife.class) != null) return;

        new ThrowingKnife().collect();
    }

    private static void clearFromLevel(Level level) {
        if (level == null) return;

        //destroy emptied heaps after the walk to avoid mutating level.heaps mid-iteration
        ArrayList<Heap> emptied = new ArrayList<>();
        for (Heap heap : level.heaps.valueList()) {
            Iterator<Item> it = heap.items.iterator();
            while (it.hasNext()) {
                if (it.next() instanceof ThrowingKnife) {
                    it.remove();
                }
            }
            if (heap.items.isEmpty()) {
                emptied.add(heap);
            }
        }
        for (Heap heap : emptied) {
            heap.destroy();
        }

        for (Mob mob : level.mobs.toArray(new Mob[0])) {
            PinCushion pc = mob.buff(PinCushion.class);
            if (pc != null) {
                pc.removeType(ThrowingKnife.class);
            }
        }
    }
}
