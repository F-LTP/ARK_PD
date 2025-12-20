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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHeroInfo;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.GameMath;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.RectF;

import java.util.ArrayList;

public class HeroSelectScene extends PixelScene {

	private Image background;
    private Image fadeLeft, fadeRight;
	private RenderedTextBlock prompt;

	//fading UI elements
    private RenderedTextBlock title;
	private ArrayList<StyledButton> heroBtns = new ArrayList<>();
	private StyledButton startBtn;
	private IconButton infoButton;
    private IconButton btnOptions;
    private GameOptions optionsPane;
	private IconButton challengeButton;
	private IconButton toggleButton;
	private IconButton btnExit;
	private int change;

	private RectF insets;

	@Override
	public void create() {
		super.create();

		Dungeon.hero = null;

		Badges.loadGlobal();
		Journal.loadGlobal();

		insets = Game.platform.getSafeInsets(PlatformSupport.INSET_BLK).scale(1f/defaultZoom);

        float w = (Camera.main.width - insets.left - insets.right);
        float h = (Camera.main.height - insets.top - insets.bottom);

		background = new Image(HeroClass.WARRIOR.splashArt()){
			@Override
			public void update() {
				if (rm > 1f){
					rm -= Game.elapsed;
					gm = bm = rm;
				} else {
					rm = gm = bm = 1;
				}
			}
		};
		background.scale.set(Camera.main.height/background.height);

		background.x = (Camera.main.width - background.width())/2f;
		background.y = (Camera.main.height - background.height())/2f;

		background.visible = false;
		PixelScene.align(background);
		add(background);

		if (background.x > 0){
			Image fadeLeft = new Image(TextureCache.createGradient(0xFF000000, 0x00000000));
			fadeLeft.x = background.x-2;
			fadeLeft.scale.set(4, background.height());
			add(fadeLeft);

			Image fadeRight = new Image(fadeLeft);
			fadeRight.x = background.x + background.width() + 2;
			fadeRight.y = background.y + background.height();
			fadeRight.angle = 180;
			add(fadeRight);
		}

		prompt = PixelScene.renderTextBlock(Messages.get(this, "title"), 12);
		prompt.hardlight(Window.TITLE_COLOR);
		prompt.setPos( (Camera.main.width - prompt.width())/2f, (Camera.main.height - HeroBtn.HEIGHT - prompt.height() - 4 - insets.bottom));
		PixelScene.align(prompt);
		add(prompt);

		startBtn = new StyledButton(Chrome.Type.GREY_BUTTON_TR, ""){
			@Override
			protected void onClick() {
				super.onClick();

				if (GamesInProgress.selectedClass == null) return;

				Dungeon.hero = null;
                Dungeon.initSeed();
                ActionIndicator.clearAction();
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

				if (SPDSettings.intro()) {
					SPDSettings.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					Game.switchScene( InterlevelScene.class );
				}
			}
		};
		startBtn.icon(Icons.get(Icons.ENTER));
		startBtn.setSize(80, 21);
		startBtn.setPos((Camera.main.width - startBtn.width())/2f, (Camera.main.height - HeroBtn.HEIGHT + 2 - startBtn.height()) - insets.bottom);
		add(startBtn);
		startBtn.visible = false;

        infoButton = new IconButton(Icons.get(Icons.INFO)){
            @Override
            protected void onClick() {
                super.onClick();
                HeroClass cls = GamesInProgress.selectedClass;
                if (cls != null) {
                    Window info = new WndHeroInfo(GamesInProgress.selectedClass);
                    if (landscape()) {
                        info.offset((int)(w / 6), 0);
                    }
                    TomorrowRogueNight.scene().addToFront(info);
                }
            }
        };
		infoButton.visible = false;
		infoButton.setSize(21, 21);
		add(infoButton);

		HeroClass[] classes = HeroClass.values();

		int btnWidth = HeroBtn.MIN_WIDTH;
		int curX = (Camera.main.width - btnWidth * 6)/2;
		if (curX > 0){
			btnWidth += Math.min(curX/(6/2), 15);
			curX = (Camera.main.width - btnWidth * 6)/2;
		}

		int i = 0;
		int heroBtnleft = curX;
		for (HeroClass cl : classes){
			if (i==6) break;
			HeroBtn button = new HeroBtn(cl);
			button.setRect(curX, Camera.main.height-HeroBtn.HEIGHT+3-insets.bottom, btnWidth, HeroBtn.HEIGHT);
			curX += btnWidth;
			add(button);
			heroBtns.add(button);
			i++;
		}

        for (HeroClass cl : HeroClass.values()){
            HeroBtn button = new HeroBtn(cl);
            add(button);
            heroBtns.add(button);
        }

        optionsPane = new GameOptions();
        optionsPane.visible = optionsPane.active = false;
        optionsPane.layout();
        add(optionsPane);

        btnOptions = new IconButton(Icons.get(Icons.PREFS)){
            @Override
            protected void onClick() {
                super.onClick();
                optionsPane.visible = !optionsPane.visible;
                optionsPane.active = !optionsPane.active;
            }

            @Override
            protected void onPointerDown() {
                super.onPointerDown();
            }

            @Override
            protected void onPointerUp() {
                updateOptionsColor();
            }
        };
        updateOptionsColor();
        btnOptions.visible = false;

        if(!SPDSettings.intro()){
            add(btnOptions);
        }
        btnOptions.setRect(heroBtns.get(0).left() + 16, Camera.main.height-HeroBtn.HEIGHT-16, 20, 21);
        optionsPane.setPos(heroBtns.get(0).left(), 0);

//		challengeButton = new IconButton(
//				Icons.get( SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
//			@Override
//			protected void onClick() {
//				TomorrowRogueNight.scene().addToFront(new WndChallenges(SPDSettings.challenges(), true) {
//					public void onBackPressed() {
//						super.onBackPressed();
//						icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
//					}
//				} );
//			}
//
//			@Override
//			public void update() {
//				if( !visible && GamesInProgress.selectedClass != null){
//					visible = true;
//				}
//				super.update();
//			}
//		};
//		challengeButton.setRect(heroBtnleft + 16, Camera.main.height-HeroBtn.HEIGHT-16 - insets.bottom, 21, 21);
//		challengeButton.visible = false;
//
//		if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)){
//			add(challengeButton);
//		} else {
//			Dungeon.challenges = 0;
//			SPDSettings.challenges(0);
//		}

		toggleButton = new IconButton(
				Icons.get( Icons.HERO_CHANGES)){
			@Override
			protected void onClick() {
				toggleAvailableHeroes();
			}

			@Override
			public void update() {
				super.update();
			}
		};

		btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width() - insets.right, insets.top );
		add( btnExit );
		btnExit.visible = !SPDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;

