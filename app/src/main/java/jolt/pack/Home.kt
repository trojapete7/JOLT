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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

//Request code for intent to start BT service
const val REQUEST_ENABLE_BT = 1

//MAC Address for HC-05 BT Module
const val MODULE_MAC = "98:D3:B1:FD:71:35"

//Serial Port Protocol UUID
val UUID: UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

//Period to scan for available BT devices
private const val SCAN_PERIOD: Long = 10000

//Initializing bluetooth adapter object with available BT adapter in device
//Can be null
val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

//Flag for seeing if the timer or monitoring is currently running or not
var isTimerRunning: Boolean = false

//Initializing connected thread object for the BT connection
var btt: ConnectedThread? = null

//Flag for how many times user interrupts monitoring
var userUnlock: Int = 0

//String to be sent to Arduino board to control LED "vibrations"
const val vibrate: String = "1"

//Variable to keep track of user's point total
var currentPoints: Int = 500

var totalPoints: Int = 0

var sessionDate: LocalDateTime? = null
val dt: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd'-'HH:mm")

/**CLASS FOR THE HOME/MONITORING FRAGMENT. THE MAIN SCREEN OF THE JOLT APPLICATION.**/
class Home : Fragment() {

    var durationInMillis  = 0L
    //Method to get duration user has set for monitoring mode
    private fun getDuration (Long: Long):Long {
        durationInMillis = Long
        return Long
    }

    /**
    *  Sets listeners for all button objects within the fragment_home.xml layout
    *  Calls corresponding functions and performs various other related actions
    *  such as setting UI visibility, setting text size,  setting timer flag, etc.
    **/
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

            sessionDate = LocalDateTime.now()

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

        return view
    }

    /**
     *  Captures when the user unlocks their phone screen while monitoring mode is active
     *  Only increments X for user interruption during monitoring mode.
     *  Sends the corresponding message through the BT connection to interact with the LED "vibration"
     *  in order to alert the user that they need to refocus.
     **/
    override fun onResume() {
        super.onResume()

        if (isTimerRunning){
            userUnlock++
            btt!!.write(vibrate.toByteArray())
        }
    }

    /**
     *  Function that is called whenever the "Connect to Device" button is clicked. If bluetoothAdapter
     *  is null then the error message text view is updated to warn the user their device is
     *  incompatible. Then checks if the bluetoothAdapter is enabled. If it is not enabled an intent
     *  is created to ask the user to turn on the BT adapter. Otherwise, the adapter must already be
     *  turned on so initiateBluetoothProcess is called.
     **/
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

    /**
     *  This function is called after startActivityForResult() function is done. If the BT intent
     *  to turn on the BT adapter succeeds then initiateBluetoothProcess() is called.
     **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            initiateBluetoothProcess()
        }
    }

    /**
     *  Function that sets up the Serial Port for BT connection data transfer. SPP used on an
     *  RFcomm Socket to Service Record. This is the more secure version of the RFcomm implementation.
     *  Not vulnerable to Man-In-The-Middle attacks.
     **/
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

                Log.i("[BLUETOOTH]", "Creating and running thread")
                btt = ConnectedThread(mmSocket, mHandler)
                btt!!.start()

            }
        }
    }

    /**
     *  Function to setup CountDownTimer object. Requires onTick() and onFinish() implementation.
     *  onTick() runs every second and calculates what to display to the user. onFinish() runs once
     *  the millisUntilFinished is equal to 0.
     **/
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
                errMess.visibility = View.INVISIBLE

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
        alertMess.visibility = View.INVISIBLE
        (activity as MainActivity).setNavigationVisibility(true)
    }

    private fun hideUi() {
        editMinutes.visibility = View.INVISIBLE
        editSeconds.visibility = View.INVISIBLE
        separatorMinutesToSeconds.visibility = View.INVISIBLE
        bt_connection_butt.visibility = View.INVISIBLE
        start_monitor_butt.visibility = View.INVISIBLE
        alertMess.visibility = View.VISIBLE
        (activity as MainActivity).setNavigationVisibility(false)
    }

    /**
     *  cleanMinutes() and cleanSeconds() takes care of input validation. If user enters nothing
     *  into the timer box and starts the timer will not crash the application. Will simply set the
     *  timer to 0 milliseconds causing it to call onFinish() from the CountDownTimer object immediately.
     **/
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

    /**
     *  Calculates the total points earned per session. Is called whenever the user finishes a
     *  monitoring session. Also sets a text view to display the number of points earned and the
     *  number of times the user interrupted the monitoring.
     **/
    private fun pointsEarned(){
        //val timeEarned = (durationInMillis - afterTime)/1000
        val secondsEarned = ((durationInMillis)/1000).toInt()
        val minutesEarned = secondsEarned/60
        totalPoints = (minutesEarned * 10) - (15 * userUnlock)

        if (totalPoints < 10){
            totalPoints = 10
        }

        points.text = "You earned $totalPoints points and unlocked: $userUnlock time(s)"
        currentPoints += totalPoints
    }

}