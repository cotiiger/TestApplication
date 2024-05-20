package com.example.testapplication

import android.R.attr.path
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.testapplication.databinding.FragmentMainBinding
import kotlinx.coroutines.launch
import kotlin.io.path.Path


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel = MainViewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // val binding: FragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        val centerImage = view.findViewById<ImageView>(R.id.centerImage)
        val sizingArea = view.findViewById<ImageView>(R.id.sizingArea)
        val clippedArea = view.findViewById<ImageView>(R.id.clippedArea)
        val drawableView = view.findViewById<DrawableView>(R.id.drawableView)

        // 各種設定
        drawableView.viewModel = viewModel
        sizingArea.isVisible = false

        // repeatOnLifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    // sizingAreaを囲った丸が納まるサイズにする
                    if(it.circleWidth > 0 && it.circleHeight > 0) {
                        // 中心画像の切り抜き
                        val centerImageSrcBitmap = centerImage.drawable.toBitmap() // srcから取りたい時はこちら
                        // val centerImageSrcBitmap = centerImage.background.toBitmap() // backgroundから取りたい時はこちら

                        // 元画像を表示していたViewの大きさになおす
                        val resizedBitmap = Bitmap.createScaledBitmap(
                            centerImageSrcBitmap,
                            centerImage.measuredWidth,
                            centerImage.measuredHeight,
                            true
                        )

                        // 囲った範囲に切り抜く
                        val clippedBitmap = Bitmap.createBitmap(
                            resizedBitmap,
                            it.circleX_Min.toInt(),
                            it.circleY_Min.toInt(),
                            (it.circleWidth).toInt(),
                            (it.circleHeight).toInt()
                        )

                        sizingArea.isVisible = true

                        // 掲出範囲のViewをリサイズ
                        resizeView(sizingArea, it.circleWidth.toInt(), it.circleHeight.toInt())
                        resizeView(clippedArea, it.circleWidth.toInt(), it.circleHeight.toInt())

                        // 囲った場所の上に乗せる
                        sizingArea.x = it.circleX_Min
                        sizingArea.y = it.circleY_Min

                        // 切り抜かれた画像をセット
                        clippedArea.setImageBitmap(clippedBitmap)

                        // 残りのタスク
                        // 指が画像の外にでた場合にクラッシュする不具合の修正
                        // 努力目標：なぞる始点と終点の近さに制限を設ける
                        // 努力目標：制限から外れたら軌跡を消すようにする
                    }
                }
            }
        }

        // バックキーを押した時の処理
        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction().remove(this@MainFragment).commit()
            }
        })
    }

    fun resizeView(view: View, width: Int, height: Int) {
        val layoutParams = view.layoutParams
        layoutParams.width = width
        layoutParams.height = height
        view.layoutParams = layoutParams
    }
}