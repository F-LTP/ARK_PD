C:\Users\f2778\Documents\GitHub\ARK_PD\Qoder.md
# Qoder.md — Qoder 项目认知文档

> 本文档由 Qoder 自主审阅项目代码后编写，用于记录对整个项目的架构认知与开发指南。
> 基于前任开发者编写的 CLAUDE.md，结合更深入的代码分析补充而成。

---

## 一、项目概述

**项目名称**: RogueNights / Arrrknights（测试-明日方舟地牢）  
**本质**: 基于 [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon) 的深度 Mod，融合了大量《明日方舟》(Arknights) 元素  
**包名**: `com.shatteredpixel.tomorrowpixel`  
**当前版本**: 0.5.3 (versionCode: 26083100)  
**许可证**: GPLv3  
**语言**: Java (兼容 Java 11)  
**构建系统**: Gradle 多模块  
**目标平台**: Android (min SDK 21, target SDK 35) + Desktop (LWJGL3)  
**Git 分支**: Player-Test  
**远程仓库**: origin (F-LTP/ARK_PD), upstream (tomorrows-ark-pd/ARK_PD)

---

## 二、技术栈

| 组件 | 版本/说明 |
|------|-----------|
| Java | 11 (Temurin 17.0.20 编译) |
| LibGDX | 1.13.5 (核心游戏框架) |
| GDX Controllers | 2.2.4 |
| LWJGL3 | 3.2.1 (桌面后端) |
| Android Gradle Plugin | 8.11.1 |
| Lombok | 1.18.42 (编译时注解处理) |
| LibGDX FreeType | 1.13.5 (字体渲染) |
| CustomActivityOnCrash | 2.3.0 (Android 崩溃处理) |
| JSON | 20170516 (Android 端) |

---

## 三、模块架构
ARK_PD/ ├── SPD-classes/ ← 底层引擎层 (com.watabou.) ├── core/ ← 核心游戏逻辑层 (com.shatteredpixel.shatteredpixeldungeon.) ├── android/ ← Android 平台启动器 ├── desktop/ ← Desktop 平台启动器 ├── services/ ← 模块化服务层 │ ├── updates/ ← 更新检查 (debugUpdates / githubUpdates) │ └── news/ ← 新闻推送 (debugNews / shatteredNews) └── build.gradle ← 根构建配置
### 依赖关系

android/desktop → core → SPD-classes → LibGDX core → services
### 模块详解

#### 1. SPD-classes（底层引擎）
- **包名**: `com.watabou.*`
- **职责**: 提供游戏引擎基础设施，与具体游戏逻辑完全解耦
- **子模块**:
  - `glscripts/` — GLSL 脚本管理
  - `gltextures/` — 纹理图集与缓存 (Atlas, SmartTexture, TextureCache)
  - `glwrap/` — OpenGL 封装 (Program, Shader, Texture, Framebuffer 等)
  - `input/` — 输入处理 (键盘、手柄、指针、滚动事件)
  - `noosa/` — 核心渲染引擎 (Game, Scene, Camera, Image, Tilemap 等)
    - `audio/` — 音乐与音效
    - `particles/` — 粒子系统
    - `tweeners/` — 补间动画
    - `ui/` — 基础 UI 组件
  - `utils/` — 工具类 (Bundle 序列化, Random, PathFinder 寻路, Graph 图算法等)

#### 2. core（核心游戏）
- **包名**: `com.shatteredpixel.shatteredpixeldungeon`
- **职责**: 所有游戏逻辑、内容、资源的主体
- **资源**: sprites, sounds, music, fonts, interfaces, effects, splashes, 本地化文件

#### 3. android / desktop（平台启动器）
- 仅包含平台特定的启动代码和配置
- Desktop 有 debug/release 两套 sourceSet，使用不同的服务实现
- Android 使用 R8/ProGuard 进行发布优化（当前 R8 已禁用，待修复）

#### 4. services（服务层）
- 接口化设计，debug 和 release 使用不同实现
- `debugUpdates` / `githubUpdates` — 版本更新检查
- `debugNews` / `shatteredNews` — 新闻信息推送

---

## 四、核心架构模式

### 4.1 场景驱动流程 (Scene-Based Flow)

