package com.example.jetpackservice

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.IllegalStateException

class RouteTrackingService : Service() {

    private lateinit var notificationBuilder: NotificationCompat.Builder

    private lateinit var serviceHandler: Handler



    override fun onCreate() {
        super.onCreate()

        notificationBuilder = startForegroundService()
        val handlerThread = HandlerThread("RouteTracking").apply {
            start()
        }
        serviceHandler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val info = intent?.getStringExtra(EXTRA_CONTENT) ?: throw IllegalStateException("Error de info")
        serviceHandler.post{
            tracking()
            notifyCompletion(info)
            stopForeground(true)
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun tracking() {
        //for(i in 10 downTo 10){
            Thread.sleep(1000)
            notificationBuilder.setContentText("1 segundos para llegar")
        //}
    }

    private fun notifyCompletion(info:String) {
        Handler(Looper.getMainLooper()).post {
            mutableTrackingCompletion.value = info
        }
    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private fun startForegroundService():NotificationCompat.Builder{
        val pendingIntent: PendingIntent = getPendingIntent()
        val channelId = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            createNotificationChannel()
        }else{
            ""
        }

        val notificationBuilder = getNotificationBuilder(pendingIntent, channelId)

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        return notificationBuilder
    }

    private fun getNotificationBuilder(pendingIntent: PendingIntent?, channelId: String): NotificationCompat.Builder =
        NotificationCompat.Builder(this, channelId)
            .setContentTitle("Vehiculo aproximandose")
            .setContentText("Flota: El vehiculo ha parado...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setTicker("Vehiculo en proximidad")


    private fun getPendingIntent()=
        PendingIntent.getActivity(this,0,Intent(this, MainActivity::class.java),0)


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "routeTracking"
        val channelName = "Route Tracking"
        val channel= NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val service = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    companion object{
        const val NOTIFICATION_ID = 25
        const val EXTRA_CONTENT = "data"

        private val mutableTrackingCompletion = MutableLiveData<String>()
        val trackingCompletion: LiveData<String> = mutableTrackingCompletion

    }
}