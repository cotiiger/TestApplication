package com.example.testapplication

import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow
import kotlin.math.sqrt

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState.init())
    val uiState = _uiState.asStateFlow()

    fun recordFirstTouchEvent(x: Float, y: Float) {
        _uiState.update {
            it.copy(
                firstTouchX = x,
                firstTouchY = y,
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
            if(x < 0 || y < 0) return
            if (x < it.circleX_Min) { _uiState.update { it.copy(circleX_Min = x) } }
            if (x > it.circleX_Max) { _uiState.update { it.copy(circleX_Max = x) } }
            if (y < it.circleY_Min) { _uiState.update { it.copy(circleY_Min = y) } }
            if (y > it.circleY_Max) { _uiState.update { it.copy(circleY_Max = y) } }
        }
    }

    fun actionUp(x: Float, y: Float) {
        val distance = calculateDistance(x, y, _uiState.value.firstTouchX.toDouble(), _uiState.value.firstTouchY.toDouble())
        if(distance < 50) {
            calculateCircleSize()
        }
    }

    private fun calculateCircleSize() {
        _uiState.update { it.copy(
            circleWidth = it.circleX_Max - it.circleX_Min,
            circleHeight = it.circleY_Max - it.circleY_Min
        ) }
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Double, y2: Double): Double {
        return sqrt( (x1 - x2).pow(2.0) + (y1 - y2).pow(2.0))
    }

    fun clipImage(bitmap: Bitmap) : Bitmap {
        return Bitmap.createBitmap(
            bitmap,
            _uiState.value.circleX_Min.toInt(),
            _uiState.value.circleY_Min.toInt(),
            _uiState.value.circleWidth.toInt(),
            _uiState.value.circleHeight.toInt()
        )
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModelが破棄されるときにリソースをクリーンアップします
    }
}

data class MainUiState (
//    val touchUiState: TouchUiState,
    val firstTouchX: Float = 0f,
    val firstTouchY: Float = 0f,
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

//data class TouchUiState (
//    val circleX_Min: Float = 0f,
//    val circleX_Max: Float = 0f,
//    val circleY_Min: Float = 0f,
//    val circleY_Max: Float = 0f
//)