package com.supgro.Controller.Fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.supgro.Controller.Adapter.NewMemberAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.supgro.Controller.Adapter.PostAdapter
import com.supgro.Controller.Adapter.UserAdapter
import com.supgro.Controller.Model.Post
import com.supgro.Controller.Model.User
import com.supgro.R
import kotlinx.android.synthetic.main.fragment_search.view.*


class HomeFragment : Fragment()  {

    private var postAdapter: PostAdapter? = null
    private var postList: MutableList<Post>? = null
    private var supportingList: MutableList<Post>? = null

    /*private var newMemberAdapter: NewMemberAdapter? = null
    private var userList: MutableList<User>? = null*/
    var firebaseUser: FirebaseUser? = null




    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_home, fragment_container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser



     /*   var NmrecyclerView: RecyclerView? = null
        NmrecyclerView = view.findViewById(R.id.new_members_recyclerView)
        val NmlinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        NmlinearLayoutManager.reverseLayout = true
        NmlinearLayoutManager.stackFromEnd = true
        NmrecyclerView.layoutManager = NmlinearLayoutManager

        userList = ArrayList()
        newMemberAdapter = context?.let { NewMemberAdapter(it, userList as ArrayList<User>) }
        NmrecyclerView.adapter = newMemberAdapter
*/
       /* val notiRef = FirebaseDatabase.getInstance().reference.child("MsgNotification")
        //.child(FirebaseAuth.getInstance().currentUser!!.uid)

               val notiRef = FirebaseDatabase.getInstance().reference.child("MsgNotification")
                   //.child(FirebaseAuth.getInstance().currentUser!!.uid)

               notiRef.addValueEventListener(object : ValueEventListener {
                   override fun onDataChange(p0: DataSnapshot) {

                       val badgecounter = Badge
                       val viewPagerAdapter: ViewPager = ViewPager(supportFragmentManager!!)
                       var countmessages = 0
                       for(dataSnapshot in p0.children)
                       {
                           val chat = dataSnapshot.getValue(Chat::class.java)
                           if (chat!!.receiver.equals(firebaseUser!!.uid)&& !chat.isseen){
                              countmessages += 1
                           }
                       }

                       if (countmessages == 0)
                           pagerAdapter.add
                   }


                   override fun onCancelled(p0: DatabaseError) {
                       TODO("Not yet implemented")
                   }

               })*/


               var recyclerView: RecyclerView? = null
                    recyclerView = view.findViewById(R.id.recycler_View_Home)
                    val linearLayoutManager = LinearLayoutManager(context)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    recyclerView.layoutManager = linearLayoutManager

                    postList = ArrayList()
                    postAdapter = context?.let { PostAdapter(it, postList as ArrayList<Post>) }
                    recyclerView.adapter = postAdapter
            checkSupportings()





               return view




        }





// this is to check on the post of people who you are supporting/following
    //This is saying your only seeing the post of the people you are following
    private fun checkSupportings() {
        supportingList = ArrayList()
//issue fixed because of removing null
        val supportingRef = FirebaseAuth.getInstance().currentUser?.uid?.let {
            FirebaseDatabase.getInstance().reference
                .child("Support").child(it)
                .child("Supporting")
        }

        supportingRef?.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    (supportingList as ArrayList<String>).clear()
                    for (snapshot in p0.children){

                        snapshot.key?.let{(supportingList as ArrayList<String>).add(it)}
                    }


                    retrieveTextandImagePost()
                    //retrieveTextPost()

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })


    }

  private fun retrieveTextandImagePost() {
        val imagePostRef = FirebaseDatabase.getInstance().reference
            .child("Post")

      imagePostRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                postList?.clear()
                for (snapshot in p0.children){

                    val post = snapshot.getValue(Post::class.java)

                    for (id in (supportingList as ArrayList<String>)){

                        if (post!!.publisher == id.toString()){

                            postList!!.add(post)



                        }

                       postAdapter!!.notifyDataSetChanged()



                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }



}
