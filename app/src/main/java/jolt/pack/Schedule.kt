package jolt.pack

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_schedule.*

class Schedule : Fragment() {

    lateinit var am: AlarmManager
    lateinit var tp: TimePicker
    lateinit var update_text: TextView
    lateinit var btnStart: Button
    lateinit var btnStop: Button
    var hour: Int = 0
    var min: Int = 0
    lateinit var pi : PendingIntent

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_schedule, container, false)

        am = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        tp = rootView.findViewById(R.id.tp)
        update_text = rootView.findViewById(R.id.update_text) as TextView
        btnStart = rootView.findViewById(R.id.start_alarm) as Button
        btnStop = rootView.findViewById(R.id.stop_alarm) as Button

        val myIntent : Intent = Intent(activity, AlarmReceiver::class.java)
        val calendar =  android.icu.util.Calendar.getInstance()

        btnStart.setOnClickListener {
            calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, tp.hour)
            calendar.set(android.icu.util.Calendar.MINUTE, tp.minute)
            calendar.set(android.icu.util.Calendar.SECOND, 0)
            calendar.set(android.icu.util.Calendar.MILLISECOND, 0)
            hour = tp.hour
            min = tp.minute

            var hrStr : String = hour.toString()
            var minStr : String = min.toString()
            if (hour > 12){
                hrStr = (hour - 12).toString()
            }
            if (min < 10 ) {
                minStr = "0$min"
            }
            setAlarmText("Alarm set to: $hrStr : $minStr")
            myIntent.putExtra("extra", "on")
            pi = PendingIntent.getBroadcast(activity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,pi)
        }

        btnStop.setOnClickListener {
            setAlarmText("Alarm off")
            pi = PendingIntent.getBroadcast(activity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            am.cancel(pi)
            myIntent.putExtra("extra","off")
            activity?.sendBroadcast(myIntent)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_AddSchedule.setOnClickListener {
            findNavController().navigate(R.id.addSchedule)
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun setAlarmText(s: String) {
        update_text.text = s
    }

}



