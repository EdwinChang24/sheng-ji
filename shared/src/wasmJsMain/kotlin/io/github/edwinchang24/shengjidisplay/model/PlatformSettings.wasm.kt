package io.github.edwinchang24.shengjidisplay.model

actual val defaultPlatformSettings: PlatformSettings = PlatformSettings.Web

operator fun PlatformSettings.invoke() = this as PlatformSettings.Web
