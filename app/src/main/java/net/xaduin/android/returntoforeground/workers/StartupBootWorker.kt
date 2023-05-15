package net.xaduin.android.returntoforeground.workers

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import net.xaduin.android.returntoforeground.R

private const val TAG: String = "StartupBootWorker"

class StartupBootWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val applicationContext = applicationContext

        // Verificar si la aplicación se está ejecutando en primer plano
        val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == applicationContext.packageName) {
                // La aplicación está ejecutándose en primer plano, no se hace nada
                return Result.success()
            }
        }

        // La aplicación no está ejecutándose en primer plano, crear una notificación para que el usuario la abra manualmente
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel_id"
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Aplicación iniciada automáticamente")
            .setContentText("Haz clic aquí para abrir la aplicación.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Lanzar la notificación
        notificationManager.notify(0, notificationBuilder.build())

        return Result.success()
    }

//    override fun doWork(): Result {
//        val applicationContext = applicationContext
//
//        // Verificar si la acción es BOOT_COMPLETED
//        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
//            // Crear una notificación para que el usuario abra manualmente la aplicación
//            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val channelId = "default_channel_id"
//            val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Aplicación iniciada automáticamente")
//                .setContentText("Haz clic aquí para abrir la aplicación.")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val channel = NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_HIGH)
//                notificationManager.createNotificationChannel(channel)
//            }
//
//            // Lanzar la notificación
//            notificationManager.notify(0, notificationBuilder.build())
//        }
//
//        return Result.success()
//    }
}