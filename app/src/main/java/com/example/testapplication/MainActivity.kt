package com.example.testapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val circleSearchDemoButton = findViewById<TextView>(R.id.circleSearchDemo)
        val rectangleSearchDemo = findViewById<TextView>(R.id.rectangleSearchDemo)

        circleSearchDemoButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, MainFragment()).commit()
        }
        rectangleSearchDemo.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AreaMoveFragment()).commit()
        }
    }
}