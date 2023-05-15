package net.xaduin.android.returntoforeground.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import net.xaduin.android.returntoforeground.MainActivity

private const val TAG: String = "MyRelaunchService"

class RelaunchService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Thread.sleep(10000)
        startForeground(FOREGROUND_SERVICE_ID, Notification())

        Log.d(TAG, "onStartCommand")
        // Reiniciar la actividad principal cuando se reinicie la aplicación
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        applicationContext.startActivity(mainActivityIntent)

        return START_STICKY
    }

    companion object {
        private const val FOREGROUND_SERVICE_ID = 101
    }
}