package oppen.smolicon.view

import android.content.res.Resources
import android.graphics.Canvas
import android.view.MotionEvent
import oppen.smolicon.lib.Skiss
import oppen.smolicon.lib.SkissView
import oppen.smolicon.lib.color
import kotlin.math.floor

class ScreenMatrix(view: SkissView, val onDropper: (colour: String?) -> Unit): Skiss(view) {

    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val background = color("#efefef")
    private val gridColour = color("#ffffff")
    private var cellDiam = 10.0

    var inkColour: String? = null
    val pixels = arrayOfNulls<String>(256)

    private var drawGrid = false
    private var eraseMode = false
    private var dropper = false
    private var fill = false

    override fun setup(width: Int, height: Int) {
        cellDiam = screenWidth/16.0
        skissView.layoutParams.height = screenWidth
    }

    override fun update(canvas: Canvas) {
        fill(background)
        noStroke()
        square(0, 0, width)

        repeat(16){ yIndex ->
            repeat(16){ xIndex ->
                when (val colour = pixels[(yIndex * 16) + xIndex]) {
                    null -> { //NOOP
                    }
                    else -> {
                            fill(color(colour))
                            noStroke()
                            square(yIndex * cellDiam, xIndex * cellDiam, cellDiam)
                    }
                }
            }
        }

        if(drawGrid){
            stroke(gridColour)
            noFill()
            line(0, 0, width, height)
            line(width, 0, 0, height)
            repeat(17) { index ->
                line(0, index * cellDiam, width, index * cellDiam)
                line(index * cellDiam, 0, index * cellDiam, height)
            }
        }
    }

    private fun pixelColour(x: Double, y: Double): String?{
        return pixels[pixelIndex(x, y)]
    }

    private fun pixelIndex(x: Double, y: Double): Int{
        val xx = (x - (x % cellDiam)) / cellDiam
        val yy = (y - (y % cellDiam)) / cellDiam
        return floor((xx * 16.0) + yy).toInt()
    }

    private fun fill(x: Double, y: Double){
        val targetColour = pixelColour(x, y)
        floodFill(x, y, targetColour)
    }

    private fun floodFill(x: Double, y: Double, targetColour: String?){
        if(x < 0 || x > width) return
        if(y < 0 || y > height) return
        if(pixelColour(x, y) == inkColour) return

        val cD = cellDiam.toInt()

        if(pixelColour(x, y) == targetColour){
            pixels[pixelIndex(x, y)] = inkColour
            floodFill(x, y - cD, targetColour)//north
            floodFill(x + cD, y, targetColour)//east
            floodFill(x, y + cD, targetColour)//south
            floodFill(x - cD, y, targetColour)//west
        }
    }

    override fun onTouch(x: Int, y: Int) {
        super.onTouch(x, y)

        val xx = (x - (x % cellDiam)) / cellDiam
        val yy = (y - (y % cellDiam)) / cellDiam
        val index = floor((xx * 16.0) + yy).toInt()
        if(index < 256 && inkColour != null) {
            when {
                fill -> fill(x.toDouble(), y.toDouble())
                dropper -> {
                    onDropper(pixels[index])
                    dropper = false
                }
                eraseMode -> pixels[index] = null
                else -> pixels[index] = inkColour
            }
        }
    }

    override fun onMove(event: MotionEvent) {
        repeat(event.historySize){ h ->
            repeat(event.pointerCount){ p ->
                onTouch(event.getHistoricalX(p, h).toInt(), event.getHistoricalY(p, h).toInt())
            }
        }
    }

    fun setColour(colour: String?) {
        if(colour == null) return
        inkColour = colour
        eraseMode = false
    }

    fun toggleGrid(){
        drawGrid = !drawGrid
    }

    //todo - swap all these with a `DrawState` sealed class - we can only be in any one state at a time
    fun toggleEraser(){
        eraseMode = !eraseMode
        if(eraseMode){
            dropper = false
            fill = false
        }
    }

    fun toggleDropper(){
        dropper = true
        eraseMode = false
        fill = false
    }

    fun toggleFill(){
        fill = !fill
        if(fill){
            dropper = false
            eraseMode = false
        }

    }

    fun eraseAll(){
        repeat(256) { index ->
            pixels[index] = null
        }
    }
}