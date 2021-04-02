package com.vesko.homeproject.utils

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

class StoreImageUtil private constructor(){

    companion object {
        val instance : StoreImageUtil by lazy { StoreImageUtil() }
    }

    fun storeImage (bitmap:Bitmap, filesDir: File){
        try {

            val imageFile = File(filesDir, "icon.png")
            val outputStream = FileOutputStream(imageFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Throwable) {
            // Several error may come out with file handling or DOM
            e.printStackTrace()
        }

    }

}