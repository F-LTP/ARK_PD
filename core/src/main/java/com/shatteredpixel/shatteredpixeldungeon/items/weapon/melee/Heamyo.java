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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Heamyo extends MeleeWeapon {

    public static final String AC_DUMMY = "DUMMY";

    {
        image = ItemSpriteSheet.HEAMYO;
        hitSound = Assets.Sounds.GOLD;

        tier = 1;
        ACC = 12000f;
        DLY = 0.1f; //0.67x speed
        RCH = 300;

        defaultAction = AC_DUMMY;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_DUMMY);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_DUMMY)) {
            curUser = hero;
            GameScene.selectCell(dummyPlacer);
        }
    }

    private final CellSelector.Listener dummyPlacer = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            Char existing = Actor.findChar(cell);
            if (existing instanceof Skeleton) {
                existing.die(null);
            } else if (existing != null) {
                GLog.w("Cell occupied by " + existing.getClass().getSimpleName());
            } else if (Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) {
                Skeleton dummy = new Skeleton();
                dummy.pos = cell;
                Dungeon.level.occupyCell(dummy);
                GameScene.add(dummy);
            }
        }

        @Override
        public String prompt() {
            return "훈련인형 소환/삭제";
        }
    };

    @Override
    public int max(int lvl) {
        return 100000000;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        //  defender.sprite.killAndErase();
        //   defender.destroy();

        return super.proc(attacker, defender, damage);
    }

    @Override
    public int defenseFactor(Char owner) {
        return 600 + 300 * buffedLvl();    //6 extra defence, plus 3 per level;
    }

    public String statsInfo() {
        if (isIdentified()) {
            return Messages.get(this, "stats_desc", 6 + 3 * buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 6);
        }
    }
}