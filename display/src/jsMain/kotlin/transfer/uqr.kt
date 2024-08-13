@file:JsModule("uqr")
@file:JsNonModule

package transfer

external fun encode(text: String, options: dynamic): QrCodeGenerateResult

external interface QrCodeGenerateResult {
    val size: Int
    val data: Array<Array<Boolean>>
}
