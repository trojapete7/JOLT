package jolt.pack

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


const val REQUEST_ENABLE_BT = 1
const val MODULE_MAC = "98:D3:B1:FD:71:35"
val UUID: UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
private const val SCAN_PERIOD: Long = 10000
val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
var isTimerRunning: Boolean = false
var btt: ConnectedThread? = null

const val vibrate: String = "1"

class Home : Fragment() {

    var durationInMillis  = 0L
    private fun getDuration (Long: Long):Long {
        durationInMillis = Long
        return Long
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        //Connect to Device button actions
        view.findViewById<Button>(R.id.bt_connection_butt).setOnClickListener {
            Log.i("[BLUETOOTH]", "Attempting to send data")
            onConnectClick()
        }

        //Start Monitoring button actions
        view.findViewById<Button>(R.id.start_monitor_butt).setOnClickListener {
            val minutes = view.findViewById<EditText>(R.id.editMinutes).text.toString()
            val seconds = view.findViewById<EditText>(R.id.editSeconds).text.toString()

            val numMinutes = cleanMinutes(minutes)
            val numSeconds = cleanSeconds(seconds)

            hideUi()
            errMess.visibility = View.VISIBLE
            errMess.textSize = 30F
            val beginVal: Long = ((numMinutes*60*1000)+(numSeconds*1000)).toLong()
            getDuration(beginVal)

            isTimerRunning = true
            timer(beginVal, 1000).start()

        }

        //Cancel Monitoring button actions
        view.findViewById<Button>(R.id.cancel_monitor_butt).setOnClickListener {
            errMess.visibility = View.INVISIBLE
            makeUiVisible()
        }

        return view
    }

    //var beforeTime = System.currentTimeMillis()
    //var afterTime = System.currentTimeMillis()
    var userUnlock: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        //beforeTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        //afterTime = System.currentTimeMillis() - beforeTime

        if (isTimerRunning){
            userUnlock++
            btt!!.write(vibrate.toByteArray())
        }
    }

    private fun onConnectClick (){

        if (bluetoothAdapter == null){
            errMess.text = getString(R.string.err_message_no_bt)
        }

        if (bluetoothAdapter?.isEnabled == false){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

        } else {
            errMess.textSize = 14F
            errMess.text = getString(R.string.bt_already_enabled)
            initiateBluetoothProcess()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            initiateBluetoothProcess()
        }
    }

     private fun initiateBluetoothProcess() {
        if (bluetoothAdapter != null) {
            if(bluetoothAdapter.isEnabled) {
                var tmp: BluetoothSocket?
                val mmDevice: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(MODULE_MAC)
                var mmSocket: BluetoothSocket? = null

                try {
                    tmp = mmDevice?.createRfcommSocketToServiceRecord(UUID)
                    mmSocket = tmp
                    mmSocket?.connect()
                    Log.i("[BLUETOOTH]", "Connected to " + mmDevice?.name)
                }

                catch(e:IOException) {
                    mmSocket?.close()
                }

                Log.i("[BLUETOOTH]", "Creating handler")
                val handlerThread = HandlerThread("HC-05_HandlerThread")
                val mHandler = Handler(Looper.getMainLooper())

                fun handleMessage(msg: Message){
                    if(msg.what == ConnectedThread.RESPONSE_MESSAGE){
                        val txt: String = msg.obj.toString()
                    }
                }

                Log.i("[BLUETOOTH]", "Creating and running thread")
                btt = ConnectedThread(mmSocket, mHandler)
                btt!!.start()

            }
        }
    }

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

                if (displaySeconds.toInt() < 10) {
                    errMess.text = ("$displayMinutes : 0$displaySeconds")
                }

                else {
                    errMess.text = ("$displayMinutes : $displaySeconds")
                }

            }

            override fun onFinish(){
                makeUiVisible()
                isTimerRunning = false
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
        //val timeEarned = (durationInMillis - afterTime)/1000
        val secondsEarned = (durationInMillis)/1000
        val minutesEarned = secondsEarned/60
        var totalPoints = (minutesEarned * 5) - (15 * userUnlock)

        if (totalPoints < 10){
            totalPoints = 10
        }

        points.text = "You earned $totalPoints points and unlocked: $userUnlock time(s)"
    }


}



