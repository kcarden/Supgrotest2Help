package supgro.com.Controller.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import supgro.com.Controller.CommentsActivity
import supgro.com.Controller.MainActivity
import supgro.com.Controller.Model.Post
import supgro.com.Controller.Model.User
import supgro.com.Controller.ShowUsersActivity
import supgro.com.R

class PostAdapter (private val pContext: Context, val mPost: List<Post>): RecyclerView.Adapter <RecyclerView.ViewHolder>(){

    private var firebaseUser: FirebaseUser? = null
    private val imagePost = 1
    private val textPost = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View
        return if (viewType == imagePost) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_post_layout, parent, false)

            ImageHolder(
                view
            )


        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.text_post_layout, parent, false)

            TextHolder(
                view
            )

        }
    }

        /*return when (viewType) {
            imagePost -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.image_post_layout, parent, false)
                RecyclerView(view)
            }
            textPost -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.text_post_layout, parent, false)
                RecyclerView(view)
            }

    }*/




    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

      firebaseUser = FirebaseAuth.getInstance().currentUser

        val post = mPost[position]

        if (holder.itemViewType == textPost){
 //Text ViewHolder <----------------------------------------------------------------------------------------------------------------------
            val viewHolder = holder as TextHolder



                viewHolder.postText.setText(post.getPostText())



            //viewHolder.postText.setText(post.getPostText())

            publisherInfo(holder.posterprofileImage, holder.userName, holder.publisher, post.getPublisher())
            getTotalNumberOfComments(holder.comments, post.getPostId() )

            isLike(post.getPostId(), holder.likeButton)
            numberoflikes(holder.likes, post.getPostId())
            checkSavedStatus(post.getPostId(), holder.saveButton)


            //setting up the like and unlike buttonw
            holder.likeButton.setOnClickListener {
                if (holder.likeButton.tag == "Like"){
                    FirebaseDatabase.getInstance().reference.child("Likes")
                        .child(post.getPostId())
                        .child(firebaseUser!!.uid)
                        .setValue(true)

                    addNotification(post.getPublisher(), post.getPostId())

                }else{FirebaseDatabase.getInstance().reference.child("Likes")
                    .child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .removeValue()

                    val intent = Intent(pContext, MainActivity::class.java)
                    pContext.startActivity(intent)

                }
            }

            holder.likes.setOnClickListener {
                val intent = Intent(pContext, ShowUsersActivity::class.java)
                intent.putExtra("id", post.getPostId())
                intent.putExtra("title", "likes")
                pContext.startActivity(intent)
            }

            holder.commentButton.setOnClickListener {
                val intentcomment = Intent(pContext, CommentsActivity::class.java)
                intentcomment.putExtra("postId", post.getPostId())
                intentcomment.putExtra("publisherId", post.getPostId())
                pContext.startActivity(intentcomment)

            }

            holder.comments.setOnClickListener {
                val intentcomment = Intent(pContext, CommentsActivity::class.java)
                intentcomment.putExtra("postId", post.getPostId())
                intentcomment.putExtra("publisherId", post.getPostId())
                pContext.startActivity(intentcomment)

            }

            holder.saveButton.setOnClickListener {
                if (holder.saveButton.tag == "Save"){

                    FirebaseDatabase.getInstance().reference.child("Saves")
                        .child(firebaseUser!!.uid)
                        .child(post.getPostId())
                        .setValue(true)



                }else{

                    FirebaseDatabase.getInstance().reference.child("Saves")
                        .child(firebaseUser!!.uid)
                        .child(post.getPostId())
                        .removeValue()
                }

            }
//image ViewHolder <---------------------------------------------------------------------------------------------------
       }else {
            val viewHolder = holder as ImageHolder

            Glide.with(pContext).load(post!!.getPostImage())
                .placeholder(R.drawable.add_profile_image).into(holder.postImage)



            if (post.getCaption().equals("")) {

                holder.caption.visibility = View.GONE
            } else {

                holder.caption.visibility = View.VISIBLE
                holder.caption.setText(post.getCaption())

            }



            publisherInfo(
                holder.posterprofileImage,
                holder.userName,
                holder.publisher,
                post.getPublisher()
            )
            getTotalNumberOfComments(holder.comments, post.getPostId())

            isLike(post.getPostId(), holder.likeButton)
            numberoflikes(holder.likes, post.getPostId())
            checkSavedStatus(post.getPostId(), holder.saveButton)


            //setting up the like and unlike buttonw
            holder.likeButton.setOnClickListener {
                if (holder.likeButton.tag == "Like") {
                    FirebaseDatabase.getInstance().reference.child("Likes")
                        .child(post.getPostId())
                        .child(firebaseUser!!.uid)
                        .setValue(true)

                    addNotification(post.getPublisher(), post.getPostId())

                } else {
                    FirebaseDatabase.getInstance().reference.child("Likes")
                        .child(post.getPostId())
                        .child(firebaseUser!!.uid)
                        .removeValue()

                    val intent = Intent(pContext, MainActivity::class.java)
                    pContext.startActivity(intent)

                }
            }

            holder.likes.setOnClickListener {
                val intent = Intent(pContext, ShowUsersActivity::class.java)
                intent.putExtra("id", post.getPostId())
                intent.putExtra("title", "likes")
                pContext.startActivity(intent)
            }

            holder.commentButton.setOnClickListener {
                val intentcomment = Intent(pContext, CommentsActivity::class.java)
                intentcomment.putExtra("postId", post.getPostId())
                intentcomment.putExtra("publisherId", post.getPostId())
                pContext.startActivity(intentcomment)

            }

            holder.comments.setOnClickListener {
                val intentcomment = Intent(pContext, CommentsActivity::class.java)
                intentcomment.putExtra("postId", post.getPostId())
                intentcomment.putExtra("publisherId", post.getPostId())
                pContext.startActivity(intentcomment)

            }

            holder.saveButton.setOnClickListener {
                if (holder.saveButton.tag == "Save") {

                    FirebaseDatabase.getInstance().reference.child("Saves")
                        .child(firebaseUser!!.uid)
                        .child(post.getPostId())
                        .setValue(true)


                } else {

                    FirebaseDatabase.getInstance().reference.child("Saves")
                        .child(firebaseUser!!.uid)
                        .child(post.getPostId())
                        .removeValue()
                }

            }

        }





    }
