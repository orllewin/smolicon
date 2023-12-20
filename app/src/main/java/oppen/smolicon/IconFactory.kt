package oppen.smolicon

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix

object IconFactory {

    fun create(colours: Array<String?>, onBitmap: (bitmap: Bitmap) -> Unit){
        val intColours = IntArray(256)
        repeat(256){ index ->
            val colourStr = colours[index]
            if(colourStr == null){
                intColours[index] = Color.TRANSPARENT
            }else{
                intColours[index] = Color.parseColor(colourStr)
            }
        }

        create(intColours, onBitmap)
    }

    private fun create(colors: IntArray, onBitmap: (bitmap: Bitmap) -> Unit){
        val bitmap = Bitmap.createBitmap(colors, 16, 16, Bitmap.Config.ARGB_8888)

        val rotateMatrix = Matrix()
        rotateMatrix.postRotate(90f)
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, 16, 16, rotateMatrix, false)
        bitmap.recycle()

        val flipMatrix = Matrix()
        flipMatrix.postScale(-1f, 1f, 8f, 8f)
        val flippedBitmap = Bitmap.createBitmap(rotatedBitmap, 0, 0, 16, 16, flipMatrix, false)
        rotatedBitmap.recycle()

        onBitmap(flippedBitmap)
    }
}