package jolt.pack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.schedule_time_dialog.*


class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_time_dialog)

        if(intent.getBooleanExtra("notification", false)) {
            txtTitleView.text = intent.getStringExtra("title")
            txtMsgView.text = intent.getStringExtra("message")
        }
    }
}