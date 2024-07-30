package settings

actual val defaultPlatformSettings: PlatformSettings = PlatformSettings.Desktop()

operator fun PlatformSettings.invoke() = this as PlatformSettings.Desktop
