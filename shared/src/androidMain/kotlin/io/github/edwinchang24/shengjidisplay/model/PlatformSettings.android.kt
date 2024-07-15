package io.github.edwinchang24.shengjidisplay.model

actual val defaultPlatformSettings: PlatformSettings = PlatformSettings.Android()

operator fun PlatformSettings.invoke() = this as PlatformSettings.Android
