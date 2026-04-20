package com.shatteredpixel.shatteredpixeldungeon.items.wands.SP;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlueBlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SkillBook;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAmplified;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StaffOfPurgatory extends Wand {
    {
        image = ItemSpriteSheet.WAND_LAVA;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
    }
    private int min(int lvl) { return 2 + lvl; }
    private int max(int lvl) { return 4 + lvl * 3; }
    private int min() { return min(buffedLvl()); }
    private int max() { return max(buffedLvl()); }
    private int aoeDamageRoll() { return Random.NormalIntRange(min(), max()); }
    @Override
    protected void onZap( Ballistica bolt ) {

        Char ch = Actor.findChar( bolt.collisionPos );
        if (ch != null) {
            if (!ch.properties().contains(Char.Property.IMMOVABLE)) {

                int beamdis = Dungeon.level.distance(Dungeon.hero.pos, ch.pos);

                processSoulMark(ch, chargesPerCast());
                int newpos = ch.pos;
                CellEmitter.get(Dungeon.hero.pos).burst(Speck.factory(Speck.WOOL), 10);
                CellEmitter.get(ch.pos).burst(Speck.factory(Speck.WOOL), 10);

                ch.pos = Dungeon.hero.pos;
                ch.sprite.place(Dungeon.hero.pos);

                if (!ch.flying && Dungeon.level.map[ch.pos] == Terrain.CHASM) {
                    if (ch instanceof Mob) {
                        Mob mob = ((Mob)ch);
                        Chasm.mobFall(mob);

                        Statistics.enemiesSlain++;
                        Badges.validateMonstersSlain();
                        Statistics.qualifiedForNoKilling = false;

                        if (mob.EXP > 0 && Dungeon.hero.lvl <= mob.maxLvl) {
                            Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(mob, "exp", mob.EXP));
                            Dungeon.hero.earnExp(mob.EXP, mob.getClass());
                        } else {
                            Dungeon.hero.earnExp(0, mob.getClass());
                        }
                    }
                }

                Dungeon.hero.sprite.place(newpos);
                Dungeon.hero.pos = newpos;

                if (!Dungeon.bossLevel() && !Dungeon.isInRhodes())  wandattack(ch, beamdis);
                //area damage around both the hero and the swapped target
                aoeBlast(Dungeon.hero.pos, aoeDamageRoll());
                aoeBlast(ch.pos, aoeDamageRoll());

                ch.damage(aoeDamageRoll(), this);
                Buff.affect(ch, Cripple.class, 2 + buffedLvl());

            } else {
                Dungeon.level.pressCell(bolt.collisionPos);
            }
        }
    }
    private void aoeBlast(int center, int dmg) {
        for (int i : PathFinder.NEIGHBOURS8) {
            int cell = center + i;
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                ch.damage(dmg, this);
                ch.sprite.centerEmitter().burst(BlastParticle.FACTORY, 3);
            }
        }
        CellEmitter.get(center).burst(BlastParticle.FACTORY, 10);
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
    }
    private void wandattack(Char defender, int distance) {
        Ballistica beam = new Ballistica(Dungeon.hero.pos, defender.pos, Ballistica.WONT_STOP);
        int maxDistance = distance;

        ArrayList<Char> chars = new ArrayList<>();


        int terrainPassed = 2;
        for (int c : beam.subPath(1, maxDistance)) {

            Char ch;
            if ((ch = Actor.findChar( c )) != null) {

                //we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
                //terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
                terrainPassed = terrainPassed%3;

                chars.add( ch );
            }
        }

        for (Char ch : chars) {
            if (!ch.properties().contains(Char.Property.IMMOVABLE) && ch != defender) {

                ch.sprite.centerEmitter().burst(BlastParticle.FACTORY, Random.IntRange(2, 5));
                ScrollOfTeleportation.teleportChar(ch);
                if (ch instanceof Mob) {
                    ((Mob) ch).state = ((Mob) ch).SLEEPING;
                }
            }
        }
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", min(), max());
        else
            return Messages.get(this, "stats_desc", min(0), max(0));
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        if (Dungeon.hero.belongings.getItem(SkillBook.class) != null) {
            SkillBook Item = Dungeon.hero.belongings.getItem(SkillBook.class);
            Item.SetCharge(1);
            SpellSprite.show(attacker, SpellSprite.CHARGE);
        }
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FIRE,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    protected int initialCharges() {
        return 1;
    }
}
