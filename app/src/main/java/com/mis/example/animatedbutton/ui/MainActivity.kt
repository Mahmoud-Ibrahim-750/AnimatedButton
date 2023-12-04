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

        val loginBtn = findViewById<AnimatedButton>(R.id.login_btn)

        loginBtn.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.animateLoadingToDoneState()
            }, 2000)

            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.animateDoneToNormalState()
            }, 4000)

            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.animateNormalToLoadingState()
            }, 6000)

            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.animateLoadingToErrorState()
            }, 8000)

            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.animateErrorToNormalState()
            }, 10000)
        }
    }
}
