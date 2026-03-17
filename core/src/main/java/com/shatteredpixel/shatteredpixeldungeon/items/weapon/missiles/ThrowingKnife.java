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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WolfMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

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
		if (isDuplicate(hero)) {
			duplicateDestroyed = true;
			hero.spendAndNext(TIME_TO_PICK_UP);
			return true;
		}
		return super.doPickUp(hero);
	}

	private boolean isDuplicate(Hero hero) {
		if (hero.belongings.getItem(ThrowingKnife.class) != null) {
			return true;
		}

		//WolfMark buff
		if (hero.buff(WolfMark.class) != null) {
			return true;
		}

		// current floor
		for (Heap heap : Dungeon.level.heaps.valueList()) {
			for (Item item : heap.items) {
				if (item instanceof ThrowingKnife && item != this) {
					return true;
				}
			}
		}

		//PinCushion
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			PinCushion pc = mob.buff(PinCushion.class);
			if (pc != null && pc.containsType(ThrowingKnife.class)) {
				return true;
			}
		}

		//items falling through chasms to other floors
		for (int key : Dungeon.droppedItems.keyArray()) {
			ArrayList<Item> items = Dungeon.droppedItems.get(key);
			if (items != null) {
				for (Item item : items) {
					if (item instanceof ThrowingKnife) {
						return true;
					}
				}
			}
		}

		return false;
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

	public static class KnifeSafeguard extends Buff {

		private int turnsLost = 0;

		private static final int SAFEGUARD_TURNS = 100;
		private static final String TURNS_LOST = "turnsLost";

		@Override
		public boolean act() {
			if (Dungeon.hero.belongings.getItem(ThrowingKnife.class) != null) {
				turnsLost = 0;
			} else {
				turnsLost++;
				if (turnsLost >= SAFEGUARD_TURNS) {
					ThrowingKnife newKnife = new ThrowingKnife();
					if (!newKnife.collect()) {
						Dungeon.level.drop(newKnife, target.pos).sprite.drop();
					}
					GLog.p(Messages.get(ThrowingKnife.class, "safeguard"));
					turnsLost = 0;
				}
			}

			spend(TICK);
			return true;
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TURNS_LOST, turnsLost);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			turnsLost = bundle.getInt(TURNS_LOST);
		}
	}
}
