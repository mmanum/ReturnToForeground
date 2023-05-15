package net.xaduin.android.returntoforeground.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log


private const val TAG = "AlarmReceiver"

// Define el BroadcastReceiver que se lanzará al recibir la señal de alarma
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "-----> onReceive: " + SystemClock.elapsedRealtime())
        // Crea un OneTimeWorkRequest con la tarea a ejecutar
//        val workRequest = OneTimeWorkRequestBuilder<ForegroundWorker>().build()
//
//        // Enqueue el OneTimeWorkRequest para que se ejecute
//        if(context != null)
//            WorkManager.getInstance(context).enqueue(workRequest)
    }
}