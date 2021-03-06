package jolt.pack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_rewards.*

const val six_flags: Int = 2000
const val lyft_coupon: Int = 750
const val big_mac: Int = 500
const val wendys_frosty: Int = 200

class Rewards : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_rewards, container, false)

        view.findViewById<Button>(R.id.six_flags_button).setOnClickListener {
            currentPoints -= six_flags
            user_points.text = currentPoints.toString()
        }

        view.findViewById<Button>(R.id.lyft_button).setOnClickListener {
            currentPoints -= lyft_coupon
            user_points.text = currentPoints.toString()
        }

        view.findViewById<Button>(R.id.big_mac_button).setOnClickListener {
            currentPoints-= big_mac
            user_points.text = currentPoints.toString()
        }

        view.findViewById<Button>(R.id.frosty_button).setOnClickListener {
            currentPoints -= wendys_frosty
            user_points.text = currentPoints.toString()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_points.text = currentPoints.toString()
    }

}