@file:JsModule("uqr")

package transfer

external fun encode(text: String, options: JsAny?): QrCodeGenerateResult

external interface QrCodeGenerateResult {
    val size: Int
    val data: JsArray<JsArray<JsBoolean>>
}
