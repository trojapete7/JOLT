package jolt.pack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_my_rewards.*
import org.w3c.dom.Text

class MyRewards: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_my_rewards, container, false)

        val six_flags = view.findViewById<TextView>(R.id.six_flags_num) as TextView
        six_flags.setText(num_day_passes)

        val lyft = view.findViewById<TextView>(R.id.lyft_num) as TextView
        lyft.setText(num_lyft)

        val big_mac = view.findViewById<TextView>(R.id.big_mac_num) as TextView
        big_mac.setText(num_big_macs)

        val frosty = view.findViewById<TextView>(R.id.frosty_num)
        frosty.setText(num_frosty)

        return view
    }
}