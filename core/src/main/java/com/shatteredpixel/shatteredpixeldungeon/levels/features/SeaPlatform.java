package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.SeaBossLevel2;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.List;

public class SeaPlatform extends Platform {

    {
        image = 5;
        generatorClass = LittleHandy.class;
    }

    @Override
    public void activate(Char ch) {
        return;
    }

    public static class LittleHandy extends Platform.Generator {
        {
            image = ItemSpriteSheet.SAINT_HAND;

            platformClass = SeaPlatform.class;

            bones = false;
        }

        @Override
        protected void onThrow( int cell ) {
            // Only activate in boss level 2 in Iberia and if thrown tile is a Nethersea Brand
            if (Dungeon.level instanceof SeaBossLevel2 && Dungeon.level.seaTerrors.get(cell) != null) {
                Dungeon.level.createPlatform(this, cell);
            } else {
                super.onThrow(cell);
            }
        }

        @Override
        public List<Platform> generate(int pos, Level level ) {
            if (level != null && level.heroFOV != null && level.heroFOV[pos]) {
                Sample.INSTANCE.play(Assets.Sounds.GRASS);
            }

            List<Platform> platforms = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS9) {
                int c = pos + n;
                // Generate Platform in 3x3 if it is a Nethersea Brand tile
                if (c >= 0 && c < Dungeon.level.length()
                        && Dungeon.level.platforms.get(c) == null
                        && Dungeon.level.seaTerrors.get(c) != null
                        && Dungeon.level.map[c] != Terrain.WELL) {
                    if (Dungeon.level.heroFOV[c]) {
                        CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                    }

                    Platform platform = Reflection.newInstance(platformClass);
                    platform.pos = c;
                    platforms.add(platform);
                }
            }
            return platforms;
        }

        @Override
        public String desc() {
            if (Dungeon.level instanceof SeaBossLevel2) {
                return Messages.get(this, "desc_active");
            } else {
                return Messages.get(this, "desc");
            }
        }
    }

    public static class EnhancedLittleHandy extends LittleHandy {

        public static final String AC_SPAWN = "SPAWN";

        private static final ItemSprite.Glowing GLOW = new ItemSprite.Glowing( 0x44AAFF, 0.4f );

        {
            defaultAction = AC_SPAWN;
            stackable = false;
            bones = false;
        }

        @Override
        public ItemSprite.Glowing glowing() {
            return GLOW;
        }

        @Override
        public ArrayList<String> actions( Hero hero ) {
            ArrayList<String> actions = super.actions( hero );
            actions.add( AC_SPAWN );
            return actions;
        }

        @Override
        public void execute( Hero hero, String action ) {
            super.execute( hero, action );
            // Only activate in boss level 2 in Iberia and if thrown tile is a Nethersea Brand
            if (Dungeon.level instanceof SeaBossLevel2 && action.equals( AC_SPAWN )) {
                GameScene.selectCell( spawnListener );
            }
        }

        private final CellSelector.Listener spawnListener = new CellSelector.Listener() {
            @Override
            public void onSelect( Integer cell ) {
                if (cell == null) return;
                if (!Dungeon.level.heroFOV[cell]) {
                    GLog.w( Messages.get( EnhancedLittleHandy.class, "bad_target" ) );
                    return;
                }
                List<Platform> placed = Dungeon.level.createPlatform( EnhancedLittleHandy.this, cell );
                if (placed.isEmpty()) {
                    GLog.w( Messages.get( EnhancedLittleHandy.class, "no_valid_tiles" ) );
                } else {
                    curUser.spendAndNext( 1f );
                }
            }

            @Override
            public String prompt() {
                return Messages.get( EnhancedLittleHandy.class, "prompt" );
            }
        };
    }
}