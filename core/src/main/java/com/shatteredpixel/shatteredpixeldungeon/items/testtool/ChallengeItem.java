package com.shatteredpixel.shatteredpixeldungeon.items.testtool;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

abstract public class ChallengeItem extends Item {
    {
        image = ItemSpriteSheet.BONK;
        bones = false;
        stackable = false;
        unique = true;
    }
    protected boolean changeDefAct = false;

    protected void changeDefaultAction(String action){
        if(!allowChange(action)) return;
        defaultAction = action;
    }

    @Override
    public void execute(Hero hero, String action){
        super.execute(hero, action);
        if(changeDefAct) changeDefaultAction(action);
    }

    protected boolean allowChange(String action){
        return !(action.equals(AC_DROP) || action.equals(AC_THROW));
    }
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
