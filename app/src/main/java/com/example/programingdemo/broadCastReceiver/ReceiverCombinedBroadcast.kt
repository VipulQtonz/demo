package com.example.programingdemo.broadCastReceiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.widget.Toast
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.AIR_PLANE_MODE
import com.example.programingdemo.utlis.Const.BATTERY_LEVEL
import com.example.programingdemo.utlis.Const.PERCENTAGE

class ReceiverCombinedBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        when (intent.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> getAirPlaneModeStatus(intent, context)
            BluetoothAdapter.ACTION_STATE_CHANGED -> getBluetoothStatus(intent, context)
            ConnectivityManager.CONNECTIVITY_ACTION -> getNetworkStatus(context)
            Intent.ACTION_BATTERY_CHANGED -> getBatteryPercent(intent, context)
            Intent.ACTION_BATTERY_LOW -> Toast.makeText(
                context,
                R.string.battery_low,
                Toast.LENGTH_SHORT
            ).show()

            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(
                    context,
                    R.string.device_boot_completed,
                    Toast.LENGTH_SHORT
                ).show()
            }

            Intent.ACTION_DATE_CHANGED -> Toast.makeText(
                context,
                R.string.date_changed,
                Toast.LENGTH_LONG
            ).show()

            Intent.ACTION_REBOOT -> Toast.makeText(
                context,
                R.string.device_rebooting,
                Toast.LENGTH_SHORT
            ).show()

            Intent.ACTION_CALL -> Toast.makeText(
                context,
                R.string.incoming_call,
                Toast.LENGTH_SHORT
            ).show()

            Intent.ACTION_TIME_CHANGED -> Toast.makeText(
                context,
                R.string.time_changed,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getAirPlaneModeStatus(intent: Intent, context: Context?) {
        val isAirplaneModeEnabled = intent.getBooleanExtra(AIR_PLANE_MODE, false)
        if (isAirplaneModeEnabled) {
            Toast.makeText(context, R.string.airplane_mode_enabled, Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(context, R.string.airplane_mode_disabled, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getBluetoothStatus(intent: Intent, context: Context?) {
        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
        when (state) {
            BluetoothAdapter.STATE_OFF -> {
                Toast.makeText(context, R.string.bluetooth_off, Toast.LENGTH_SHORT).show()
            }

            BluetoothAdapter.STATE_ON -> {
                Toast.makeText(context, R.string.bluetooth_on, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getNetworkStatus(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val isMobileDataEnabled =
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true

        if (isMobileDataEnabled) {
            Toast.makeText(context, R.string.mobile_data_on, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, R.string.mobile_data_off, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBatteryPercent(intent: Intent, context: Context?) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPercentage = if (level != -1 && scale != -1) {
            (level / scale.toFloat() * 100).toInt()
        } else {
            -1
        }

        if (batteryPercentage != -1) {
            Toast.makeText(
                context,
                "$BATTERY_LEVEL $batteryPercentage$PERCENTAGE",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, R.string.battery_level_unknown, Toast.LENGTH_SHORT)
                .show()
        }
    }
}
