package com.example.testapplication

import android.animation.ValueAnimator
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
import androidx.core.content.ContextCompat

/**
 * 指でタップした軌跡を描画するViewです。
 */
class DrawableView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    lateinit var viewModel: MainViewModel

    private val path = Path()
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.trajectory_color)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    private var circleX = 0f
    private var circleY = 0f
    private var circleRadius = 0f
    private var shouldDrawCircle = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 線を描画する
        canvas.drawPath(path, paint)

        if(shouldDrawCircle) {
            canvas.drawCircle(circleX, circleY, circleRadius, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewModel.recordFirstTouchEvent(event.x, event.y)
                // タッチした位置にパスを移動
                path.moveTo(x, y)

                circleX = x
                circleY = y
                shouldDrawCircle = true
                startRadiusAnimation()
            }
            MotionEvent.ACTION_MOVE -> {
                val shouldXPointInView = event.x > 0 && event.x < this.width
                val shouldYPointInView = event.y > 0 && event.y < this.height
                if(shouldXPointInView && shouldYPointInView) {
                    viewModel.recordTouchEvent(event.x, event.y)
                }
                // タッチしている位置までパスを線でつなぐ
                path.lineTo(x, y)
                invalidate() // 再描画を要求
            }
            MotionEvent.ACTION_UP -> {
                viewModel.actionUp(event.x, event.y)
                // タッチ終了
                path.reset()
                invalidate() // 再描画を要求して画面をクリア

                shouldDrawCircle = false
            }
        }
        return true
    }

    private fun startRadiusAnimation() {
        // ValueAnimatorを使用して半径をアニメーション
        val animator = ValueAnimator.ofFloat(0f, 50f) // 半径を0から300にアニメーション
        animator.duration = 300 // アニメーションの持続時間（ミリ秒）
        animator.addUpdateListener { animation ->
            circleRadius = animation.animatedValue as Float
            invalidate() // 再描画を要求してアニメーションを反映
        }
        animator.start()
    }
}