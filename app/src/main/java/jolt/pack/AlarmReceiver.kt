package jolt.pack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val getResult : String? = intent!!.getStringExtra("extra")

        val serviceIntent: Intent = Intent(context,RingtoneService::class.java)
        serviceIntent.putExtra("extra",getResult)
        context!!.startService(serviceIntent)

    }
}