package jolt.pack

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()


        // Write a message to the database
 /*       val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Successful DB Connection!")*/


        fun storeDateToFirebase() {
            val handler = Handler()
            val runnable = object : Runnable {
                override fun run() {
                    handler.postDelayed(this, 1000)
                    var any = try {
                        val date = Date()
                        val newDate = Date(date.time)
                        val dt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        val stringDate: String = dt.format(newDate)
                        println("Submission Date: $stringDate")
                        val databaseReference =
                            FirebaseDatabase.getInstance().reference.child("My_Date")
                        databaseReference.child("init_date").setValue(stringDate)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            handler.postDelayed(runnable, 1 * 1000)
        }

        storeDateToFirebase()

    }




    override fun createDeviceProtectedStorageContext(): Context {
        return super.createDeviceProtectedStorageContext()


    }




    private fun setupNavigation(){

        val navController = findNavController (R.id.nav_host_fragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.setupWithNavController(navController)

    }

    fun setNavigationVisibility(b: Boolean){
        if(b){
            bottom_nav.visibility = View.VISIBLE
        }

        else {
            bottom_nav.visibility = View.GONE
        }
    }
}