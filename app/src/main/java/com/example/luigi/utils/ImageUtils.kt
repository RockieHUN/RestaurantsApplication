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

        suspend fun uriToByteArray(activity : Activity, uri : Uri): ByteArray?{
            //var os = ByteArrayOutputStream()
            val inputStream = activity.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()

            return byteArray
        }

        suspend fun byteArrayToBitmap(byteArray : ByteArray): Bitmap{
            return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        }


        //convert Uri to Bitmap and scale down the size
        suspend fun uriToScaledBitmap(activity: Activity, uri : Uri) : Bitmap?{

            val inputStream = activity.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()

            if (byteArray != null) {
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)

                var height = bitmap.height
                var width = bitmap.width

                //if the image is too big, resize
                if (height > 1000 || width > 1000){
                    height = (height * 0.75 ).toInt()
                    width = (width * 0.75).toInt()
                }

                return Bitmap.createScaledBitmap(bitmap, height, width,false)
            }
            return null
        }


        suspend fun bitmapToByteArray(bitmap : Bitmap) : ByteArray{
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            return stream.toByteArray()
        }

    }
}