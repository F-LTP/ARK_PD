package com.shatteredpixel.shatteredpixeldungeon.items.testtool;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Generators extends Item {

    public static final String AC_GIVE	= "GIVE";
    {
        image = ItemSpriteSheet.ORE;

        stackable = false;
        unique = true;
    }
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_GIVE );
        return actions;
    }
}
