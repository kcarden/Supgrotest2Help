  package com.supgro.Controller.Adapter

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.*
import android.widget.*
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.edit_post_bottomsheet_fragment.*
import com.supgro.Controller.Fragments.ProfileFragment
import com.supgro.Controller.Service.CommentsActivity
import com.supgro.Controller.Model.Post
import com.supgro.Controller.Model.User
import com.supgro.Controller.Service.ShowUsersActivity
import com.supgro.R
import java.util.Date.from

  class PostAdapter (private val pContext: Context, val mPost: List<Post>): RecyclerView.Adapter <RecyclerView.ViewHolder>() {

     private var firebaseUser: FirebaseUser? = null
     private val typeTwo_textPost = 1
     private val textPost = 2
     internal lateinit var cancelBtn: Button
     internal lateinit var settingDialog: Dialog
     private lateinit var profileId: String
     internal lateinit var EditPostTxtBtn: TextView
     internal lateinit var DelPostBtn: TextView

      //notification channel
      val CHANNEL_ID = "channelID"
      val CHANNEL_NAME = "channelName"
      val NOTIFICATION_ID = 0


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


         } else
         {
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
         if (pref != null) {


             this.profileId = pref.getString("profileId", "none")!!
         }





         val post = mPost[position]







         if (holder.itemViewType == textPost) {
             //Text ViewHolder <----------------------------------------------------------------------------------------------------------------------


             val viewHolder = holder as TextHolder

           /*  val now = LocalDateTime.now()
             var formatter = DateTimeFormatter.ofPattern("h:mm")


             holder.dateandtime.text = formatter.format(now)*/



             createNotificationChannel()
             val notification = NotificationCompat.Builder(pContext, CHANNEL_ID)
                 .setContentTitle("saved Post")
                 .setSmallIcon(R.drawable.circle_supgro_logo)
                 .setPriority(NotificationCompat.PRIORITY_HIGH)
                 .build()

             val notificationManager = NotificationManagerCompat.from(pContext)

             //val noti = notificationManager.notify(NOTIFICATION_ID, notification)



             viewHolder.userName.setOnClickListener {
                 val pref = pContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                 pref.putString("profileId", post.publisher)
                 pref.apply()

                 (pContext as FragmentActivity).supportFragmentManager.beginTransaction()
                     .replace(R.id.fragment_container, ProfileFragment()).commit()
             }

             viewHolder.posterprofileImage.setOnClickListener {
                 val pref = pContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                 pref.putString("profileId", post.publisher)
                 pref.apply()

                 (pContext as FragmentActivity).supportFragmentManager.beginTransaction()
                     .replace(R.id.fragment_container, ProfileFragment()).commit()
             }

             if (post.publisher == firebaseUser!!.uid) {
                 holder.postSettings.visibility = View.VISIBLE

             } else if (post.publisher != firebaseUser!!.uid) {
                 holder.postSettings.visibility = View.GONE


             }
             viewHolder.postSettings.setOnClickListener() {
                 settingAlertDialog(post.postid, mPost[position].posttext)
             }

             holder.postText.text = post.posttext


             //viewHolder.postText.setText(post.posttext)

             publisherInfo(
                 holder.posterprofileImage,
                 holder.userName,
                 holder.publisher,
                 post.publisher
             )
             getTotalNumberOfComments(holder.comments, post.postid)

             isLike(post.postid, holder.likeButton)
             numberoflikes(holder.likes, post.postid)
             checkSavedStatus(post.postid, holder.saveButton)


             //setting up the like and unlike buttonw
             holder.likeButton.setOnClickListener {
                 if (holder.likeButton.tag == "Like") {
                     FirebaseDatabase.getInstance().reference.child("Likes")
                         .child(post.postid)
                         .child(firebaseUser!!.uid)
                         .setValue(true)

                     addNotification(post.publisher, post.postid)

                 } else {
                     FirebaseDatabase.getInstance().reference.child("Likes")
                         .child(post.postid)
                         .child(firebaseUser!!.uid)
                         .removeValue()

                     /*val intent = Intent(pContext, MainActivity::class.java)
                    pContext.startActivity(intent)*/

                 }
             }

             holder.likes.setOnClickListener {
                 val intent = Intent(pContext, ShowUsersActivity::class.java)
                 intent.putExtra("id", post.postid)
                 intent.putExtra("title", "likes")
                 pContext.startActivity(intent)
             }

             holder.commentButton.setOnClickListener {
                 val intentcomment = Intent(pContext, CommentsActivity::class.java)
                 intentcomment.putExtra("postId", post.postid)
                 intentcomment.putExtra("publisherId", post.publisher)
                 pContext.startActivity(intentcomment)

             }

             holder.comments.setOnClickListener {
                 val intentcomment = Intent(pContext, CommentsActivity::class.java)
                 intentcomment.putExtra("postId", post.postid)
                 intentcomment.putExtra("publisherId", post.publisher)
                 pContext.startActivity(intentcomment)

             }

             holder.saveButton.setOnClickListener {


                 if (holder.saveButton.tag == "Save" ) {
                     FirebaseDatabase.getInstance().reference.child("Saves")
                         .child(firebaseUser!!.uid)
                         .child(post.postid)
                         .setValue(true)

                     addNotificationSaved(post.publisher, post.postid)
                     notificationManager.notify(NOTIFICATION_ID, notification)


                 } else {

                     FirebaseDatabase.getInstance().reference.child("Saves")
                         .child(firebaseUser!!.uid)
                         .child(post.postid)
                         .removeValue()
                 }






             }
//image ViewHolder <---------------------------------------------------------------------------------------------------
         } else {
             val viewHolder = holder as TypeTwoTextHolder

             /*holder.changecolor.setOnClickListener {

                 holder.postbackground.setBackgroundColor(R.drawable.bombardier_bg)


             }*/

             viewHolder.userName.setOnClickListener {
                 val pref = pContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                 pref.putString("profileId", post.publisher)
                 pref.apply()

                 (pContext as FragmentActivity).supportFragmentManager.beginTransaction()
                     .replace(R.id.fragment_container, ProfileFragment()).commit()
             }

             viewHolder.posterprofileImage.setOnClickListener {
                 val pref = pContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                 pref.putString("profileId", post.publisher)
                 pref.apply()

                 (pContext as FragmentActivity).supportFragmentManager.beginTransaction()
                     .replace(R.id.fragment_container, ProfileFragment()).commit()
             }

             //settings Dialog Box
             if (post.publisher == firebaseUser!!.uid) {
                 holder.postSettings.visibility = View.VISIBLE

             } else if (post.publisher != firebaseUser!!.uid) {
                 holder.postSettings.visibility = View.GONE


             }
             viewHolder.postSettings.setOnClickListener() {
                 settingAlertDialogTypeTwo(post.postid, mPost[position].posttypetwotext)
             }



             viewHolder.postTypeTwoText.text = post.posttypetwotext

             /* Glide.with(pContext).load(post!!.getPostImage())
                 .placeholder(R.drawable.add_profile_image).into(holder.postImage)*/
             publisherInfo(
                 holder.posterprofileImage,
                 holder.userName,
                 holder.publisher,
                 post.publisher
             )
             getTotalNumberOfComments(holder.comments, post.postid)

             isLike(post.postid, holder.likeButton)
             numberoflikes(holder.likes, post.postid)
             checkSavedStatus(post.postid, holder.saveButton)


             //setting up the like and unlike buttonw
             holder.likeButton.setOnClickListener {
                 if (holder.likeButton.tag == "Like") {
                     FirebaseDatabase.getInstance().reference.child("Likes")
                         .child(post.postid)
                         .child(firebaseUser!!.uid)
                         .setValue(true)

                     addNotification(post.publisher, post.postid)

                 } else {
                     FirebaseDatabase.getInstance().reference.child("Likes")
                         .child(post.postid)
                         .child(firebaseUser!!.uid)
                         .removeValue()

                     /*val intent = Intent(pContext, MainActivity::class.java)
                    pContext.startActivity(intent)*/

                 }
             }

             holder.likes.setOnClickListener {
                 val intent = Intent(pContext, ShowUsersActivity::class.java)
                 intent.putExtra("id", post.postid)
                 intent.putExtra("title", "likes")
                 pContext.startActivity(intent)
             }

             holder.commentButton.setOnClickListener {
                 val intentcomment = Intent(pContext, CommentsActivity::class.java)
                 intentcomment.putExtra("postId", post.postid)
                 intentcomment.putExtra("publisherId", post.postid)
                 pContext.startActivity(intentcomment)

             }

             holder.comments.setOnClickListener {
                 val intentcomment = Intent(pContext, CommentsActivity::class.java)
                 intentcomment.putExtra("postId", post.postid)
                 intentcomment.putExtra("publisherId", post.postid)
                 pContext.startActivity(intentcomment)

             }

             holder.saveButton.setOnClickListener {
                 if (holder.saveButton.tag == "Save") {

                     FirebaseDatabase.getInstance().reference.child("Saves")
                         .child(firebaseUser!!.uid)
                         .child(post.postid)
                         .setValue(true)

                     addNotificationSaved(post.publisher, post.postid)
                   // notificationManager.notify(NOTIFICATION_ID, notification)






                 } else {

                     FirebaseDatabase.getInstance().reference.child("Saves")
                         .child(firebaseUser!!.uid)
                         .child(post.postid)
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
         return if (mPost[position].posttypetwotext.isEmpty()) textPost else typeTwo_textPost

     }


     class TypeTwoTextHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

         var posterprofileImage: CircleImageView =
             itemView.findViewById(R.id.user_profile_image_search)
         var likeButton: ImageView = itemView.findViewById(R.id.post_image_like_btn)
         var commentButton: ImageView = itemView.findViewById(R.id.post_image_comment_btn)
         var saveButton: ImageView = itemView.findViewById(R.id.post_save_comment_btn)
         var userName: TextView = itemView.findViewById(R.id.user_name_search)
         var likes: TextView = itemView.findViewById(R.id.likes)
         var publisher: TextView = itemView.findViewById(R.id.publisher)
         var comments: TextView = itemView.findViewById(R.id.comment_counter)
         var postSettings: ImageView = itemView.findViewById(R.id.text_post_settings)
         //var oldText: TextView = itemView.findViewById(R.id.show_old_text)
//         var dateandtime: TextView = itemView.findViewById(R.id.dateandtime)
         //var changecolor: Button = itemView.findViewById(R.id.change_color_btn)
         //var postbackground: ConstraintLayout = itemView.findViewById(R.id.post_background)



         var postTypeTwoText: TextView = itemView.findViewById(R.id.post_TT_text_home)


     }

     //Text post
     inner class TextHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
         var posterprofileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
         var likeButton: ImageView = itemView.findViewById(R.id.post_image_like_btn)
         var commentButton: ImageView = itemView.findViewById(R.id.post_image_comment_btn)
         var saveButton: ImageView = itemView.findViewById(R.id.post_save_comment_btn)
         var userName: TextView = itemView.findViewById(R.id.user_name_search)
         var likes: TextView = itemView.findViewById(R.id.likes)
         var publisher: TextView = itemView.findViewById(R.id.publisher)
         var comments: TextView = itemView.findViewById(R.id.comment_counter)
         var postSettings: ImageView = itemView.findViewById(R.id.text_post_settings)
        // var dateandtime: TextView = itemView.findViewById(R.id.dateandtime)



         var postText: TextView = itemView.findViewById(R.id.post_text_home)
     }

     private fun publisherInfo(
         profileImage: CircleImageView,
         userName: TextView,
         publisher: TextView,
         publisherID: String
     ) {

         val userRef = FirebaseDatabase.getInstance().reference.child("username").child(publisherID)
         userRef.addValueEventListener(object : ValueEventListener {

             override fun onDataChange(p0: DataSnapshot) {
                 if (p0.exists()) {
                     val user = p0.getValue<User>(User::class.java)

                     //Picasso.get().load(user!!.getImage()).placeholder(R.drawable.add_profile_image).into(profileImage)

                     userName.setText(user!!.username)

                     Glide.with(pContext).load(user!!.image).placeholder(R.drawable.avataphoto)
                         .into(profileImage)
                     publisher.setText(user!!.fullname)


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

         LikesRef.addValueEventListener(object : ValueEventListener {

             override fun onDataChange(p0: DataSnapshot) {

                 if (p0.child(firebaseUser!!.uid).exists()) {

                     likeButton.setImageResource(R.drawable.heart_red)
                     likeButton.tag = "Liked"
                 } else {
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

         LikesRef.addValueEventListener(object : ValueEventListener {

             override fun onDataChange(p0: DataSnapshot) {

                 if (p0.exists()) {
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

         LikesRef.addValueEventListener(object : ValueEventListener {

             override fun onDataChange(p0: DataSnapshot) {

                 if (p0.exists()) {
//will get the total number of likes on the post when clike
                     comments.text = p0.childrenCount.toString() + " comments"


                 } else {

                 }
             }

             override fun onCancelled(p0: DatabaseError) {
                 TODO("Not yet implemented")
             }
         })
     }

     private fun checkSavedStatus(postId: String, imageView: ImageView) {

         val saveRef = FirebaseDatabase.getInstance().reference
             .child("Saves")
             .child(firebaseUser!!.uid)

         saveRef.addValueEventListener(object : ValueEventListener {

             override fun onDataChange(p0: DataSnapshot) {
                 if (p0.child(postId).exists()) {

                     imageView.setImageResource(R.drawable.savedfilled)
                     imageView.tag = "Saved"
                 } else {

                     imageView.setImageResource(R.drawable.saveunfilled)
                     imageView.tag = "Save"


                 }
             }

             override fun onCancelled(p0: DatabaseError) {
             }


         })


     }

     private fun addNotification(userId: String, postId: String) {

         val notiRef = FirebaseDatabase.getInstance().reference
             .child("Notifications")
             .child(userId)

         val notiMap = HashMap<String, Any>()
         notiMap["userid"] = firebaseUser!!.uid
         notiMap["text"] = "liked your post: "
         notiMap["postId"] = "postId"
         notiMap["ispost"] = true

         notiRef.push().setValue(notiMap)


     }

     private fun addNotificationSaved(userId: String, postId: String) {

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





     private fun settingAlertDialog(post: String, getPostText: String) {

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


     private fun settingAlertDialogTypeTwo(post: String, getPostTypeTwoText: String) {

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




     private fun createNotificationChannel(){



          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
              val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                  NotificationManager.IMPORTANCE_DEFAULT).apply {
                  lightColor = Color.GREEN
                  enableLights(true)
              }
              val manager = pContext.getSystemService(Context.NOTIFICATION_SERVICE)  as NotificationManager
              manager.createNotificationChannel(channel)

          }
      }

 }