package com.example.jetpackservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.jetpackservice.RouteTrackingService.Companion.EXTRA_CONTENT
import com.example.jetpackservice.worker.BoxWorker
import com.example.jetpackservice.worker.BoxWorker.Companion.INPUT_DATA

class MainActivity : AppCompatActivity() {
    private val workManager = WorkManager.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //launchTrackingService()
        val networkConstraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val boxWorkerRequest = OneTimeWorkRequest.Builder(BoxWorker::class.java)
            .setConstraints(networkConstraints).setInputData(getData(INPUT_DATA, resources.getString(R.string.app_data))).build()
        workManager.getWorkInfoByIdLiveData(boxWorkerRequest.id).observe(this,{info ->
            if(info.state.isFinished){
                Toast.makeText(this, "Resultado del servicio", Toast.LENGTH_LONG).show()
                //launchTrackingService()
            }
        })

        workManager.beginWith(boxWorkerRequest).enqueue()
    }

    private fun getData(key:String, value:String)= Data.Builder().putString(key, value).build()

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