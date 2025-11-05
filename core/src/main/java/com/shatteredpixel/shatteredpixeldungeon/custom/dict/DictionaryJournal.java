package com.shatteredpixel.shatteredpixeldungeon.custom.dict;

import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WebParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.Collection;
import java.util.LinkedHashMap;

public enum DictionaryJournal {

    ARMORS,
    ARTIFACTS,
    ALCHEMY,
    WEAPONS,
    PLANTS,
    WANDS,
    RINGS,
    SKILLS,
    DOCUMENTS,
    UNCLASSIFIED;

    private LinkedHashMap<String, Integer> d = new LinkedHashMap<>();

    public Collection<String> keyList(){return d.keySet();}

    public Collection<Integer> imageList() {return d.values();}

    static {
        //armors
        ARMORS.d.put("armor_cloth",         ItemSpriteSheet.ARMOR_CLOTH);
        ARMORS.d.put("armor_leather",       ItemSpriteSheet.ARMOR_LEATHER);
        ARMORS.d.put("armor_mail",          ItemSpriteSheet.ARMOR_MAIL);
        ARMORS.d.put("armor_scale",         ItemSpriteSheet.ARMOR_SCALE);
        ARMORS.d.put("armor_plate",         ItemSpriteSheet.ARMOR_PLATE);
        //ARMORS.d.put("armor_epic",          ItemSpriteSheet.ARMOR_WARRIOR);
        ARMORS.d.put("armor_glyph_1",       ItemSpriteSheet.STYLUS);
        ARMORS.d.put("armor_glyph_2",       ItemSpriteSheet.STYLUS);
        ARMORS.d.put("armor_glyph_3",       ItemSpriteSheet.STYLUS);
        ARMORS.d.put("armor_glyph_4",       ItemSpriteSheet.CURSE_INFUSE);

        //artifacts
        ARTIFACTS.d.put("artifact_toolkit",         ItemSpriteSheet.ARTIFACT_TOOLKIT);
        ARTIFACTS.d.put("artifact_alchemy",          ItemSpriteSheet.ARTIFACT_THORNS);
        ARTIFACTS.d.put("artifact_chalice",         ItemSpriteSheet.ARTIFACT_CHALICE1);
        ARTIFACTS.d.put("artifact_cloak",           ItemSpriteSheet.ARTIFACT_CLOAK);
        ARTIFACTS.d.put("artifact_rose",            ItemSpriteSheet.ARTIFACT_ROSE1);
        ARTIFACTS.d.put("artifact_chain",           ItemSpriteSheet.ARTIFACT_CHAINS);
        ARTIFACTS.d.put("artifact_horn",            ItemSpriteSheet.ARTIFACT_HORN1);
        ARTIFACTS.d.put("artifact_isekai",          ItemSpriteSheet.ARTIFACT_ASH);
        ARTIFACTS.d.put("artifact_sandal",          ItemSpriteSheet.ARTIFACT_SANDALS);
        ARTIFACTS.d.put("artifact_seal",            ItemSpriteSheet.ARTIFACT_NEARL);
        ARTIFACTS.d.put("artifact_talisman",        ItemSpriteSheet.ARTIFACT_TALISMAN);
        ARTIFACTS.d.put("artifact_armband",         ItemSpriteSheet.ARTIFACT_ARMBAND);
        ARTIFACTS.d.put("artifact_hourglass",       ItemSpriteSheet.ARTIFACT_HOURGLASS);
        ARTIFACTS.d.put("artifact_book",            ItemSpriteSheet.ARTIFACT_SPELLBOOK);
        ARTIFACTS.d.put("artifact_camera",          ItemSpriteSheet.ARTIFACT_CAMERA);

        //alchemy, so many...
        //bomb
        ALCHEMY.d.put("bomb",               ItemSpriteSheet.BOMB);
        ALCHEMY.d.put("bomb_frost",         ItemSpriteSheet.FROST_BOMB);
        ALCHEMY.d.put("bomb_flame",         ItemSpriteSheet.FIRE_BOMB);
        ALCHEMY.d.put("bomb_wool",          ItemSpriteSheet.WOOLY_BOMB);
        ALCHEMY.d.put("bomb_lens",          ItemSpriteSheet.LENS_BOMB);
        ALCHEMY.d.put("bomb_noise",         ItemSpriteSheet.NOISEMAKER);
        ALCHEMY.d.put("bomb_flash",         ItemSpriteSheet.FLASHBANG);
        ALCHEMY.d.put("bomb_shocking",      ItemSpriteSheet.SHOCK_BOMB);
        ALCHEMY.d.put("bomb_regrowth",      ItemSpriteSheet.REGROWTH_BOMB);
        ALCHEMY.d.put("bomb_holy",          ItemSpriteSheet.HOLY_BOMB);
        ALCHEMY.d.put("bomb_arcane",        ItemSpriteSheet.ARCANE_BOMB);
        ALCHEMY.d.put("bomb_shrapnel",      ItemSpriteSheet.SHRAPNEL_BOMB);

        //cats
        ALCHEMY.d.put("catalyst_potion",    ItemSpriteSheet.POTION_CATALYST);
        ALCHEMY.d.put("catalyst_scroll",    ItemSpriteSheet.SCROLL_CATALYST);
        //food
        ALCHEMY.d.put("food_ration",        ItemSpriteSheet.RATION);
        ALCHEMY.d.put("food_small_ration",  ItemSpriteSheet.OVERPRICED);
        ALCHEMY.d.put("food_frozen",        ItemSpriteSheet.CARPACCIO);
        ALCHEMY.d.put("food_pie",           ItemSpriteSheet.MEAT_PIE);
        ALCHEMY.d.put("food_pasty",         ItemSpriteSheet.PASTY);
        ALCHEMY.d.put("food_fruit",         ItemSpriteSheet.BLANDFRUIT);
        ALCHEMY.d.put("food_chunk",         ItemSpriteSheet.BLAND_CHUNKS);
        ALCHEMY.d.put("food_berry",         ItemSpriteSheet.BERRY);
        ALCHEMY.d.put("meatcutlet",         ItemSpriteSheet.CUTLET);
        ALCHEMY.d.put("sandvich",           ItemSpriteSheet.SANDBITCH);
        ALCHEMY.d.put("food_honeybread",ItemSpriteSheet.PAN_CAKE);
        ALCHEMY.d.put("food_notbarfood",ItemSpriteSheet.MEAT_SRICK);
        ALCHEMY.d.put("food_fry_gamza",ItemSpriteSheet.POTATO_FRY);
        ALCHEMY.d.put("food_fry_egg",ItemSpriteSheet.EGG_FRY);
        ALCHEMY.d.put("food_yukjeon",ItemSpriteSheet.YUKJEON);
        ALCHEMY.d.put("food_smokeegg",ItemSpriteSheet.SMOKEEGG);
        ALCHEMY.d.put("food_glassate",ItemSpriteSheet.NUNEDDINE);

        //potions, enhanced ones implemented
        ALCHEMY.d.put("potion_exp",         ItemSpriteSheet.POTION_INDIGO);
        ALCHEMY.d.put("potion_frost",       ItemSpriteSheet.POTION_INDIGO);
        ALCHEMY.d.put("potion_haste",       ItemSpriteSheet.POTION_INDIGO);
        ALCHEMY.d.put("potion_healing",     ItemSpriteSheet.POTION_AZURE);
        ALCHEMY.d.put("potion_inv",         ItemSpriteSheet.POTION_AZURE);
        ALCHEMY.d.put("potion_fly",         ItemSpriteSheet.POTION_AZURE);
        ALCHEMY.d.put("potion_fire",        ItemSpriteSheet.POTION_JADE);
        ALCHEMY.d.put("potion_vision",      ItemSpriteSheet.POTION_JADE);
        ALCHEMY.d.put("potion_paralyse",    ItemSpriteSheet.POTION_JADE);
        ALCHEMY.d.put("potion_purify",      ItemSpriteSheet.POTION_CRIMSON);
        ALCHEMY.d.put("potion_str",         ItemSpriteSheet.POTION_CRIMSON);
        ALCHEMY.d.put("potion_gas",         ItemSpriteSheet.POTION_CRIMSON);

        //scrolls,exotics,stones
        ALCHEMY.d.put("scroll_identify",    ItemSpriteSheet.SCROLL_GYFU);
        ALCHEMY.d.put("scroll_sleep",       ItemSpriteSheet.SCROLL_GYFU);
        ALCHEMY.d.put("scroll_image",       ItemSpriteSheet.SCROLL_GYFU);
        ALCHEMY.d.put("scroll_mapping",     ItemSpriteSheet.SCROLL_ISAZ);
        ALCHEMY.d.put("scroll_challenge",   ItemSpriteSheet.SCROLL_ISAZ);
        ALCHEMY.d.put("scroll_uncurse",     ItemSpriteSheet.SCROLL_ISAZ);
        ALCHEMY.d.put("scroll_recharging",  ItemSpriteSheet.SCROLL_KAUNAN);
        ALCHEMY.d.put("scroll_retribution", ItemSpriteSheet.SCROLL_KAUNAN);
        ALCHEMY.d.put("scroll_terror",      ItemSpriteSheet.SCROLL_KAUNAN);
        ALCHEMY.d.put("scroll_tp",          ItemSpriteSheet.SCROLL_ODAL);
        ALCHEMY.d.put("scroll_alter",       ItemSpriteSheet.SCROLL_ODAL);
        ALCHEMY.d.put("scroll_upgrade",     ItemSpriteSheet.SCROLL_ODAL);
        ALCHEMY.d.put("scrollofwarp",       ItemSpriteSheet.SCROLL_TIWAZ);

        //brew,elixir,2 to show all
        ALCHEMY.d.put("potion_brews",       ItemSpriteSheet.BREW_INFERNAL);
        ALCHEMY.d.put("potion_elixirs",     ItemSpriteSheet.ELIXIR_AQUA);
        //spells, 2, 1 general//useless, 1 detail
        ALCHEMY.d.put("qianweifushi",       ItemSpriteSheet.AVANT_TRANS);
        ALCHEMY.d.put("spell_spells",       ItemSpriteSheet.PHASE_SHIFT);
        ALCHEMY.d.put("spell_spells2",      ItemSpriteSheet.PHASE_SHIFT);
        //ALCHEMY.d.put("enforcers_details",  ItemSpriteSheet.LIQUID_METAL);

        SKILLS.d.put("skill_powerfulstrike",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_tacticalchanting",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_executionmode",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_fate",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_panorama",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_foodprep",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_chainhook",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_whispers",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_crimsoncutte",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_shinkageryu",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_fierceglare",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_camouflage",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_wolfspirit",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_thoughts",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_hotblade",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_spreadspores",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_phantommirror",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_live",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_hikari",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_soul",ItemSpriteSheet.SKILL_CHIP1);
        SKILLS.d.put("skill_wolfpack",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_mentalburst",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_reflow",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_emergencydefibrillator",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_jackinthebox",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_rockfailhammer",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_chargingps",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_neverbackdown",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_coversmoke",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_benasprotracto",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_ancientkin",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_landingstrike",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_dreamland",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_flashshield",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_nervous",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_deephealing",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_spikes",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_genesis",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_predators",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_dawn",ItemSpriteSheet.SKILL_CHIP2);
        SKILLS.d.put("skill_shadowassault",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_soaringfeather",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_sburst",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_nigetraid",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_terminationt",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_truesilverslash",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_yourwish",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_everyone",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_sharpness",ItemSpriteSheet.SKILL_CHIP3);
        SKILLS.d.put("skill_sun",ItemSpriteSheet.SKILL_CHIP3);
        //plants
        PLANTS.d.put("seed_blindweed",      ItemSpriteSheet.SEED_BLINDWEED);
        PLANTS.d.put("seed_dreamfoil",      ItemSpriteSheet.SEED_DREAMFOIL);
        PLANTS.d.put("seed_earthroot",      ItemSpriteSheet.SEED_EARTHROOT);
        PLANTS.d.put("seed_fadeleaf",       ItemSpriteSheet.SEED_FADELEAF);
        PLANTS.d.put("seed_firebloom",      ItemSpriteSheet.SEED_FIREBLOOM);
        PLANTS.d.put("seed_icecap",         ItemSpriteSheet.SEED_ICECAP);
        PLANTS.d.put("seed_rotberry",       ItemSpriteSheet.SEED_ROTBERRY);
        PLANTS.d.put("seed_sorrowmoss",     ItemSpriteSheet.SEED_SORROWMOSS);
        PLANTS.d.put("seed_starflower",     ItemSpriteSheet.SEED_STARFLOWER);
        PLANTS.d.put("seed_stromvine",      ItemSpriteSheet.SEED_STORMVINE);
        PLANTS.d.put("seed_sungrass",       ItemSpriteSheet.SEED_SUNGRASS);
        PLANTS.d.put("seed_swiftthistle",   ItemSpriteSheet.SEED_SWIFTTHISTLE);
        //ring
        RINGS.d.put("ring_accuracy",        ItemSpriteSheet.RING_AGATE);
        RINGS.d.put("ring_amplified",       ItemSpriteSheet.RING_AGATE);
        RINGS.d.put("ring_assassin",        ItemSpriteSheet.RING_AGATE);
        RINGS.d.put("ring_command",         ItemSpriteSheet.RING_AGATE);
        RINGS.d.put("ring_dominate",        ItemSpriteSheet.RING_AGATE);
        RINGS.d.put("ring_element",         ItemSpriteSheet.RING_AGATE);
        RINGS.d.put("ring_energy",          ItemSpriteSheet.RING_DIAMOND);
        RINGS.d.put("ring_evasion",         ItemSpriteSheet.RING_DIAMOND);
        RINGS.d.put("ring_force",           ItemSpriteSheet.RING_DIAMOND);
        RINGS.d.put("ring_furor",           ItemSpriteSheet.RING_EMERALD);
        RINGS.d.put("ring_haste",           ItemSpriteSheet.RING_EMERALD);
        RINGS.d.put("ring_might",           ItemSpriteSheet.RING_EMERALD);
        RINGS.d.put("ring_mistress",        ItemSpriteSheet.RING_EMERALD);
        RINGS.d.put("ring_shoot",           ItemSpriteSheet.RING_QUARTZ);
        RINGS.d.put("ring_sun",             ItemSpriteSheet.RING_QUARTZ);
        RINGS.d.put("ring_tenacity",        ItemSpriteSheet.RING_QUARTZ);
        RINGS.d.put("ring_wealth",          ItemSpriteSheet.RING_QUARTZ);
        //wand
        WANDS.d.put("wand_blastwave",       ItemSpriteSheet.WAND_BLAST_WAVE);
        WANDS.d.put("wand_weedy",           ItemSpriteSheet.WAND_BLAST_WAVE);
        WANDS.d.put("wand_corrosion",       ItemSpriteSheet.WAND_CORROSION);
        WANDS.d.put("wand_breeze",          ItemSpriteSheet.WAND_CORROSION);
        WANDS.d.put("wand_corruption",      ItemSpriteSheet.WAND_CORRUPTION);
        WANDS.d.put("wand_oo",              ItemSpriteSheet.WAND_CORRUPTION);
        WANDS.d.put("wand_beam",            ItemSpriteSheet.WAND_DISINTEGRATION);
        WANDS.d.put("wand_vigna",           ItemSpriteSheet.WAND_DISINTEGRATION);
        WANDS.d.put("wand_fireblast",       ItemSpriteSheet.WAND_FIREBOLT);
        WANDS.d.put("wand_skyfire",         ItemSpriteSheet.WAND_FIREBOLT);
        WANDS.d.put("wand_frost",           ItemSpriteSheet.WAND_FROST);
        WANDS.d.put("wand_leaf",            ItemSpriteSheet.WAND_FROST);
        WANDS.d.put("wand_hall",            ItemSpriteSheet.WAND_PODENCO);
        WANDS.d.put("wand_podenco",         ItemSpriteSheet.WAND_PODENCO);
        WANDS.d.put("wand_heal",            ItemSpriteSheet.WAND_SUSSURRO);
        WANDS.d.put("wand_susulo",          ItemSpriteSheet.WAND_SUSSURRO);
        WANDS.d.put("wand_lightning",       ItemSpriteSheet.WAND_LIGHTNING);
        WANDS.d.put("wand_grey",            ItemSpriteSheet.WAND_LIGHTNING);
        WANDS.d.put("wand_soil",            ItemSpriteSheet.WAND_LIVING_EARTH);
        WANDS.d.put("wand_mudrock",         ItemSpriteSheet.WAND_LIVING_EARTH);
        WANDS.d.put("wand_magicmissile",    ItemSpriteSheet.WAND_MAGIC_MISSILE);
        WANDS.d.put("wand_absinthe",        ItemSpriteSheet.WAND_MAGIC_MISSILE);
        WANDS.d.put("wand_light",           ItemSpriteSheet.WAND_PRISMATIC_LIGHT);
        WANDS.d.put("wand_shining",         ItemSpriteSheet.WAND_PRISMATIC_LIGHT);
        WANDS.d.put("wand_purgatory",       ItemSpriteSheet.WAND_LAVA);
        WANDS.d.put("wand_grass",           ItemSpriteSheet.WAND_REGROWTH);
        WANDS.d.put("wand_lena",            ItemSpriteSheet.WAND_REGROWTH);
        WANDS.d.put("wand_silence",         ItemSpriteSheet.WAND_SNOWSANT);
        WANDS.d.put("wand_snowsant",        ItemSpriteSheet.WAND_SNOWSANT);
        WANDS.d.put("wand_suzuran",         ItemSpriteSheet.WAND_SUZRAN);
        WANDS.d.put("wand_time",            ItemSpriteSheet.WAND_MOSTIMA);
        WANDS.d.put("wand_transfusion",     ItemSpriteSheet.WAND_TRANSFUSION);
        WANDS.d.put("wand_angelina",        ItemSpriteSheet.WAND_TRANSFUSION);
        WANDS.d.put("wand_warding",         ItemSpriteSheet.WAND_WARDING);
        WANDS.d.put("wand_mayer",           ItemSpriteSheet.WAND_WARDING);
        //weapon, melee
        WEAPONS.d.put("melee_wornsword",    ItemSpriteSheet.WORN_SHORTSWORD);
        WEAPONS.d.put("melee_gloves",       ItemSpriteSheet.GLOVES);
        WEAPONS.d.put("melee_dagger",       ItemSpriteSheet.DAGGER);
        WEAPONS.d.put("melee_staff",        ItemSpriteSheet.MAGES_STAFF);
        WEAPONS.d.put("melee_ex42",         ItemSpriteSheet.EX41);
        WEAPONS.d.put("melee_quarterstaff", ItemSpriteSheet.NEARL_AXE);
        WEAPONS.d.put("melee_chensword",    ItemSpriteSheet.SHORTSWORD);
        WEAPONS.d.put("melee_rhodessword",  ItemSpriteSheet.YATO);

        WEAPONS.d.put("melee_shortsword",   ItemSpriteSheet.SHORTSWORD);
        WEAPONS.d.put("melee_handaxe",      ItemSpriteSheet.HAND_AXE);
        WEAPONS.d.put("melee_spear",        ItemSpriteSheet.SPEAR);
        WEAPONS.d.put("melee_dirk",         ItemSpriteSheet.DIRK);
        WEAPONS.d.put("melee_enfild",       ItemSpriteSheet.ENFILD);
        WEAPONS.d.put("melee_midnightsword",ItemSpriteSheet.MIDSWORD);
        WEAPONS.d.put("melee_firmament",    ItemSpriteSheet.FIRMAMENT);

        WEAPONS.d.put("melee_sword",        ItemSpriteSheet.SWORD);
        WEAPONS.d.put("melee_mace",         ItemSpriteSheet.MACE);
        WEAPONS.d.put("melee_scimitar",     ItemSpriteSheet.SCIMITAR);
        WEAPONS.d.put("melee_roundshield",  ItemSpriteSheet.ROUND_SHIELD);
        WEAPONS.d.put("melee_sai",          ItemSpriteSheet.SAI);
        WEAPONS.d.put("melee_whip",         ItemSpriteSheet.WHIP);
        WEAPONS.d.put("melee_shishioh",     ItemSpriteSheet.SHISHIOH);
        WEAPONS.d.put("melee_flag",         ItemSpriteSheet.FLAG);
        WEAPONS.d.put("melee_dp27",         ItemSpriteSheet.DP27);
        WEAPONS.d.put("melee_c1",           ItemSpriteSheet.C1);
        WEAPONS.d.put("melee_gamzashield",  ItemSpriteSheet.GAMZA_SHIELD);
        WEAPONS.d.put("melee_enfild2",      ItemSpriteSheet.ANDREANA);

        WEAPONS.d.put("melee_longsword",    ItemSpriteSheet.LONGSWORD);
        WEAPONS.d.put("melee_battleaxe",    ItemSpriteSheet.BATTLE_AXE);
        WEAPONS.d.put("melee_bladedemon",   ItemSpriteSheet.BLADE_DEMON);
        WEAPONS.d.put("melee_runicblade",   ItemSpriteSheet.RUNIC_BLADE);
        WEAPONS.d.put("melee_assassin",     ItemSpriteSheet.ASSASSINS_BLADE);
        WEAPONS.d.put("melee_bow",          ItemSpriteSheet.CROSSBOW);
        WEAPONS.d.put("melee_crabgun",      ItemSpriteSheet.BEENS);
        WEAPONS.d.put("melee_flail",        ItemSpriteSheet.FLAIL);
        WEAPONS.d.put("melee_flametail",    ItemSpriteSheet.FLAMETAIL);
        WEAPONS.d.put("melee_folksong",     ItemSpriteSheet.SPECTER);
        WEAPONS.d.put("melee_kazemaru",     ItemSpriteSheet.KAZEMARU);
        WEAPONS.d.put("melee_m1887",        ItemSpriteSheet.M1887);
        WEAPONS.d.put("melee_metallicunion",ItemSpriteSheet.PANDA);
        WEAPONS.d.put("melee_naginata",     ItemSpriteSheet.NAGINATA);
        WEAPONS.d.put("melee_scythe",       ItemSpriteSheet.SICKEL);
        WEAPONS.d.put("melee_snowhunter",   ItemSpriteSheet.CLIFF);
        WEAPONS.d.put("melee_journalist",   ItemSpriteSheet.SCENE);
        WEAPONS.d.put("melee_gluttony",     ItemSpriteSheet.CARNEL);
        WEAPONS.d.put("melee_golddogsword", ItemSpriteSheet.GOLDDOG);

        WEAPONS.d.put("melee_greatsword",   ItemSpriteSheet.GREATSWORD);
        WEAPONS.d.put("melee_hammer",       ItemSpriteSheet.WAR_HAMMER);
        WEAPONS.d.put("melee_glaive",       ItemSpriteSheet.GLAIVE);
        WEAPONS.d.put("melee_greataxe",     ItemSpriteSheet.GREATAXE);
        WEAPONS.d.put("melee_greatshield",  ItemSpriteSheet.GREATSHIELD);
        WEAPONS.d.put("melee_gauntlet",     ItemSpriteSheet.GAUNTLETS);
        WEAPONS.d.put("melee_divineavatar", ItemSpriteSheet.BABY_KNIGHT);
        WEAPONS.d.put("melee_echeveria",    ItemSpriteSheet.ECHEVERIA);
        WEAPONS.d.put("melee_krissvector",  ItemSpriteSheet.KRISS_V);
        WEAPONS.d.put("melee_lonejourney",  ItemSpriteSheet.LONE);
        WEAPONS.d.put("melee_r4c",          ItemSpriteSheet.R4C);
        WEAPONS.d.put("melee_radiantspear", ItemSpriteSheet.NEARL_SPEAR);
        WEAPONS.d.put("melee_suffering",    ItemSpriteSheet.FLAMMETTA);
        WEAPONS.d.put("melee_swordofa",     ItemSpriteSheet.ARTORIUS);
        WEAPONS.d.put("melee_catgun",       ItemSpriteSheet.CATGUN);
        WEAPONS.d.put("melee_imageoverform",ItemSpriteSheet.DUSK);
        WEAPONS.d.put("melee_minosfury",    ItemSpriteSheet.VULCAN);
        WEAPONS.d.put("melee_niansword",    ItemSpriteSheet.NIANSWORD);
        WEAPONS.d.put("melee_patriotspear", ItemSpriteSheet.REQUIEM);
        WEAPONS.d.put("melee_kollamsword",  ItemSpriteSheet.DONKEY_SWORD);
        WEAPONS.d.put("melee_sakura",       ItemSpriteSheet.SAKURA_FUBUKI);

        WEAPONS.d.put("melee_pickaxe",      ItemSpriteSheet.PICKAXE);
        //ench & curse
        WEAPONS.d.put("weapon_ench1",       ItemSpriteSheet.STONE_ENCHANT);
        WEAPONS.d.put("weapon_ench2",       ItemSpriteSheet.STONE_ENCHANT);
        WEAPONS.d.put("weapon_ench3",       ItemSpriteSheet.STONE_ENCHANT);
        WEAPONS.d.put("weapon_ench4",       ItemSpriteSheet.STONE_ENCHANT);
        //missile
        WEAPONS.d.put("missile_dart",       ItemSpriteSheet.DART);
        WEAPONS.d.put("missile_darttipped", ItemSpriteSheet.PARALYTIC_DART);
        WEAPONS.d.put("missile_stone",      ItemSpriteSheet.THROWING_STONE);
        WEAPONS.d.put("missile_knife",      ItemSpriteSheet.THROWING_KNIFE);
        WEAPONS.d.put("missile_light",      ItemSpriteSheet.HOLY_KNIFE);
        WEAPONS.d.put("missile_firesteel",  ItemSpriteSheet.FLINT);
        WEAPONS.d.put("missile_purgatory",  ItemSpriteSheet.LAVA);
        WEAPONS.d.put("missile_fishing",    ItemSpriteSheet.FISHING_SPEAR);
        WEAPONS.d.put("missile_shuriken",   ItemSpriteSheet.SHURIKEN);
        WEAPONS.d.put("missile_club",       ItemSpriteSheet.THROWING_CLUB);
        WEAPONS.d.put("missile_spear",      ItemSpriteSheet.THROWING_SPEAR);
        WEAPONS.d.put("missile_kunai",      ItemSpriteSheet.KUNAI);
        WEAPONS.d.put("missile_bolas",      ItemSpriteSheet.BOLAS);
        WEAPONS.d.put("missile_rage",       ItemSpriteSheet.THROWING_STONE);
        WEAPONS.d.put("missile_mag",        ItemSpriteSheet.AMMO1);
        WEAPONS.d.put("missile_thunder",    ItemSpriteSheet.LISKARM_DOLL);
        WEAPONS.d.put("missile_javelin",    ItemSpriteSheet.JAVELIN);
        WEAPONS.d.put("missile_boomerang",  ItemSpriteSheet.BOOMERANG);
        WEAPONS.d.put("missile_axe",        ItemSpriteSheet.TOMAHAWK);
        WEAPONS.d.put("missile_spmag",      ItemSpriteSheet.AMMO2);
        WEAPONS.d.put("missile_hammer",     ItemSpriteSheet.THROWING_HAMMER);
        WEAPONS.d.put("missile_trident",    ItemSpriteSheet.TRIDENT);
        WEAPONS.d.put("missile_cube",       ItemSpriteSheet.FORCE_CUBE);
        WEAPONS.d.put("missile_spiritbow",  ItemSpriteSheet.SPIRIT_BOW);

        //Documents
        DOCUMENTS.d.put("info_intro",       ItemSpriteSheet.EBONY_CHEST);
        DOCUMENTS.d.put("info_item",        ItemSpriteSheet.BREW_INFERNAL);
        DOCUMENTS.d.put("info_tier",        ItemSpriteSheet.GLAIVE);
        DOCUMENTS.d.put("info_melee",       ItemSpriteSheet.GREATAXE);
        DOCUMENTS.d.put("info_ranged",      ItemSpriteSheet.TOMAHAWK);
        DOCUMENTS.d.put("info_armor",       ItemSpriteSheet.ARMOR_PLATE);
        DOCUMENTS.d.put("info_wand",        ItemSpriteSheet.WAND_LIVING_EARTH);
        DOCUMENTS.d.put("info_wand_cursed", ItemSpriteSheet.CURSE_INFUSE);
        DOCUMENTS.d.put("info_ring",        ItemSpriteSheet.RING_AMETHYST);
        DOCUMENTS.d.put("info_artifact",    ItemSpriteSheet.ARTIFACT_CHALICE3);
        DOCUMENTS.d.put("info_scroll",      ItemSpriteSheet.SCROLL_ISAZ);
        DOCUMENTS.d.put("info_potion",      ItemSpriteSheet.POTION_BISTRE);
        DOCUMENTS.d.put("info_lim_drop",    ItemSpriteSheet.POTION_CRIMSON);
        DOCUMENTS.d.put("info_seed",        ItemSpriteSheet.SEED_BLINDWEED);
        DOCUMENTS.d.put("info_bomb",        ItemSpriteSheet.BOMB);
        DOCUMENTS.d.put("info_food",        ItemSpriteSheet.RATION);
        DOCUMENTS.d.put("info_gold",        ItemSpriteSheet.GOLD);
        DOCUMENTS.d.put("info_alchemy",     ItemSpriteSheet.ARTIFACT_TOOLKIT);
        DOCUMENTS.d.put("info_priority",    ItemSpriteSheet.ARTIFACT_HOURGLASS);
        DOCUMENTS.d.put("info_hero",        DictSpriteSheet.HERO);
        DOCUMENTS.d.put("info_mob",         DictSpriteSheet.RAT);
        DOCUMENTS.d.put("info_property",    DictSpriteSheet.ELEMENTAL_CHAOS);
        DOCUMENTS.d.put("info_spawn",       DictSpriteSheet.SPAWNER);
        DOCUMENTS.d.put("info_special_mob", DictSpriteSheet.ALBINO);
        DOCUMENTS.d.put("info_trap",        DictSpriteSheet.TRAP_GREEN_RECT);
        DOCUMENTS.d.put("info_room",        ItemSpriteSheet.LOCKED_CHEST);
        DOCUMENTS.d.put("info_shop",        ItemSpriteSheet.GOLD);
        DOCUMENTS.d.put("info_feeling",     DictSpriteSheet.AREA_HALL);
        DOCUMENTS.d.put("info_chasm",       DictSpriteSheet.CHASM);
        DOCUMENTS.d.put("info_lck_floor",   DictSpriteSheet.LOCKED_FLOOR);
        DOCUMENTS.d.put("info_ranking",     ItemSpriteSheet.AMULET);
        DOCUMENTS.d.put("info_bones",       ItemSpriteSheet.REMAINS);

        //unclassified
        //buffs
        UNCLASSIFIED.d.put("buff_neg1",     DictSpriteSheet.BUFF_NEGATIVE);
        UNCLASSIFIED.d.put("buff_neg2",     DictSpriteSheet.BUFF_NEGATIVE);
        UNCLASSIFIED.d.put("buff_neg3",     DictSpriteSheet.BUFF_NEGATIVE);
        UNCLASSIFIED.d.put("buff_pos1",     DictSpriteSheet.BUFF_POSITIVE);
        UNCLASSIFIED.d.put("buff_pos2",     DictSpriteSheet.BUFF_POSITIVE);
        UNCLASSIFIED.d.put("buff_pos3",     DictSpriteSheet.BUFF_POSITIVE);
        UNCLASSIFIED.d.put("buff_pos4",     DictSpriteSheet.BUFF_POSITIVE);
        UNCLASSIFIED.d.put("buff_pos5",     DictSpriteSheet.BUFF_POSITIVE);
        //traps
        UNCLASSIFIED.d.put("trap_1",        DictSpriteSheet.TRAP_GREEN_RECT);
        UNCLASSIFIED.d.put("trap_2",        DictSpriteSheet.TRAP_GREEN_RECT);
        UNCLASSIFIED.d.put("trap_3",        DictSpriteSheet.TRAP_GREEN_RECT);
        UNCLASSIFIED.d.put("trap_4",        DictSpriteSheet.TRAP_GREEN_RECT);
        //miscs
        UNCLASSIFIED.d.put("mob_blacksmith",DictSpriteSheet.BLACKSMITH);
        UNCLASSIFIED.d.put("mob_ghost",DictSpriteSheet.SAD_GHOST);
        UNCLASSIFIED.d.put("mob_imp",DictSpriteSheet.IMP);
        UNCLASSIFIED.d.put("mob_image",DictSpriteSheet.IMAGE);
        UNCLASSIFIED.d.put("mob_pris_image",DictSpriteSheet.PRISMATIC_IMAGE);
        UNCLASSIFIED.d.put("mob_wandmaker",DictSpriteSheet.WAND_MAKER);
        UNCLASSIFIED.d.put("mob_ratking",DictSpriteSheet.RAT_KING);
        UNCLASSIFIED.d.put("misc_ankh",  ItemSpriteSheet.ANKH);
        UNCLASSIFIED.d.put("misc_gear",  ItemSpriteSheet.ARTIFACT_ROSEMARY);
        UNCLASSIFIED.d.put("misc_armorkit",ItemSpriteSheet.KIT);
        UNCLASSIFIED.d.put("misc_seal",  ItemSpriteSheet.SEAL);
        UNCLASSIFIED.d.put("misc_vial",  ItemSpriteSheet.VIAL);
        UNCLASSIFIED.d.put("misc_gold",  ItemSpriteSheet.GOLD);
        UNCLASSIFIED.d.put("misc_dust",  ItemSpriteSheet.DUST);
        UNCLASSIFIED.d.put("misc_randombox",ItemSpriteSheet.CHEST);
        UNCLASSIFIED.d.put("misc_soon",  ItemSpriteSheet.SOMETHING);
    }

}
