package com.telegram.chart

import android.content.Context
import android.graphics.*
import android.support.v4.view.MotionEventCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class Chart : View {
    val DEBUG_TAG = "Chart gesture"

    private val paintBack = Paint().apply { color = Color.RED }
    private val window = ActiveWindow()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        //todo
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(paintBack)
        window.draw(canvas)
    }

    var isDown = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action: Int = MotionEventCompat.getActionMasked(event)
        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                isDown = true
                Log.d(DEBUG_TAG, "Action was DOWN")
                true
            }
            MotionEvent.ACTION_MOVE -> {
                moveWindow(event)
                true
            }
            MotionEvent.ACTION_UP -> {
                isDown = false
                Log.d(DEBUG_TAG, "Action was UP")
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun moveWindow(event: MotionEvent) {
        window.move(event)
        invalidate()
    }
}
