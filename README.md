# 飞行基站（Fabric 1.21.11）

一个为 Minecraft 1.21.11 基于 Fabric 的模组，提供“飞行基站”方块与相关能力：当基站用红石激活时，附近玩家获得飞行能力；断电则移除。包含自定义物品组、方块实体逻辑、合成配方与命令等。

## 功能概述
- 方块：`飞行基站`（带方块实体，接受红石信号）
- 能力：基站用红石激活时为半径范围内玩家授予飞行；断电取消
- 掉落与挖掘：推荐工具为镐，最低铁镐；约 2 秒破坏，破坏后掉落自身
- 自定义物品组：在创造模式物品栏的模组页签中展示
- 合成：提供飞行基站的合成配方（JSON 数据包）
- 多语言与配置：中文/英文名称，基础配置项（范围等）

## 兼容性与依赖
- Minecraft：1.21.11
- Java：21（推荐）
- Fabric Loader：0.18.x（与本工程 Gradle/loom 对应）
- Fabric API：0.141.x+1.21.11（工程已集成）

## 安装与使用
1. 从项目根目录构建模组：
   ```bash
   # Windows
   .\gradlew.bat build
   # macOS/Linux
   ./gradlew build
   ```
2. 在输出目录中获取 JAR：`build/libs/flybase-1.0.0.jar`
3. 将 JAR 放入目标客户端的 `mods/` 文件夹，确保客户端 Fabric Loader 与 Fabric API 版本匹配（1.21.11 系列）。
4. 启动游戏，在创造模式物品组或使用命令获取并放置方块。
5. 使用红石为基站通电，附近玩家将获得飞行能力；断电则恢复普通状态。

## 模组指令
- 基本命令前缀：`/flybase`
- `info`: 显示当前配置。
  - 示例：`/flybase info`
- `space <x> [y] [z]`: 设置飞行空间范围（单位：方块）。
  - 仅 `x` 参数时，设置为立方体（x×x×x）。示例：`/flybase space 32`
  - 三维参数时，分别设置长宽高。示例：`/flybase space 24 16 24`
- `slow_falling_time <seconds>`: 设置取消飞行后的缓降效果时长（秒）。
  - 示例：`/flybase slow_falling_time 5`

说明：指令会持久化到配置，重启服务器后仍生效。服务器可通过权限插件或配置限制谁可执行上述指令。

## 开发与运行（可选）
- 生成并运行客户端（开发环境）：
  ```bash
  # Windows
  .\gradlew.bat runClient
  # macOS/Linux
  ./gradlew runClient
  ```
- 资源热重载：游戏内按 `F3+T` 重新加载资源（模型/纹理/语言）。

## 方块与挖掘细节
- 方块设置参考原版石头，硬度/爆破抗性合理；`requiresTool()` 限制破坏掉落需正确工具。
- 推荐工具与等级通过标签声明：
  - `data/minecraft/tags/block/mineable/pickaxe.json`
  - `data/minecraft/tags/block/needs_iron_tool.json`
- 掉落通过战利品表与兜底逻辑保证：
  - 战利品表：`data/flybase/loot_tables/blocks/flying_base.json`
  - 破坏逻辑兜底：`FlyingBaseBlock.afterBreak()` 保证至少掉落自身方块物品。

## 资源与模型
- 资源包路径：`src/main/resources`
  - 方块状态：`assets/flybase/blockstates/flying_base.json`
  - 模型（方块/物品）：`assets/flybase/models/block/`、`assets/flybase/models/item/`
  - 纹理（方块/物品）：`assets/flybase/textures/block/`、`assets/flybase/textures/item/`
  - 合成/数据：`data/flybase/recipes/`、`data/flybase/loot_tables/`

## 已知问题
- 物品图标在物品栏与掉落物外观为紫黑占位符：
  - 现状：方块在世界中渲染正常；物品模型待进一步修复。
  - 临时建议：可通过 F3+T 重载资源、清理缓存并确认 `assets/flybase/models/item/flying_base.json` 使用 `minecraft:item/generated`，`layer0` 指向 `flybase:item/flying_base`，同时确保纹理 PNG 为 16x16 RGBA 并打包进 JAR。

## 排障建议
- 若破坏不掉落：确认 `minecraft` 命名空间下标签文件存在（见上文），并在服务端日志查看数据包加载是否报错。
- 若资源缺失：确认 `pack.mcmeta` 的 `pack_format` 为 44（1.21），并用 F3+T 重载或重启客户端。
- 清理缓存：删除项目临时目录（如 `.gradle/`）后重建；或在 IDE 中刷新 Gradle。

## 许可证
- 项目根目录包含 `LICENSE` 文件；遵循其中条款。

## 致谢
- Fabric 文档与社区示例（方块、方块实体、数据包与标签）。

---
如需继续修复物品图标或扩展玩法（范围、能耗、GUI 等），欢迎提 Issue 或继续交流。
