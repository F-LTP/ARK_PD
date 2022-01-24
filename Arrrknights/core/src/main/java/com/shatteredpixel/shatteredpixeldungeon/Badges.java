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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Thunderbolt;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Badges {

	public static boolean Destoryd = false;
	
	public enum Badge {
		MASTERY_WARRIOR,
		MASTERY_MAGE,
		MASTERY_ROGUE,
		MASTERY_HUNTRESS,
		MASTERY_ROSECAT,
		MASTERY_NEARL,
//한 줄당 16칸, 모든 위치는 현재 좌표보다 1을 빼서 추가할것
		//bronze
		UNLOCK_MAGE                 ( 1 ),
		UNLOCK_ROGUE                ( 2 ),
		UNLOCK_HUNTRESS             ( 3 ),
		UNLOCK_ROSECAT             ( 19 ),
		UNLOCK_NEARL               (20),
		MONSTERS_SLAIN_1            ( 4 ),
		MONSTERS_SLAIN_2            ( 5 ),
		GOLD_COLLECTED_1            ( 6 ),
		GOLD_COLLECTED_2            ( 7 ),
		ITEM_LEVEL_1                ( 8 ),
		LEVEL_REACHED_1             ( 9 ),
		STRENGTH_ATTAINED_1         ( 10 ),
		FOOD_EATEN_1                ( 11 ),
		POTIONS_COOKED_1            ( 12 ),
		BOSS_SLAIN_1                ( 13 ),
		DEATH_FROM_FIRE             ( 14 ),
		DEATH_FROM_POISON           ( 15 ),
		DEATH_FROM_GAS              ( 16 ),
		DEATH_FROM_HUNGER           ( 17 ),
		DEATH_FROM_FALLING          ( 18 ),

		//silver
		NO_MONSTERS_SLAIN           ( 32 ),
		GRIM_WEAPON                 ( 33 ),
		MONSTERS_SLAIN_3            ( 34 ),
		MONSTERS_SLAIN_4            ( 35 ),
		GOLD_COLLECTED_3            ( 36 ),
		GOLD_COLLECTED_4            ( 37 ),
		ITEM_LEVEL_2                ( 38 ),
		ITEM_LEVEL_3                ( 39 ),
		LEVEL_REACHED_2             ( 40 ),
		LEVEL_REACHED_3             ( 41 ),
		STRENGTH_ATTAINED_2         ( 42 ),
		STRENGTH_ATTAINED_3         ( 43 ),
		FOOD_EATEN_2                ( 44 ),
		FOOD_EATEN_3                ( 45 ),
		POTIONS_COOKED_2            ( 46 ),
		POTIONS_COOKED_3            ( 47 ),
		BOSS_SLAIN_2                ( 48 ),
		BOSS_SLAIN_3                ( 49 ),
		ALL_POTIONS_IDENTIFIED      ( 50 ),
		ALL_SCROLLS_IDENTIFIED      ( 51 ),
		DEATH_FROM_GLYPH            ( 52 ),
		BOSS_SLAIN_1_WARRIOR,
		BOSS_SLAIN_1_MAGE,
		BOSS_SLAIN_1_ROGUE,
		BOSS_SLAIN_1_HUNTRESS,
		BOSS_SLAIN_1_ROSECAT,
		BOSS_SLAIN_1_NEARL,
		BOSS_SLAIN_1_ALL_CLASSES    ( 52, true ),
		GAMES_PLAYED_1              ( 53, true ),

		//gold
		PIRANHAS                    ( 64 ),
		//these names are a bit outdated, but it doesn't really matter.
		BAG_BOUGHT_SEED_POUCH,
		BAG_BOUGHT_SCROLL_HOLDER,
		BAG_BOUGHT_POTION_BANDOLIER,
		BAG_BOUGHT_WAND_HOLSTER,
		ALL_BAGS_BOUGHT             ( 65 ),
		MASTERY_COMBO               ( 66 ),
		ITEM_LEVEL_4                ( 67 ),
		LEVEL_REACHED_4             ( 68 ),
		STRENGTH_ATTAINED_4         ( 69 ),
		FOOD_EATEN_4                ( 70 ),
		POTIONS_COOKED_4            ( 71 ),
		BOSS_SLAIN_4                ( 72 ),
		ALL_WEAPONS_IDENTIFIED      ( 73 ),
		ALL_SKILLBOOK_IDENTIFIED        ( 74 ),
		ALL_WANDS_IDENTIFIED        ( 75 ),
		ALL_RINGS_IDENTIFIED        ( 76 ),
		ALL_ARTIFACTS_IDENTIFIED    ( 77 ),
		VICTORY                     ( 78 ),
		YASD                        ( 79, true ),
		BOSS_SLAIN_3_GLADIATOR,
		BOSS_SLAIN_3_BERSERKER,
		BOSS_SLAIN_3_WARLOCK,
		BOSS_SLAIN_3_BATTLEMAGE,
		BOSS_SLAIN_3_FREERUNNER,
		BOSS_SLAIN_3_ASSASSIN,
		BOSS_SLAIN_3_SNIPER,
		BOSS_SLAIN_3_WARDEN,
		BOSS_SLAIN_3_DESTROY,
		BOSS_SLAIN_3_GUARDIAN,
		BOSS_SLAIN_3_WAR,
		BOSS_SLAIN_3_KNIGHT,
		BOSS_SLAIN_3_SAVIOR,
		BOSS_SLAIN_3_ALL_SUBCLASSES ( 80, true ),
		GAMES_PLAYED_2              ( 81, true ),

		BLAZE_CHAMPION1              ( 82, true ),
		AMIYA_CHAMPION1              ( 83, true ),
		RED_CHAMPION1              ( 84, true ),
		GREY_CHAMPION1              ( 85, true ),
		ROSE_CHAMPION1              ( 86, true ),
		NEARL_CHAMPION1              ( 87, true ),

		GOLD_COLLECTED_5              ( 89 ),

		SIESTA_PART1              ( 90 ),
		GAVIAL_PART1              ( 90 ),

		//RED , 버그 유저
		BUG (176),

		//platinum
		HAPPY_END                   ( 96 ),
		ALL_ITEMS_IDENTIFIED        ( 97, true ),
		VICTORY_WARRIOR,
		VICTORY_MAGE,
		VICTORY_ROGUE,
		VICTORY_HUNTRESS,
		VICTORY_ROSECAT,
		VICTORY_NEARL,
		VICTORY_ALL_CLASSES         ( 98, true ),
		GAMES_PLAYED_3              ( 99, true ),
		CHAMPION_1                  ( 100 ),
		FRAGGING                       ( 101 ),
		PRAY (102),

		BLAZE_CHAMPION2              ( 104, true ),
		AMIYA_CHAMPION2              ( 105, true ),
		RED_CHAMPION2              ( 106, true ),
		GREY_CHAMPION2              ( 107, true ),
		ROSE_CHAMPION2              ( 108, true ),
		NEARL_CHAMPION2              ( 109, true ),

		SIESTA_PART2              ( 111),
		GAVIAL_PART2              ( 111 ),

		//diamond
		GAMES_PLAYED_4              ( 112, true ),
		CHAMPION_2                  ( 113 ),
		CHAMPION_3                  ( 114 ),
		CHAMPION_4                  ( 114 ),
		ROARINGFLARE                  ( 115 ),
		EVILTIME_END                  ( 116 ),
		WILL    ( 117 ),

		BLAZE_CHAMPION3              ( 120, true ),
		AMIYA_CHAMPION3              ( 121, true ),
		RED_CHAMPION3             ( 122, true ),
		GREY_CHAMPION3              ( 123, true ),
		ROSE_CHAMPION3              ( 124, true ),
		NEARL_CHAMPION3              ( 125, true ),

		//Puple
		Get_25_STONES                (136),
		Get_40_STONES                (137),
		DOLL_COLLECTOR                (138),
		SLAIN_PURSUER				(139),

		//GREEN
		SKIN_BABOSKADI(160, false, true),
		SKIN_TALU(161, false, true),
		SKIN_NOVA(162, false, true),
		SKIN_SUSUU(163, false, true),
		SKIN_GRN(164, false, true),
		SKIN_LAPPY(165,false,true),
		SKIN_JESSI(166, false, true),
		SKIN_LEAF(167, false, true),
		SKIN_MUDROCK(168, false, true),
		SKIN_ASTESIA(170, false, true),
		SKIN_SPECTER(169, false, true),
		SKIN_SCHWARZ(171, false, true),

		//기타
		SUPPORT;

		public boolean meta;
		public boolean skin;

		public int image;
		
		Badge( int image ) {
			this( image, false, false );
		}
		
		Badge( int image, boolean meta ) {
			this(image, true, false);
		}

		Badge( int image, boolean meta, boolean isSkin ) {
			this.image = image;
			this.meta = meta;
			this.skin = isSkin;
		}

		public String desc(){
			return Messages.get(this, name());
		}

		public String Skindesc_change(){
			return Messages.get(this, "change");
		}

		public String Skindesc_lock(){
			return Messages.get(this, "skinlock");
		}

		public String Skindesc_Default(){
			return Messages.get(this, "default");
		}
		
		Badge() {
			this( -1 );
		}
	}
	
	private static HashSet<Badge> global;
	private static HashSet<Badge> local = new HashSet<>();
	
	private static boolean saveNeeded = false;

	public static void reset() {
		local.clear();
		loadGlobal();
	}
	
	public static final String BADGES_FILE	= "badges.dat";
	private static final String BADGES		= "badges";
	
	private static final HashSet<String> removedBadges = new HashSet<>();
	static{
		//no recently removed badges
	}

	private static final HashMap<String, String> renamedBadges = new HashMap<>();
	static{
		//no recently renamed badges
	}

	public static HashSet<Badge> restore( Bundle bundle ) {
		HashSet<Badge> badges = new HashSet<>();
		if (bundle == null) return badges;
		
		String[] names = bundle.getStringArray( BADGES );
		for (int i=0; i < names.length; i++) {
			try {
				if (renamedBadges.containsKey(names[i])){
					names[i] = renamedBadges.get(names[i]);
				}
				if (!removedBadges.contains(names[i])){
					badges.add( Badge.valueOf( names[i] ) );
				}
			} catch (Exception e) {
				TomorrowRogueNight.reportException(e);
			}
		}

		addReplacedBadges(badges);
	
		return badges;
	}
	
	public static void store( Bundle bundle, HashSet<Badge> badges ) {
		addReplacedBadges(badges);

		int count = 0;
		String names[] = new String[badges.size()];
		
		for (Badge badge:badges) {
			names[count++] = badge.toString();
		}
		bundle.put( BADGES, names );
	}
	
	public static void loadLocal( Bundle bundle ) {
		local = restore( bundle );
	}
	
	public static void saveLocal( Bundle bundle ) {
		store( bundle, local );
	}
	
	public static void loadGlobal() {
		if (global == null) {
			try {
				Bundle bundle = FileUtils.bundleFromFile( BADGES_FILE );
				global = restore( bundle );

			} catch (IOException e) {
				global = new HashSet<>();
			}
		}
	}


	public static void DestroyGlobal() {
		if(!global.contains(Badge.BUG)) {
			global.add(Badge.BUG);
			saveNeeded = true;
		}

		if(global.contains(Badge.HAPPY_END)) {
			global.remove(Badge.HAPPY_END);
		}

		local.clear();
		loadGlobal();
	}


	public static void DestroyBUG() {
		if(global.contains(Badge.BUG)) {
			global.remove(Badge.BUG);
		}
	}

	public static void saveGlobal() {
		if (saveNeeded) {
			
			Bundle bundle = new Bundle();
			store( bundle, global );
			
			try {
				FileUtils.bundleToFile(BADGES_FILE, bundle);
				saveNeeded = false;
			} catch (IOException e) {
				TomorrowRogueNight.reportException(e);
			}
		}
	}

	public static int unlocked(boolean global){
		if (global) return Badges.global.size();
		else        return Badges.local.size();
	}

	public static void validateMonstersSlain() {
		Badge badge = null;
		
		if (!local.contains( Badge.MONSTERS_SLAIN_1 ) && Statistics.enemiesSlain >= 10) {
			badge = Badge.MONSTERS_SLAIN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_2 ) && Statistics.enemiesSlain >= 50) {
			badge = Badge.MONSTERS_SLAIN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_3 ) && Statistics.enemiesSlain >= 150) {
			badge = Badge.MONSTERS_SLAIN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_4 ) && Statistics.enemiesSlain >= 250) {
			badge = Badge.MONSTERS_SLAIN_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateGoldCollected() {
		Badge badge = null;
		
		if (!local.contains( Badge.GOLD_COLLECTED_1 ) && Statistics.goldCollected >= 100) {
			badge = Badge.GOLD_COLLECTED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_2 ) && Statistics.goldCollected >= 500) {
			badge = Badge.GOLD_COLLECTED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_3 ) && Statistics.goldCollected >= 2500) {
			badge = Badge.GOLD_COLLECTED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_4 ) && Statistics.goldCollected >= 7500) {
			badge = Badge.GOLD_COLLECTED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_5 ) && Statistics.goldCollected >= 15000) {
			badge = Badge.GOLD_COLLECTED_5;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateLevelReached() {
		Badge badge = null;
		
		if (!local.contains( Badge.LEVEL_REACHED_1 ) && Dungeon.hero.lvl >= 6) {
			badge = Badge.LEVEL_REACHED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_2 ) && Dungeon.hero.lvl >= 12) {
			badge = Badge.LEVEL_REACHED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_3 ) && Dungeon.hero.lvl >= 18) {
			badge = Badge.LEVEL_REACHED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_4 ) && Dungeon.hero.lvl >= 24) {
			badge = Badge.LEVEL_REACHED_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateStrengthAttained() {
		Badge badge = null;
		
		if (!local.contains( Badge.STRENGTH_ATTAINED_1 ) && Dungeon.hero.STR >= 13) {
			badge = Badge.STRENGTH_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_2 ) && Dungeon.hero.STR >= 15) {
			badge = Badge.STRENGTH_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_3 ) && Dungeon.hero.STR >= 17) {
			badge = Badge.STRENGTH_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_4 ) && Dungeon.hero.STR >= 19) {
			badge = Badge.STRENGTH_ATTAINED_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateFoodEaten() {
		Badge badge = null;
		
		if (!local.contains( Badge.FOOD_EATEN_1 ) && Statistics.foodEaten >= 10) {
			badge = Badge.FOOD_EATEN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_2 ) && Statistics.foodEaten >= 20) {
			badge = Badge.FOOD_EATEN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_3 ) && Statistics.foodEaten >= 30) {
			badge = Badge.FOOD_EATEN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_4 ) && Statistics.foodEaten >= 40) {
			badge = Badge.FOOD_EATEN_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validatePotionsCooked() {
		Badge badge = null;
		
		if (!local.contains( Badge.POTIONS_COOKED_1 ) && Statistics.potionsCooked >= 3) {
			badge = Badge.POTIONS_COOKED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_2 ) && Statistics.potionsCooked >= 6) {
			badge = Badge.POTIONS_COOKED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_3 ) && Statistics.potionsCooked >= 9) {
			badge = Badge.POTIONS_COOKED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_4 ) && Statistics.potionsCooked >= 12) {
			badge = Badge.POTIONS_COOKED_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validatePiranhasKilled() {
		Badge badge = null;
		
		if (!local.contains( Badge.PIRANHAS ) && Statistics.piranhasKilled >= 6) {
			badge = Badge.PIRANHAS;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateItemLevelAquired( Item item ) {
		
		// This method should be called:
		// 1) When an item is obtained (Item.collect)
		// 2) When an item is upgraded (ScrollOfUpgrade, ScrollOfWeaponUpgrade, ShortSword, WandOfMagicMissile)
		// 3) When an item is identified

		// Note that artifacts should never trigger this badge as they are alternatively upgraded
		if (!item.levelKnown || item instanceof Artifact) {
			return;
		}
		
		Badge badge = null;
		if (!local.contains( Badge.ITEM_LEVEL_1 ) && item.level() >= 3) {
			badge = Badge.ITEM_LEVEL_1;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_2 ) && item.level() >= 6) {
			badge = Badge.ITEM_LEVEL_2;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_3 ) && item.level() >= 9) {
			badge = Badge.ITEM_LEVEL_3;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_4 ) && item.level() >= 12) {
			badge = Badge.ITEM_LEVEL_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateAllBagsBought( Item bag ) {
		
		Badge badge = null;
		if (bag instanceof VelvetPouch) {
			badge = Badge.BAG_BOUGHT_SEED_POUCH;
		} else if (bag instanceof ScrollHolder) {
			badge = Badge.BAG_BOUGHT_SCROLL_HOLDER;
		} else if (bag instanceof PotionBandolier) {
			badge = Badge.BAG_BOUGHT_POTION_BANDOLIER;
		} else if (bag instanceof MagicalHolster) {
			badge = Badge.BAG_BOUGHT_WAND_HOLSTER;
		}
		
		if (badge != null) {
			
			local.add( badge );
			
			if (!local.contains( Badge.ALL_BAGS_BOUGHT ) &&
				local.contains( Badge.BAG_BOUGHT_SEED_POUCH ) &&
				local.contains( Badge.BAG_BOUGHT_SCROLL_HOLDER ) &&
				local.contains( Badge.BAG_BOUGHT_POTION_BANDOLIER ) &&
				local.contains( Badge.BAG_BOUGHT_WAND_HOLSTER )) {
						
					badge = Badge.ALL_BAGS_BOUGHT;
					local.add( badge );
					displayBadge( badge );
			}
		}
	}
	
	public static void validateItemsIdentified() {
		
		for (Catalog cat : Catalog.values()){
			if (cat.allSeen()){
				Badge b = Catalog.catalogBadges.get(cat);
				if (!global.contains(b)){
					displayBadge(b);
				}
			}
		}
		
		if (!global.contains( Badge.ALL_ITEMS_IDENTIFIED ) &&
			global.contains( Badge.ALL_WEAPONS_IDENTIFIED ) &&
			global.contains( Badge.ALL_SKILLBOOK_IDENTIFIED ) &&
			global.contains( Badge.ALL_WANDS_IDENTIFIED ) &&
			global.contains( Badge.ALL_RINGS_IDENTIFIED ) &&
			global.contains( Badge.ALL_ARTIFACTS_IDENTIFIED ) &&
			global.contains( Badge.ALL_POTIONS_IDENTIFIED ) &&
			global.contains( Badge.ALL_SCROLLS_IDENTIFIED )) {
			
			displayBadge( Badge.ALL_ITEMS_IDENTIFIED );
		}
	}
	
	public static void validateDeathFromFire() {
		Badge badge = Badge.DEATH_FROM_FIRE;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromPoison() {
		Badge badge = Badge.DEATH_FROM_POISON;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromGas() {
		Badge badge = Badge.DEATH_FROM_GAS;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromHunger() {
		Badge badge = Badge.DEATH_FROM_HUNGER;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromGlyph() {
		Badge badge = Badge.DEATH_FROM_GLYPH;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}
	
	public static void validateDeathFromFalling() {
		Badge badge = Badge.DEATH_FROM_FALLING;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}
	
	private static void validateYASD() {
		if (global.contains( Badge.DEATH_FROM_FIRE ) &&
			global.contains( Badge.DEATH_FROM_POISON ) &&
			global.contains( Badge.DEATH_FROM_GAS ) &&
			global.contains( Badge.DEATH_FROM_HUNGER) &&
			global.contains( Badge.DEATH_FROM_GLYPH) &&
			global.contains( Badge.DEATH_FROM_FALLING)) {
			
			Badge badge = Badge.YASD;
			displayBadge( badge );
		}
	}
	
	public static void validateBossSlain() {
		Badge badge = null;
		switch (Dungeon.depth) {
		case 5:
			badge = Badge.BOSS_SLAIN_1;
			break;
		case 10:
			badge = Badge.BOSS_SLAIN_2;
			break;
		case 15:
			badge = Badge.BOSS_SLAIN_3;
			break;
		case 20:
			badge = Badge.BOSS_SLAIN_4;
			break;
		}
		
		if (badge != null) {
			local.add( badge );
			displayBadge( badge );
			
			if (badge == Badge.BOSS_SLAIN_1) {
				switch (Dungeon.hero.heroClass) {
				case WARRIOR:
					badge = Badge.BOSS_SLAIN_1_WARRIOR;
					break;
				case MAGE:
					badge = Badge.BOSS_SLAIN_1_MAGE;
					break;
				case ROGUE:
					badge = Badge.BOSS_SLAIN_1_ROGUE;
					break;
				case HUNTRESS:
					badge = Badge.BOSS_SLAIN_1_HUNTRESS;
					break;
				case ROSECAT:
					badge = Badge.BOSS_SLAIN_1_ROSECAT;
					break;
				case NEARL:
						badge = Badge.BOSS_SLAIN_1_NEARL;
						break;
				}
				local.add( badge );
				if (!global.contains( badge )) {
					global.add( badge );
					saveNeeded = true;
				}
				
				if (global.contains( Badge.BOSS_SLAIN_1_WARRIOR ) &&
					global.contains( Badge.BOSS_SLAIN_1_MAGE ) &&
					global.contains( Badge.BOSS_SLAIN_1_ROGUE ) &&
					global.contains( Badge.BOSS_SLAIN_1_HUNTRESS) &&
						global.contains( Badge.BOSS_SLAIN_1_ROSECAT) &&
						global.contains( Badge.BOSS_SLAIN_1_NEARL)) {
					
					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!global.contains( badge )) {
						displayBadge( badge );
						global.add( badge );
						saveNeeded = true;
					}
				}
			} else
			if (badge == Badge.BOSS_SLAIN_3) {
				switch (Dungeon.hero.subClass) {
				case GLADIATOR:
					badge = Badge.BOSS_SLAIN_3_GLADIATOR;
					break;
				case BERSERKER:
					badge = Badge.BOSS_SLAIN_3_BERSERKER;
					break;
				case WARLOCK:
					badge = Badge.BOSS_SLAIN_3_WARLOCK;
					break;
				case BATTLEMAGE:
					badge = Badge.BOSS_SLAIN_3_BATTLEMAGE;
					break;
				case FREERUNNER:
					badge = Badge.BOSS_SLAIN_3_FREERUNNER;
					break;
				case ASSASSIN:
					badge = Badge.BOSS_SLAIN_3_ASSASSIN;
					break;
				case SNIPER:
					badge = Badge.BOSS_SLAIN_3_SNIPER;
					break;
				case WARDEN:
					badge = Badge.BOSS_SLAIN_3_WARDEN;
					break;
				case DESTROYER:
					badge = Badge.BOSS_SLAIN_3_DESTROY;
					break;
				case GUARDIAN:
					badge = Badge.BOSS_SLAIN_3_GUARDIAN;
					break;
					case WAR:
						badge = Badge.BOSS_SLAIN_3_WAR;
						break;
					case KNIGHT:
						badge = Badge.BOSS_SLAIN_3_KNIGHT;
						break;
					case SAVIOR:
						badge = Badge.BOSS_SLAIN_3_SAVIOR;
						break;
				default:
					return;
				}
				local.add( badge );
				if (!global.contains( badge )) {
					global.add( badge );
					saveNeeded = true;
				}
				
				if (global.contains( Badge.BOSS_SLAIN_3_GLADIATOR ) &&
					global.contains( Badge.BOSS_SLAIN_3_BERSERKER ) &&
					global.contains( Badge.BOSS_SLAIN_3_WARLOCK ) &&
					global.contains( Badge.BOSS_SLAIN_3_BATTLEMAGE ) &&
					global.contains( Badge.BOSS_SLAIN_3_FREERUNNER ) &&
					global.contains( Badge.BOSS_SLAIN_3_ASSASSIN ) &&
					global.contains( Badge.BOSS_SLAIN_3_SNIPER ) &&
					global.contains( Badge.BOSS_SLAIN_3_WARDEN ) &&
					global.contains( Badge.BOSS_SLAIN_3_DESTROY ) &&
					global.contains( Badge.BOSS_SLAIN_3_GUARDIAN ) &&
						global.contains( Badge.BOSS_SLAIN_3_WAR ) &&
						global.contains( Badge.BOSS_SLAIN_3_KNIGHT ) &&
						global.contains( Badge.BOSS_SLAIN_3_SAVIOR )) {
					
					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!global.contains( badge )) {
						displayBadge( badge );
						global.add( badge );
						saveNeeded = true;
					}
				}
			}
		}
	}
	
	public static void validateMastery() {
		
		Badge badge = null;
		switch (Dungeon.hero.heroClass) {
		case WARRIOR:
			badge = Badge.MASTERY_WARRIOR;
			break;
		case MAGE:
			badge = Badge.MASTERY_MAGE;
			break;
		case ROGUE:
			badge = Badge.MASTERY_ROGUE;
			break;
		case HUNTRESS:
			badge = Badge.MASTERY_HUNTRESS;
			break;
		case ROSECAT:
			badge = Badge.MASTERY_ROSECAT;
			break;
		case NEARL:
			badge = Badge.MASTERY_NEARL;
			break;
		}
		
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}
	}
	
	public static void validateMageUnlock(){
		if (Statistics.upgradesUsed >= 1 && !global.contains(Badge.UNLOCK_MAGE)){
			displayBadge( Badge.UNLOCK_MAGE );
		}
	}
	
	public static void validateRogueUnlock(){
		if (Statistics.sneakAttacks >= 10 && !global.contains(Badge.UNLOCK_ROGUE)){
			displayBadge( Badge.UNLOCK_ROGUE );
		}
	}
	
	public static void validateHuntressUnlock(){
		if (Statistics.thrownAssists >= 15 && !global.contains(Badge.UNLOCK_HUNTRESS)){
			displayBadge( Badge.UNLOCK_HUNTRESS );
		}
	}

	public static void validateRoseUnlock(){
		if (Statistics.foodEaten >= 25 && !global.contains(Badge.UNLOCK_ROSECAT)){
			displayBadge( Badge.UNLOCK_ROSECAT );}
	}

	public static void validateNearlUnlock(){
		if (Statistics.enemiesSlain >= 200 && !global.contains(Badge.UNLOCK_NEARL)){
			displayBadge( Badge.UNLOCK_NEARL );}
	}
	
	public static void validateMasteryCombo( int n ) {
		if (!local.contains( Badge.MASTERY_COMBO ) && n == 10) {
			Badge badge = Badge.MASTERY_COMBO;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateVictory() {

		Badge badge = Badge.VICTORY;
		displayBadge( badge );

		switch (Dungeon.hero.heroClass) {
		case WARRIOR:
			badge = Badge.VICTORY_WARRIOR;
			break;
		case MAGE:
			badge = Badge.VICTORY_MAGE;
			break;
		case ROGUE:
			badge = Badge.VICTORY_ROGUE;
			break;
		case HUNTRESS:
			badge = Badge.VICTORY_HUNTRESS;
			break;
		case ROSECAT:
			badge = Badge.VICTORY_ROSECAT;
			break;
			case NEARL:
			badge = Badge.VICTORY_NEARL;
			break;
		}
		local.add( badge );
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}
		
		if (global.contains( Badge.VICTORY_WARRIOR ) &&
			global.contains( Badge.VICTORY_MAGE ) &&
			global.contains( Badge.VICTORY_ROGUE ) &&
			global.contains( Badge.VICTORY_HUNTRESS ) &&
			global.contains( Badge.VICTORY_ROSECAT ) &&
		   global.contains( Badge.VICTORY_NEARL )){
			
			badge = Badge.VICTORY_ALL_CLASSES;
			displayBadge( badge );
		}
	}

	public static void validateNoKilling() {
		if (!global.contains( Badge.NO_MONSTERS_SLAIN ) && Statistics.completedWithNoKilling) {
			Badge badge = Badge.NO_MONSTERS_SLAIN;
			global.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateGrimWeapon() {
		if (!local.contains( Badge.GRIM_WEAPON )) {
			Badge badge = Badge.GRIM_WEAPON;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateGamesPlayed() {
		Badge badge = null;
		if (Rankings.INSTANCE.totalNumber >= 10) {
			badge = Badge.GAMES_PLAYED_1;
		}
		if (Rankings.INSTANCE.totalNumber >= 50) {
			badge = Badge.GAMES_PLAYED_2;
		}
		if (Rankings.INSTANCE.totalNumber >= 250) {
			badge = Badge.GAMES_PLAYED_3;
		}
		if (Rankings.INSTANCE.totalNumber >= 1000) {
			badge = Badge.GAMES_PLAYED_4;
		}
		
		displayBadge( badge );
	}

	public static void validateskadiskin() {
		Badge badge = Badge.SKIN_BABOSKADI;
		displayBadge( badge );
	}

	public static void validategrnskin() {
		Badge badge = Badge.SKIN_GRN;
		displayBadge( badge );
	}

	public static void validatejessiskin() {
		Badge badge = Badge.SKIN_JESSI;
		displayBadge( badge );
	}

	public static void validatelappyskin() {
		Badge badge = Badge.SKIN_LAPPY;
		displayBadge( badge );
	}

	public static void validatetaluskin() {
		Badge badge = Badge.SKIN_TALU;
		displayBadge( badge );
	}

	public static void validatenovaskin() {
		Badge badge = Badge.SKIN_NOVA;
		displayBadge( badge );
	}

	public static void validatesusuuskin() {
		Badge badge = Badge.SKIN_SUSUU;
		displayBadge( badge );
	}

	public static void validateleafskin() {
		Badge badge = Badge.SKIN_LEAF;
		displayBadge( badge );
	}

	public static void validateRockskin() {
		Badge badge = Badge.SKIN_MUDROCK;
		displayBadge( badge );
	}

	public static void validateAstesiaskin() {
		Badge badge = Badge.SKIN_ASTESIA;
		displayBadge( badge );
	}

	public static void validatesameskin() {
		Badge badge = Badge.SKIN_SPECTER;
		displayBadge( badge );
	}

	public static void validateschwazrskin() {
		Badge badge = Badge.SKIN_SCHWARZ;
		displayBadge( badge );
	}

	// 0.3.2버전의 스킨 관련 처리로 인해 추가된 구문입니다. 추후 필요없어질 수 있습니다.
	public static void allskindestroy() {
		saveNeeded = true;
		if(global.contains(Badge.SKIN_TALU)) {
			global.remove(Badge.SKIN_TALU);
		}
		if(global.contains(Badge.SKIN_NOVA)) {
			global.remove(Badge.SKIN_NOVA);
		}
		if(global.contains(Badge.SKIN_BABOSKADI)) {
			global.remove(Badge.SKIN_BABOSKADI);
		}
		if(global.contains(Badge.SKIN_GRN)) {
			global.remove(Badge.SKIN_GRN);
		}
		if(global.contains(Badge.SKIN_JESSI)) {
			global.remove(Badge.SKIN_JESSI);
		}
		if(global.contains(Badge.SKIN_SUSUU)) {
			global.remove(Badge.SKIN_SUSUU);
		}
		if(global.contains(Badge.SKIN_LAPPY)) {
			global.remove(Badge.SKIN_LAPPY);
		}
		if(global.contains(Badge.SKIN_LEAF)) {
			global.remove(Badge.SKIN_LEAF);
		}
		if(global.contains(Badge.SKIN_MUDROCK)) {
			global.remove(Badge.SKIN_MUDROCK);
		}
		if(global.contains(Badge.SKIN_SPECTER)) {
			global.remove(Badge.SKIN_SPECTER);
		}
		if(global.contains(Badge.SKIN_ASTESIA)) {
			global.remove(Badge.SKIN_ASTESIA);
		}
		if(global.contains(Badge.SKIN_SCHWARZ)) {
			global.remove(Badge.SKIN_SCHWARZ);
		}

		if (Badges.isUnlocked(Badge.SUPPORT)) {
			Badges.validatetaluskin();
			Badges.validatenovaskin();
			Badges.validateskadiskin();
			Badges.validatesusuuskin();
			Badges.validategrnskin();
			Badges.validatejessiskin();
			Badges.validatelappyskin();
			Badges.validateleafskin();
			Badges.validateRockskin();
			Badges.validatesameskin();
			Badges.validateAstesiaskin();
			Badges.validateschwazrskin();
		}

		// 배지 파괴 후, 조건에 맞는 배지는 다시 획득합니다.
		if(!Badges.isUnlocked(Badges.Badge.SKIN_TALU) && Badges.isUnlocked(Badges.Badge.EVILTIME_END)) {Badges.validatetaluskin();}
		if(!Badges.isUnlocked(Badges.Badge.SKIN_NOVA) && Badges.isUnlocked(Badges.Badge.FRAGGING)) { Badges.validatenovaskin();}
		if(!Badges.isUnlocked(Badges.Badge.SKIN_BABOSKADI) && Badges.isUnlocked(Badges.Badge.GAMES_PLAYED_2)) { Badges.validateskadiskin();}
		if(!Badges.isUnlocked(Badges.Badge.SKIN_SUSUU) && Badges.isUnlocked(Badges.Badge.ALL_POTIONS_IDENTIFIED)) { Badges.validatesusuuskin();}
		if(!Badges.isUnlocked(Badges.Badge.SKIN_GRN) && Badges.isUnlocked(Badges.Badge.GAMES_PLAYED_1)) { Badges.validategrnskin();}
		if(!Badges.isUnlocked(Badges.Badge.SKIN_JESSI) && Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE)) { Badges.validatejessiskin();}
		if(!Badges.isUnlocked(Badges.Badge.SKIN_LAPPY) && Badges.isUnlocked(Badges.Badge.HAPPY_END)) { Badges.validatelappyskin();}
		if(!Badges.isUnlocked(Badge.SKIN_LEAF) && Badges.isUnlocked(Badge.CHAMPION_1)) { Badges.validateleafskin();}
		if(!Badges.isUnlocked(Badge.SKIN_MUDROCK) && Badges.isUnlocked(Badge.BOSS_SLAIN_3_ALL_SUBCLASSES)) { Badges.validateRockskin();}
		if(!Badges.isUnlocked(Badge.SKIN_SPECTER) && Badges.isUnlocked(Badge.GAMES_PLAYED_1)) { Badges.validatesameskin();}
		if(!Badges.isUnlocked(Badge.SKIN_ASTESIA) && Badges.isUnlocked(Badge.SLAIN_PURSUER)) { Badges.validateAstesiaskin();}
		if(!Badges.isUnlocked(Badge.SKIN_SCHWARZ) && Badges.isUnlocked(Badge.SIESTA_PART2)) { Badges.validateschwazrskin();}
	}

	//necessary in order to display the happy end badge in the surface scene
	public static void silentValidateHappyEnd() {
		if (!local.contains( Badge.HAPPY_END )){
			local.add( Badge.HAPPY_END );
		}
	}

	//하극상 배지용
	public static void silentValidateFragging() {
		if (!local.contains( Badge.FRAGGING )){
			local.add( Badge.FRAGGING );
		}
	}
	
	public static void validateHappyEnd() {
		displayBadge( Badge.HAPPY_END );
	}
	public static void validateFragging() {
		displayBadge( Badge.FRAGGING );
	}

	public static void validateChampion( int challenges ) {
		if (challenges == 0) return;
		Badge badge = null;
		if (challenges >= 1) {
			badge = Badge.CHAMPION_1;
		}
		if (challenges >= 3){
			if (!global.contains(badge)){
				global.add(badge);
				saveNeeded = true;
			}
			badge = Badge.CHAMPION_2;
		}
		if (challenges >= 6){
			if (!global.contains(badge)){
				global.add(badge);
				saveNeeded = true;
			}
			badge = Badge.CHAMPION_3;
		}
		if (challenges >= 8){
			if (!global.contains(badge)){
				global.add(badge);
				saveNeeded = true;
			}
			badge = Badge.CHAMPION_4;
		}
		local.add(badge);
		displayBadge( badge );
	}

	public static void validateChampion_char( int challenges ) {
		if (challenges == 0) return;
		Badge badge = null;

		switch (Dungeon.hero.heroClass) {
			case WARRIOR:
				if (challenges >= 1) {
					badge = Badge.BLAZE_CHAMPION1;
				}
				if (challenges >= 3) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.BLAZE_CHAMPION2;
				}
				if (challenges >= 6) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.BLAZE_CHAMPION3;
				}
				break;
			case MAGE:
				if (challenges >= 1) {
					badge = Badge.AMIYA_CHAMPION1;
				}
				if (challenges >= 3) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.AMIYA_CHAMPION2;
				}
				if (challenges >= 6) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.AMIYA_CHAMPION3;
				}
				break;
			case ROGUE:
				if (challenges >= 1) {
					badge = Badge.RED_CHAMPION1;
				}
				if (challenges >= 3) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.RED_CHAMPION2;
				}
				if (challenges >= 6) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.RED_CHAMPION3;
				}
				break;
			case HUNTRESS:
				if (challenges >= 1) {
					badge = Badge.GREY_CHAMPION1;
				}
				if (challenges >= 3) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.GREY_CHAMPION2;
				}
				if (challenges >= 6) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.GREY_CHAMPION3;
				}
				break;
			case ROSECAT:
				if (challenges >= 1) {
					badge = Badge.ROSE_CHAMPION1;
				}
				if (challenges >= 3) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.ROSE_CHAMPION2;
				}
				if (challenges >= 6) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.ROSE_CHAMPION3;
				}
				break;
			case NEARL:
				if (challenges >= 1) {
					badge = Badge.NEARL_CHAMPION1;
				}
				if (challenges >= 3) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.NEARL_CHAMPION2;
				}
				if (challenges >= 6) {
					if (!global.contains(badge)){
						global.add(badge);
						saveNeeded = true;
					}
					badge = Badge.NEARL_CHAMPION3;
				}
				break;
			default:
				break;
		}

		if (badge != null) {
		local.add(badge);
		displayBadge( badge ); }
	}

	public static void validateroaringflare() {
		if (!local.contains( Badge.ROARINGFLARE )){
			global.add(Badge.ROARINGFLARE);
			saveNeeded = true;
			local.add(Badge.ROARINGFLARE);
		displayBadge( Badge.ROARINGFLARE );}
	}

	public static void validateeviltimeend() {
		if (!local.contains( Badge.EVILTIME_END )){
			global.add(Badge.EVILTIME_END);
			saveNeeded = true;
			local.add(Badge.EVILTIME_END);
		displayBadge( Badge.EVILTIME_END );}
	}

	public static void validatesiesta1() {
		if (!local.contains( Badge.SIESTA_PART1 )){
			global.add(Badge.SIESTA_PART1);
			saveNeeded = true;
			local.add(Badge.SIESTA_PART1);
			displayBadge( Badge.SIESTA_PART1 );}
	}

	public static void validatesiesta2() {
		if (!local.contains( Badge.SIESTA_PART2 )){
			global.add(Badge.SIESTA_PART2);
			saveNeeded = true;
			local.add(Badge.SIESTA_PART2);
			displayBadge( Badge.SIESTA_PART2 );}
	}

	public static void validategavial1() {
		if (!local.contains( Badge.GAVIAL_PART1 )){
			global.add(Badge.GAVIAL_PART1);
			saveNeeded = true;
			local.add(Badge.GAVIAL_PART1);
			displayBadge( Badge.GAVIAL_PART1 );}
	}

	public static void validategavial2() {
		if (!local.contains( Badge.GAVIAL_PART2 )){
			global.add(Badge.GAVIAL_PART2);
			saveNeeded = true;
			local.add(Badge.GAVIAL_PART2);
			displayBadge( Badge.GAVIAL_PART2 );}
	}

	public static void validatepursuerkill() {
		if (!local.contains( Badge.SLAIN_PURSUER )){
			global.add(Badge.SLAIN_PURSUER);
			saveNeeded = true;
			local.add(Badge.SLAIN_PURSUER);
			displayBadge( Badge.SLAIN_PURSUER );}
	}

	public static void validatewill() {
		if (!local.contains( Badge.WILL )){
			global.add(Badge.WILL);
			saveNeeded = true;
			local.add(Badge.WILL);
			displayBadge( Badge.WILL );}
	}

	public static void validatestone25()
	{
		if (!local.contains( Badge.Get_25_STONES )){
			global.add(Badge.Get_25_STONES);
			saveNeeded = true;
			local.add(Badge.Get_25_STONES);
			displayBadge( Badge.Get_25_STONES );}
	}

	public static void validatestone40()
	{
		if (!local.contains( Badge.Get_40_STONES )){
			global.add(Badge.Get_40_STONES);
			saveNeeded = true;
			local.add(Badge.Get_40_STONES);
			displayBadge( Badge.Get_40_STONES );}
	}

	public static void validatepray() {
		if (!local.contains( Badge.PRAY )){
			global.add(Badge.PRAY);
			saveNeeded = true;
			local.add(Badge.PRAY);
			displayBadge( Badge.PRAY );}
	}

	public static void validatedoll() {
		if (!local.contains( Badge.DOLL_COLLECTOR )){
			global.add(Badge.DOLL_COLLECTOR);
			saveNeeded = true;
			local.add(Badge.DOLL_COLLECTOR);
			displayBadge( Badge.DOLL_COLLECTOR );}
	}


	public static boolean isdollcollector() {
		return local.contains( Badge.DOLL_COLLECTOR) ;
	}


	private static void displayBadge( Badge badge ) {
		
		if (badge == null) {
			return;
		}
		
		if (global.contains( badge )) {
			
			if (!badge.meta) {
				GLog.h( Messages.get(Badges.class, "endorsed", badge.desc()) );
			}
			
		} else {
			
			global.add( badge );
			saveNeeded = true;
			
			if (badge.meta) {
				GLog.h( Messages.get(Badges.class, "new_super", badge.desc()) );
			} else {
				GLog.h( Messages.get(Badges.class, "new", badge.desc()) );
			}
			PixelScene.showBadge( badge );
		}
	}
	
	public static boolean isUnlocked( Badge badge ) {
		return global.contains( badge );
	}
	
	public static HashSet<Badge> allUnlocked(){
		loadGlobal();
		return new HashSet<>(global);
	}
	
	public static void disown( Badge badge ) {
		loadGlobal();
		global.remove( badge );
		saveNeeded = true;
	}
	
	public static void addGlobal( Badge badge ){
		if (!global.contains(badge)){
			global.add( badge );
			saveNeeded = true;
		}
	}

	public static List<Badge> filterReplacedBadges( boolean global ) {

		ArrayList<Badge> badges = new ArrayList<>(global ? Badges.global : Badges.local);

		Iterator<Badge> iterator = badges.iterator();
		while (iterator.hasNext()) {
			Badge badge = iterator.next();
			if ((!global && badge.meta) || badge.skin || badge.image == -1) {
				iterator.remove();
			}
		}

		Collections.sort(badges);

		return filterReplacedBadges(badges);

	}

	public static List<Badge> filterSkinBadges(boolean global ) {

		ArrayList<Badge> badges = new ArrayList<>(global ? Badges.global : Badges.local);

		Iterator<Badge> iterator = badges.iterator();
		while (iterator.hasNext()) {
			Badge badge = iterator.next();
			if ((!global && badge.meta) || !badge.skin || badge.image == -1) {
				iterator.remove();
			}
		}

		Collections.sort(badges);

		return filterSkindBadges(badges);

	}

	private static final Badge[][] tierBadgeReplacements = new Badge[][]{
			{Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4},
			{Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4, Badge.GOLD_COLLECTED_5},
			{Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4},
			{Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4},
			{Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4},
			{Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4},
			{Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4 },
			{Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4, Badge.VICTORY, Badge.VICTORY_ALL_CLASSES},
			{Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4},
			{Badge.CHAMPION_1, Badge.CHAMPION_2, Badge.CHAMPION_3, Badge.CHAMPION_4},
			{Badge.Get_25_STONES, Badge.Get_40_STONES},
			{Badge.BLAZE_CHAMPION1, Badge.BLAZE_CHAMPION2, Badge.BLAZE_CHAMPION3},
			{ Badge.AMIYA_CHAMPION1, Badge.AMIYA_CHAMPION2, Badge.AMIYA_CHAMPION3},
			{Badge.RED_CHAMPION1, Badge.RED_CHAMPION2, Badge.RED_CHAMPION3},
			{Badge.GREY_CHAMPION1, Badge.GREY_CHAMPION2, Badge.GREY_CHAMPION3},
			{ Badge.ROSE_CHAMPION1, Badge.ROSE_CHAMPION2, Badge.ROSE_CHAMPION3},
			{ Badge.NEARL_CHAMPION1, Badge.NEARL_CHAMPION2, Badge.NEARL_CHAMPION3},
			{ Badge.SIESTA_PART1, Badge.SIESTA_PART2},
			{ Badge.GAVIAL_PART1, Badge.GAVIAL_PART2}
	};

	private static final Badge[][] metaBadgeReplacements = new Badge[][]{
			{Badge.DEATH_FROM_FIRE, Badge.YASD},
			{Badge.DEATH_FROM_GAS, Badge.YASD},
			{Badge.DEATH_FROM_HUNGER, Badge.YASD},
			{Badge.DEATH_FROM_POISON, Badge.YASD},
			{Badge.DEATH_FROM_GLYPH, Badge.YASD},
			{Badge.DEATH_FROM_FALLING, Badge.YASD },

			{Badge.ALL_WEAPONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_SKILLBOOK_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_WANDS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARTIFACTS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED}
	};

	private static final Badge[][] skinBadgeReplacements = new Badge[][]{
			{Badge.SKIN_BABOSKADI}, {Badge.SKIN_TALU}, {Badge.SKIN_NOVA}, {Badge.SKIN_SUSUU}, {Badge.SKIN_GRN}, {Badge.SKIN_LAPPY}, {Badge.SKIN_JESSI}, {Badge.SKIN_LEAF},
			{Badge.SKIN_ASTESIA}, {Badge.SKIN_SPECTER}, {Badge.SKIN_MUDROCK}, {Badge.SKIN_SCHWARZ}
	};
	
	public static List<Badge> filterReplacedBadges( List<Badge> badges ) {
		leaveBest( badges, Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4 );
		leaveBest( badges, Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4, Badge.GOLD_COLLECTED_5 );
		leaveBest( badges, Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4, Badge.VICTORY, Badge.VICTORY_ALL_CLASSES );
		leaveBest( badges, Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4 );
		leaveBest( badges, Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4 );
		leaveBest( badges, Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4 );
		leaveBest( badges, Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4 );
		leaveBest( badges, Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4 );
		leaveBest( badges, Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4 );
		leaveBest( badges, Badge.CHAMPION_1, Badge.CHAMPION_2, Badge.CHAMPION_3, Badge.CHAMPION_4 );
		leaveBest(badges,Badge.Get_25_STONES, Badge.Get_40_STONES);
		leaveBest( badges, Badge.BLAZE_CHAMPION1, Badge.BLAZE_CHAMPION2, Badge.BLAZE_CHAMPION3 );
		leaveBest( badges, Badge.AMIYA_CHAMPION1, Badge.AMIYA_CHAMPION2, Badge.AMIYA_CHAMPION3 );
		leaveBest( badges, Badge.RED_CHAMPION1, Badge.RED_CHAMPION2, Badge.RED_CHAMPION3 );
		leaveBest( badges, Badge.GREY_CHAMPION1, Badge.GREY_CHAMPION2, Badge.GREY_CHAMPION3 );
		leaveBest( badges, Badge.ROSE_CHAMPION1, Badge.ROSE_CHAMPION2, Badge.ROSE_CHAMPION3 );
		leaveBest( badges, Badge.NEARL_CHAMPION1, Badge.NEARL_CHAMPION2, Badge.NEARL_CHAMPION3 );
		leaveBest( badges, Badge.SIESTA_PART1, Badge.SIESTA_PART2);
		leaveBest( badges, Badge.GAVIAL_PART1, Badge.GAVIAL_PART2);

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveBest( badges, tierReplace );
		}

		for (Badge[] metaReplace : metaBadgeReplacements){
			leaveBest( badges, metaReplace );
		}
		
		return badges;
	}

	public static List<Badge> filterSkindBadges( List<Badge> badges ) {
		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveBest( badges, tierReplace );
		}

		for (Badge[] metaReplace : metaBadgeReplacements){
			leaveBest( badges, metaReplace );
		}

		for (Badge[] skinReplace : skinBadgeReplacements){
			leaveBest( badges, skinReplace );
		}

		return badges;
	}
	
	private static void leaveBest( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static List<Badge> filterHigherIncrementalBadges(List<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveWorst( badges, tierReplace );
		}

		Collections.sort( badges );

		return badges;
	}

	private static void leaveWorst( Collection<Badge> list, Badge...badges ) {
		for (int i=0; i < badges.length; i++) {
			if (list.contains( badges[i])) {
				for (int j=i+1; j < badges.length; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static Collection<Badge> addReplacedBadges(Collection<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			addLower( badges, tierReplace );
		}

		for (Badge[] metaReplace : metaBadgeReplacements){
			addLower( badges, metaReplace );
		}

		for (Badge[] skinReplace : skinBadgeReplacements){
			addLower( badges, skinReplace );
		}

		return badges;
	}

	private static void addLower( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.add( badges[j] );
				}
				break;
			}
		}
	}
}
