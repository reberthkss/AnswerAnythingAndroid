package com.answer.anything.utils.QRCode

import android.graphics.Bitmap

interface QRCodeGenerator {
    fun generateQRCode(text: String): Bitmap?
}