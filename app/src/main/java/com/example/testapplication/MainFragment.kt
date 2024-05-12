package com.example.testapplication

import android.R.attr.path
import android.annotation.SuppressLint
import android.graphics.Path
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
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

        val binding: FragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        val centerImage = view.findViewById<ImageView>(R.id.centerImage)
        val sizingArea = view.findViewById<ImageView>(R.id.sizingArea)

        centerImage.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.recordFirstTouchEvent(event.x, event.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val previousX = viewModel.uiState.value.previousX
                        val previousY = viewModel.uiState.value.previousY
                        val midX: Float = (previousX + event.x) / 2
                        val midY: Float = (previousY + event.y) / 2
                        Path().quadTo(previousX, previousY, midX, midY)
                        viewModel.recordTouchEvent(event.x, event.y)
                    }

                    MotionEvent.ACTION_UP -> {
                        viewModel.calculateCircleSize()
                    }
                }
                return true
            }
        })

        // repeatOnLifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    // sizingAreaを囲った丸が納まるサイズにする
                    if(it.circleWidth > 0 && it.circleHeight > 0) {
                        val layoutParams = sizingArea.layoutParams
                        layoutParams.width = it.circleWidth.toInt()
                        layoutParams.height = it.circleHeight.toInt()
                        sizingArea.layoutParams = layoutParams

                        sizingArea.x += it.circleX_Min
                        sizingArea.y += it.circleY_Min
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction().remove(this@MainFragment).commit()
            }
        })
    }
}