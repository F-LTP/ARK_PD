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

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ScoutSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BombtailSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Necromancer extends Mob {
	
	{
		spriteClass = ScoutSprite.class;
		
		HP = HT = 20;
		defenseSkill = 14;
		
		EXP = 7;
		maxLvl = 14;
		
		loot = new PotionOfHealing();
		lootChance = 0.2f; //see createloot
		
		HUNTING = new Hunting();
		immunities.add(Silence.class);
	}
	
	public boolean summoning = false;
	public int summoningPos = -1;
	
	private boolean firstSummon = true;
	
	private NecroSkeleton mySkeleton;
	private int storedSkeletonID = -1;

	@Override
	protected boolean act() {
		if (summoning && state != HUNTING){
			summoning = false;
			if (sprite instanceof ScoutSprite) ((ScoutSprite) sprite).cancelSummoning();
		}

		if (Camouflage.CamoFlageEnemy(this)) Buff.affect(this, Camouflage.class, 10f);
		return super.act();
	}

	@Override
	public void damage(int dmg, Object src) {
		if (src == mySkeleton)
			if (Dungeon.isChallenged(Challenges.TACTICAL_UPGRADE))
			dmg = 0;
			else
				dmg /=2;
		super.damage(dmg, src);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}
	
	@Override
	public void rollToDropLoot() {
		lootChance *= ((6f - Dungeon.LimitedDrops.NECRO_HP.count) / 6f);
		super.rollToDropLoot();
	}
	
	@Override
	protected Item createLoot(){
		Dungeon.LimitedDrops.NECRO_HP.count++;
		return super.createLoot();
	}
	
	@Override
	public void die(Object cause) {
		if (storedSkeletonID != -1){
			Actor ch = Actor.findById(storedSkeletonID);
			storedSkeletonID = -1;
			if (ch instanceof NecroSkeleton){
				mySkeleton = (NecroSkeleton) ch;
			}
		}
		
		if (mySkeleton != null && mySkeleton.isAlive()){
			mySkeleton.die(null);
		}
		
		super.die(cause);
			if (Random.Int(0,110) <= 1)
			{
				Dungeon.level.drop(Generator.random(Generator.Category.SKL_T1), pos ).sprite.drop( pos );
			}
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return false;
	}

	private static final String SUMMONING = "summoning";
	private static final String FIRST_SUMMON = "first_summon";
	private static final String SUMMONING_POS = "summoning_pos";
	private static final String MY_SKELETON = "my_skeleton";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( SUMMONING, summoning );
		bundle.put( FIRST_SUMMON, firstSummon );
		if (summoning){
			bundle.put( SUMMONING_POS, summoningPos);
		}
		if (mySkeleton != null){
			bundle.put( MY_SKELETON, mySkeleton.id() );
		} else if (storedSkeletonID != -1){
			bundle.put( MY_SKELETON, storedSkeletonID );
		}//change from budding,shattered
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		summoning = bundle.getBoolean( SUMMONING );
		if (bundle.contains(FIRST_SUMMON)) firstSummon = bundle.getBoolean(FIRST_SUMMON);
		if (summoning){
			summoningPos = bundle.getInt( SUMMONING_POS );
		}
		if (bundle.contains( MY_SKELETON )){
			storedSkeletonID = bundle.getInt( MY_SKELETON );
		}
	}
	
	public void onZapComplete(){
		if (mySkeleton == null || mySkeleton.sprite == null || !mySkeleton.isAlive()){
			return;
		}
		
		//heal skeleton first
		if (mySkeleton.buff(Ooze.class) == null) {

			if (sprite.visible || mySkeleton.sprite.visible) {
				sprite.parent.add(new Beam.HealthRay(sprite.center(), mySkeleton.sprite.center()));
			}

			Buff.affect(mySkeleton, Ooze.class).set( Ooze.DURATION) ;
		}
		
		next();
	}
	
	private class Hunting extends Mob.Hunting{
		
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;

			if (enemySeen){
				target = enemy.pos;
			}//change from budding,shattered

			if (storedSkeletonID != -1){
				Actor ch = Actor.findById(storedSkeletonID);
				storedSkeletonID = -1;
				if (ch instanceof NecroSkeleton){
					mySkeleton = (NecroSkeleton) ch;
				}
			}
			
			if (summoning){
				
				//push anything on summoning spot away, to the furthest valid cell
				if (Actor.findChar(summoningPos) != null) {
					int pushPos = pos;
					for (int c : PathFinder.NEIGHBOURS8) {
						if (Actor.findChar(summoningPos + c) == null
								&& Dungeon.level.passable[summoningPos + c]
								&& (Dungeon.level.openSpace[summoningPos + c] || !hasProp(Actor.findChar(summoningPos), Property.LARGE))
								&& Dungeon.level.trueDistance(pos, summoningPos + c) > Dungeon.level.trueDistance(pos, pushPos)) {
							pushPos = summoningPos + c;
						}
					}
					
					//push enemy, or wait a turn if there is no valid pushing position
					if (pushPos != pos) {
						Char ch = Actor.findChar(summoningPos);
						Actor.addDelayed( new Pushing( ch, ch.pos, pushPos ), -1 );
						
						ch.pos = pushPos;
						Dungeon.level.occupyCell(ch );
						
					} else {

						Char blocker = Actor.findChar(summoningPos);
						if (blocker.alignment != alignment){
							blocker.damage( Random.NormalIntRange(2, 10), this );
						}

						spend(TICK);
						return true;
					}
				}
				
				summoning = firstSummon = false;
				
				mySkeleton = new NecroSkeleton();
				mySkeleton.pos = summoningPos;
				GameScene.add( mySkeleton );
				Dungeon.level.occupyCell( mySkeleton );
				((ScoutSprite)sprite).finishSummoning();
				
				if (buff(Corruption.class) != null){
					Buff.affect(mySkeleton, Corruption.class);
				}
				for (Buff b : buffs(ChampionEnemy.class)){
					Buff.affect( mySkeleton, b.getClass());
				}
				
				spend(TICK);
				return true;
			}
			
			if (mySkeleton != null &&
					(!mySkeleton.isAlive()
					|| !Dungeon.level.mobs.contains(mySkeleton)
					|| mySkeleton.alignment != alignment)){
				mySkeleton = null;
			}
			
			//if enemy is seen, and enemy is within range, and we haven no skeleton, summon a skeleton!
			if (enemySeen && Dungeon.level.distance(pos, enemy.pos) <= 4 && mySkeleton == null){
				
				summoningPos = -1;
				for (int c : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(enemy.pos+c) == null
							&& Dungeon.level.passable[enemy.pos+c]
							&& fieldOfView[enemy.pos+c]
							&& Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, summoningPos)){
						summoningPos = enemy.pos+c;
					}
				}
				
				if (summoningPos != -1){
					
					summoning = true;
					sprite.zap( summoningPos );
					
					spend( firstSummon ? TICK : 2*TICK );
				} else {
					//wait for a turn
					spend(TICK);
				}
				
				return true;
			//otherwise, if enemy is seen, and we have a skeleton...
			} else if (enemySeen && mySkeleton != null){
				
				//target = enemy.pos;//change from budding,shattered
				spend(TICK);
				
				if (!fieldOfView[mySkeleton.pos]){
					
					//if the skeleton is not next to the enemy
					//teleport them to the closest spot next to the enemy that can be seen
					if (!Dungeon.level.adjacent(mySkeleton.pos, enemy.pos)){
						int telePos = -1;
						for (int c : PathFinder.NEIGHBOURS8){
							if (Actor.findChar(enemy.pos+c) == null
									&& Dungeon.level.passable[enemy.pos+c]
									&& fieldOfView[enemy.pos+c]
									&& Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, telePos)){
								telePos = enemy.pos+c;
							}
						}
						
						if (telePos != -1){
							
							ScrollOfTeleportation.appear(mySkeleton, telePos);
							mySkeleton.teleportSpend();
							
							if (sprite != null && sprite.visible){
								sprite.zap(telePos);
								return false;
							} else {
								onZapComplete();
							}
						}
					}
					
					return true;
					
				} else {
					
					//zap skeleton
					if (mySkeleton.buff(Burning.class) == null) {
						if (sprite != null && sprite.visible){
							sprite.zap(mySkeleton.pos);
							return false;
						} else {
							onZapComplete();
						}
					}
					
				}
				
				return true;
				
			//otherwise, default to regular hunting behaviour
			} else {
				return super.act(enemyInFOV, justAlerted);
			}
		}
	}
	
	public static class NecroSkeleton extends Skeleton {
		
		{
			state = WANDERING;
			
			spriteClass = BombtailSprite.class;
			
			//no loot or exp
			maxLvl = -5;
			
			//20/25 health to start
			HP = 6;
		}

		@Override
		public float spawningWeight() {
			return 0;
		}

		private void teleportSpend(){
			spend(TICK);
		}
		
		public static class NecroSkeletonSprite extends BombtailSprite {
			
			public NecroSkeletonSprite(){
				super();
				brightness(0.75f);
			}
			
			@Override
			public void resetColor() {
				super.resetColor();
				brightness(0.75f);
			}
		}
		
	}
}
