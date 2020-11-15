 package supgro.com.Controller.Adapter

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.SyncStateContract.Helpers.update
import android.sax.StartElementListener
import android.text.method.Touch
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.inflate
import android.widget.*
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.account_setting_activity.*
import kotlinx.android.synthetic.main.activity_add_text_post.*
import kotlinx.android.synthetic.main.activity_edit_post.view.*
import kotlinx.android.synthetic.main.edit_post_bottomsheet_fragment.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.profile_fragment_username
import kotlinx.android.synthetic.main.post_settings.*
import kotlinx.android.synthetic.main.post_settings.view.*
import org.w3c.dom.Text
import supgro.com.Controller.Adapter.MyImagesAdapter.TextHolder
import supgro.com.Controller.Fragments.BottomSheetFragment
import supgro.com.Controller.Fragments.ProfileFragment
import supgro.com.Controller.Service.CommentsActivity
import supgro.com.Controller.Service.MainActivity
import supgro.com.Controller.Model.Post
import supgro.com.Controller.Model.User
import supgro.com.Controller.Service.Edit_Post_Activity
import supgro.com.Controller.Service.ShowUsersActivity
import supgro.com.R
import java.sql.Ref
import kotlin.math.E
import androidx.fragment.app.FragmentManager as FragmentManager1
import com.bumptech.glide.manager.SupportRequestManagerFragment as SupportRequestManagerFragment1

 class PostAdapter (private val pContext: Context, val mPost: List<Post>): RecyclerView.Adapter <RecyclerView.ViewHolder>(){

    private var firebaseUser: FirebaseUser? = null
    private val typeTwo_textPost = 1
    private val textPost = 2
    internal lateinit var cancelBtn: Button
    internal lateinit var settingDialog: Dialog
    private lateinit var profileId: String
    internal lateinit var EditPostTxtBtn: TextView
     internal lateinit var DelPostBtn: TextView

     internal lateinit var editPostDialog: AlertDialog
     val bottomSheetFragment = BottomSheetDialog(pContext)
     val view = LayoutInflater.from(pContext).inflate(R.layout.edit_post_dialog_box, null)
     private var postId: String = ""









     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

       /* val view = LayoutInflater.from(pContext).inflate(R.layout.text_post_layout, parent, false)
        return TextHolder(view)*/
       val view: View
        return if (viewType == typeTwo_textPost) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.type_two_text_post_layout, parent, false)

            TypeTwoTextHolder(
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






    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

      firebaseUser = FirebaseAuth.getInstance().currentUser




        val pref = pContext?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null){


            this.profileId = pref.getString("profileId", "none")!!
        }

        val post = mPost[position]







        if (holder.itemViewType == textPost){
 //Text ViewHolder <----------------------------------------------------------------------------------------------------------------------

            val viewHolder = holder as TextHolder

            viewHolder.userName.setOnClickListener {
                val pref = pContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                pref.putString("profileId", post.getPublisher())
                pref.apply()

                (pContext as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()
            }

            viewHolder.posterprofileImage.setOnClickListener {
                val pref = pContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                pref.putString("profileId", post.getPublisher())
                pref.apply()

                (pContext as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()
            }

            if (post.getPublisher() == firebaseUser!!.uid){
                holder.postSettings.visibility = View.VISIBLE

            }else if(post.getPublisher() != firebaseUser!!.uid){
                holder.postSettings.visibility = View.GONE


            }
            viewHolder.postSettings.setOnClickListener() {
                settingAlertDialog(post.getPostId(), mPost[position].getPostText())
            }

            holder.postText.text = post.getPostText()



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

                    /*val intent = Intent(pContext, MainActivity::class.java)
                    pContext.startActivity(intent)*/

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

                    addNotificationSaved(post.getPublisher(), post.getPostId())



                }else{

                    FirebaseDatabase.getInstance().reference.child("Saves")
                        .child(firebaseUser!!.uid)
                        .child(post.getPostId())
                        .removeValue()
                }

            }
//image ViewHolder <---------------------------------------------------------------------------------------------------
      }
else {
            val viewHolder = holder as TypeTwoTextHolder

            viewHolder.userName.setOnClickListener {
                val pref = pContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                pref.putString("profileId", post.getPublisher())
                pref.apply()

                (pContext as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()
            }

            viewHolder.posterprofileImage.setOnClickListener {
                val pref = pContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                pref.putString("profileId", post.getPublisher())
                pref.apply()

                (pContext as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()
            }

            viewHolder.postSettings.setOnClickListener() {
                settingAlertDialogTypeTwo(post.getPostId(), mPost[position].getPostText())
            }
            //settings Dialog Box

           /* viewHolder.postSettings.setOnClickListener() {
                myDialog = Dialog(pContext)
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                myDialog.setContentView(R.layout.post_settings)
                myDialog.setTitle("hello")

                myDialog.show()

            }*/

            viewHolder.postTypeTwoText.text = post.getPostTypeTwoText()

            /* Glide.with(pContext).load(post!!.getPostImage())
                 .placeholder(R.drawable.add_profile_image).into(holder.postImage)*/
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

                    /*val intent = Intent(pContext, MainActivity::class.java)
                    pContext.startActivity(intent)*/

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

                    addNotificationSaved(post.getPublisher(), post.getPostId())


                }else{

                    FirebaseDatabase.getInstance().reference.child("Saves")
                        .child(firebaseUser!!.uid)
                        .child(post.getPostId())
                        .removeValue()
                }

            }


            }







    }

    private fun startActivity(editpostIntent: Intent) {

    }
