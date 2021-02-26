package com.answer.anything.utils.QRCode

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.annotation.Nullable
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class QRCodeGeneratorImplementation(val height: Int, val width: Int): QRCodeGenerator {
    private val TAG = "QRCodeGeneratorImplementation"
    private val codeWriter = MultiFormatWriter()

    override fun generateQRCode(text: String): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Log.d(TAG, "height => ${height} width => ${width}")
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (coordX in 0 until width) {
                for (coordY in 0 until height) {
                    bitmap.setPixel(coordX, coordY, if (bitMatrix[coordX, coordY]) Color.WHITE else Color.BLACK)
                }
            }
            return bitmap
        } catch (e: WriterException) {
            Log.e(TAG, "${e.message}")
            return null
        }
    }
}