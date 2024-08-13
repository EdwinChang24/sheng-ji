package transfer

import kotlinx.browser.window
import org.w3c.dom.url.URL

actual fun openUrlWeb(url: String) {
    window.location.href = url
}

actual fun removeUrlParamWeb() {
    val url =
        window.location.origin +
            window.location.pathname +
            '?' +
            URL(window.location.href).searchParams.apply { delete("d") }.toString()
    window.history.replaceState(window.history.state, "", url.removeSuffix("?"))
}
