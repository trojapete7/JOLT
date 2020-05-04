package jolt.pack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_rewards.*
import java.nio.file.attribute.UserPrincipal

class Rewards : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        six_flags_button.setOnClickListener {
            user_points.text = "0"
        }

        lyft_button.setOnClickListener {
            user_points.text = "4000"
        }

        big_mac_button.setOnClickListener {
            user_points.text = "4500"
        }

        frosty_button.setOnClickListener {
            user_points.text = "4800"
        }

        return view
    }
}