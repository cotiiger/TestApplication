package com.example.testapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel = MainViewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // val binding: FragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        val mainImage = view.findViewById<ImageView>(R.id.centerImage) // 切り抜きたい画像
        val sizingArea = view.findViewById<ImageView>(R.id.sizingArea) // 囲った後に上に表示される範囲
        val clippedArea = view.findViewById<ImageView>(R.id.clippedArea) // 囲った範囲に切り抜いた画像
        val drawableView = view.findViewById<DrawableView>(R.id.drawableView) // 指の軌跡の表示と計測をするView

        // 各種設定
        drawableView.viewModel = viewModel

        // repeatOnLifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    // sizingAreaを囲った丸が納まるサイズにする
                    if(it.circleWidth > 0 && it.circleHeight > 0) {
                        // 中心画像の画像データを取得
                        val centerImageSrcBitmap = mainImage.drawable.toBitmap() // srcから取りたい時はこちら
                        // val centerImageSrcBitmap = centerImage.background.toBitmap() // backgroundから取りたい時はこちら

                        // 画像データを表示していたViewの大きさに変更する
                        val resizedBitmap = Bitmap.createScaledBitmap(
                            centerImageSrcBitmap,
                            mainImage.measuredWidth,
                            mainImage.measuredHeight,
                            true
                        )

                        // 囲った範囲に切り抜く
                        val clippedBitmap = viewModel.clipImage(resizedBitmap)

                        // 掲出範囲のViewをリサイズ
                        resizeView(sizingArea, it.circleWidth.toInt(), it.circleHeight.toInt())
                        resizeView(clippedArea, it.circleWidth.toInt(), it.circleHeight.toInt())

                        // 囲った場所の上に乗せる
                        sizingArea.x = it.circleX_Min + mainImage.x
                        sizingArea.y = it.circleY_Min + mainImage.y

                        // 切り抜かれた画像をセット
                        clippedArea.setImageBitmap(clippedBitmap)
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