package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private var background = 0

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        cLayout.background = getDrawable(intent.extras?.get("background") as Int)
        isAutoNext.isChecked = intent.extras?.get("isAutoNext") == true
        isVisualize.isChecked = intent.extras?.get("isVisualize") == true

        done_button.setOnClickListener {
            val intent = Intent()
            intent.putExtra("background", background)
            intent.putExtra("isAutoNext", isAutoNext.isChecked)
            intent.putExtra("isVisualize", isVisualize.isChecked)
            setResult(RESULT_OK, intent)
            finish()
        }

        blue_button.setOnClickListener(this)
        emerald_button.setOnClickListener(this)
        purpleBlue_button.setOnClickListener(this)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(v: View?) {
        when (v) {
            blue_button -> background = R.drawable.gradient_background
            emerald_button -> background = R.drawable.emerald_background
            purpleBlue_button -> background = R.drawable.purple_blue_background
        }
        cLayout.background = getDrawable(background)
    }
}