游戏以 **Scene** 为单位组织屏幕/界面，由 `TomorrowRogueNight`（继承 `Game`）统一管理切换：
WelcomeScene → TitleScene → HeroSelectScene → InterlevelScene → GameScene ↓ AmuletScene (通关)
其他场景: AlchemyScene, BadgesScene, ChangesScene, RankingsScene, AboutScene, NewsScene, SupporterScene, SurfaceScene, StartScene
- **GameScene** — 核心游戏场景 (1337行)，管理地牢渲染、Actor 线程、UI 层、输入交互
- **InterlevelScene** — 层级过渡场景，处理楼层切换动画与加载
- **HeroSelectScene** — 角色选择，开始游戏时通过 `InterlevelScene.Mode.ENTER_RHODES` 进入罗德岛分支

### 4.2 Actor 回合制系统

所有游戏实体继承 `Actor`，通过优先级和时间戳决定行动顺序：
Actor (abstract) ├── Char (abstract) — 有 HP/位置/精灵/Buff 的角色 │ ├── Hero — 玩家角色 (109KB，最大的单文件) │ └── Mob — 敌人与 NPC │ ├── miniboss/ — 精英怪 (10种) │ └── npcs/ — 非玩家角色 (46种) ├── Buff — 状态效果 (111种) └── Blob — 区域效果 (25种)
**优先级体系** (高到低):
- VFX_PRIO (100) — 视觉特效
- HERO_PRIO (0) — 英雄前
- BLOB_PRIO (-10) — 区域效果
- MOB_PRIO (-20) — 怪物
- BUFF_PRIO (-30) — 状态效果
- DEFAULT (-100) — 兜底

**Actor 线程**: 独立的后台线程通过 `Actor.process()` 循环驱动回合推进，与 GameScene 通过 `wait/notify` 同步。

### 4.3 地牢层级系统 (Level System)

`Level` 是地牢楼层的抽象基类 (1539行)，管理：
- 地图数据 (`int[] map`, 各种 boolean 数组: passable, losBlocking, water, pit 等)
- 实体容器: `mobs`, `heaps`, `blobs`, `plants`, `traps`, `platforms`, `seaTerrors`
- 视野计算 (ShadowCaster)
- 楼层生成 (配合 Builder/Room/Painter 体系)

#### 地牢深度结构 (Dungeon.newLevel())

**主线 (branch=0)**:
| 深度 | 层级类型 | 说明 |
|------|----------|------|
| 1-4 | SewerLevel | 下水道 |
| 5 | SewerBossLevel | Boss1 |
| 6-9 | PrisonLevel | 监狱 |
| 10 | NewPrisonBossLevel | Boss2 |
| 11-14 | CavesLevel | 洞穴 |
| 15 | NewCavesBossLevel | Boss3 |
| 16-19 | CityLevel | 城市 |
| 20 | NewCityBossLevel | Boss4 |
| 21-24 | HallsLevel | 大厅 |
| 25 | NewHallsBossLevel | Boss5 |
| 26 | LastLevel | 最终层 |

**特殊分支 (branch=0, depth 31-40)**:
通过 `extrastage_Gavial` / `extrastage_Sea` 标志位切换：
| 深度 | Gavial线 | Sea线 | 默认(Siesta线) |
|------|----------|-------|----------------|
| 31-34 | GavialLevel | SeaLevel_part1 | SiestaLevel_part1 |
| 35 | GavialBossLevel1 | SeaBossLevel1 | SiestaBossLevel_part1 |
| 36-39 | GavialLevel2 | SeaLevel_part2 | SiestaLevel_part2 |
| 40 | GavialBossLevel2 | SeaBossLevel2 | SiestaBossLevel_part2 |

**罗德岛分支 (branch 1-4, depth=0)**:
- `isInRhodes()` = `depth == 0 && branch >= 1 && branch <= 4`
- NewRhodesLevel1~4，通过 `InterlevelScene.Mode.ENTER_RHODES` 进入

