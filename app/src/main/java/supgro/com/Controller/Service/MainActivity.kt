package supgro.com.Controller.Service

import android.app.NotificationManager
import android.content.Intent
import android.icu.text.Transliterator
import supgro.com.R
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.notifications_item_layout.*
import supgro.com.Controller.Fragments.*
import supgro.com.Controller.Model.Notification
import supgro.com.Controller.Model.Post

class MainActivity (): AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    var post: MutableList<Post>? = null
    var noti: MutableList<Notification>? = null




    /* This selectedFragment was used to test when the buttons were clicked they would go to that fragment/activity
     selectedFragment = HomeFragment()
     internal var selectedFragment: Fragment? = null*/

    private val count: Int = 1

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    //passing in moveToFragment function

                    moveToFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true

                }
                R.id.nav_search -> {
                    moveToFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true



                }
                R.id.nav_add -> {
                    moveToFragment(PostSelectionFragment())
                    //item.isChecked = false
                //startActivity(Intent(this@MainActivity, AddPostActivity::class.java))
                    return@OnNavigationItemSelectedListener true

                }
                R.id.nav_notifications -> {
                    if (moveToFragment( NotificationsFragment()) != null){
                        badgeClear(R.id.nav_notifications).apply {
                            isDestroyed

                          //  new_notifications_icon.visibility = View.VISIBLE
                        }

                    }
                    return@OnNavigationItemSelectedListener true


                }
                R.id.nav_profile -> {

                     moveToFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true

                  /*  if (item.isEnabled){
                        item.isVisible = false
                    }*/


                }
            }
       /* if (selectedFragment != null){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
        }*/

false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!


        //bottom_nav_view is the name of the bottom navigation bar
val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

 navView.setOnNavigationItemSelectedListener (onNavigationItemSelectedListener)


        //this selects the first fragment activity to be loaded

        navView.getOrCreateBadge(R.id.nav_notifications).apply{
            val notiRef = FirebaseDatabase.getInstance().reference.child("Notifications")





            notiRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                      number = p0.childrenCount.toInt()
                        isVisible = true
                       // noti!!.clear()


                    }else{
                        number = 0
                    }




                    }



                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        }

       // navView.removeBadge(R.id.nav_notifications)

        moveToFragment(HomeFragment())



    }




private fun moveToFragment(fragment: Fragment){

    val fragmentTrans = supportFragmentManager.beginTransaction()
    fragmentTrans.replace(R.id.fragment_container, fragment)
    fragmentTrans.commit()


}

    private fun badgeClear(id: Int){
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        val badgeDrawable = navView.getOrCreateBadge(id)
        if (badgeDrawable != null){
            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()
        }
    }
}