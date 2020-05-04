package jolt.pack

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.recycler_view_item.*
import java.util.*


class Calendar : Fragment() {

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)



        // Create database instances for four sessions
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Session1")

        val database2 = FirebaseDatabase.getInstance()
        val myRef2 = database2.getReference("Session2")

        val database3 = FirebaseDatabase.getInstance()
        val myRef3 = database3.getReference("Session3")

        val database4 = FirebaseDatabase.getInstance()
        val myRef4 = database4.getReference("Session4")

        //write to database with new time values
        myRef.child("Date").setValue("05/01/2020-07:00")
        myRef.child("Points").setValue("Points: 250")

        myRef2.child("Date").setValue("05/02/2020-08:00")
        myRef2.child("Points").setValue("Points: 200")

        myRef3.child("Date").setValue("05/03/2020-10:00")
        myRef3.child("Points").setValue("Points: 300")

        myRef4.child("Date").setValue("05/03/2020-12:00")
        myRef4.child("Points").setValue("Points: 400")


        //read from database to change session time values
        myRef.child("Date").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val sessionTime = dataSnapshot.getValue(String::class.java)
                    textView2.text = sessionTime
                    textView5.text = sessionTime
                    textView8.text = sessionTime
                    textView12.text = sessionTime
                }



            override fun onCancelled(databaseError: DatabaseError) {}
        })



        //store current time to database (for testing purposes)
        fun storeDateToFirebase() {
            val handler = Handler()
            val runnable = object : Runnable {
                override fun run() {
                    //handler.postDelayed(this, 1000)
                    var any = try {
                        val date = Date()
                        val newDate = Date(date.time)
                        val dt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        val stringDate: String = dt.format(newDate)
                        println("Submission Date: $stringDate")
                        val databaseReference =
                            FirebaseDatabase.getInstance().reference.child("My_Date")
                        databaseReference.child("init_date2").setValue(stringDate)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            handler.postDelayed(runnable, 1 * 1000)
        }

        storeDateToFirebase()



            // Inflate the layout for this fragment

            val viewAdapter = MyAdapter(arrayOf("Flo", "Ly", "Jo"))

            view.findViewById<RecyclerView>(R.id.leaderboard_list).run {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
            }
            return view
        }

    }

    class MyAdapter(private val myDataset: Array<String>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ViewHolder {
            // create a new view
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false)


            return ViewHolder(itemView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.item.findViewById<TextView>(R.id.user_name_text).text = myDataset[position]

            holder.item.findViewById<ImageView>(R.id.user_avatar_image)
                .setImageResource(listOfAvatars[position])

            holder.item.setOnClickListener {
                val bundle = bundleOf("userName" to myDataset[position])

                Navigation.findNavController(holder.item).navigate(
                    R.id.action_calendar_to_calendar3,
                    bundle)
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }

        private val listOfAvatars = listOf(
        R.drawable.avatar_1_raster, R.drawable.avatar_2_raster, R.drawable.avatar_3_raster)



    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }*/

