package settings

actual val defaultPlatformSettings: PlatformSettings = PlatformSettings.Android()

operator fun PlatformSettings.invoke() = this as PlatformSettings.Android
