# BBK 开发者工具 (DevSettingsOpener)

一个帮助步步高 (EEBBK) 学习平板开启开发者选项的小工具。

## 背景

步步高学习平板的系统设置经过深度定制，隐藏或劫持了开发者选项的常规入口：

- 设置中的"关于平板"被替换为定制页面
- 连续点击"版本号"7 次会被劫持，打开工厂测试 App 而非启用开发者选项
- Shell / ContentResolver 等方式因权限限制无法写入 `development_settings_enabled`

**核心发现：** 步步高只是隐藏了入口，没有删除 Android 原生的 Settings Activity。通过 Intent 直接调用 `Settings$MyDeviceInfoActivity` 可以绕过劫持，正常启用开发者选项。

## 适用设备

- **步步高 (EEBBK)** 学习平板，已验证型号：
  - EEBBK T1
- 理论上适用于大部分使用 `com.eebbk.*` 系统包的步步高学习平板
- 基于高通 (Qualcomm) 平台

## 使用方法

### 第一步：启用开发者选项

1. 安装 APK 到平板上（U 盘、微信传文件等方式）
2. 打开 App，点击 **「打开 设备信息（点版本号7次）」**
3. 在打开的原生设备信息页面，找到「版本号」，**连续点击 7 次**
4. 提示"您已处于开发者模式"即为成功

### 第二步：开启 USB 调试

1. 点击 **「打开 开发者选项」**
2. 找到 **USB 调试**，开启
3. USB 连接电脑，平板弹出确认框，点击 **允许**
4. 电脑终端执行 `adb devices` 确认连接

## 其他功能

| 按钮 | 说明 |
|------|------|
| 系统设置 | 打开 Android 原生系统设置 |
| USB 详情 | 打开 USB 配置页面 |
| 调试工具箱 | 打开 bbklogger 的 DebugToolbox（如果设备存在） |
| BackDoor | 打开 magicmouse 的后门 Activity（如果设备存在） |

## 构建

```bash
# 需要 Android SDK (compileSdk 34) 和 JDK 8+
cd DevSettingsOpener
./gradlew assembleRelease
```

APK 输出路径：`app/build/outputs/apk/release/app-release.apk`

> 注意：构建 release 版本需要签名密钥，请自行生成或修改 `app/build.gradle` 中的 `signingConfigs`。

## 原理

步步高学习平板对系统做了以下定制：

1. 替换了"设置"App 的入口界面，隐藏了"关于平板"等页面
2. 劫持了"连续点击版本号"的行为，跳转到 `com.eebbk.stresstest`（工厂测试）
3. 但 **没有移除** Android 原生的 `com.android.settings.Settings$MyDeviceInfoActivity`

本工具通过 Intent 直接启动这个原生 Activity，绕过了步步高的定制界面限制：

```java
Intent intent = new Intent();
intent.setComponent(new ComponentName(
    "com.android.settings",
    "com.android.settings.Settings$MyDeviceInfoActivity"));
startActivity(intent);
```

## 失败的尝试（供参考）

在找到上述方法之前，以下方法均因权限不足而失败：

| 方法 | 失败原因 |
|------|----------|
| `settings put global development_settings_enabled 1` | 需要 `INTERACT_ACROSS_USERS` |
| `Settings.Global.putInt()` | 需要 `WRITE_SECURE_SETTINGS` |
| `content insert --uri content://settings/global` | 权限不足 |
| `setprop sys.usb.config mtp,adb` | 普通 app 无权修改系统属性 |
| `svc usb setFunctions mtp,adb` | 需要 system 权限 |

## 相关 BBK 系统包

| 包名 | 说明 |
|------|------|
| `com.eebbk.selftest` | 工厂自检工具（114 个硬件测试 Activity） |
| `com.eebbk.stresstest` | 压力测试，劫持了版本号点击行为 |
| `com.eebbk.bbklogger` | 日志工具，含 DebugToolboxActivity |
| `com.eebbk.magicmouse` | 含 BackDoorActivity 和 SettingsActivity |
| `com.eebbk.ovumserver` | BBK 系统服务 |
| `com.eebbk.filetransfer` | 文件传输工具 |
| `com.xtc.talentwatchpad` | 主学习应用 |

## License

MIT
