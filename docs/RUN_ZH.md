# 中文打开、运行和演示说明

## 这个项目是什么

这是 **UP（Unfinished Project，给同学完成的练习）**，不是完整 Demo。
项目使用 **Kotlin + Android 原生界面 + Room + SharedPreferences**。
Room 底层使用 SQLite。符合课程 Kotlin 要求；本题重点是存储，因此界面使用原生 Views，未使用可选的 Compose。

界面已完成：房源列表、收藏、预算筛选、英文/法文切换。
UP 目前只在内存里保留状态，重启后丢失，学生需要补上数据持久化。
首次打开显示空列表，点击 Load samples 可以预览 6 条房源。

## Android Studio 打开

1. 启动 Android Studio，在欢迎页点击 **Open**。
2. 选择 `C:\Users\Administrator\IdeaProjects\kotlin`，不要只选 app 文件夹。
3. 等待 Gradle Sync；首次同步需要联网下载依赖。
4. 如果提示缺少 SDK，在 **Tools → SDK Manager** 安装 Android API 37
   和 Build-Tools 36.0.0。看不到 API 37 时先更新 Android Studio/SDK 工具。
5. 在设置的 **Build, Execution, Deployment → Build Tools → Gradle**
   中使用项目的 Gradle Wrapper，Gradle JDK 选择 JDK 17。
6. **Tools → Device Manager** 启动模拟器；Android 6.0 / API 23 以上即可。
   也可以接上开启 USB 调试的 Android 手机。
7. 顶部选择 **app** 和设备，点击绿色三角 **Run**。
8. 安装后的 App 名称为 **LocalNest UP**。

如果没有 app 配置，先确认 Sync 成功，再用 **Run → Edit Configurations →
+ → Android App**，Module 选 `app`。

## 课堂如何演示 UP

1. 说明：Room 保存房源和收藏，SharedPreferences 保存语言与筛选条件。
2. 点击 Load samples，展示排序后的 6 条房源。
3. 点击 Save this place 收藏；进入 Settings 演示 Favorites only。
4. 取消收藏筛选，打开预算筛选，只显示月租不超过 €800 的 4 条房源。
5. 切换法文，让同学看到设置改变界面。
6. 强行停止 App，再打开：UP 会丢失状态，这正是要完成的部分。
7. 打开 EXERCISES.md，按 UP-1 到 UP-4 分配练习。

完成练习后，重复第 6 步，应该直接恢复房源、收藏、语言和筛选，不用重新导入。
不要用“清除数据”或卸载重装测试持久化，这两种操作本来就会删除本地数据。
也不要仅用退出最近任务代替强行停止，操作系统不一定终止进程。

## 学生修改的位置

在 Android Studio 左边切到 **Project** 视图，打开：

`app/src/main/java/edu/isep/localnest/data/`

- `ListingRepository.kt`：UP-1 查询、UP-2 缓存、UP-3 收藏持久化。
- `UserPreferences.kt`：UP-4 读取和保存设置。

在 **Edit → Find → Find in Files** 搜索 `TODO UP-`，可以找到所有练习位置。
DAO、数据库、界面和后台线程已经提供，不需要先学会编写 UI。

## 编译与 APK

在 Android Studio 的 Terminal（PowerShell）运行：

```powershell
.\gradlew.bat assembleDebug
```

APK 在 `app/build/outputs/apk/debug/app-debug.apk`。
设备测试需要先启动模拟器，再运行：

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

两个持久化验收测试在 UP 中标记为 Ignore，完成 TODO 后删除 Ignore 再验证。

本机原有 `JAVA_HOME` 指向不存在的 `C:\Program Files\Java\jdk-24`。
Android Studio 中选择有效的 Gradle JDK 即可；如果使用外部 PowerShell，
可以先在当前窗口执行下列命令，再运行 gradlew（不修改系统环境变量）：

```powershell
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot'
```

## 提交提醒

现有远端是 `Wangyinji/kotlin`。最终课程仓库名需要按老师给的
`II.3510_2627_Skills_Topic` 格式调整；交付时已确认该仓库是公开仓库。
本次先交付 UP；完整 Demo、报告和 PPT 是后续单独交付物。
