package net.xaduin.android.returntoforeground.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private val TAG = "HomeBroadcastReceiver"

class HomeBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i(TAG, "inside onReceive() home...")
    }
}