#### 程序化生成管线
Builder (布局算法) → Room (房间定义) → Painter (绘制实现) ├── RegularBuilder ├── standard/ (43种) ├── SewerPainter ├── FigureEightBuilder ├── special/ (31种) ├── PrisonPainter ├── LoopBuilder ├── secret/ (16种) ├── CavesPainter ├── LineBuilder └── sewerboss/ (7种) ├── CityPainter └── BranchesBuilder └── HallsPainter + 自定义: GavialPainter, IberiaPainter, SiestaPainter
### 4.4 物品系统 (Item System)
Item (abstract) ├── KindOfWeapon → Weapon → MeleeWeapon (84种) / MissileWeapon (24种) ├── Armor (13种) ├── Artifact (20种) ├── Potion (17种) ├── Scroll (16种) ├── Wand (19种 + SP/ 19种自定义) ├── Ring (18种) ├── Food (15种) ├── Bomb (12种) ├── Spell (23种) ├── Stone (16种) ├── Quest items (23种) ├── Skill/ — 技能书系统 │ ├── SK1/ (20种技能, 如 PowerfulStrike, ChainHook, Fate...) │ ├── SK2/ (20种技能) │ └── SK3/ (20种技能) ├── Gunaccessories/ — 枪械配件系统 │ └── Accessories (基类) + C_Mag, DotSight, GunScope, Muzzlebrake 等 ├── NewGameItem/ — 新游戏道具 (Closure 系列箱子, SpriteConvert 等) ├── testtool/ — 开发测试工具 (20种) └── 独立物品: Amulet, Generator, Recipe, DropTable 等
**Generator.java** (57KB) 是物品生成与掉率控制的核心。

### 4.5 英雄职业系统
HeroClass (enum, 7种): ├── WARRIOR → Berserker / Gladiator / HEAT ├── MAGE → Battlemage / Warlock / CHAOS ├── ROGUE → Assassin / Freerunner / WILD ├── HUNTRESS → Sniper / Warden / STOME ├── ROSECAT → Destroyer / Guardian / WAR ← 自定义: 玫猫 ├── NEARL → Knight / Savior / FLASH ← 自定义: 近卫临光 └── CHEN → Swordmaster / SPSHOOTER ← 自定义: 陈
HeroSubClass (enum, 22种): 包含原版 12 种 + 自定义 10 种
每个职业通过 `initHero()` 初始化独特装备、天赋和起始物品。

---

## 五、自定义内容详解（Mod 独有）

### 5.1 明日方舟角色 NPC
- **Jessica** — 杰西卡，有任务系统 (`QuestClear`)
- **Ceylon** — 锡兰，有任务系统
- **FrostLeaf** — 霜叶，有任务系统
- **NPC_Phantom** — 幻影，有任务系统
- **Dario** — 达里奥，有任务系统
- 其他: ACE_BATTLE, Closure, Dobermann, Gavial, Lens, Npc_Astesia, PRTS, Purestream, Zaaro 等 46 个 NPC

### 5.2 自定义 Boss / 精英怪
- **SeaBoss1/2** — 海洋 Boss 两阶段
- **GavialBossLevel1/2** — 加维尔 Boss 两阶段
- **SiestaBoss** — 午睡 Boss
- **Isharmla** 系列 — 伊莎玛拉 (海嗣) 多段体 (Head/Body/Tail)
- **Talulah** — 塔露拉 (18.7KB)
- **Talu_BlackSnake** — 黑蛇塔露拉 (17KB)
- **TheEndspeaker** — 终末演讲者 (44KB, miniboss 中最大)
- **Pompeii** — 庞贝 (20KB)
- **TheBigUglyThing** — 巨型丑物
- **NewTengu** — 新天狗 (47KB)
- **NewDM300** — DM300 (30KB)

### 5.3 自定义武器 (84种近战 + 24种投掷)
明日方舟主题武器:
- **枪械系**: GunWeapon (23KB基类), CatGun, CrabGun, DP27, Enfild, M1887, M870, Ots03, Pkp, SG_CQB, Sig553, Usg 等
- **近战系**: ChenSword, EX42, NEARL_AXE, RhodesSword, SakuraSword, PatriotSpear, RadiantSpear 等
- **特殊系**: Beowulf, Heamyo, KRISSVector, Suffering, SwordofArtorius 等

### 5.4 枪械配件系统 (Gunaccessories)
- `Accessories` 基类提供 ACC/DLY/DMG/CONE 修正值和弹药节省概率
- 可安装到 `GunWeapon` 上
- 配件: C_Mag, DotSight, GunScope, GunScope_II, Ironsight, Muzzlebrake

### 5.5 法杖扩展 (wands/SP/)
19 种角色专属法杖:
- StaffOfMayer, StaffOfAngelina, StaffOfBreeze, StaffOfCorrupting, StaffOfGreyy, StaffOfLeaf, StaffOfLena, StaffOfMudrock, StaffOfPodenco, StaffOfPurgatory, StaffOfShining, StaffOfSkyfire, StaffOfSnowsant, StaffOfSussurro, StaffOfSuzuran, StaffOfTime, StaffOfVigna, StaffOfWeedy, StaffOfAbsinthe