		toggleButton.setSize(21,21);
		toggleButton.setPos( btnExit.width() - 21 + insets.left, insets.top );
		add(toggleButton);
		toggleButton.visible = true;

		PointerArea fadeResetter = new PointerArea(0, 0, Camera.main.width, Camera.main.height){
			@Override
			public boolean onSignal(PointerEvent event) {
				resetFade();
				return false;
			}
		};
		add(fadeResetter);
		resetFade();

		if (GamesInProgress.selectedClass != null){
			setSelectedHero(GamesInProgress.selectedClass);
		}

		fadeIn();

	}

    private void updateOptionsColor(){
        if (!SPDSettings.customSeed().isEmpty()){
            btnOptions.icon().hardlight(1f, 1.5f, 0.67f);
        } else if (SPDSettings.challenges() != 0){
            btnOptions.icon().hardlight(2f, 1.33f, 0.5f);
        } else {
            btnOptions.icon().resetColor();
        }
    }

	private void setSelectedHero(HeroClass cl){
		GamesInProgress.selectedClass = cl;

		background.texture( cl.splashArt() );
		background.visible = true;
		background.hardlight(1.5f,1.5f,1.5f);

		prompt.visible = false;
		startBtn.visible = true;
		startBtn.text(Messages.titleCase(cl.title()));
		startBtn.textColor(Window.TITLE_COLOR);
		startBtn.setSize(startBtn.reqWidth() + 8, 21);
		startBtn.setPos((Camera.main.width - startBtn.width())/2f, startBtn.top());
		PixelScene.align(startBtn);

		infoButton.visible = true;
		infoButton.setPos(startBtn.right(), startBtn.top());

        btnOptions.visible = btnOptions.active = !SPDSettings.intro();
        btnOptions.setPos(startBtn.left()-btnOptions.width(), startBtn.top());

        optionsPane.setPos(heroBtns.get(0).left(), startBtn.top() - optionsPane.height() - 2);
        align(optionsPane);
//		challengeButton.visible = true;
//		challengeButton.setPos(startBtn.left()-challengeButton.width(), startBtn.top());
	}

	private void toggleAvailableHeroes() {
		GamesInProgress.selectedClass = null;

		background.visible = false;
		startBtn.visible = false;
		infoButton.visible = false;
        btnOptions.visible = false;
        optionsPane.visible = false;
//		challengeButton.visible = false;

		if (change == 0) change = 1;
		else change = 0;

		int i = change * 6;

		for (int j = 0; j<heroBtns.size(); j++) {
			heroBtns.get(j).destroy();
		}

		HeroClass[] classes = HeroClass.values();

		int btnWidth = HeroBtn.MIN_WIDTH;
		int curX = (Camera.main.width - btnWidth * 6)/2;
		if (curX > 0){
			btnWidth += Math.min(curX/(6/2), 15);
			curX = (Camera.main.width - btnWidth * 6)/2;
		}
		for (int p = 0; p<6; p++){
			if (classes.length <= p+i) break;
			HeroBtn button = new HeroBtn(classes[p+i]);
			button.setRect(curX, Camera.main.height-HeroBtn.HEIGHT+3-insets.bottom, btnWidth, HeroBtn.HEIGHT);
			curX += btnWidth;
			add(button);
			heroBtns.add(button);
		}
	}

	private float uiAlpha;

	@Override
	public void update() {
		super.update();
        if (SPDSettings.intro() && Rankings.INSTANCE.totalNumber > 0){
            SPDSettings.intro(false);
        }
        btnExit.visible = btnExit.active = !SPDSettings.intro();
		//do not fade when a window is open
		for (Object v : members){
			if (v instanceof Window) resetFade();
		}
		if (GamesInProgress.selectedClass != null) {
			if (uiAlpha > 0f){
				uiAlpha -= Game.elapsed/4f;
			}
			float alpha = GameMath.gate(0f, uiAlpha, 1f);
			for (StyledButton b : heroBtns){
				b.alpha(alpha);
			}
            updateFade();
		}
	}

    private void updateFade(){
        float alpha = GameMath.gate(0f, uiAlpha, 1f);
        for (StyledButton b : heroBtns){
            b.enable(alpha != 0);
            b.alpha(alpha);
        }
        startBtn.enable(alpha != 0);
        startBtn.alpha(alpha);
        btnExit.enable(btnExit.visible && alpha != 0);
        btnExit.icon().alpha(alpha);
        toggleButton.enable(alpha != 0);
        toggleButton.icon().alpha(alpha);
        optionsPane.active = optionsPane.visible && alpha != 0;
        optionsPane.alpha(alpha);
        btnOptions.enable(alpha != 0);
        btnOptions.icon().alpha(alpha);
        infoButton.enable(alpha != 0);
        infoButton.icon().alpha(alpha);
    }
	private void resetFade(){
        //starts fading after 4 seconds, fades over 4 seconds.
        uiAlpha = 2f;
        updateFade();
	}

	@Override
	protected void onBackPressed() {
		if (btnExit.visible){
			TomorrowRogueNight.switchScene(TitleScene.class);
		} else {
			super.onBackPressed();
		}
	}

	private class HeroBtn extends StyledButton {

		private HeroClass cl;

		private static final int MIN_WIDTH = 20;
		private static final int HEIGHT = 24;

		HeroBtn ( HeroClass cl ){
			super(Chrome.Type.GREY_BUTTON_TR, "");

			this.cl = cl;

			switch (cl) {
				case WARRIOR: default:
					icon(new Image(Icons.BLAZE.get()));
					break;
				case MAGE:
					icon(new Image(Icons.AMIYA.get()));
					break;
				case ROGUE:
					icon(new Image(Icons.P_RED.get()));
					break;
				case HUNTRESS:
					icon(new Image(Icons.GREY.get()));
					break;
				case ROSECAT:
					icon(new Image(Icons.ROSEMARI.get()));
					break;
				case NEARL:
					icon(new Image(Icons.NEARL.get()));
					break;
				case CHEN: //첸포인트
					icon(new Image(Icons.CHEN.get()));
					break;
			}

		}

		@Override
		public void update() {
			super.update();
			if (cl != GamesInProgress.selectedClass){
				if (!cl.isUnlocked()){
					icon.brightness(0.1f);
				} else {
					icon.brightness(0.6f);
				}
			} else {
				icon.brightness(1f);
			}
		}

		@Override
		protected void onClick() {
			super.onClick();

			if( !cl.isUnlocked() ){
				TomorrowRogueNight.scene().addToFront( new WndMessage(cl.unlockMsg()));
			} else if (GamesInProgress.selectedClass == cl) {
                Window w = new WndHeroInfo(cl);
                TomorrowRogueNight.scene().addToFront(w);
			} else {
				setSelectedHero(cl);
			}
		}
	}

    private class GameOptions extends Component {
        private NinePatch bg;

        private ArrayList<StyledButton> buttons;
        private ArrayList<ColorBlock> spacers;

        @Override
        protected void createChildren() {

            bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
            add(bg);

            buttons = new ArrayList<>();
            spacers = new ArrayList<>();
            StyledButton seedButton = new StyledButton(Chrome.Type.BLANK, Messages.get(HeroSelectScene.class, "custom_seed"), 6){
                @Override
                protected void onClick() {
                    if (!Badges.isUnlocked(Badges.Badge.VICTORY) && !DeviceCompat.isDebug()){
                        TomorrowRogueNight.scene().addToFront( new WndTitledMessage(
                                Icons.get(Icons.SEED_POUCH),
                                Messages.get(HeroSelectScene.class, "custom_seed"),
                                Messages.get(HeroSelectScene.class, "custom_seed_nowin"))
                        );
                        return;
                    }

                    String existingSeedtext = SPDSettings.customSeed();
                    TomorrowRogueNight.scene().addToFront( new WndTextInput(Messages.get(HeroSelectScene.class, "custom_seed_title"),
                            Messages.get(HeroSelectScene.class, "custom_seed_desc"),
                            existingSeedtext,
                            20,
                            false,
                            Messages.get(HeroSelectScene.class, "custom_seed_set"),
                            Messages.get(HeroSelectScene.class, "custom_seed_clear")){
                        @Override
                        public void onSelect(boolean positive, String text) {
                            text = DungeonSeed.formatText(text);
                            long seed = DungeonSeed.convertFromText(text);

                            if (positive && seed != -1){

                                for (GamesInProgress.Info info : GamesInProgress.checkAll()){
                                    if ((info.customSeed == null || info.customSeed.isEmpty()) && info.seed == seed){
                                        SPDSettings.customSeed("");
                                        icon.resetColor();
                                        TomorrowRogueNight.scene().addToFront(new WndMessage(Messages.get(HeroSelectScene.class, "custom_seed_duplicate")));
                                        return;
                                    }
                                }

                                SPDSettings.customSeed(text);
                                icon.hardlight(1f, 1.5f, 0.67f);
                            } else {
                                SPDSettings.customSeed("");
                                icon.resetColor();
                            }
                            updateOptionsColor();
                        }
                    });
                }
            };
            seedButton.leftJustify = true;
            seedButton.icon(Icons.get(Icons.SEED_POUCH));
            if (!SPDSettings.customSeed().isEmpty()) seedButton.icon().hardlight(1f, 1.5f, 0.67f);;
            buttons.add(seedButton);
            add(seedButton);

            StyledButton challengeButton = new StyledButton(Chrome.Type.BLANK, Messages.get(WndChallenges.class, "title"), 6){
                @Override
                protected void onClick() {
                    if (!Badges.isUnlocked(Badges.Badge.VICTORY) && !DeviceCompat.isDebug()){
                        TomorrowRogueNight.scene().addToFront( new WndTitledMessage(
                                Icons.get(Icons.CHALLENGE_OFF),
                                Messages.get(WndChallenges.class, "title"),
                                Messages.get(HeroSelectScene.class, "challenges_nowin")
                        ));
                        return;
                    }

                    TomorrowRogueNight.scene().addToFront(new WndChallenges(SPDSettings.challenges(), true) {
                        public void onBackPressed() {
                            super.onBackPressed();
                            icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
                            updateOptionsColor();
                        }
                    } );
                }
            };
            challengeButton.leftJustify = true;
            challengeButton.icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
            add(challengeButton);
            buttons.add(challengeButton);

            for (int i = 1; i < buttons.size(); i++){
                ColorBlock spc = new ColorBlock(1, 1, 0xFF000000);
                add(spc);
                spacers.add(spc);
            }
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;

            int width = 0;
            for (StyledButton btn : buttons){
                if (width < btn.reqWidth()) width = (int)btn.reqWidth();
            }
            width += bg.marginHor();

            int top = (int)y + bg.marginTop() - 1;
            int i = 0;
            for (StyledButton btn : buttons){
                btn.setRect(x+bg.marginLeft(), top, width - bg.marginHor(), 16);
                top = (int)btn.bottom();
                if (i < spacers.size()) {
                    spacers.get(i).size(btn.width(), 1);
                    spacers.get(i).x = btn.left();
                    spacers.get(i).y = PixelScene.align(btn.bottom()-0.5f);
                    i++;
                }
            }

            this.width = width;
            this.height = top+bg.marginBottom()-y-1;
            bg.size(this.width, this.height);

        }

        private void alpha( float value ){
            bg.alpha(value);

            for (StyledButton btn : buttons){
                btn.alpha(value);
            }

            for (ColorBlock spc : spacers){
                spc.alpha(value);
            }
        }

    }
}