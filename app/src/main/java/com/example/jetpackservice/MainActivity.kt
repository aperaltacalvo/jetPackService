package com.example.jetpackservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.jetpackservice.RouteTrackingService.Companion.EXTRA_CONTENT

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchTrackingService()
    }

    private fun launchTrackingService(){
        RouteTrackingService.trackingCompletion.observe(this,{
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
        val serviceIntent = Intent(this, RouteTrackingService::class.java).apply {
            putExtra(EXTRA_CONTENT, "START_TRACKING_JANUARY")
        }

        //serviceIntent.putExtra(EXTRA_CONTENT, "START_TRACKING_JANUARY")
        ContextCompat.startForegroundService(this,serviceIntent)
    }
}