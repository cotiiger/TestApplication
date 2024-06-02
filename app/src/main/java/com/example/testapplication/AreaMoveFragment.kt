package com.example.testapplication

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class AreaMoveFragment : Fragment(R.layout.fragment_area_move) {

    private val viewModel: AreaMoveViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // バックキーを押した時の処理
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction().remove(this@AreaMoveFragment).commit()
            }
        })
    }
}