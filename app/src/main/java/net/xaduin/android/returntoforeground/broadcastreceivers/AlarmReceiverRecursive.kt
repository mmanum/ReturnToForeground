package net.xaduin.android.returntoforeground.broadcastreceivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log

private const val TAG = "AlarmReceiverRecursive"

// Define el BroadcastReceiver que se lanzará al recibir la señal de alarma
class AlarmReceiverRecursive : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "-----> onReceive222: " + SystemClock.elapsedRealtime())

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent2 = Intent(context, AlarmReceiverRecursive::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager.set( // .setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10000,
            alarmIntent2
        )


        // Crea un OneTimeWorkRequest con la tarea a ejecutar
//        val workRequest = OneTimeWorkRequestBuilder<ForegroundWorker>().build()
//
//        // Enqueue el OneTimeWorkRequest para que se ejecute
//        if(context != null)
//            WorkManager.getInstance(context).enqueue(workRequest)
    }
}