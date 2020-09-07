package supgro.com.Controller

import android.content.Intent
import supgro.com.R
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*
import supgro.com.Controller.Fragments.*

class MainActivity : AppCompatActivity() {

   /* This selectedFragment was used to test when the buttons were clicked they would go to that fragment/activity
    selectedFragment = HomeFragment()
    internal var selectedFragment: Fragment? = null*/

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
                    moveToFragment(NotificationsFragment())
                    return@OnNavigationItemSelectedListener true


                }
                R.id.nav_profile -> {
                    moveToFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true


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

        //bottom_nav_view is the name of the bottom navigation bar
val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

 navView.setOnNavigationItemSelectedListener (onNavigationItemSelectedListener)

        //this selects the first fragment activity to be loaded

        moveToFragment(HomeFragment())

    }

private fun moveToFragment(fragment: Fragment){

    val fragmentTrans = supportFragmentManager.beginTransaction()
    fragmentTrans.replace(R.id.fragment_container, fragment)
    fragmentTrans.commit()


}
}