package com.example.programingdemo.broadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.AIR_PLANE_MODE
import com.example.programingdemo.utlis.Const.RECEIVER_STATIC_BROADCAST

class ReceiverStaticBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra(AIR_PLANE_MODE, false)

            if (isAirplaneModeOn) {
                Toast.makeText(context, R.string.airplane_mode_enabled, Toast.LENGTH_SHORT).show()
                Log.d(RECEIVER_STATIC_BROADCAST, context.getString(R.string.airplane_mode_enabled))
            } else {
                Toast.makeText(context, R.string.airplane_mode_disabled, Toast.LENGTH_SHORT).show()
                Log.d(RECEIVER_STATIC_BROADCAST, context.getString(R.string.airplane_mode_disabled))
            }
        }
    }
}