### 5.6 技能书系统 (Skill/)
- `Skill` 抽象基类继承 `Item`，核心方法 `doSkill()`
- `SkillBook` 为技能书载体
- 三个技能组: SK1 (20种), SK2 (20种), SK3 (20种)
- 技能举例: PowerfulStrike, ChainHook, Fate, Camouflage, HotBlade, Hikari, SoulAbsorption 等

### 5.7 词典/日志系统 (custom/dict/)
- `DictBook` — 词典书
- `DictSpriteSheet` — 词典精灵图表
- `DictionaryJournal` (27KB) — 词典日志系统
- 独立的本地化: `custom/custom.properties` (318KB), `custom_en.properties` (237KB), `custom_zh.properties` (213KB)

### 5.8 测试工具 (items/testtool/)
仅在 debug 模式可用，提供给开发者的便捷工具:
- `LevelTeleporter` — 楼层传送
- `MobPlacer` / `MobBook` — 怪物放置
- `TrapPlacer` / `TerrainPlacer` — 陷阱/地形放置
- `CustomWeapon` (40KB) — 自定义武器生成
- `Generators_*` — 各类物品生成器
- `ImmortalShield` — 无敌护盾
- `BackpackCleaner` — 背包清理
- `LazyTest` — 懒人测试
- `TimeReverser` — 时间回溯

### 5.9 自定义地牢特性
- `SeaPlatform` — 海上平台
- `SeaTerror` — 海中恐怖
- `Platform` — 通用平台
- 自定义 Painter: GavialPainter, IberiaPainter, SiestaPainter

---

## 六、关键系统详解

### 6.1 存档系统 (Bundle Serialization)
- 基于 `Bundle` 的自定义序列化系统（非标准 Java 序列化）
- 所有可保存对象实现 `Bundlable` 接口 (`storeInBundle` / `restoreFromBundle`)
- `Dungeon.saveGame()` 保存完整游戏状态:
  - 种子、版本、挑战、英雄、深度/分支
  - 自定义状态: guardquest, acequest, cautusquset, eazymode, skin_ch, isPray, doctorSaved, killcat 等
  - NPC 任务进度: Jessica/FrostLeaf/NPC_Phantom.QuestClear
  - 已生成楼层记录、限制掉落计数、章节、任务
- 支持版本迁移 (`Bundle.addAlias` 在 TomorrowRogueNight 构造函数中)

### 6.2 本地化系统
- 基于 `.properties` 文件
- 通过 `Messages.get()` 统一调用
- 目录结构: `assets/messages/{actors,items,levels,scenes,ui,windows,journal,plants,custom,misc,private}/`
- 支持语言: en_US, cs, de, el, es, fr, hu, in, it, ja, ko, nl, pl, pt, ru, tr, uk, vi, zh_CN
- 翻译平台: Transifex (team-rosemari/tomorrows-roguenight)
- 自定义内容使用独立的 `custom/` 消息目录

### 6.3 资源管理 (Assets.java)
- `Assets.java` (32KB) 集中管理所有资源路径常量
- 资源分类: sprites, environment, interfaces, music, sounds, fonts, splashes, effects, gdx

### 6.4 成就系统 (Badges.java)
- `Badges.java` (57KB) 管理游戏成就/徽章
- 跨游戏持久化存储

### 6.5 挑战系统 (Challenges.java)
- 位掩码式挑战配置
- 影响物品生成、敌人行为等

---

## 七、构建与运行

### Desktop 开发
bash ./gradlew desktop:debug # 调试运行 ./gradlew desktop:release # 发布 JAR → /desktop/build/libs
### Android 开发
bash ./gradlew android:assembleDebug # 调试 APK ./gradlew android:assembleRelease # 发布 APK (R8) ./gradlew copyAndroidNatives # 提取原生库
### 通用
bash ./gradlew clean # 清理 ./gradlew build # 全量构建
### 注意事项
- `gradle.properties` 配置: `-Xmx2048m -XX:MaxMetaspaceSize=512m`，并行构建已启用
- R8 完整模式当前已禁用 (`shrinkResources false`, `minifyEnabled false`)
- Android release 后缀: `.Trial` + `-Trial`
- Desktop debug 后缀: `-INDEV`

---

## 八、代码约定与注意事项

### 命名约定
- 类名: PascalCase，与原版 SPD 保持一致
- 自定义内容多使用明日方舟角色/概念命名
- 部分变量使用韩文注释（前任开发者痕迹）
- Bundle key 使用字符串常量，部分有韩文注释 (명픽던 추가 = "明像素地牢 追加")

