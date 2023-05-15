package net.xaduin.android.returntoforeground.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import net.xaduin.android.returntoforeground.workers.StartupBootWorker

private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED
            && context != null ) {
            val myWorkRequest = OneTimeWorkRequestBuilder<StartupBootWorker>().build()
            WorkManager.getInstance(context).enqueue(myWorkRequest)
        }

        Log.d(TAG, "MyBroadcastReceiver-onReceiver")
    }
}