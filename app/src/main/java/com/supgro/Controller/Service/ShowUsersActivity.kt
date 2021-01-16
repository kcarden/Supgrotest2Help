package com.supgro.Controller.Service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.supgro.Controller.Adapter.UserAdapter
import com.supgro.Controller.Model.User
import com.supgro.R

class ShowUsersActivity : AppCompatActivity() {


    var id: String = ""
    var title: String = ""

    var userAdapter: UserAdapter? = null
    var userList: List<User>? = null
    var idList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_users)

        val intent = intent!!
        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userList = ArrayList()
        userAdapter = UserAdapter(this, userList as ArrayList<User>, false)
        recyclerView.adapter = userAdapter

        idList = ArrayList()

        when(title){
            "likes" -> getLikes()
            "supporting" -> getSupporting()
            "supporters" -> getSupporters()
            "views" -> getViews()
        }

    }

    private fun getViews() {

    }



    private fun getSupporters() {


        val supportersRef = FirebaseDatabase.getInstance().reference
            .child("Support").child(id!!)
            .child("Supporters")

        supportersRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                (idList as ArrayList<String>).clear()

                for (snapshot in p0.children){
                    (idList as ArrayList<String>).add(snapshot.key!!)
                }
                showUsers()
                }


            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }



    private fun getSupporting() {

        val supportingRef = FirebaseDatabase.getInstance().reference
            .child("Support").child(id!!)
            .child("Supporting")

        supportingRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                (idList as ArrayList<String>).clear()

                for (snapshot in p0.children){
                    (idList as ArrayList<String>).add(snapshot.key!!)
                }
                showUsers()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }



    private fun getLikes() {

        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(id!!)

        LikesRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){

                    (idList as ArrayList<String>).clear()

                    for (snapshot in p0.children){
                        (idList as ArrayList<String>).add(snapshot.key!!)
                    }
                    showUsers()

                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun showUsers() {
        val userRef = FirebaseDatabase.getInstance().getReference().child("username")
        userRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                (userList as ArrayList<User>).clear()

                for (snapshot in dataSnapshot.children){

                    val user = snapshot.getValue(User::class.java)

                    for(id in idList!!){

                        if (user!!.uid == id){
                            (userList as ArrayList<User>).add(user!!)
                        }
                    }

                }


                    userAdapter?.notifyDataSetChanged()
                }



            override fun onCancelled(p0: DatabaseError) {


            }
        })
    }
}