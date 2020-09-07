package supgro.com.Controller.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.login_activity_layout.*
import supgro.com.Controller.Model.User
import supgro.com.Controller.AccountSetting_Activity
import supgro.com.Controller.Adapter.MyImagesAdapter
import supgro.com.Controller.Model.Post
import supgro.com.Controller.ShowUsersActivity
import supgro.com.R
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {

    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    var postList: List<Post>? = null
    var myImagesAdapter: MyImagesAdapter? = null

    var myImagesAdapterSavedImg: MyImagesAdapter? = null
    var postListSaved: List<Post>? = null
    var mySaveImg: List<String>? = null

    var myImagesAdapterAboutMe: MyImagesAdapter? = null
    var postListAboutMe: List<Post>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        if (profileId == firebaseUser.uid){
            view.edit_Profile_Btn.text = "Edit Profile"

        }else if(profileId != firebaseUser.uid){

            checkSupportAndSupportingButtonStatus()
        }



        //recycleView for gride image

        var recyclerViewAboutMeButton: RecyclerView
        recyclerViewAboutMeButton = view.findViewById(R.id.recyclrer_view_about_page)
        recyclerViewAboutMeButton.setHasFixedSize(true)
        val linearLayoutManager3: LinearLayoutManager = GridLayoutManager(context, 3)
        recyclerViewAboutMeButton.layoutManager = linearLayoutManager3

        postListAboutMe = ArrayList()
        myImagesAdapterAboutMe = context?.let { MyImagesAdapter(it, postListAboutMe as ArrayList<Post>) }
        recyclerViewAboutMeButton.adapter = myImagesAdapterAboutMe


        var recyclerViewuploadImages: RecyclerView
        recyclerViewuploadImages = view.findViewById(R.id.recyclrer_view_grid_view)
        recyclerViewuploadImages.setHasFixedSize(true)
        val linearLayoutManager: LinearLayoutManager = GridLayoutManager(context, 3)
        recyclerViewuploadImages.layoutManager = linearLayoutManager

        postList = ArrayList()
        myImagesAdapter = context?.let { MyImagesAdapter(it, postList as ArrayList<Post>) }
        recyclerViewuploadImages.adapter = myImagesAdapter

        //recycleView for saved image
        var recyclerViewsaveImages: RecyclerView
        recyclerViewsaveImages = view.findViewById(R.id.recyclrer_view_saved_post)
        recyclerViewsaveImages.setHasFixedSize(true)
        val linearLayoutManager2: LinearLayoutManager = GridLayoutManager(context, 3)
        recyclerViewsaveImages.layoutManager = linearLayoutManager2

        postListSaved = ArrayList()
         myImagesAdapterSavedImg = context?.let { MyImagesAdapter(it, postListSaved as ArrayList<Post>) }
        recyclerViewsaveImages.adapter = myImagesAdapterSavedImg






        recyclerViewAboutMeButton.visibility = View.VISIBLE
        recyclerViewsaveImages.visibility = View.GONE
        recyclerViewuploadImages.visibility = View.GONE

        var about_me_btn:TextView
        about_me_btn = view.findViewById(R.id.about_me_btn)
        about_me_btn.setOnClickListener{
            recyclerViewsaveImages.visibility = View.GONE
            recyclerViewuploadImages.visibility = View.GONE
            recyclerViewAboutMeButton.visibility = View.VISIBLE

        }

        var uploadedImageBtn:TextView
        uploadedImageBtn = view.findViewById(R.id.grid_view_btn)
        uploadedImageBtn.setOnClickListener{
            recyclerViewsaveImages.visibility = View.GONE
            recyclerViewuploadImages.visibility = View.VISIBLE
            recyclerViewAboutMeButton.visibility = View.GONE



        }

        var savedImageBtn:TextView
        savedImageBtn = view.findViewById(R.id.saved_Filled_btn)
        savedImageBtn.setOnClickListener{
            recyclerViewsaveImages.visibility = View.VISIBLE
            recyclerViewuploadImages.visibility = View.GONE
            recyclerViewAboutMeButton.visibility = View.GONE


        }

        view.supporters_Count_txt.setOnClickListener {
            val intent = Intent(context, ShowUsersActivity::class.java)
            intent.putExtra("id", profileId)
            intent.putExtra("title", "supporters")
            startActivity(intent)
        }

        view.supporting_Count_Txt.setOnClickListener {
            val intent = Intent(context, ShowUsersActivity::class.java)
            intent.putExtra("id", profileId)
            intent.putExtra("title", "supporting")
            startActivity(intent)
        }


        view.edit_Profile_Btn.setOnClickListener {
            val getButtonTxt = view.edit_Profile_Btn.text.toString()

            //When on the profile page of other users, this allows members to either see follow/unfollow and for own user to see their own info and able to edit their own page
            when {
                getButtonTxt == "Edit Profile" -> startActivity(Intent(context, AccountSetting_Activity::class.java))

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

        getSupporters()
        getSupporting()
        userInfo()
        myphotos()
        getTotalNumberOfPost()
        mySaves()

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
                    view?.supporters_Count_txt?.text = p0.childrenCount.toString()
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
                    view?.supporting_Count_Txt?.text = p0.childrenCount.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun myphotos(){
        val postsRef = FirebaseDatabase.getInstance().reference.child("Posts")
        postsRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){

                    (postList as ArrayList<Post>).clear()

                    for (snapshot in p0.children){

                        val post = snapshot.getValue(Post::class.java)!!
                        if (post.getPublisher().equals(profileId)){
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

                    //Picasso.get().load(user!!.getImage()).placeholder(R.drawable.add_profile_image).resize(200, 200).into(add_profile_image)
                    Glide.with(this@ProfileFragment)
                        .load(user!!.getImage())
                        .placeholder(R.drawable.add_profile_image)
                        .into(add_profile_image)
                    view?.profile_fragment_username?.text = user!!.getUsername()
                    view?.profile_fragment_fullname?.text = user!!.getFullName()
                    view?.profile_fragment_about_me?.text = user!!.getAbout()
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
            .child("Posts")

        postRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    var postCounter = 0
                    for (snapShot in dataSnapshot.children){
                        val post = snapShot.getValue(Post::class.java)
                        if (post!!.getPublisher() == profileId){

                            postCounter++
                        }
                    }

                    post_count_txt!!.text = "" + postCounter
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
            .child("Posts")

        postRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){

                    (postListSaved as ArrayList<Post>).clear()

                    for (snapshot in dataSnapshot.children){

                        val post = snapshot.getValue(Post::class.java)

                        for (key in mySaveImg!!){

                            if (post!!.getPostId() == key){
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

    private fun addNotification(){

        val notiRef = FirebaseDatabase.getInstance().reference
            .child("Notifications")
            .child(profileId)

        val notiMap = HashMap<String, Any>()
        notiMap["userid"] = firebaseUser!!.uid
        notiMap["text"] = "started supporting you"
        notiMap["postId"] = ""
        notiMap["ispost"] = false

        notiRef.push().setValue(notiMap)


    }
}
