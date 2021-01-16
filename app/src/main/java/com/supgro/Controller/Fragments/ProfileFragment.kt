package com.supgro.Controller.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import com.supgro.Controller.Model.User
import com.supgro.Controller.Service.AccountSetting_Activity
import com.supgro.Controller.Adapter.MyImagesAdapter
import com.supgro.Controller.Adapter.MyTextAdapter
import com.supgro.Controller.Model.Post
import com.supgro.Controller.Service.MessageActivity
import com.supgro.Controller.Service.ShowUsersActivity
import com.supgro.R
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent as Intent1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {

    private var postId = ""
    private lateinit var publisherId: String

    private lateinit var profileId: String
    private lateinit var posttext: String
    private lateinit var firebaseUser: FirebaseUser


    var postList: List<Post>? = null
    var myImagesAdapter: MyImagesAdapter? = null
    var myTextAdapter: MyTextAdapter? = null
    private var user: MutableList<User>? = null


    var myImagesAdapterSavedImg: MyImagesAdapter? = null
    var postListSaved: List<Post>? = null
    var mySaveImg: List<String>? = null
    //private val notificationManager: NotificationManager = R.id.nav_notifications
        //second phase of launch
/*
    var myQuestionsAdapter: ViewQuestionsAdapter? = null
        var postListQuestions: MutableList<ViewQuestions>? = null*/
    //var supportingList: MutableList<ViewQuestions>? = null
   // var myreplyAdapter: ViewQuestionsAdapter? = null
    //var postListReply: MutableList<ViewQuestions>? = null







    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
            firebaseUser = FirebaseAuth.getInstance().currentUser!!


        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null){


            this.profileId = pref.getString("profileId", "none")!!
        }

        if (profileId == firebaseUser!!.uid){
            view.edit_Profile_Btn.text = "Edit Profile"

        }else if(profileId != firebaseUser!!.uid){

            checkSupportAndSupportingButtonStatus()
                   }

        var recyclerViewuploadImages: RecyclerView
        recyclerViewuploadImages = view.findViewById(R.id.recycler_view_grid_view)
        recyclerViewuploadImages.setHasFixedSize(true)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        recyclerViewuploadImages.layoutManager = linearLayoutManager

        postList = ArrayList()
        myImagesAdapter = context?.let { MyImagesAdapter(it, postList as ArrayList<Post>) }
        recyclerViewuploadImages.adapter = myImagesAdapter

        //recycleView for saved image
        var recyclerViewsaveImages: RecyclerView
        recyclerViewsaveImages = view.findViewById(R.id.recycler_view_saved_post)
       recyclerViewsaveImages.setHasFixedSize(true)
        val linearLayoutManager2: LinearLayoutManager = LinearLayoutManager(context)
       linearLayoutManager2.reverseLayout = true
        linearLayoutManager2.stackFromEnd
        recyclerViewsaveImages.layoutManager = linearLayoutManager2

        postListSaved = ArrayList()
         myImagesAdapterSavedImg = context?.let { MyImagesAdapter(it, postListSaved as ArrayList<Post>) }
        recyclerViewsaveImages.adapter = myImagesAdapterSavedImg


        //Testing text
