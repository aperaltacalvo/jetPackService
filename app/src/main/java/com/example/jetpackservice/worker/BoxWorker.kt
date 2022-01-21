package com.example.jetpackservice.worker

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class BoxWorker(context: Context , params: WorkerParameters):Worker(context, params) {
    override fun doWork(): Result {
        val info = inputData.getString(INPUT_DATA)
        Thread.sleep(3000L)
        val outData = Data.Builder().putString(OUTPUT_DATA, info + "_OUTPUT")
            .build()

        return Result.success(outData)
    }

    companion object{
        const val INPUT_DATA = "in_data"
        const val OUTPUT_DATA = "out_data"

    }
}