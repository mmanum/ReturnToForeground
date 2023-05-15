package net.xaduin.android.returntoforeground.workers

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import net.xaduin.android.returntoforeground.MainActivity

private const val TAG: String = "CurrentAppWorker"

class CurrentAppWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Crea un intent para lanzar la actividad principal de la aplicación
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        // Lanza la actividad en primer plano
//        mySleep(10000)

        Log.d(TAG, "-----> The App is .... " + getForegroundAppPackageName())
        Log.d(TAG, "-----> The App is 2 .... " + getForegroundAppPackageName2())
//        applicationContext.startActivity(intent)
        return Result.success()
    }

    private fun mySleep(millis: Long) {
        try {
            Thread.sleep(millis, 0)
        } catch (e: InterruptedException) {
//            Timber.e(e.message)
        }
    }

    private fun getForegroundAppPackageName(): String? {
        val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val runningProcesses = activityManager?.runningAppProcesses

        runningProcesses?.forEach { processInfo ->
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                processInfo.pkgList.forEach { packageName ->
                    return packageName
                }
            }
        }
        return null
    }

    private fun getForegroundAppPackageName2(): String? {
        val am = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = am.runningAppProcesses
        if (!processes.isNullOrEmpty()) {
            for (process in processes) {
                if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    val packageName = process.processName
                    val topActivity = process.pkgList?.getOrNull(0) // asumimos que el primer elemento en pkgList es la actividad en primer plano
                    // Utiliza packageName y topActivity para determinar si tu aplicación está en primer plano o si el usuario está en la pantalla de inicio
                    Log.d(TAG, packageName + ":::::::::" + topActivity)
                    break
                }
            }
        }
        return "vacio";
    }

    private fun getForegroundAppPackageNameDeprecated(): String? {
        val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val taskList = activityManager?.getRunningTasks(1)
        return taskList?.get(0)?.topActivity?.packageName
    }
}