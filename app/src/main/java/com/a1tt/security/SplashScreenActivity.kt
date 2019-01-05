package com.a1tt.security

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.a1tt.security.MainApplication.Companion.countDownTimer
import com.a1tt.security.MainApplication.Companion.timerCounter
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.a1tt.security.shedulers.AppListSheduler


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
    }

    override fun onPostResume() {
        super.onPostResume()

        Thread(AppListSheduler(this as Context, null)).start()

        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        countDownTimer = object : CountDownTimer(timerCounter, 10) {
            override fun onTick(millisUntilFinished: Long) {
                timerCounter = millisUntilFinished
            }

            override fun onFinish() {
                onTimer()
            }
        }.start()

        val image = findViewById<View>(R.id.animImage) as ImageView
        val animation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.fadeout)
        image.startAnimation(animation)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
        finish()
    }

    override fun onPause() {
        super.onPause()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }

    fun onTimer() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}