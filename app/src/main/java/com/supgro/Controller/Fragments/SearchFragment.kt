package com.supgro.Controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import com.supgro.Controller.Adapter.UserAdapter
import com.supgro.Controller.Model.User
import com.supgro.Controller.Service.*
import com.supgro.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SearchFragment : Fragment() {

    //initallizing everything to get started
    private var recyclerView: RecyclerView? = null
    private var userAdapter: UserAdapter? = null
    private var mUser: MutableList<User>? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, fragment_container, false)


        //initallizing everything to get started
        recyclerView = view.findViewById(R.id.recycler_View_Search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)


        //initallizing everything to get started
        mUser = ArrayList()
        userAdapter = context?.let { UserAdapter(it, mUser as ArrayList<User>, true) }
        recyclerView?.adapter = userAdapter

        view.seach_edit_text.addTextChangedListener(object: TextWatcher
        {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (view.seach_edit_text.text.toString() == ""){


                }else{
                    recyclerView?.visibility = View.VISIBLE

                    retrieveUsers()
                    searchUser(s.toString().toLowerCase())
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }



        })

        view.depression_category_txt_btn.setOnClickListener {

            val intent = Intent(context, Depression_Post_Activity::class.java)
            startActivity(intent)
        }
        view.anxiety_category_txt_btn.setOnClickListener {

            val intent = Intent(context, Anxiety_Post_Activity::class.java)
            startActivity(intent)
        }
        view.feeling_alone_category_txt_btn.setOnClickListener {

            val intent = Intent(context, Feeling_Alone_Post_Activity::class.java)
            startActivity(intent)
        }
        view.eating_disorder_category_txt_btn.setOnClickListener {

            val intent = Intent(context, EatingDisorder_Post_Activity::class.java)
            startActivity(intent)
        }
        view.selfharm_category_txt_btn.setOnClickListener {

            val intent = Intent(context, SelfHarm_Post_Activity::class.java)
            startActivity(intent)
        }
        view.supgro_meeting_space.setOnClickListener {
            val intent = Intent(context, Supgro_MeetUp_Activity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun searchUser(input: String) {
        val query = FirebaseDatabase.getInstance().getReference()
            .child("username")
            .orderByChild("username" )
            //.orderByChild("7_fullname")
            .startAt(input)
                //need to add this
            .endAt(input + "\uf8ff")

        query.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                mUser?.clear()

                for (snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(User::class.java)
                    if (user != null){
                        mUser?.add(user)
                    }
                }

                userAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {


            }
        })



    }

    private fun retrieveUsers() {

        val userRef = FirebaseDatabase.getInstance().getReference().child("username")
        userRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (view?.seach_edit_text?.text.toString() == ""){
                    mUser?.clear()
                    for (snapshot in dataSnapshot.children){
                        val user = snapshot.getValue(User::class.java)
                        if (user != null){
                            mUser?.add(user)
                        }
                    }

                    userAdapter?.notifyDataSetChanged()
                }

            }

            override fun onCancelled(p0: DatabaseError) {


            }
        })

    }



    }