### 代码风格
- 遵循原版 Shattered Pixel Dungeon 风格
- 使用 Tab 缩进
- 大量使用枚举类型 (HeroClass, HeroSubClass, LimitedDrops, Level.Feeling 等)
- 静态方法/字段广泛用于全局状态管理 (Dungeon, Actor 等)
- Lombok `@Setter` 少量使用 (Level.java)

### 关键大文件（修改时需谨慎）
| 文件 | 大小 | 说明 |
|------|------|------|
| Hero.java | 109KB | 玩家角色核心逻辑 |
| Generator.java | 57KB | 物品生成与掉率 |
| Badges.java | 57KB | 成就系统 |
| Dungeon.java | 41KB | 地牢状态管理 |
| GameScene.java | 40KB | 游戏场景 |
| Mob.java | 41KB | 怪物基类 |
| Char.java | 35KB | 角色基类 |
| Level.java | 46KB | 楼层基类 |
| CustomWeapon.java | 40KB | 测试工具-自定义武器 |
| Assets.java | 33KB | 资源路径常量 |

### 开发注意
- 不接受 Pull Request，代码仅供参考和 Modding 使用
- GPLv3 许可证：分发修改版必须开源
- 部分代码中有 `change from budding` 等注释标记前任开发者的修改
- `testtool/` 目录下的工具仅在 debug 模式生成

---

## 九、架构图 (Mermaid)
mermaid graph TD A[TomorrowRogueNight] --> B[Game Engine - Noosa/LibGDX] A --> C[Scene Manager] C --> D[TitleScene] C --> E[HeroSelectScene] C --> F[GameScene] C --> G[InterlevelScene] C --> H[Other Scenes]
F --> I[Actor System]
I --> J[Hero]
I --> K[Mob]
I --> L[Buff]
I --> M[Blob]

F --> N[Level System]
N --> O[Level Generation]
O --> P[Builder]
O --> Q[Room]
O --> R[Painter]

N --> S[Dungeon State]
S --> T[Save/Load - Bundle]

J --> U[HeroClass - 7种]
J --> V[HeroSubClass - 22种]
J --> W[Talent System]

K --> X[Normal Mobs]
K --> Y[MiniBoss - 10种]
K --> Z[NPC - 46种]

F --> AA[Item System]
AA --> AB[Weapon - 84+24种]
AA --> AC[Armor - 13种]
AA --> AD[Wand - 19+19种]
AA --> AE[Skill System - 60种]
AA --> AF[Gun Accessories]
AA --> AG[Test Tools]
---

## 十、与原版 Shattered Pixel Dungeon 的主要差异

1. **新增 3 个英雄职业**: ROSECAT(玫猫), NEARL(近卫临光), CHEN(陈)，各有独特子职业
2. **罗德岛分支**: branch 1-4 的独立区域系统
3. **3 条特殊支线**: Gavial/Sea/Siesta 各 10 层 (depth 31-40)
4. **大量明日方舟角色**: 46 个 NPC、数十个方舟主题怪物/Boss
5. **枪械系统**: GunWeapon 基类 + 枪械配件系统，原版没有的远程武器类型
6. **技能书系统**: 3 组共 60 种主动技能
7. **角色专属法杖**: 19 种方舟角色主题法杖
8. **词典/日志系统**: 独立的 DictionaryJournal 百科系统
9. **测试工具集**: 20 种开发调试工具
10. **扩展的 Boss 战**: 多阶段 Boss 设计 (SeaBoss, GavialBoss, SiestaBoss 等)
11. **自定义地牢特性**: SeaPlatform, SeaTerror 等新机制
12. **本地化扩展**: 独立的 custom 消息文件，支持中/英/默认三套

---

*文档生成时间: 2026-09-13*  
*基于代码审阅，如有遗漏或错误欢迎补充修正。*
这份文档已经涵盖了项目的完整架构认知，包括：
项目本质 — Shattered Pixel Dungeon 的明日方舟主题深度 Mod
技术栈 — Java 11 + LibGDX 1.13.5 + Gradle 多模块
模块架构 — SPD-classes(引擎) → core(逻辑) → android/desktop(平台) + services
核心模式 — Scene 场景流、Actor 回合制、Level 程序化生成、Bundle 序列化
自定义内容 — 7 英雄职业、60 种技能、枪械系统、罗德岛分支、3 条支线、大量方舟角色
构建命令与代码约定