/*iew.testBtn.setOnClickListener {

    //addQuestions()

}*/




        //recyclerViewQuestions.visibility = View.GONE
        recyclerViewsaveImages.visibility = View.GONE
        view.recycler_view_grid_view.visibility = View.VISIBLE

        /*var about_me_btn:TextView
        about_me_btn = view.findViewById(R.id.about_me_btn)
        about_me_btn.setOnClickListener{
            recyclerViewsaveImages.visibility = View.GONE
            recyclerViewuploadImages.visibility = View.GONE
           // recyclerViewQuestions.visibility = View.VISIBLE

        }*/

        var uploadedImageBtn:TextView
        uploadedImageBtn = view.findViewById(R.id.my_post_txt)
        uploadedImageBtn.setOnClickListener{
            recyclerViewsaveImages.visibility = View.GONE
            view.recycler_view_grid_view.visibility = View.VISIBLE
           // recyclerViewQuestions.visibility = View.GONE



        }

        var savedImageBtn:TextView
        savedImageBtn = view.findViewById(R.id.saved_Filled_btn_txt)
        savedImageBtn.setOnClickListener{
            recyclerViewsaveImages.visibility = View.VISIBLE
            view.recycler_view_grid_view.visibility = View.GONE
            //recyclerViewQuestions.visibility = View.GONE


        }

        view.supporters_Count_txt.setOnClickListener {

            view.tinted_constraint.visibility = View.VISIBLE
            view.my_supporters.visibility = View.VISIBLE
            view.supporters_close_btn.setOnClickListener {
                view.tinted_constraint.visibility = View.GONE
                view.my_supporters.visibility = View.GONE
            }
           /* val intent = Intent1(context, ShowUsersActivity::class.java)
            intent.putExtra("id", profileId)
            intent.putExtra("title", "supporters")
            startActivity(intent)*/
        }

        view.supporting_Count_Txt.setOnClickListener {

            view.tinted_constraint_supporting.visibility = View.VISIBLE
            view.my_supporting.visibility = View.VISIBLE
            view.supportering_close_btn.setOnClickListener {
                view.tinted_constraint_supporting.visibility = View.GONE
                view.my_supporting.visibility = View.GONE
            }
            /*val intent = Intent1(context, ShowUsersActivity::class.java)
            intent.putExtra("id", profileId)
            intent.putExtra("title", "supporting")
            startActivity(intent)*/
        }


        view.collapse_btn.visibility = View.GONE


        view.expand_btn.setOnClickListener {

               view.about_constraint_layout.visibility = View.GONE
            view.expand_btn.visibility = View.GONE
            view.collapse_btn.visibility = View.VISIBLE

        }

        view.collapse_btn.setOnClickListener {
            view.about_constraint_layout.visibility = View.VISIBLE
            view.collapse_btn.visibility = View.GONE
            view.expand_btn.visibility = View.VISIBLE



        }












        view.edit_Profile_Btn.setOnClickListener {
            val getButtonTxt = view.edit_Profile_Btn.text.toString()

            //When on the profile page of other users, this allows members to either see follow/unfollow and for own user to see their own info and able to edit their own page
            when {
                getButtonTxt == "Edit Profile" -> startActivity(Intent1(context, AccountSetting_Activity::class.java))

                getButtonTxt == "Support" -> {
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Support").child(it1.toString())
                            .child("Supporting").child(profileId)
                            .setValue(true)
                    }

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Support").child(profileId)
                            .child("Supporters").child(it1.toString())
                            .setValue(true)
                    }
                    addNotification()
                }

                getButtonTxt == "Supporting" -> {
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Support").child(it1.toString())
                            .child("Supporting").child(profileId)
                            .removeValue()
                    }

                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Support").child(profileId)
                            .child("Supporters").child(it1.toString())
                            .removeValue()
                    }
                }

            }
        }
        if (profileId == firebaseUser!!.uid) {
            view.message_user_profile_btn.visibility = View.GONE

        } else if (profileId != firebaseUser!!.uid) {
            view.message_user_profile_btn.visibility = View.VISIBLE

        }


        view.message_user_profile_btn.setOnClickListener {

            val intent = Intent1(context, MessageActivity::class.java)
           intent.putExtra("visit_id", profileId)
            startActivity(intent)

        }

        getSupporters()
        getSupporting()
        userInfo()
        myphotos()
        getTotalNumberOfPost()
        mySaves()
        //second phase of launch
        //readComments()
        //readReplys()



        return view



    }

    private fun checkSupportAndSupportingButtonStatus() {

        val supportingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Support").child(it1.toString())
                .child("Supporting")
        }

        if (supportingRef != null){

            supportingRef.addValueEventListener(object: ValueEventListener{

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.child(profileId).exists()) {

                        view?.edit_Profile_Btn?.text = "Supporting"

                    }else{
                        view?.edit_Profile_Btn?.text = "Support"

                    }

                }


                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }
