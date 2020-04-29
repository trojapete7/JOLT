package jolt.pack

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_home.*

const val REQUEST_ENABLE_BT = 1
const val MODULE_MAC = "98:D3:B1:FD:71:35"
private const val SCAN_PERIOD: Long = 10000

class Home : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errMess.text = getString(R.string.welcome_instruction)
    }

    // Might need onActivityResult() here to catch the result from startActivityForResult() method
    fun onConnectClick (view: View){
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
}
