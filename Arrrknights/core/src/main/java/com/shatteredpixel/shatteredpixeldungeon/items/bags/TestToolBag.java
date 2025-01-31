package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.testtool.ChallengeItem;
import com.shatteredpixel.shatteredpixeldungeon.items.testtool.Generators;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

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

public class TestToolBag extends Bag {

    {
        image = ItemSpriteSheet.HEAMYO;
    }

    @Override
    public boolean canHold( Item item ) {
        if (item instanceof Generators || item instanceof ChallengeItem){
            return super.canHold(item);
        } else {
            return false;
        }
    }
    @Override
    public int capacity(){
        return 19;
    }


}
