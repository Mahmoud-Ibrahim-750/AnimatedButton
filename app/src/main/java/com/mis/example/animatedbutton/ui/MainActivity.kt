package com.mis.example.animatedbutton.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mis.animatedbutton.AnimatedButton
import com.mis.animatedbutton.ButtonAnimation.LoadingToDone
import com.mis.animatedbutton.ButtonAnimation.NormalToLoading
import com.mis.animatedbutton.ButtonAnimation.DoneToNormal
import com.mis.animatedbutton.ButtonAnimation.ErrorToNormal
import com.mis.animatedbutton.ButtonAnimation.LoadingToError
import com.mis.example.animatedbutton.R
import kotlin.time.Duration
import kotlin.time.measureTime

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
                it.showAnimation(LoadingToDone)
            }, 2000)

            // return to normal state
            Handler(Looper.getMainLooper()).postDelayed({
                it.showAnimation(DoneToNormal)
            }, 4000)

            // start loading again
            Handler(Looper.getMainLooper()).postDelayed({
                it.showAnimation(NormalToLoading)
            }, 6000)

            // show error this time
            Handler(Looper.getMainLooper()).postDelayed({
                it.showAnimation(LoadingToError)
            }, 8000)

            // finally, return to normal state
            Handler(Looper.getMainLooper()).postDelayed({
                it.showAnimation(ErrorToNormal)
            }, 10000)
        }

        // override the default button behavior (show loading when another action happens)
        customBehaviorLoadingBtn.setOnClickListener {
            it as AnimatedButton

            Snackbar.make(it, "Button Clicked", Snackbar.LENGTH_SHORT).show()

            // show loading 2 seconds after click
            Handler(Looper.getMainLooper()).postDelayed({
                it.showAnimation(NormalToLoading)
            }, 0)

            // show done after 2 seconds of loading
            Handler(Looper.getMainLooper()).postDelayed({
                it.showAnimation(LoadingToDone)
            }, 100)

            // return to normal state after 2 seconds of done
            Handler(Looper.getMainLooper()).postDelayed({
                it.showAnimation(DoneToNormal)
            }, 200)
        }
    }
}
