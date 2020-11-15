package supgro.com.Controller.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import supgro.com.Controller.Fragments.PostDetailsFragment
import supgro.com.Controller.Fragments.ProfileFragment
import supgro.com.Controller.Model.Notification
import supgro.com.Controller.Model.Post
import supgro.com.Controller.Model.User
import supgro.com.R
import kotlin.coroutines.coroutineContext

class NotificationAdapter(val mContext: Context, val mNotification: List<Notification>): RecyclerView.Adapter<NotificationAdapter.Holder>() {

    private val onNavigationView: Int = R.id.nav_notifications
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.notifications_item_layout, parent, false)
        return Holder(view)

    }



    override fun onBindViewHolder(holder: Holder, position: Int) {


        val notification = mNotification[position]


        if (notification.getText().equals("started supporting you")){

            holder.commentText.text = "started supporting you"

            holder.newIcon.visibility = View.VISIBLE

        }else if (notification.getText().equals("liked your post")){

            holder.commentText.text = "liked your post"

        }else if (notification.getText().contains("commented:")){

            holder.commentText.text = notification.getText().replace("commented:", "commented: ")

        }else if(notification.getText().equals("saved your post")){

            holder.commentText.text = "saved your post!"
        }else{

            holder.commentText.text = notification.getText()

            holder.newIcon.visibility = View.GONE

        }



       userInfo(holder.profileImage, holder.userName, notification.getUserId())

        if (notification.isIsPost())
        {
            holder.postImage.visibility = View.VISIBLE
            getPostImage(holder.postImage, notification.getPostid())
        }
        else
        {
            holder.postImage.visibility = View.GONE
        }

       holder.itemView.setOnClickListener{
           if (notification.isIsPost()){

                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("postId", notification.getPostid())

                editor.apply()

                (mContext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PostDetailsFragment()).commit()
            }else{

                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()

                editor.putString("profileId", notification.getUserId())

                editor.apply()

                (mContext as FragmentActivity).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()

            }
        }


    }

    override fun getItemCount(): Int {
        return mNotification.size

    }

    inner class Holder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){

        var profileImage: CircleImageView = itemView.findViewById(R.id.notification_profile_image)
        var postImage: ImageView = itemView.findViewById(R.id.notification_post_image)
        var userName: TextView = itemView.findViewById(R.id.username_notification)
        var commentText: TextView = itemView.findViewById(R.id.comment_notification)
        var newIcon: ImageView = itemView.findViewById(R.id.new_notifications_icon)




    }

    private fun userInfo(imageView: ImageView, userName: TextView, publisherId: String){

        val userRef = FirebaseDatabase.getInstance().getReference()
            .child("username")
            .child(publisherId)
        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {



                if (p0.exists()){
                    val user = p0.getValue<User>(User::class.java)

                    //Picasso.get().load(user!!.getImage()).placeholder(R.drawable.add_profile_image).resize(200, 200).into(add_profile_image)
                    Glide.with(mContext).load(user!!.getImage()).placeholder(R.drawable.add_profile_image).into(imageView)
                    userName.text = user!!.getUsername()

                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getPostImage(imageView: ImageView, postID: String){

        val postRef = FirebaseDatabase.getInstance().reference
            .child("Posts")
            .child(postID)

        postRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {



                if (p0.exists()){

                    val post = p0.getValue<Post>(Post::class.java)

                    Glide.with(mContext).load(post!!.getPostImage()).placeholder(R.drawable.add_profile_image).into(imageView)

                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}