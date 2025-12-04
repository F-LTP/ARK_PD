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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;

import java.util.HashSet;
import java.util.Set;

public class BossMultiHealthBar extends Component {

    private Image bar;

    private Image rawShielding;
    private Image shieldedHP;
    private Image hp;

    private static final Set<Mob> bosses = new HashSet<>();

    private Image skull;
    private Emitter blood;

    private static String asset = Assets.Interfaces.BOSSHP;

    private static BossMultiHealthBar instance;
    private static boolean bleeding;

    BossMultiHealthBar() {
        super();
        visible = active = (!bosses.isEmpty());
        instance = this;
    }

    @Override
    protected void createChildren() {
        bar = new Image(asset, 0, 0, 64, 16);
        add(bar);

        width = bar.width;
        height = bar.height;

        rawShielding = new Image(asset, 15, 25, 47, 4);
        rawShielding.alpha(0.5f);
        add(rawShielding);

        shieldedHP = new Image(asset, 15, 25, 47, 4);
        add(shieldedHP);

        hp = new Image(asset, 15, 19, 47, 4);
        add(hp);

        skull = new Image(asset, 5, 18, 6, 6);
        add(skull);

        blood = new Emitter();
        blood.pos(skull);
        blood.pour(BloodParticle.FACTORY, 0.3f);
        blood.autoKill = false;
        blood.on = false;
        add( blood );
    }

    @Override
    protected void layout() {
        bar.x = x;
        bar.y = y;

        hp.x = shieldedHP.x = rawShielding.x = bar.x+15;
        hp.y = shieldedHP.y = rawShielding.y = bar.y+6;

        skull.x = bar.x+5;
        skull.y = bar.y+5;
    }

    @Override
    public void update() {
        super.update();
        if (!bosses.isEmpty()){

            if (!atLeastOneBossAlive()){
                bosses.clear();
                visible = active = false;
            } else {

                float health = 0;
                float shield = 0;
                float max = 0;

                for (Mob boss : bosses) {
                    health += boss.HP;
                    shield += boss.shielding();
                    max += boss.HT;
                }

                hp.scale.x = Math.max( 0, (health-shield)/max);
                shieldedHP.scale.x = health/max;
                rawShielding.scale.x = shield/max;

                float bleedThreshold = 1.0f / bosses.size();
                if (hp.scale.x < bleedThreshold) bleed( true );

                if (bleeding != blood.on){
                    if (bleeding)   skull.tint( 0xcc0000, 0.6f );
                    else            skull.resetColor();
                    blood.on = bleeding;
                }
            }
        }
    }

    private boolean atLeastOneBossAlive() {
        for (Mob boss : bosses) {
            if (boss.isAlive() && Dungeon.level.mobs.contains(boss)) {
                return true;
            }
        }
        return false;
    }

    public static void assignBoss(Mob boss){
        BossMultiHealthBar.bosses.add(boss);
        bleed(false);
        if (instance != null) {
            instance.visible = instance.active = true;
        }
    }

    public static void bleed(boolean value){
        bleeding = value;
    }

}