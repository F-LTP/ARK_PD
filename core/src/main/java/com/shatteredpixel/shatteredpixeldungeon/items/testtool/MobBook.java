package com.shatteredpixel.shatteredpixeldungeon.items.testtool;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.BloodMagister;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Centurion;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.EmperorPursuer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Faust;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Kaltsit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.MagicGolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Mon3tr;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Sentinel;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.Shadow;
import com.shatteredpixel.shatteredpixeldungeon.custom.dict.Dict;
import com.shatteredpixel.shatteredpixeldungeon.custom.dict.WndScrollTitledMessage;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MobBook extends ChallengeItem{
    {
        image = ItemSpriteSheet.GUIDE_PAGE;
        defaultAction = AC_READ;
    }

    private static final String AC_READ = "read";

    private int mobTier = 1;
    private int mobIndex = 0;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
         if (action.equals(AC_READ)) {
            GameScene.show(new MobBook.WndSetMob());
        }
    }


    protected int maxMobIndex(int tier){
        switch (tier){
            case 1: return MobBook.DataPack.GREAT_CRAB.ordinal();
            case 2: return MobBook.DataPack.NEW_FIRE_ELE.ordinal() - MobBook.DataPack.GREAT_CRAB.ordinal() - 1;
            case 3: return MobBook.DataPack.DM201.ordinal() - MobBook.DataPack.NEW_FIRE_ELE.ordinal() - 1;
            case 4: return MobBook.DataPack.ELE_CHAOS.ordinal() - MobBook.DataPack.DM201.ordinal() - 1;
            case 5: return MobBook.DataPack.ACIDIC.ordinal() - MobBook.DataPack.ELE_CHAOS.ordinal() - 1;
            case 6: return MobBook.DataPack.PIRANHA.ordinal() - MobBook.DataPack.ACIDIC.ordinal() - 1;
            case 7: return MobBook.DataPack.FIRECORE.ordinal() - MobBook.DataPack.PIRANHA.ordinal() - 1;
            case 8: return MobBook.DataPack.T_SHAMAN.ordinal() - MobBook.DataPack.FIRECORE.ordinal() - 1;
            case 9: default: return MobBook.DataPack.GUIDER.ordinal() - MobBook.DataPack.T_SHAMAN.ordinal() - 1;
        }
    }
    private int dataThreshold(int tier){
        switch (tier){
            case 1: default:
                return 0;
            case 2:
                return MobBook.DataPack.GREAT_CRAB.ordinal()+1;
            case 3:
                return MobBook.DataPack.NEW_FIRE_ELE.ordinal()+1;
            case 4:
                return MobBook.DataPack.DM201.ordinal()+1;
            case 5:
                return MobBook.DataPack.ELE_CHAOS.ordinal()+1;
            case 6:
                return MobBook.DataPack.ACIDIC.ordinal()+1;
            case 7:
                return MobBook.DataPack.PIRANHA.ordinal()+1;
            case 8:
                return MobBook.DataPack.FIRECORE.ordinal()+1;
            case 9:
                return MobBook.DataPack.T_SHAMAN.ordinal()+1;
        }
    }

    @Override
    public void storeInBundle(Bundle b){
        super.storeInBundle(b);
        b.put("mobTier", mobTier);
        b.put("mobIndex", mobIndex);
    }

    @Override
    public void restoreFromBundle(Bundle b){
        super.restoreFromBundle(b);
        mobTier = b.getInt("mobTier");
        mobIndex = b.getInt("mobIndex");
    }


    private class WndSetMob extends Window {

        private static final int WIDTH = 148;
        private static final int HEIGHT = 120;
        private static final int BTN_SIZE = 16;
        private static final int GAP = 2;

        private RenderedTextBlock selectedPage;
        private ArrayList<IconButton> mobButtons = new ArrayList<>();
        private RenderedTextBlock selectedMob;
        private RedButton lookButton;
        public WndSetMob(){
            super();

            resize(WIDTH, HEIGHT);

            RedButton lhs = new RedButton("<<<", 8){
                @Override
                public void onClick(){
                    mobTier--;
                    if(mobTier < 1 || mobTier>9){
                        mobTier = 9;
                    }
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier));
                    refreshImage();
                    updateSelectedMob();
                }
            };
            lhs.setRect(GAP, GAP, 24, 18);
            add(lhs);

            RedButton rhs = new RedButton(">>>", 8){
                @Override
                public void onClick(){
                    mobTier++;
                    if(mobTier < 1 || mobTier > 9){
                        mobTier = 1;
                    }
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier));
                    refreshImage();
                    updateSelectedMob();
                }
            };
            rhs.setRect(WIDTH - 24 - GAP,  GAP, 24, 18);
            add(rhs);

            selectedPage = PixelScene.renderTextBlock("", 9);
            PixelScene.align(selectedPage);
            add(selectedPage);

            selectedMob = PixelScene.renderTextBlock("", 9);
            selectedMob.hardlight(0xFFFF44);
            PixelScene.align(selectedMob);
            add(selectedMob);

            createMobImage();
            lookButton = new RedButton(Messages.get(MobBook.class,"look"), 8){
                @Override
                public void onClick(){
                    if (inside(x, y)) {
                        GameScene.show(new WndScrollTitledMessage(Reflection.newInstance(allData.get(dataThreshold(mobTier)+mobIndex).getMobClass()).sprite(), M.TL(allData.get(dataThreshold(mobTier) + mobIndex).mobClass,"name"), M.L(Dict.class,"mob_"+allData.get(dataThreshold(mobTier) + mobIndex).mobClass.toString().substring(59)+"_d"),152));
                        //GameScene.show(new WndTitledMessage(Reflection.newInstance(allData.get(dataThreshold(mobTier)+mobIndex).getMobClass()).sprite(), M.TL(allData.get(dataThreshold(mobTier) + mobIndex).mobClass,"name"), M.L(Dict.class,"mob_"+allData.get(dataThreshold(mobTier) + mobIndex).mobClass.toString().substring(59)+"_d")));
                    }
                }
            };
            lookButton.setRect((WIDTH-24)/2, selectedMob.bottom()+GAP, 24, 18);
            add(lookButton);
            updateSelectedMob();
            layout();
        }

        private void updateSelectedMob(){
            int selected = mobTier;
            StringBuilder sb = new StringBuilder();
            for(int i=1;i<=9;++i){
                sb.append((i==selected? "* ":"- "));
            }
            selectedPage.text(sb.toString());
            selectedPage.maxWidth(WIDTH / 2);
            selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
            updateMobText();
        }

        private void updateMobText(){
            selectedMob.text( M.L(allData.get(dataThreshold(mobTier) + mobIndex).mobClass, "name") );
        }

        private void layout(){
            selectedPage.maxWidth(WIDTH / 2);
            selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
            selectedMob.maxWidth(WIDTH);
            selectedMob.setPos((WIDTH - selectedMob.width())/2, 80);
            lookButton.setPos((WIDTH-lookButton.width())/2,100);
            resize(WIDTH, (int)lookButton.bottom() + 1);
        }

        private void createMobImage() {
            int maxNum = maxMobIndex(mobTier) + 1;
            //(N+1)/2
            int firstLine = (maxNum >> 1) + (maxNum & 1);
            float left1 = (WIDTH - (GAP + BTN_SIZE) * firstLine + GAP)/2f;
            float left2 = (WIDTH - (GAP + BTN_SIZE) * (maxNum - firstLine) + GAP)/2f;
            for (int i = 0; i < maxNum; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    public void onClick() {
                        super.onClick();
                        mobIndex = j;
                        updateMobText();
                    }
                };
                btn.icon( Reflection.newInstance(allData.get(dataThreshold(mobTier)+i).getMobClass()).sprite());
                //btn.icon( DictSpriteSheet.miscImages(allData.get(dataThreshold(mobTier)+i).imageId) );
                float max = Math.max(btn.icon().width(), btn.icon().height());
                btn.icon().scale = new PointF(BTN_SIZE/max, BTN_SIZE/max);
                if(i<firstLine){
                    btn.setRect(left1, 30f, BTN_SIZE, BTN_SIZE );
                    left1 += GAP + BTN_SIZE;
                }else{
                    btn.setRect(left2, 56f, BTN_SIZE, BTN_SIZE);
                    left2 += GAP + BTN_SIZE;
                }
                add(btn);
                mobButtons.add(btn);
            }
        }

        private void clearImage(){
            for(int i=0, len = mobButtons.size();i<len;++i){
                mobButtons.get(i).destroy();
            }
        }

        private void refreshImage(){
            clearImage();
            createMobImage();
        }
    }


    //packed with a linkedHashmap to find class by ordinal at O(1);
    private static LinkedHashMap<Integer, MobBook.DataPack> allData = new LinkedHashMap<>();
    static {
        for(MobBook.DataPack dp : MobBook.DataPack.values()){
            allData.put(dp.ordinal(), dp);
        }
    }
    private enum DataPack{
        RAT(Slug.class),
        //TESTRAT(TestRat.class, DictSpriteSheet.RAT),
        GNOLL(Gnoll.class),
        SNAKE(Snake.class),
        ALBINO(Albino.class),
        CRAB(Hound.class),
        SWARM(Swarm.class),
        SLIME(Slime.class),
        C_SLIME(CausticSlime.class),
        SENTINEL(Sentinel.class),
        GOO(Goo.class),
        F_RAT(FetidSlug.class),
        GNOLL_DARTER(GnollTrickster.class),
        GREAT_CRAB(ReunionDefender.class),

        SKELETON(Skeleton.class),
        AIR(AirborneSoldier.class),//now the id is meaningless
        THIEF(Thief.class),
        BANDIT(Bandit.class),
        DM100(DM100.class),
        GUARD(Guard.class),
        NECRO(Necromancer.class),
        CENTURION(Centurion.class),
        TENGU(NewTengu.class),
        ROT_LASHER(RotLasher.class),
        ROT_HEART(RotHeart.class),
        NEW_FIRE_ELE(Elemental.NewbornFireElemental.class),

        BAT(Bat.class),
        BRUTE(Brute.class),
        ARMORED_BRUTE(ArmoredBrute.class),
        SHAMAN(Shaman.random()),
        SPINNER(ExplodSlug_N.class),
        ZEALOT(MudrockZealot.class),
        BLOOD(BloodMagister.class),
        M_GOLEM(MagicGolem.class),
        DM300(NewDM300.class),
        PYLON(Pylon.class),
        DM200(DM200.class),
        DM201(DM201.class),

        GHOUL(Ghoul.class),
        WARLOCK(Warlock.class),
        MONK(Monk.class),
        SENIOR(Senior.class),
        GOLEM(Golem.class),
        FAUST(Faust.class),
        MFST(DwarfKing.class),
        ELE_FIRE(Elemental.FireElemental.class),
        ELE_FROST(Elemental.FrostElemental.class),
        ELE_LIGHTNING(Elemental.ShockElemental.class),
        ELE_CHAOS(Elemental.ChaosElemental.class),

        RIPPER(RipperDemon.class),
        SPAWNER(DemonSpawner.class),
        EYE(Eye.class),
        DRONE(EmpireDrone.class),
        SUCCUBUS(Succubus.class),
        RAIDER(Raider.class),
        STRIKER(Striker.class),
        STRIKER_E(StrikerElite.class),
        PURSUER(EmperorPursuer.class),
        YOG(Talulah.class),
        LARVA(Talulah.InfectedPatrolCaptain.class),
        BFIST(YogFist.BurningFist.class),
        RFIST(YogFist.RottingFist.class),
        FIST(YogFist.BrightFist.class),
        BS(Talu_BlackSnake.class),
        SCORPIO(Scorpio.class),
        ACIDIC(Acidic.class),

        BEE(Bee.class),
        STATUE(Statue.class),
        ARMORED_STATUE(ArmoredStatue.class),
        WRAITH(Wraith.class),
        MIMIC(Mimic.class),
        GOLDEN(GoldenMimic.class),
        CAYSTAL(CrystalMimic.class),
        SLAYER(Crownslayer_shadow.class),
        CANNOT(Cannot.class),
        FANATIC(Fanatic.class),
        SAND(SandPillar.class),
        KALTSIT(Kaltsit.class),
        MON3TR(Mon3tr.class),
        SHADOW(Shadow.TypeSPT.class),
        PIRANHA(Piranha.class),

        INFANTRY(Infantry.class),
        ERGATE(Ergate.class),
        SAILOR(Piersailor.class),
        BOAT(HeavyBoat.class),
        SNIPER(Sniper.class),
        CASTER(WaveCaster.class),
        AGENT(Agent.class),
        KLN(SiestaBoss.class),
        BLACK(Schwarz.class),
        LAVASLUG(LavaSlug.class ),
        METALCRAB(MetalCrab.class),
        SPIDER(MutantSpider.class),
        BREAKER(Rockbreaker.class),
        EXPLODE(ExplodeSlug_A.class),
        ORIGIN(Originiutant.class),
        ACID_A(AcidSlug_A.class),
        POMPEII(Pompeii.class),
        FIRECORE(FireCore.class),

        WARRIOR(TiacauhWarrior.class),
        T_FANATIC(TiacauhFanatic.class),
        LANCER(TiacauhLancer.class),
        ADDICT(TiacauhAddict.class),
        T_RIPPER(TiacauhRipper.class),
        SHREDDER(TiacauhShredder.class),
        RITUALIST(TiacauhRitualist.class),
        TOMIMI(Tomimi.class),
        BRAVE(TiacauhBrave.class),
        T_SNIPER(TiacauhSniper.class),
        MUSHROOM(GiantMushroom.class),
        EUNECTES(Eunectes.class),
        UGLY(TheBigUglyThing.class),
        T_SHAMAN(TiacauhShaman.class),

        RUNNER(SeaRunner.class),
        DRIFTER(FloatingSeaDrifter.class),
        REAPER(SeaReaper.class),
        CAPSULE(SeaCapsule.class),
        OCTO(Sea_Octo.class),
        LEAF(SeaLeef.class),
        //SEABOSS1(SeaBoss1.class),change from budding
        GUIDER(NetherseaBrandguider.class);
        private Class<? extends Mob> mobClass;

        DataPack(Class<? extends Mob> cls){
            this.mobClass = cls;
        }

        public Class<? extends Mob> getMobClass(){return mobClass;}
    }
}