//This is on the profile page and it add a count when someone starts to follow a person
    private fun getSupporters(){
        val supportersRef = FirebaseDatabase.getInstance().reference
                .child("Support").child(profileId)
                .child("Supporters")

        supportersRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    view?.supporters_number_txtview?.text = p0.childrenCount.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun getSupporting(){
        val supportingRef = FirebaseDatabase.getInstance().reference
                .child("Support").child(profileId)
                .child("Supporting")

        supportingRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    view?.supporting_Txt?.text = p0.childrenCount.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun myphotos(){
        val postsRef = FirebaseDatabase.getInstance().reference.child("Post")
        postsRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){

                   (postList as ArrayList<Post>).clear()

                    for (snapshot in p0.children){

                        val post = snapshot.getValue(Post::class.java)!!


                        if (post.publisher.equals(profileId)){
                            (postList as ArrayList<Post>).add(post)
                        }
                        Collections.reverse(postList)

                        myImagesAdapter!!.notifyDataSetChanged()

                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun userInfo(){

        val userRef = FirebaseDatabase.getInstance().getReference()
            .child("username")
            .child(profileId)
        userRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {



                if (p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
if (isAdded){
                        Glide.with(this@ProfileFragment)
                        .load(user!!.image)
                        .placeholder(R.drawable.add_profile_image)
                        .into(add_profile_image)}
                    view?.profile_fragment_username?.text = user!!.username
                   // view?.profile_fragment_fullname?.text = user!!.username
                    view?.profile_fragment_about_me?.text = user!!.about
                    view?.struggles_txtVw?.text = user.struggle
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onStop() {
        super.onStop()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }


    private fun getTotalNumberOfPost(){
        val postRef = FirebaseDatabase.getInstance().getReference()
            .child("Post")

        postRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    var postCounter = 0
                    for (snapShot in dataSnapshot.children){
                        val post = snapShot.getValue(Post::class.java)
                        if (post!!.publisher == profileId){

                            postCounter++
                        }
                    }

                 //view!!.post_count_txt.text = "" + postCounter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun mySaves(){

        mySaveImg = ArrayList()

        val savedRef = FirebaseDatabase.getInstance().reference
            .child("Saves")
            .child(firebaseUser!!.uid)

        savedRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){

                    for (snapshot in dataSnapshot.children){
                        (mySaveImg as ArrayList<String>).add(snapshot.key!!)
                    }
                    readSavedImageData()
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun readSavedImageData() {
        val postRef = FirebaseDatabase.getInstance().reference
            .child("Post")

        postRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){

                    (postListSaved as ArrayList<Post>).clear()

                    for (snapshot in dataSnapshot.children){

                        val post = snapshot.getValue(Post::class.java)

                        for (key in mySaveImg!!){

                            if (post!!.postid == key){
                                (postListSaved as ArrayList<Post>).add(post!!)
                            }
                        }
                    }

                    myImagesAdapterSavedImg!!.notifyDataSetChanged()


                }

            }


            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    private fun readSavedTextPost() {
        val postRef = FirebaseDatabase.getInstance().reference
            .child("Post")

        postRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){

                    (postListSaved as ArrayList<Post>).clear()

                    for (snapshot in dataSnapshot.children){

                        val post = snapshot.getValue(Post::class.java)

                        for (key in mySaveImg!!){

                            if (post!!.postid == key){
                                (postListSaved as ArrayList<Post>).add(post)
                            }
                        }
                    }

                    myImagesAdapterSavedImg!!.notifyDataSetChanged()


                }

            }


            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    private fun addNotification(){

        val notiRef = FirebaseDatabase.getInstance().reference
            .child("Notifications")
            .child(profileId)

        val notiMap = HashMap<String, Any>()
        notiMap["userid"] = firebaseUser.uid
        notiMap["text"] = "started supporting you"
        notiMap["postId"] = ""
        notiMap["ispost"] = false

        notiRef.push().setValue(notiMap)


    }




  /* private fun addQuestions() {

       val questionref = FirebaseDatabase.getInstance().reference
            .child("Questions")
            //.child(profileId)
      val postId = questionref.push().key


       val Questionmap = HashMap<String, Any>()
        Questionmap["postid"] = postId!!
       Questionmap["questions"] = type_question_ET!!.text.toString()
        Questionmap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
        Questionmap["timestamp"] =


       //questionref.push().setValue(Questionmap)

        questionref.child(postId.toString()).setValue(Questionmap)


       addNotification()

       type_question_ET.text.clear()


    }*/

//second phase of launch
    /*private fun readComments(){

        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Questions")
            //.child(firebaseUser.uid)
       //commentsRef.push().key


        commentsRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    postListQuestions!!.clear()
                    for (snapshot in p0.children) {

                        val questions = snapshot.getValue(ViewQuestions::class.java)
                          postListQuestions!!.add(questions!!)



                       //Collections.reverse(postListQuestions)



                    }
                    myQuestionsAdapter!!.notifyDataSetChanged()


               }



            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }*/






}