//End of viewHolders <------------------------------------------------------------------------------------------------



    override fun getItemCount(): Int {

        return mPost.size

    }

    override fun getItemViewType(position: Int): Int {
        return if (mPost[position].getPostImage().isEmpty())  textPost else imagePost

    }


     class ImageHolder (@NonNull itemView: View): RecyclerView.ViewHolder(itemView){

        var posterprofileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        var likeButton: ImageView = itemView.findViewById(R.id.post_image_like_btn)
        var commentButton: ImageView = itemView.findViewById(R.id.post_image_comment_btn)
        var saveButton: ImageView = itemView.findViewById(R.id.post_save_comment_btn)
        var userName: TextView = itemView.findViewById(R.id.user_name_search)
        var likes: TextView = itemView.findViewById(R.id.likes)
        var publisher: TextView = itemView.findViewById(R.id.publisher)
        var comments: TextView = itemView.findViewById(R.id.comment_counter)
        var postSettings: ImageView = itemView.findViewById(R.id.text_post_settings)

         var postImage: ImageView = itemView.findViewById(R.id.post_image_home)
         var caption: TextView = itemView.findViewById(R.id.caption)



     }

    //Text post
   inner class TextHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView) {
        var posterprofileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        var likeButton: ImageView = itemView.findViewById(R.id.post_image_like_btn)
        var commentButton: ImageView = itemView.findViewById(R.id.post_image_comment_btn)
        var saveButton: ImageView = itemView.findViewById(R.id.post_save_comment_btn)
        var userName: TextView = itemView.findViewById(R.id.user_name_search)
        var likes: TextView = itemView.findViewById(R.id.likes)
        var publisher: TextView = itemView.findViewById(R.id.publisher)
        var comments: TextView = itemView.findViewById(R.id.comment_counter)
        var postSettings: ImageView = itemView.findViewById(R.id.text_post_settings)

        var postText: TextView = itemView.findViewById(R.id.post_text_home)
    }

    private fun publisherInfo(profileImage: CircleImageView, userName: TextView, publisher: TextView, publisherID: String) {

        val userRef = FirebaseDatabase.getInstance().reference.child("username").child(publisherID)
        userRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user = p0.getValue<User>(User::class.java)

                    //Picasso.get().load(user!!.getImage()).placeholder(R.drawable.add_profile_image).into(profileImage)

                    userName.setText(user!!.getUsername())

                    Glide.with(pContext).load(user!!.getImage()).placeholder(R.drawable.avataphoto).into(profileImage)
                    publisher.setText(user!!.getFullName())



                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //change like button color
    private fun isLike(postId: String, likeButton: ImageView) {

        //selects user i believe
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Likes")
            .child(postId)

        LikesRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.child(firebaseUser!!.uid).exists()){

                    likeButton.setImageResource(R.drawable.heart_red)
                    likeButton.tag = "Liked"
                }else{
                    likeButton.setImageResource(R.drawable.heart)
                    likeButton.tag = "Like"
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun numberoflikes(likes: TextView, postId: String) {

        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(postId)

        LikesRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
//will get the total number of likes on the post when clike
                    likes.text = p0.childrenCount.toString() //+

                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun getTotalNumberOfComments(comments: TextView, postId: String) {

        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Comments").child(postId)

        LikesRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
//will get the total number of likes on the post when clike
                    comments.text = "view all " + p0.childrenCount.toString() + " comments"


                }else{

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun checkSavedStatus(postId: String, imageView: ImageView){

        val saveRef = FirebaseDatabase.getInstance().reference
            .child("Saves")
            .child(firebaseUser!!.uid)

        saveRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(postId).exists()){

                    imageView.setImageResource(R.drawable.savedfilled)
                    imageView.tag = "Saved"
                }else{

                    imageView.setImageResource(R.drawable.saveunfilled)
                    imageView.tag = "Save"


                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }


        })



    }

    private fun addNotification(userId:  String, postId: String){

        val notiRef = FirebaseDatabase.getInstance().reference
            .child("Notifications")
            .child(userId)

        val notiMap = HashMap<String, Any>()
        notiMap["userid"] = firebaseUser!!.uid
        notiMap["text"] = "liked your post: "
        notiMap["postId"] = postId
        notiMap["ispost"] = true

        notiRef.push().setValue(notiMap)


    }

}