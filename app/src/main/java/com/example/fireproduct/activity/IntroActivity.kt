package com.example.fireproduct.activity

import android.os.Bundle
import android.os.CountDownTimer
import com.example.fireproduct.R
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (auth.currentUser != null){
                    intent(this@IntroActivity, MainActivity())
                    finish()
                } else {
                    intent(this@IntroActivity, LoginActivity())
                    finish()
                }
            }
        }.start()
    }
}