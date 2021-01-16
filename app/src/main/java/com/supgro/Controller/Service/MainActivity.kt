package com.supgro.Controller.Service

import com.supgro.R
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.supgro.Controller.Adapter.NotificationAdapter
import com.supgro.Controller.Fragments.*
import com.supgro.Controller.Model.Notification
import com.supgro.Controller.Model.Post

class MainActivity (): AppCompatActivity() {

    var post: List<Post>? = null
    private var notificationList: List<Notification>? = null
    private var notificationAdapter: NotificationAdapter? = null

    //private var firebaseUser: FirebaseUser? = null


    /* This selectedFragment was used to test when the buttons were clicked they would go to that fragment/activity
     selectedFragment = HomeFragment()
     internal var selectedFragment: Fragment? = null*/





    private val onNavigationItemSelectedListener =
                BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {
                    badgeCounter()
                    moveToFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true

                }
                R.id.nav_search -> {
                    badgeCounter()
                    moveToFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true


                }
                R.id.nav_add -> {
                    badgeCounter()
                    moveToFragment(PostSelectionFragment())
                    return@OnNavigationItemSelectedListener true

                }
                R.id.nav_notifications -> {
                    badgeCounter()
                    if (moveToFragment(NotificationsFragment()) != null) {
                        badgeClear()

                    }else{
                    }

                    return@OnNavigationItemSelectedListener true


                }
                R.id.nav_profile -> {
                    badgeCounter()
                    moveToFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true

                                  }


            }

            false

        }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //bottom_nav_view is the name of the bottom navigation bar
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(SearchFragment())

       /* if (HomeFragment() != null){
            badgeCounter()

        }
*/


        //this selects the first fragment activity to be loaded






    }





    private fun moveToFragment(fragment: Fragment) {

        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()


    }

    private fun badgeClear() {
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)


        val badgeDrawable = navView.getOrCreateBadge(R.id.nav_notifications)
        if (badgeDrawable != null) {
            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()


        }


    }
    private fun badgeCounter(){
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val currentUserID = FirebaseAuth.getInstance().currentUser

        val badgeDrawable = navView.getOrCreateBadge(R.id.nav_notifications).apply {
            val notiRef = FirebaseDatabase.getInstance().reference.child("Notifications")
                .child(currentUserID!!.uid)

            notiRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        number = p0.childrenCount.toInt()
                        isVisible = true
                    }else{
                        isVisible = false
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })





        }
        }






}