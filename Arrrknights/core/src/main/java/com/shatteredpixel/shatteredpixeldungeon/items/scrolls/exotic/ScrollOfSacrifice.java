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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ScrollOfSacrifice extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_CONFUSION;
	}
	
	@Override
	public void doRead() {
		int Sacrifice = 0;

		ArrayList<Char> affected = new ArrayList<>();//change from budding

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if (!mob.properties().contains(Char.Property.BOSS) && !mob.properties().contains(Char.Property.MINIBOSS)
			&& Dungeon.level.heroFOV[mob.pos] && Sacrifice < 3) {
				affected.add(mob);
				Sacrifice++;
			}
		}
		for (Char mob:affected){
			mob.damage(9999, this);
			mob.sprite.bloodBurstA(mob.sprite.center(), 6);
		}
		if (Sacrifice != 3) {
			Dungeon.hero.sprite.bloodBurstA(Dungeon.hero.sprite.center(), 6);
			Dungeon.hero.damage(Dungeon.hero.HT / 2,this);
			if (!Dungeon.hero.isAlive()) GLog.n(Messages.get(this, "sacrifice"));
		}

		identify();
		
		curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
		Sample.INSTANCE.play( Assets.Sounds.READ );
		
		readAnimation();
	}
	
}
