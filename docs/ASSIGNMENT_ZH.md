# 这次作业到底要做什么

你们不是要开发一套完整租房平台，而是准备一节关于 Android 本地存储的小课。
主题是 **SQLite 本地存储 + SharedPreferences**，对应 SwapCampus 的房源列表缓存和用户偏好。
已确认可以用 Room：Room 底层就是 SQLite。

完整截图标题要求 **Kotlin**；“Compose if relevant”表示按需要采用 Compose。
本项目使用 Kotlin 原生 Views，练习只聚焦存储层。
表格的两位成员是 Yinji Wang 和 Zheng LI；Firstname/Lastname 分栏，不是四个人。

## 四个交付物

| 交付物 | 内容 | 放在哪里 |
| --- | --- | --- |
| 完整 Demo | 可以真正保存房源、收藏、语言和筛选条件的 App | 完整项目 ZIP 上传 Moodle |
| 未完成项目 UP | 界面能运行，存储功能留下明确 TODO，让同学补完 | GitHub 公开仓库 |
| PDF 报告 | 说明技术、项目结构、运行方法，包含 GitHub 链接 | Moodle |
| PPT | 课堂讲解原理、演示效果、介绍同学练习 | Moodle 和 UP 的 GitHub 仓库 |

当前仓库就是第二项 **UP**，不是完成版。完整 Demo、报告和 PPT 后续单独准备。

## 两个版本到底有什么不同

两个版本的界面可以一样，区别在于数据保存是否实现。

- 完整 Demo：导入六条房源，收藏一个，改成法文，强行停止后重开，全部保持。
- UP：同样可以预览和点击，但重启会丢失；同学完成四组 TODO 后，实现 Demo 的效果。

为了让同学专注于题目，界面、Room entity、DAO、数据库和后台线程已经写好。
他们只需在 Repository 中接通 Room 的查询/插入/更新，再完成 SharedPreferences 的读取与写入。

## 建议讲解顺序

1. 用一分钟说明：为什么断网和重启后还需要本地数据。
2. 解释两种存储：Room 保存结构化房源和收藏；SharedPreferences 保存简单设置。
3. 运行完整 Demo，做一次强行停止再打开的演示。
4. 展示 Entity → DAO → Database → Repository → UI 的调用关系。
5. 发 UP 仓库地址，按 EXERCISES.md 指导同学完成 TODO。
6. 用同样的重启操作验收同学的实现。

## 截图上的时间

- Moodle 提交：2026-11-17 23:59，截图没有注明时区，以课程 Moodle 为准。
- 展示：2026-11-18 或 2026-11-25；你们具体是哪天需要看课程安排。

GitHub 仓库名还需要最终按老师的 `II.3510_2627_Skills_Topic` 格式调整。
现有公开仓库 `Wangyinji/kotlin` 先用于保存和分发 UP 源码。
