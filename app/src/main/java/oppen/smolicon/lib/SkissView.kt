package oppen.smolicon.lib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View

@SuppressLint("ClickableViewAccessibility")
class SkissView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): View(context, attrs, defStyleAttr), Choreographer.FrameCallback {

    companion object{
        var w: Int = -1
        var h: Int = -1
    }

    private var onSetup: (width: Int, height: Int) -> Unit? = {_,_->}
    private var onTouch: (x: Int, y: Int) -> Unit? = {_,_->}
    private var onMove: (motionEvent: MotionEvent) -> Unit? = {_->}
    private var onDraw: (canvas: Canvas?) -> Unit? = {_->}

    private var initialised = false
    private var hardwareAccCheckRequired = true

    init {
        setOnTouchListener{ _, motionEvent ->
            when (motionEvent!!.action and MotionEvent.ACTION_MASK){
                MotionEvent.ACTION_DOWN -> {
                    onTouch.invoke(motionEvent.x.toInt(), motionEvent.y.toInt())
                }
                MotionEvent.ACTION_MOVE -> {
                    onMove(motionEvent)
                }
            }
            true
        }
    }

    fun start(){
        onSetup.invoke(w, h)
        initialised = true
        Choreographer.getInstance().postFrameCallback(this)
    }
    fun stop() = Choreographer.getInstance().removeFrameCallback(this)
    fun pause() = stop()
    fun unpause() = Choreographer.getInstance().postFrameCallback(this)

    fun setup(onSetup: (width: Int, height: Int) -> Unit?){
        this.onSetup = onSetup
    }

    fun update(onDraw: (canvas: Canvas?) -> Unit?){
        this.onDraw = onDraw
    }

    fun onTouch(onTouch: (x: Int, y: Int) -> Unit){
        this.onTouch = onTouch
    }

    fun onMove(onMove: (motionEvent: MotionEvent) -> Unit){
        this.onMove = onMove
    }

    override fun doFrame(frameTimeNanos: Long) {
        invalidate()
        Choreographer.getInstance().postFrameCallback(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Companion.w = w
        Companion.h = h
        onSetup.invoke(w, h)
        initialised = true
    }

    override fun onDraw(canvas: Canvas?) {
        if(hardwareAccCheckRequired) hardwareAccelerationCheck(canvas)
        if(initialised) onDraw.invoke(canvas)
    }

    private fun hardwareAccelerationCheck(canvas: Canvas?){
        hardwareAccCheckRequired = false
        if(canvas == null) return

        if(canvas.isHardwareAccelerated){
            l("View is using hardware acceleration")
        }else{
            e("View is NOT using hardware acceleration")
            e("See: https://developer.android.com/guide/topics/graphics/hardware-accel.html")
        }
    }
}