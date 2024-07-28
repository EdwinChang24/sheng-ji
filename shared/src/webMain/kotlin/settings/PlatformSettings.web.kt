package settings

actual val defaultPlatformSettings: PlatformSettings = PlatformSettings.Web()

operator fun PlatformSettings.invoke() = this as PlatformSettings.Web
