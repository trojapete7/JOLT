package jolt.pack

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Schedule : Fragment() {

    lateinit var am: AlarmManager
    lateinit var tp: TimePicker
    lateinit var update_text: TextView
    lateinit var btnStart: Button
    lateinit var btnStop: Button
    lateinit var addSchedule: FloatingActionButton
    lateinit var timeTv : TextView
    private var mNotified = false
    lateinit var switch : Switch

    var Mon = false
    var Tue = false
    var Wed = false
    var Thu = false
    var Fri = false
    var Sat = false
    var Sun = false

    @RequiresApi(Build.VERSION_CODES.N)
    private val mNotificationTime = android.icu.util.Calendar.getInstance().timeInMillis + 1000

    var hour: Int = 0
    var min: Int = 0
    var dayNum = 0
    var day = ""
    var dispDay: Int = 0
    lateinit var pi : PendingIntent

    var buttonMon : Int = 1
    var buttonTue : Int = 1
    var buttonWed : Int = 1
    var buttonThu : Int = 1
    var buttonFri : Int = 1
    var buttonSat : Int = 1
    var buttonSun : Int = 1


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_schedule, container, false)
        val rootView2 = inflater.inflate(R.layout.schedule_dialog, container, false)


        am = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        tp = rootView2.findViewById<TimePicker>(R.id.tp)
        update_text = rootView2.findViewById(R.id.update_text) as TextView
        btnStart = rootView2.findViewById(R.id.start_alarm) as Button
        btnStop = rootView2.findViewById(R.id.stop_alarm) as Button
        addSchedule = rootView.findViewById<FloatingActionButton>(R.id.fab1)
        timeTv = rootView.findViewById(R.id.timeTv) as TextView
        switch = rootView.findViewById(R.id.switch1) as Switch

        var buttonMonColor = rootView2.findViewById(R.id.mondayBtn) as Button
        var buttonTueColor = rootView2.findViewById(R.id.tuesdayBtn) as Button
        var buttonWedColor = rootView2.findViewById(R.id.wednesdayBtn) as Button
        var buttonThuColor = rootView2.findViewById(R.id.thursdayBtn) as Button
        var buttonFriColor = rootView2.findViewById(R.id.fridayBtn) as Button
        var buttonSatColor = rootView2.findViewById(R.id.saturdayBtn) as Button
        var buttonSunColor = rootView2.findViewById(R.id.sundayBtn) as Button


        var myIntent : Intent = Intent(activity, AlarmReceiver::class.java)
        var calendar =  android.icu.util.Calendar.getInstance()

        switch.isClickable=false
        switch.visibility=View.INVISIBLE
        timeTv.visibility=View.INVISIBLE

        addSchedule.setOnClickListener{

            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.schedule_dialog, null)
            btnStart = mDialogView.findViewById(R.id.start_alarm) as Button
            btnStop = mDialogView.findViewById(R.id.stop_alarm) as Button

            buttonMonColor = mDialogView.findViewById(R.id.mondayBtn) as Button
            buttonTueColor = mDialogView.findViewById(R.id.tuesdayBtn) as Button
            buttonWedColor = mDialogView.findViewById(R.id.wednesdayBtn) as Button
            buttonThuColor = mDialogView.findViewById(R.id.thursdayBtn) as Button
            buttonFriColor = mDialogView.findViewById(R.id.fridayBtn) as Button
            buttonSatColor = mDialogView.findViewById(R.id.saturdayBtn) as Button
            buttonSunColor = mDialogView.findViewById(R.id.sundayBtn) as Button

            tp = mDialogView.findViewById<TimePicker>(R.id.tp)
            am = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)
                .setTitle("Set Schedule")
            val mAlertDialog = mBuilder.show()


            buttonMonColor.setOnClickListener(object : View.OnClickListener{

                override fun onClick(v: View?) {

                    if (buttonMon==1) {
                        Toast.makeText(activity, "Button was pressed", Toast.LENGTH_SHORT).show()
                        buttonMonColor.setBackgroundResource(R.color.main_color)
                        buttonMon = 2
                        dayNum = 2
                        Mon = true
                    }
                    else if (buttonMon==2){
                        buttonMonColor.setBackgroundColor(0x00000000)
                        buttonMon = 1
                        dayNum = 0
                        Mon = false
                    }
                }
            })

            buttonTueColor.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (buttonTue==1) {
                        Toast.makeText(activity, "Button was pressed", Toast.LENGTH_SHORT).show()
                        buttonTueColor.setBackgroundResource(R.color.main_color)
                        buttonTue = 2
                        dayNum = 3
                        dispDay = 2
                        Tue = true
                    }
                    else if (buttonTue==2){
                        buttonTueColor.setBackgroundColor(0x00000000)
                        buttonTue = 1
                        dayNum = 0
                        Tue = false
                    }
                }
            })

            buttonWedColor.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (buttonWed==1) {
                        Toast.makeText(activity, "Button was pressed", Toast.LENGTH_SHORT).show()
                        buttonWedColor.setBackgroundResource(R.color.main_color)
                        buttonWed = 2
                        dayNum = 4
                        Wed = true
                    }
                    else if (buttonWed==2){
                        buttonWedColor.setBackgroundColor(0x00000000)
                        buttonWed = 1
                        dayNum = 0
                        Wed = false
                    }
                }
            })

            buttonThuColor.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (buttonThu==1) {
                        Toast.makeText(activity, "Button was pressed", Toast.LENGTH_SHORT).show()
                        buttonThuColor.setBackgroundResource(R.color.main_color)
                        buttonThu = 2
                        dayNum = 5
                        Thu = true
                    }
                    else if (buttonThu==2){
                        buttonThuColor.setBackgroundColor(0x00000000)
                        buttonThu = 1
                        dayNum = 0
                        Thu = false
                    }
                }
            })

            buttonFriColor.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (buttonFri==1) {
                        Toast.makeText(activity, "Button was pressed", Toast.LENGTH_SHORT).show()
                        buttonFriColor.setBackgroundResource(R.color.main_color)
                        buttonFri = 2
                        dayNum = 6
                        Fri = true
                    }
                    else if (buttonFri==2){
                        buttonFriColor.setBackgroundColor(0x00000000)
                        buttonFri = 1
                        dayNum = 0
                        Fri = false
                    }
                }
            })

            buttonSatColor.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (buttonSat==1) {
                        Toast.makeText(activity, "Button was pressed", Toast.LENGTH_SHORT).show()
                        buttonSatColor.setBackgroundResource(R.color.main_color)
                        buttonSat = 2
                        dayNum = 7
                        Sat = true
                    }
                    else if (buttonSat==2){
                        buttonSatColor.setBackgroundColor(0x00000000)
                        buttonSat = 1
                        dayNum = 0
                        Sat = false
                    }
                }
            })

            buttonSunColor.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (buttonSun==1) {
                        Toast.makeText(activity, "Button was pressed", Toast.LENGTH_SHORT).show()
                        buttonSunColor.setBackgroundResource(R.color.main_color)
                        buttonSun = 2
                        dayNum = 1
                        Sun = true
                    }
                    else if (buttonSun==2){
                        buttonSunColor.setBackgroundColor(0x00000000)
                        buttonSun = 1
                        dayNum = 0
                        Sun = false
                    }
                }
            })

            btnStart.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {


                    when(Mon){
                        true -> day = "M"
                    }
                    when(Tue){
                        true-> day = "T"
                    }
                    when(Wed){
                        true -> day = "W"
                    }
                    when(Thu){
                        true -> day = "Th"
                    }
                    when(Fri){
                        true -> day = "F"
                    }
                    when(Sat){
                        true -> day = "Sa"
                    }
                    when(Sun){
                        true-> day = "Su"
                    }
                    when(Mon && Tue){
                        true -> day = "M T"
                    }
                    when(Mon && Wed){
                        true -> day = "M W"
                    }
                    when(Mon && Thu){
                        true -> day = "M Th"
                    }
                    when(Mon && Fri){
                        true -> day = "M F"
                    }
                    when(Mon && Sat){
                        true -> day = "M Sa"
                    }
                    when(Mon && Sun){
                        true -> day = "M Su"
                    }
                    when(Mon && Tue && Wed){
                        true -> day = "M T W"
                    }
                    when(Mon && Tue && Thu){
                        true -> day = "M T Th"
                    }
                    when(Mon && Tue && Fri){
                        true -> day = "M T F"
                    }
                    when(Mon && Tue && Sat){
                        true -> day = "M T Sa"
                    }
                    when(Mon && Tue && Sun){
                        true -> day = "M T Su"
                    }
                    when(Mon && Wed && Thu){
                        true -> day = "M W Th"
                    }
                    when(Mon && Wed && Fri) {
                        true -> day = "M W F"
                    }
                    when(Mon && Wed && Sat){
                        true -> day = "M W Sa"
                    }
                    when (Mon && Wed && Sun){
                        true -> day = "M W Su"
                    }
                    when(Mon && Thu && Fri){
                        true -> day = "M Th F"
                    }
                    when(Mon && Thu && Sat){
                        true -> day = "M Th Sa"
                    }
                    when(Mon && Thu && Sun){
                        true -> day = "M Th Su"
                    }
                    when(Mon && Fri && Sat){
                        true -> day = "M F Sa"
                    }
                    when(Mon && Fri && Sun){
                        true -> day = "M F Su"
                    }
                    when(Mon && Sat && Sun){
                        true -> day = "M Sa Su"
                    }
                    when(Mon && Tue && Wed && Thu){
                        true -> day = "M T W Th"
                    }
                    when(Mon && Tue && Wed && Fri){
                        true -> day = "M T W F"
                    }


                    if (Build.VERSION.SDK_INT   >= Build.VERSION_CODES.M) {
                        calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, tp.hour)
                        calendar.set(android.icu.util.Calendar.MINUTE, tp.minute)
                        calendar.set(android.icu.util.Calendar.SECOND, 0)
                        calendar.set(android.icu.util.Calendar.MILLISECOND, 0)
                        calendar.set(android.icu.util.Calendar.DAY_OF_WEEK, dayNum)
                        hour = tp.hour
                        min = tp.minute


                    }
                    else {
                        calendar.set(android.icu.util.Calendar.HOUR_OF_DAY,tp.currentHour)
                        calendar.set(android.icu.util.Calendar.MINUTE,tp.currentMinute)
                        calendar.set(android.icu.util.Calendar.SECOND,0)
                        calendar.set(android.icu.util.Calendar.MILLISECOND,0)
                        calendar.set(android.icu.util.Calendar.DAY_OF_WEEK,dayNum)
                        hour = tp.currentHour
                        min = tp.currentMinute
                    }
                    var hr_str : String = hour.toString()
                    var min_str : String = min.toString()
                    if (hour > 12){
                        hr_str = (hour - 12).toString()
                    }
                    if (min < 10 ) {
                        min_str = "0$min"
                    }
                    set_alarm_text("Alarm set to: $hr_str : $min_str on $day")
                    myIntent.putExtra("extra", "on")
                    pi = PendingIntent.getBroadcast(activity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                    am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,pi)

                    if (!mNotified){
                        NotificationUtils().setNotification(calendar.timeInMillis, activity!!)
                    }

                    val viewAdapter = MyAdapter(arrayOf("Alarm Set to: $hr_str : $min_str on $day"))

                    rootView.findViewById<RecyclerView>(R.id.Schedule_list).run {
                        setHasFixedSize(true)
                        adapter = viewAdapter
                    }

                    mAlertDialog.dismiss()

                }
            })
            btnStop.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v:View?) {
                    mAlertDialog.dismiss()
                    set_alarm_text("Alarm off")
                    pi = PendingIntent.getBroadcast(activity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                    am.cancel(pi)
                    myIntent.putExtra("extra","off")
                    getActivity()?.sendBroadcast(myIntent)
                }
            })



        }

        return rootView
    }


    private fun set_alarm_text(s: String) {
        update_text.setText(s)
        timeTv.setText(s)
    }

}

class MyAdapter(private val myDataset : Array<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>(){
    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_view, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item.findViewById<TextView>(R.id.listViewer).text = myDataset[position]

        holder.item.setOnClickListener{
            val bundle = bundleOf("Time" to myDataset[position])

        }
    }

    override fun getItemCount() = myDataset.size

}