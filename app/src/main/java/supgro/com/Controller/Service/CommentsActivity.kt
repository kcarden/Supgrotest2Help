package supgro.com.Controller.Service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_comments.*
import supgro.com.Controller.Adapter.Comments_Adapter
import supgro.com.Controller.Model.Comment
import supgro.com.Controller.Model.User
import supgro.com.R

class CommentsActivity : AppCompatActivity() {
    private var postId = ""
    private var publisherId = ""
    private var firebaseUser: FirebaseUser? = null
    private var commentsAdapter: Comments_Adapter? = null
    private var commentList: MutableList<Comment>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)


        val intent = intent
        postId = intent.getStringExtra("postId")!!
        publisherId = intent.getStringExtra("publisherId")!!

        firebaseUser = FirebaseAuth.getInstance().currentUser



        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.recycler_view_comments)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        recyclerView.layoutManager = linearLayoutManager

        commentList = ArrayList()
        commentsAdapter = Comments_Adapter(this, commentList)
        recyclerView.adapter = commentsAdapter


        retriveUserInfo()
        readComments()
        getPostImage()

        post_comment.setOnClickListener{
            if (add_comment!!.text.toString() == ""){Toast.makeText(this, "Please enter comment first", Toast.LENGTH_LONG).show()


            }else{
                addcomment()




            }

        }

    }

//adding the comment to the database

    private fun addcomment() {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(postId)

        val commentsMap = HashMap<String, Any>()
        commentsMap["comment"] = add_comment!!.text.toString()
        commentsMap["publisher"] = firebaseUser!!.uid


        commentsRef.push().setValue(commentsMap )

        addNotification()

        add_comment!!.text.clear()


    }
//Retriving the user who is commenting, info
    private fun retriveUserInfo(){

        val userRef = FirebaseDatabase.getInstance().reference
            .child("username")
            .child(firebaseUser!!.uid)

        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {



                if (p0.exists()){

                    val user = p0.getValue<User>(User::class.java)

                    //Picasso.get().load(user!!.getImage()).placeholder(R.drawable.add_profile_image).into(add_account_setting_image);
                    Glide.with(this@CommentsActivity).load(user!!.getImage()).placeholder(R.drawable.add_profile_image).into(profile_image_comment)

                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getPostImage(){

        val postRef = FirebaseDatabase.getInstance().reference
            .child("Posts")
            .child(postId).child("postimage")

        postRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {



                if (p0.exists()){

                    val image = p0.value.toString()

                    //Picasso.get().load(user!!.getImage()).placeholder(R.drawable.add_profile_image).into(add_account_setting_image);
                    Glide.with(this@CommentsActivity).load(image).placeholder(R.drawable.add_profile_image).into(post_image_comment)

                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //displays the comment on the screen
    private fun readComments(){

        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(postId)


        commentsRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    commentList!!.clear()

                    for (snapshot in p0.children){
                        val comment = snapshot.getValue(Comment::class.java)
                        commentList!!.add(comment!!)
                    }

                    commentsAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addNotification(){

        val notiRef = FirebaseDatabase.getInstance().reference
            .child("Notifications")
            .child(publisherId)

        val notiMap = HashMap<String, Any>()

        //this is the person who will like my stuff
        notiMap["userid"] = firebaseUser!!.uid
        notiMap["text"] = "commented:" + add_comment!!.text.toString()
        notiMap["postId"] = postId
        notiMap["ispost"] = true

        notiRef.push().setValue(notiMap)


    }
}