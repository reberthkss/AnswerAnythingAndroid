package com.answer.anything.model

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.answer.anything.utils.Constants.Companion.QR_CODE_CONTAINER_HEIGHT
import com.answer.anything.utils.Constants.Companion.QR_CODE_CONTAINER_WIDTH
import com.answer.anything.utils.Constants.Companion.WEB_SITE_URL
import com.answer.anything.utils.QRCode.QRCodeGeneratorImplementation
import java.lang.Exception

class QRCodeViewModel: ViewModel() {
    private val TAG = "QRCodeViewModel"
    private val qrCodeBitmap: MutableLiveData<Bitmap?> = MutableLiveData(null)

    fun getQRCodeBitmap(): LiveData<Bitmap?> {
        return qrCodeBitmap
    }

    fun setQRCodeBitmap(researchId: String): Boolean {
        try {
            val qrCode = QRCodeGeneratorImplementation(QR_CODE_CONTAINER_WIDTH, QR_CODE_CONTAINER_HEIGHT)
            val researchUrl = "${WEB_SITE_URL}/researchs/${researchId}";
            Log.d(TAG, "Research url => ${researchUrl}")
            val bitmap = qrCode.generateQRCode(researchUrl)
            qrCodeBitmap.value = bitmap
            return true
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")
            return false
        }
    }
}