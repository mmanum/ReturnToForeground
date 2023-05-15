package net.xaduin.android.returntoforeground.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG: String = "AlarmWorker"

class AlarmWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        Log.d(TAG, "AlarmWorker-AlarmWorker-AlarmWorker-AlarmWorker")
        return Result.success()
    }
}