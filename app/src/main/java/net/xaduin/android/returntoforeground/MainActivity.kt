package net.xaduin.android.returntoforeground

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import net.xaduin.android.returntoforeground.ui.theme.ReturnToForegroundTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import net.xaduin.android.returntoforeground.broadcastreceivers.AlarmReceiver
import net.xaduin.android.returntoforeground.broadcastreceivers.AlarmReceiverRecursive
import net.xaduin.android.returntoforeground.services.RelaunchService
import net.xaduin.android.returntoforeground.workers.CurrentAppWorker
import net.xaduin.android.returntoforeground.workers.ForegroundWorker
import java.util.concurrent.TimeUnit

private const val TAG: String = ".MainActivity"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        returnToForeground()
        generatePeriodicAlarm()
        generatePeriodicWork()

        val myCurrentAppWork = PeriodicWorkRequestBuilder<CurrentAppWorker>(3, TimeUnit.SECONDS)
            .build()
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork(
            "CurrentAppWork" ,
            ExistingPeriodicWorkPolicy.KEEP,
            myCurrentAppWork)

        setContent {
            ReturnToForegroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(applicationContext,"Android")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume-onResume-onResume-onResume-onResume-onResume-onResume-onResume")
//        android.widget.Toast.makeText(this, "Return to Foreground", android.widget.Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause-onPause-onPause-onPause-onPause-onPause-onPause-onPause-onPause")
        val myForegroundWorker = OneTimeWorkRequest.from(ForegroundWorker::class.java)
//      WorkManager.getInstance(this).enqueue(myForegroundWorker)

//        val beginUniqueWork = WorkManager.getInstance(this).beginUniqueWork(
//             "ForegroundWorker",
//             ExistingWorkPolicy.REPLACE,
//             myForegroundWorker)
//        beginUniqueWork.enqueue()


        val myCurrentAppWork = OneTimeWorkRequest.from(CurrentAppWorker::class.java)
        val beginCurrentUniqueWork = WorkManager.getInstance(this).beginUniqueWork(
            "CurrentAppWorker",
            ExistingWorkPolicy.REPLACE,
            myCurrentAppWork)
//        beginCurrentUniqueWork.enqueue()

//        android.widget.Toast.makeText(this, "Go to Background", android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Activity onDestroy-onDestroy-onDestroy-onDestroy-onDestroy-onDestroy-onDestroy")
        android.widget.Toast.makeText(this, "App killed!!!! OOhh!!", android.widget.Toast.LENGTH_LONG).show()

        val intent = Intent(this, RelaunchService::class.java)
        startService(intent)
    }

    private fun returnToForeground() {
        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(true) // Ejecutar el trabajo cuando el dispositivo esté en modo de espera
//            .setRequiresCharging(true)
            .setTriggerContentMaxDelay(10, TimeUnit.MINUTES)
            .setTriggerContentUpdateDelay(10, TimeUnit.MINUTES)
//            .setApplicationState(ApplicationState.BACKGROUND) // Ejecutar el trabajo cuando la aplicación cambie de primer plano a segundo plano
            .build()

        val foregroundWork = PeriodicWorkRequestBuilder<ForegroundWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(foregroundWork)
    }

    private fun generatePeriodicWork() {

//        WorkManager.launchFrequentTask

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<CurrentAppWorker>(20, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun generatePeriodicAlarm() {
        Log.d(TAG, "prepare alarmManager and alarmIntent")

        // Crea el PendingIntent que se disparará cuando se active el AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            Intent(applicationContext, AlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE // PendingIntent.FLAG_UPDATE_CURRENT or  PendingIntent.FLAG_MUTABLE
        )

//        val alarmManager2 = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent2 = Intent( applicationContext, AlarmReceiverRecursive::class.java).let { intent ->
            PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

//        val pendingIntent3 =
//            PendingIntent.getService(context, requestId, intent,
//                PendingIntent.FLAG_NO_CREATE)
//        if (pendingIntent3 != null && alarmManager != null) {
//            alarmManager.cancel(pendingIntent)
//        }

        // Programa la tarea para que se ejecute después de 10 segundos
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating( // .setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 0,
            1000L, // como minimo un minuto
            alarmIntent
        )

        Log.d(TAG, AlarmManager.INTERVAL_DAY.toString())
        Log.d(TAG, AlarmManager.INTERVAL_FIFTEEN_MINUTES.toString())
        Log.d(TAG, AlarmManager.INTERVAL_HOUR.toString())
        Log.d(TAG, AlarmManager.INTERVAL_HALF_HOUR.toString())

        alarmManager.set( // .setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 1000,
            alarmIntent2
        )

//        val triggerTime =  SystemClock.elapsedRealtime() + 10000L // 10 segundos
//        alarmManager.setRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + 5,
//            SystemClock.elapsedRealtime() + 10000L,
//            alarmIntent2
//        )

    }
}

@Composable
fun Greeting(context: Context, name: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
//            .border(width = 2.dp, color = androidx.compose.ui.graphics.Color.Green),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ReturnToForeground for $name in Kotlin!",
            modifier = modifier,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            val packageName = "com.netflix.ninja"
            val activityName = "com.netflix.ninja.MainActivity"
            val intent: Intent = Intent(Intent.ACTION_SEARCH)
            intent.setClassName(packageName, activityName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // intent.putExtra("query", item.label)
            // val intent = Intent().apply { setClassName(packageName, activityName) }
            //intent.setData(Uri.parse(watchUrl));
            context.startActivity(intent)
        }) {
            Text(text = "Netflix")
        }
        Spacer(modifier = Modifier.padding(8.dp))
        CloseButton()
    }
}

@Composable
fun CloseButton() {
    val currentActivity = LocalContext.current as? Activity

    Button(onClick = {
        currentActivity?.finish()
    }) {
        Text(text = "Close App")

    }
}
