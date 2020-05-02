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
        //val rootView: View = inflater.inflate(R.layout.fragment_home, container, false)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<Button>(R.id.bt_connection_butt).setOnClickListener {
            onConnectClick()
        }

        /*val hoursIn = view.findViewById<EditText>(R.id.editHours)
        val minutesIn = view.findViewById<EditText>(R.id.editMinutes)
        val secondsIn = view.findViewById<EditText>(R.id.editSeconds)*/

        view.findViewById<Button>(R.id.start_monitor_butt).setOnClickListener {
            val hours = view.findViewById<EditText>(R.id.editHours).text.toString().toInt()
            val minutes = view.findViewById<EditText>(R.id.editMinutes).text.toString().toInt()
            val seconds = view.findViewById<EditText>(R.id.editSeconds).text.toString().toInt()
            setupTimer(hours, minutes, seconds)
        }
        return view
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }*/


    // Might need onActivityResult() here to catch the result from startActivityForResult() method
    private fun onConnectClick (){
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null){
            errMess.text = getString(R.string.err_message_no_bt)
        }

        /*if (bluetoothAdapter?.isEnabled == false){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

            //ADD BT CONNECTIVITY STUFF HERE

        }*/
    }

    private fun setupTimer(hours: Int, minutes: Int, seconds: Int){
        val beginVal = (hours*60*60*1000)+(minutes*60*1000)+(seconds*1000)
        val beginValLong: Long = beginVal.toLong()

        val cdTimer = object:CountDownTimer(beginValLong,1000){
            override fun onTick(millisUntilFinished: Long){
                var difference = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60

                val elapsedHours = difference / hoursInMilli
                difference %= hoursInMilli

                val elapsedMinutes = difference / minutesInMilli
                difference %= minutesInMilli

                val elapsedSeconds = difference / secondsInMilli

                editHours.setText((elapsedHours).toInt().toString())
                editMinutes.setText((elapsedMinutes).toInt().toString())
                editSeconds.setText((elapsedSeconds).toInt().toString())
            }
            override fun onFinish(){
                errMess.text = getString(R.string.timer_done)
            }

        }
        cdTimer.start()
    }

}
