package com.supgro.Controller.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.supgro.Controller.Adapter.MessageNotificationAdapter
import com.supgro.Controller.Adapter.NotificationAdapter
import com.supgro.Controller.Model.MsgNotification
import com.supgro.Controller.Model.Notification
import com.supgro.R
import java.util.*
import kotlin.collections.ArrayList


class NotificationsFragment : Fragment() {

    private var notificationList: List<Notification>? = null
    private var notificationAdapter: NotificationAdapter? = null
    private var mNotificationList: List<MsgNotification>? = null
    private var mNotificationAdapter: MessageNotificationAdapter? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view = inflater.inflate(R.layout.fragment_notifications, container, false)

       var recyclerView: RecyclerView
        recyclerView = view.findViewById(R.id.recycler_View_notification)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        notificationList = ArrayList()

        notificationAdapter = NotificationAdapter(context!!, notificationList as ArrayList<Notification>)
        recyclerView.adapter = notificationAdapter

        var recyclerView2: RecyclerView
        recyclerView = view.findViewById(R.id.recycler_View_notification)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mNotificationList = ArrayList()

        mNotificationAdapter = MessageNotificationAdapter(context!!, mNotificationList as ArrayList<MsgNotification>)
        recyclerView.adapter = mNotificationAdapter

        readNotifications()
        readmNotifications()

         return view



    }

    override fun onDestroy() {
        super.onDestroy()
       FirebaseDatabase.getInstance().reference.child("Notifications")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .removeValue()
    }

    private fun readNotifications() {

        val notiRef = FirebaseDatabase.getInstance().reference
            .child("Notifications")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        notiRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){

                    (notificationList as ArrayList<Notification>).clear()

                    for (snapshot in dataSnapshot.children){

                        val notification = snapshot.getValue(Notification::class.java)

                        (notificationList as ArrayList<Notification>).add(notification!!)
                    }

                    Collections.reverse(notificationList)
                    notificationAdapter!!.notifyDataSetChanged()
                }


            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })





    }

    private fun readmNotifications() {

        val notiRef = FirebaseDatabase.getInstance().reference
            .child("Notifications")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        notiRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){

                    (mNotificationList as ArrayList<MsgNotification>).clear()

                    for (snapshot in dataSnapshot.children){

                        val notification = snapshot.getValue(MsgNotification::class.java)

                        (mNotificationList as ArrayList<MsgNotification>).add(notification!!)
                    }

                    Collections.reverse(mNotificationList)
                    mNotificationAdapter!!.notifyDataSetChanged()
                }


            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })





    }



}