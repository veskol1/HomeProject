package com.vesko.homeproject.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View


class ScreenshotUtil private constructor(){

    companion object {
        val instance: ScreenshotUtil by lazy { ScreenshotUtil() }
    }

    fun takeScreenshotForView(view: View, listViewToHide: ArrayList<View>): Bitmap {
        //removes unwanted views
        for (v in listViewToHide){
            v.visibility = View.INVISIBLE
        }

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        for (v in listViewToHide){
            v.visibility = View.VISIBLE
        }

        return bitmap
    }
}