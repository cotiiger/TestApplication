package com.example.testapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener


class DrawableView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var path = Path()
    private val paint = Paint().apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 30f
        isAntiAlias = true
    }

    lateinit var viewModel: MainViewModel

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 線を描画する
        canvas.drawPath(path, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewModel.recordFirstTouchEvent(event.x, event.y)
                Log.d("debugLog", event.x.toString())
                Log.d("debugLog", event.y.toString())
                // タッチした位置にパスを移動
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                viewModel.recordTouchEvent(event.x, event.y)
                Log.d("debugLog", event.x.toString())
                Log.d("debugLog", event.y.toString())

                // タッチしている位置までパスを線でつなぐ
                path.lineTo(x, y)
                invalidate() // 再描画を要求
            }
            MotionEvent.ACTION_UP -> {
                viewModel.calculateCircleSize()
                // タッチが終了したときの処理（描画に関してはここでは何もしない）
            }
        }
        return true
    }
}