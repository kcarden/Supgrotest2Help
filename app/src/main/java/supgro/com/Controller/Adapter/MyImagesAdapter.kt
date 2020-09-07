package supgro.com.Controller.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import supgro.com.Controller.Fragments.PostDetailsFragment
import supgro.com.Controller.Model.Post
import supgro.com.R

class MyImagesAdapter(val mContext: Context, mPost: List<Post>):RecyclerView.Adapter<MyImagesAdapter.Holder>() {

   private var mPost: List<Post>? = null
    private var firebaseUser: FirebaseUser? = null

    init {
        this.mPost = mPost
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.images_item_layout, parent, false)
        return Holder(view)    }



    override fun onBindViewHolder(holder: Holder, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser

        val post:  Post = mPost!![position]

        Glide.with(mContext).load(post!!.getPostImage()).placeholder(R.drawable.add_profile_image).into(holder.postImage)

       //adding this, when the image is clicked it opens bigger post
        holder.postImage.setOnClickListener {
            val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("postId", post.getPostId())

            editor.apply()

            (mContext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PostDetailsFragment()).commit()
        }


    }

    override fun getItemCount(): Int {
        return mPost!!.size

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var postImage: ImageView = itemView.findViewById(R.id.post_Image)

    }
}