package jolt.pack

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


const val REQUEST_ENABLE_BT = 1
const val MODULE_MAC = "98:D3:B1:FD:71:35"
private const val SCAN_PERIOD: Long = 10000
/*val bta: BluetoothAdapter
val mmSocket: BluetoothSocket
val mmDevice: BluetoothDevice
val mHandler: Handler
val btt: ConnectedThread = null*/

class Home : Fragment() {

    var durationInMillis  = 0L
    private fun setDuration (Long: Long):Long {
        durationInMillis = Long
        return Long
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<Button>(R.id.bt_connection_butt).setOnClickListener {
            Log.i("[BLUETOOTH]", "Attempting to send data")
            onConnectClick()
        }

        view.findViewById<Button>(R.id.start_monitor_butt).setOnClickListener {
            val minutes = view.findViewById<EditText>(R.id.editMinutes).text.toString()
            val seconds = view.findViewById<EditText>(R.id.editSeconds).text.toString()

            val numMinutes = cleanMinutes(minutes)
            val numSeconds = cleanSeconds(seconds)

            hideUi()
            errMess.visibility = View.VISIBLE
            val beginVal: Long = ((numMinutes*60*1000)+(numSeconds*1000)).toLong()
            setDuration(beginVal)
            timer(beginVal, 1000).start()


        }

        view.findViewById<Button>(R.id.cancel_monitor_butt).setOnClickListener {
            errMess.visibility = View.INVISIBLE
            makeUiVisible()
        }

        return view
    }

    var beforeTime = System.currentTimeMillis()
    var afterTime = System.currentTimeMillis()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        beforeTime = System.currentTimeMillis()
    }

    override fun onStart() {
        super.onStart()
        afterTime = System.currentTimeMillis() - beforeTime
    }

    // Might need onActivityResult() here to catch the result from startActivityForResult() method
    private fun onConnectClick (){
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null){
            errMess.text = getString(R.string.err_message_no_bt)
        }

        if (bluetoothAdapter?.isEnabled == false){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

        } else {
            errMess.text = getString(R.string.bt_already_enabled)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            //initiateBluetoothProcess()
        }
    }

    /*fun initiateBluetoothProcess() {
        if(bluetoothAdapter.is)
    }*/

    private fun timer(millisUntilFinished: Long, countDownInterval: Long):CountDownTimer {
        return object:CountDownTimer(millisUntilFinished, countDownInterval){

            override fun onTick(millisUntilFinished: Long){

                var difference = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                //val hoursInMilli = minutesInMilli * 60

                //val elapsedHours = difference / hoursInMilli
                //difference %= hoursInMilli

                val elapsedMinutes = difference / minutesInMilli
                difference %= minutesInMilli

                val elapsedSeconds = difference / secondsInMilli

                //val displayHours = ((elapsedHours).toInt().toString())
                val displayMinutes = ((elapsedMinutes).toInt().toString())
                val displaySeconds = ((elapsedSeconds).toInt().toString())

                errMess.text = ("$displayMinutes : $displaySeconds")
            }

            override fun onFinish(){
                makeUiVisible()
                errMess.text = ""
                pointsEarned()
            }
        }
    }

    private fun makeUiVisible() {
        editMinutes.visibility = View.VISIBLE
        editSeconds.visibility = View.VISIBLE
        separatorMinutesToSeconds.visibility = View.VISIBLE
        bt_connection_butt.visibility = View.VISIBLE
        start_monitor_butt.visibility = View.VISIBLE
        (activity as MainActivity).setNavigationVisibility(true)

        cancel_monitor_butt.visibility = View.INVISIBLE
    }

    private fun hideUi() {
        editMinutes.visibility = View.INVISIBLE
        editSeconds.visibility = View.INVISIBLE
        separatorMinutesToSeconds.visibility = View.INVISIBLE
        bt_connection_butt.visibility = View.INVISIBLE
        start_monitor_butt.visibility = View.INVISIBLE
        (activity as MainActivity).setNavigationVisibility(false)
        cancel_monitor_butt.visibility = View.VISIBLE
    }

    private fun cleanMinutes(m:String):Int {
        val checkMinutes: String

        if (m.isEmpty()){
            checkMinutes = "0"
            return checkMinutes.toInt()
        }

        val minutes: CharArray = m.toCharArray()
        val trueM: Int

        trueM = if (minutes[0] == '0'){
            minutes[1].toString().toInt()
        } else {
            minutes.joinToString(separator="").toInt()
        }
        return trueM
    }

    private fun cleanSeconds(s:String):Int {
        val checkSeconds: String

        if (s.isEmpty()){
            checkSeconds = "0"
            return checkSeconds.toInt()
        }
        val seconds: CharArray = s.toCharArray()
        val trueS: Int

        trueS = if (seconds[0] == '0') {
            seconds[1].toString().toInt()
        } else {
            seconds.joinToString(separator="").toInt()
        }
        return trueS
    }

    private fun pointsEarned(){
        val timeEarned = (durationInMillis - afterTime)/1000
        var minutesEarned = timeEarned/60
        //var totalPoints = (minutesEarned *5) - (14 * numUnlocks)
        points.text = "You earned " + timeEarned + " Points!"
    }
}



