package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.TheEndspeaker;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class EndspeakerRoom extends SpecialRoom {
    @Override
    public int maxHeight() { return 12; }
    @Override
    public int maxWidth() { return 12; }
    @Override
    public int minHeight() { return 9; }
    @Override
    public int minWidth() { return 9; }

    public void paint(Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );

        Door entrance = entrance();

        entrance.set( Door.Type.BARRICADE );

        Point c = center();
        int cx = c.x;
        int cy = c.y;
        TheEndspeaker endspeaker = new TheEndspeaker();
        endspeaker.pos = cx + cy * level.width();
        level.mobs.add( endspeaker );
        TheEndspeaker.Status.spawned = true;

        level.addItemToSpawn( new PotionOfLiquidFlame() );
    }
}
