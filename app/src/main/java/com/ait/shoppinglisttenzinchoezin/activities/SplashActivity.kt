package com.ait.shoppinglisttenzinchoezin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.ait.shoppinglisttenzinchoezin.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var shoppingLogo: ImageView = findViewById(R.id.ivShoppingLogo)

        shoppingLogo.startAnimation(AnimationUtils.loadAnimation(this,
            R.anim.rotate
        ))

        Handler().postDelayed(Runnable{
            run(){
                var intent = Intent(this, ScrollingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }
}
