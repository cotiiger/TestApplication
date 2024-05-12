package com.example.testapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState.init())
    val uiState = _uiState.asStateFlow()

    fun recordFirstTouchEvent(x: Float, y: Float) {
        _uiState.update {
            it.copy(
                previousX = x,
                previousY = y,
                circleX_Min = x,
                circleX_Max = x,
                circleY_Min = y,
                circleY_Max = y,
                circleWidth = 0f,
                circleHeight = 0f
            )
        }
    }

    fun recordTouchEvent(x: Float, y: Float) {
        _uiState.value.let {
            _uiState.update {
                it.copy(
                    previousX = x,
                    previousY = y
                )
            }
            if (x > 0 && x < it.circleX_Min) { _uiState.update { it.copy(circleX_Min = x) } }
            if (x > 0 && x > it.circleX_Max) { _uiState.update { it.copy(circleX_Max = x) } }
            if (y > 0 && y < it.circleY_Min) { _uiState.update { it.copy(circleY_Min = y) } }
            if (y > 0 && y > it.circleY_Max) { _uiState.update { it.copy(circleY_Max = y) } }
        }
    }

    fun calculateCircleSize() {
        _uiState.update { it.copy(
            circleWidth = it.circleX_Max - it.circleX_Min,
            circleHeight = it.circleY_Max - it.circleY_Min
        ) }
        Log.d("debugLog:1", "x+:${_uiState.value.circleX_Max} x-:${_uiState.value.circleX_Min}")
        Log.d("debugLog:2", "y+:${_uiState.value.circleY_Max} y-:${_uiState.value.circleY_Min}")
        Log.d("debugLog:3", "width:${_uiState.value.circleWidth} height:${_uiState.value.circleHeight}")
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModelが破棄されるときにリソースをクリーンアップします
    }
}

data class MainUiState (
//    val touchUiState: TouchUiState
    val previousX: Float = 0f,
    val previousY: Float = 0f,
    val circleX_Min: Float,
    val circleX_Max: Float,
    val circleY_Min: Float,
    val circleY_Max: Float,
    val circleWidth: Float = 0f,
    val circleHeight: Float = 0f
) {
    companion object {
        fun init () = MainUiState(
            circleX_Min = 0f,
            circleX_Max = 0f,
            circleY_Min = 0f,
            circleY_Max = 0f
        )
    }
}

data class TouchUiState (
    val circleX_Min: Float = 0f,
    val circleX_Max: Float = 0f,
    val circleY_Min: Float = 0f,
    val circleY_Max: Float = 0f
)