//End of viewHolders <------------------------------------------------------------------------------------------------



    override fun getItemCount(): Int {

        return mPost.size

    }

    override fun getItemViewType(position: Int): Int {
        return if (mPost[position].getPostTypeTwoText().isEmpty())  textPost else typeTwo_textPost

    }


     class TypeTwoTextHolder (@NonNull itemView: View): RecyclerView.ViewHolder(itemView){

         var posterprofileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
         var likeButton: ImageView = itemView.findViewById(R.id.post_image_like_btn)
         var commentButton: ImageView = itemView.findViewById(R.id.post_image_comment_btn)
         var saveButton: ImageView = itemView.findViewById(R.id.post_save_comment_btn)
         var userName: TextView = itemView.findViewById(R.id.user_name_search)
         var likes: TextView = itemView.findViewById(R.id.likes)
         var publisher: TextView = itemView.findViewById(R.id.publisher)
         var comments: TextView = itemView.findViewById(R.id.comment_counter)
         var postSettings: ImageView = itemView.findViewById(R.id.text_post_settings)
         //var oldText: TextView = itemView.findViewById(R.id.show_old_text)


         var postTypeTwoText: TextView = itemView.findViewById(R.id.post_TT_text_home)



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
                    publisher. setText(user!!.getFullName())



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

    private fun addNotificationSaved(userId:  String, postId: String){

        val notiRef = FirebaseDatabase.getInstance().reference
            .child("Notifications")
            .child(userId)

        val notiMap = HashMap<String, Any>()
        notiMap["userid"] = firebaseUser!!.uid
        notiMap["text"] = "saved your post!"
        notiMap["postId"] = postId
        notiMap["ispost"] = true

        notiRef.push().setValue(notiMap)


    }

     private fun settingAlertDialog(post: String, getPostText: String){

             settingDialog = Dialog(pContext)
             settingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
             settingDialog.setContentView(R.layout.post_settings)
             settingDialog.setTitle("hello")

             EditPostTxtBtn = settingDialog.findViewById(R.id.edit_post_setting) as TextView
             EditPostTxtBtn.isEnabled = true

             DelPostBtn = settingDialog.findViewById(R.id.delete_post_setting) as TextView
             DelPostBtn.isEnabled = true
             settingDialog.show()

             EditPostTxtBtn.setOnClickListener {
                 bottomSheetFragment.setContentView(view)


                 bottomSheetFragment.show()
                 if (bottomSheetFragment.isShowing) {

                     settingDialog.dismiss()
                     bottomSheetFragment.update_editTxt.setText(getPostText)

                 }

                 bottomSheetFragment.update_post_button.setOnClickListener {

                     val ref = FirebaseDatabase.getInstance().reference.child("Post")
                         .child(post)


                     val postMap = HashMap<String, Any>()
                     postMap["postid"] = post
                     postMap["posttext"] = bottomSheetFragment.update_editTxt.text.toString()
                     postMap["publisher"] = firebaseUser!!.uid

                     ref.setValue(postMap)










                     bottomSheetFragment.dismiss()

                 }

             }

             DelPostBtn.setOnClickListener {

                 val ref = FirebaseDatabase.getInstance().reference.child("Post")
                     .child(post)
                     .removeValue()

                 settingDialog.dismiss()



             }





     }


     private fun settingAlertDialogTypeTwo(post: String, getPostTypeTwoText: String){

         settingDialog = Dialog(pContext)
         settingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
         settingDialog.setContentView(R.layout.post_settings)
         settingDialog.setTitle("hello")

         EditPostTxtBtn = settingDialog.findViewById(R.id.edit_post_setting) as TextView
         EditPostTxtBtn.isEnabled = true

         DelPostBtn = settingDialog.findViewById(R.id.delete_post_setting) as TextView
         DelPostBtn.isEnabled = true
         settingDialog.show()

         EditPostTxtBtn.setOnClickListener {

             bottomSheetFragment.setContentView(view)

             bottomSheetFragment.show()
             if (bottomSheetFragment.isShowing) {

                 settingDialog.dismiss()
                 bottomSheetFragment.update_editTxt.setText(getPostTypeTwoText)

             }

             bottomSheetFragment.update_post_button.setOnClickListener {

                 val ref = FirebaseDatabase.getInstance().reference.child("Post")
                     .child(post)


                 val postMap = HashMap<String, Any>()
                 postMap["postid"] = post
                 postMap["posttypetwotext"] = bottomSheetFragment.update_editTxt.text.toString()
                 postMap["publisher"] = firebaseUser!!.uid

                 ref.setValue(postMap)

                 bottomSheetFragment.dismiss()

             }

         }

         DelPostBtn.setOnClickListener {

             val ref = FirebaseDatabase.getInstance().reference.child("Post")
                 .child(post)
                 .removeValue()

             settingDialog.dismiss()



         }


     }




 }

