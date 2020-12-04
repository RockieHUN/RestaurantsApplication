package com.example.luigi.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import com.example.luigi.R
import java.io.ByteArrayOutputStream

class ImageUtils {
    companion object{

        fun toByteArray(activity : Activity,uri : Uri): ByteArray?{
            var os = ByteArrayOutputStream()
            val inputStream = activity.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()

            return byteArray
        }

        fun toBitmap(byteArray : ByteArray): Bitmap{
            return   BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        }

    }
}