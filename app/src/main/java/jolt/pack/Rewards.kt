package jolt.pack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import java.nio.file.attribute.UserPrincipal

class Rewards : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)
        /*
        val userPoints = view.findViewById<TextView>(R.id.user_points) as TextView

        view.findViewById<Button>(R.id.six_flags_button).setOnClickListener {
            redeemDayPass(userPoints)
        }

        view.findViewById<Button>(R.id.lyft_button).setOnClickListener {
            redeemLyft(userPoints)
        }

        view.findViewById<Button>(R.id.big_mac_button).setOnClickListener {
            redeemBigMac(userPoints)
        }

        view.findViewById<Button>(R.id.frosty_button).setOnClickListener {
            redeemFrosty(userPoints)
        }
        fun redeemDayPass(userPoints: Int) {
            if (userPoints >= 5000) {
                val newPoints = userPoints - 5000
                userPoints
            }
        }

        fun redeemLyft(userPoints: Int) {

        }

        fun redeemBigMac(userPoints: Int) {

        }

        fun redeemFrosty(userPoints: Int) {

        }*/
        return view
    }
}