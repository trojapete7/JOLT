package jolt.pack

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_home.*


const val REQUEST_ENABLE_BT = 1
const val MODULE_MAC = "98:D3:B1:FD:71:35"
private const val SCAN_PERIOD: Long = 10000

class Home : Fragment() {

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<Button>(R.id.bt_connection_butt).setOnClickListener {
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
            timer(beginVal, 1000).start()

        }

        view.findViewById<Button>(R.id.cancel_monitor_butt).setOnClickListener {
            errMess.visibility = View.INVISIBLE
            makeUiVisible()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    // Might need onActivityResult() here to catch the result from startActivityForResult() method
    private fun onConnectClick (){
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null){
            errMess.text = getString(R.string.err_message_no_bt)
        }

        if (bluetoothAdapter?.isEnabled == false){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

            //ADD BT CONNECTIVITY STUFF HERE

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

                errMess.text = ("$displayMinutes : $displaySeconds")
            }

            override fun onFinish(){
                makeUiVisible()
                errMess.text = ""
            }
        }
    }

    private fun makeUiVisible() {
        editMinutes.visibility = View.VISIBLE
        editSeconds.visibility = View.VISIBLE
        separatorMinutesToSeconds.visibility = View.VISIBLE
        bt_connection_butt.visibility = View.VISIBLE
        start_monitor_butt.visibility = View.VISIBLE
        (activity as MainActivity).SetNavigationVisibilty(true)

        cancel_monitor_butt.visibility = View.INVISIBLE
    }

    private fun hideUi() {
        editMinutes.visibility = View.INVISIBLE
        editSeconds.visibility = View.INVISIBLE
        separatorMinutesToSeconds.visibility = View.INVISIBLE
        bt_connection_butt.visibility = View.INVISIBLE
        start_monitor_butt.visibility = View.INVISIBLE
        (activity as MainActivity).SetNavigationVisibilty(false)
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

}



