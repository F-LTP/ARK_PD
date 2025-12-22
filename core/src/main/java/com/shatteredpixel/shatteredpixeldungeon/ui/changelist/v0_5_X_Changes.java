package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class v0_5_X_Changes {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_v0_5_0_Changes(changeInfos);
    }

    public static void add_v0_5_0_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v0.5.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "베타 진행중",
                "아직 베타 진행중인 버전입니다!"));
        changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "이베리아",
                "새로운 보스 2종이 추가되었습니다." + "\n\n" +
                        "새로운 기믹인 _신경손상_과 _명흔_이 추가되었습니다." + "\n\n" +
                        "새로운 퀘스트가 추가되었습니다. 33층 랜덤한 방에서 찾을 수 있습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "사격무기",
                "새로운 무기 종류인 _사격 무기_가 추가되었습니다. 투척 무기로 재장전 하여, 일정 사거리 이내 적을 안전하게 처리할 수 있는 원거리 위주 무기군입니다. 추가로 다양한 부착물을 이용해 스텟을 변경 할 수 있습니다." + "\n\n" +
                        "_USG57_ 2티어 무기입니다. 기본적인 사격무기입니다. 사거리가 짧지만, 근접 페널피를 받지 않습니다." + "\n\n" +
                        "_화이트팽 465_ 3티어 무기로, 무난한 돌격소총입니다." + "\n\n" +
                        "_스털링 기관단총_ 3티어 무기로, 확률적으로 적을 한기상태로 만듭니다." + "\n\n" +
                        "_DP 27_ 4티어 무기로, 많은 양의 탄약을 장전할 수 있지만 정확도가 떨어집니다." + "\n\n" +
                        "_OTs-03_ 4티어 무기로, 강력한 데미지를 가졌지만 사격속도가 매우 느립니다." + "\n\n" +
                        "_6P41_ 5티어 무기로, 더욱 많은 양의 탄약을 장전할 수 있지만 정확도가 떨어집니다." + "\n\n" +
                        "_R4-C_ 5티어 무기로, 강력한 돌격소총입니다." + "\n\n"));
        changes.addButton(new ChangeButton(Icons.get(Icons.NAMSEK), "가비알 비주얼 리워크",
                "가비알 스테이지 _36-40층_의 비주얼이 새로 추가되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.SEED_POUCH), "고정 시드",
                "고정된 시드로 게임을 시작 할 수 있는 기능이 추가되었습니다." + "\n\n" +
                        "캐릭터 선택 화면에서 설정을 통해 시드 고정이 가능하며, 시드 고정시 뱃지 및 스킨 해금이 불가능합니다. 또한, 랭킹 페이지에서 고정된 시드 플레이는 최하단에 표시됩니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.DANWOO), "기타 편의성 업데이트",
                "박사 구출 후 켈시에게 말을 걸어도 게임이 끝나지 않습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(Icons.get(Icons.BUG_KILL), "버그 수정 1",
                "_절지생화_의 분신 소환시 간헐적으로 발생하던 크래시를 수정했습니다." + "\n\n" +
                        "_불사의 검은 뱀_의 페이즈가 진행되지 않던 버그를 수정했습니다." + "\n\n" +
                        "_정보증명서_가 제대로 저장 및 로드되지 않든 버그를 수정했습니다." + "\n\n" +
                        "_자연의 은총_및 잔디 생성이 맵 가장자리에서 발동 할 경우 발생하던 크래시를 수정했습니다" + "\n\n" +
                        "_베오울프_의 사거리가 4 이상일때 데미지 감소 페널티가 적용되던 버그를 수정했습니다" + "\n\n" +
                        "_EX-42_에 _도달의 아츠_가 있을 경우 사거리가 1로 감소하던 버그를 수정했습니다" + "\n\n" +
                        "_검술 아미야_의 _푸른 분노_특성이 표기대로 적용되지 않던 버그를 수정했습니다" + "\n\n" +
                        "_홀로 가는 먼 길_의 특수기술이 충전되지 않은 상태로도 사용 가능하던 버그를 수정했습니다"));
        changes.addButton(new ChangeButton(Icons.get(Icons.BUG_KILL), "버그 수정 2",
                "_탈룰라_가 정보증명서를 드롭하지 않던 버그를 수정했습니다." + "\n\n" +
                        "_지배의 반지_를 착용했을 때 발생하던 크래시를 수정했습니다." + "\n\n" +
                        "_나기나타_의 처형 발동 시 데미지 숫자가 출력되지 않도록 수정했습니다." + "\n\n" +
                        "_레드_의 _뼈찌르기_의 대상을 본인으로 지정하지 못하도록 수정했습니다." + "\n\n" +
                        "앱을 백그라운드로 전환 시 저장이 제대로 이루어지지 않아, 복귀 시 앱이 강제 종료되던 현상을 수정했습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "UI 업데이트",
                "여러 UI요소들이 업데이트 되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.DANWOO), "안드로이드 5.0 미만 지원중단",
                "자세한 내용은 0.5.0-B4 변경사항 코멘트를 참고 해 주세요."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "무기 상향",
                "_LL-200_ 필중 효과가 추가되었습니다." + "\n\n" +
                        "_베오울프_ 장착시 방어력을 증가시킵니다. 거리 조건이 4칸에서 3칸으로 완화되었습니다." + "\n\n" +
                        "_불꽃의 카타나_ 해방 조건이 125킬에서 85킬로 완화되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.RING), "반지 상향",
                "_저격의 반지_ 사격무기와의 시너지가 추가되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.NAMSEK), "정보증명서",
                "보스들이 드롭하는 정보증명서의 양이 증가되었습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.ENEMY_NERFS), "적 하향",
                "_가비알_ 몹들의 체력과 회피도가 하향되었습니다."));
        changeInfos.add(changes);
    }
}