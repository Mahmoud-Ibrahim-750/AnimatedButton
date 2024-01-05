package com.mis.example.animatedbutton.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mis.animatedbutton.AnimatedButton
import com.mis.example.animatedbutton.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val autoLoadingBtn = findViewById<AnimatedButton>(R.id.auto_loading_btn)
        val customBehaviorLoadingBtn = findViewById<AnimatedButton>(R.id.custom_loading_btn)

        // use the default button behaviour (auto loading when clicked)
        autoLoadingBtn.setOnClickListener {
            (it as AnimatedButton)

            // show done after 2 seconds of loading
            Handler(Looper.getMainLooper()).postDelayed({
                it.showSuccess()
            }, 2000)

            // return to normal state
            Handler(Looper.getMainLooper()).postDelayed({
                it.showNormal()
            }, 4000)

            // start loading again
            Handler(Looper.getMainLooper()).postDelayed({
                it.showLoading()
            }, 6000)

            // show error this time
            Handler(Looper.getMainLooper()).postDelayed({
                it.showFailure()
            }, 8000)

            // finally, return to normal state
            Handler(Looper.getMainLooper()).postDelayed({
                it.showNormal()
            }, 10000)
        }

        // override the default button behavior (show loading when another action happens)
        customBehaviorLoadingBtn.setOnClickListener {
            it as AnimatedButton

            // show loading after click
            Handler(Looper.getMainLooper()).postDelayed({
                it.showLoading()
            }, 1000)

            // show done after loading
            Handler(Looper.getMainLooper()).postDelayed({
                it.showFailure()
            }, 2500)

            // return to normal state after done
            Handler(Looper.getMainLooper()).postDelayed({
                it.showNormal()
            }, 2501)
        }
    }
}
