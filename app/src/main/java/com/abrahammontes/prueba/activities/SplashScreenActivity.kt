package com.abrahammontes.prueba.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewPropertyAnimator
import androidx.appcompat.app.AppCompatActivity
import com.abrahammontes.prueba.MainActivity
import com.abrahammontes.prueba.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        tvMessage.alpha = 0f
        val anim : ViewPropertyAnimator = tvMessage.animate().alpha(1f).setDuration(4000);
        anim.start()

        Handler(Looper.getMainLooper())
            .postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent)
                finish()
            }, 4000)

    }

    companion object {
        val SOLICITUD_PERMISO_ACCES_LOCATION = 1
    }
}