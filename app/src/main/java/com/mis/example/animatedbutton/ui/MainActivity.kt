package com.mis.example.animatedbutton.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mis.animatedbutton.AnimatedButton
import com.mis.animatedbutton.ButtonAnimation.LoadingToDone
import com.mis.animatedbutton.ButtonAnimation.NormalToLoading
import com.mis.animatedbutton.ButtonAnimation.DoneToNormal
import com.mis.animatedbutton.ButtonAnimation.ErrorToNormal
import com.mis.animatedbutton.ButtonAnimation.LoadingToError
import com.mis.example.animatedbutton.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginBtn = findViewById<AnimatedButton>(R.id.login_btn)
        val customBehaviorLoginBtn = findViewById<AnimatedButton>(R.id.custom_login_btn)

        // use the default button behaviour (auto loading when clicked)
        loginBtn.setOnClickListener {
            // show done after 2 seconds of loading
            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.showAnimation(LoadingToDone)
            }, 2000)

            // return to normal state
            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.showAnimation(DoneToNormal)
            }, 4000)

            // start loading again
            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.showAnimation(NormalToLoading)
            }, 6000)

            // show error this time
            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.showAnimation(LoadingToError)
            }, 8000)

            // finally, return to normal state
            Handler(Looper.getMainLooper()).postDelayed({
                loginBtn.showAnimation(ErrorToNormal)
            }, 10000)
        }

        // override the default button behavior (show loading when another action happens)
        customBehaviorLoginBtn
            .setAutoLoading(false) // disable auto-loading behavior
            .setOnClickListener {
                // show loading 2 seconds after click
                Handler(Looper.getMainLooper()).postDelayed({
                    customBehaviorLoginBtn.showAnimation(NormalToLoading)
                }, 2000)

                // show done after 2 seconds of loading
                Handler(Looper.getMainLooper()).postDelayed({
                    customBehaviorLoginBtn.showAnimation(LoadingToDone)
                }, 4000)

                // return to normal state after 2 seconds of done
                Handler(Looper.getMainLooper()).postDelayed({
                    customBehaviorLoginBtn.showAnimation(DoneToNormal)
                }, 6000)
            }
    }
}
