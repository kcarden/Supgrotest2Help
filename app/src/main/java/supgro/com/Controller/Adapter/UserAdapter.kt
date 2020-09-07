package supgro.com.Controller.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import supgro.com.Controller.Fragments.ProfileFragment
import supgro.com.Controller.MainActivity
import supgro.com.Controller.Model.User
import supgro.com.R
import supgro.com.R.id.support_btn_search_layout

class UserAdapter (var mContext: Context,
                   var mUser: List<User>,
                   var isFragment: Boolean = false):
                        RecyclerView.Adapter<UserAdapter.Holder>(){

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return Holder(view)

            }

    override fun onBindViewHolder(holder: UserAdapter.Holder, position: Int) {
        val user = mUser[position]

        //Picasso.get().load(user!!.getImage()).placeholder(R.drawable.add_profile_image).resize(50, 50).into(holder.userProfileImage)
        Glide.with(mContext)
            .load(user!!.getImage())
            .placeholder(R.drawable.add_profile_image)
            .into(holder.userProfileImage)
        holder.username.text = user.getUsername()
        holder.fullname.text = user.getFullName()



        checkSupportingStatus(user.getUid(), holder.supportbtn)

        //this is when user clicks anywhere on users found in search, it will go to their page
        holder.itemView.setOnClickListener(View.OnClickListener{
       if (isFragment){
               val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
               pref.putString("profileId", user.getUid())
               pref.apply()

               (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                   .replace(R.id.fragment_container, ProfileFragment()).commit()


      }else{
           val intent = Intent(mContext, MainActivity::class.java)
           intent.putExtra("publisherId", user.getUid())
           mContext.startActivity(intent)
       }

       })
        
        holder.supportbtn.setOnClickListener{
            if (holder.supportbtn.text.toString() == "Support"){

                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Support").child(it1.toString())
                        .child("Supporting").child(user.getUid())
                        .setValue(true).addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                firebaseUser?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Support").child(user.getUid())
                                        .child("Supporters").child(it1.toString())
                                        .setValue(true).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

                                            }
                                        }

                                }
                            }
                        }

                    addNotification(user.getUid())
                }

            }else{

                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Support").child(it1.toString())
                        .child("Supporting").child(user.getUid())
                        .removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                firebaseUser?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Support").child(user.getUid())
                                        .child("Supporters").child(it1.toString())
                                        .removeValue().addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

                                            }
                                        }

                                }
                            }
                        }
                }
            }
        }

    }



    override fun getItemCount(): Int {
         return mUser.size
    }




    inner class Holder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

       var username: TextView = itemView.findViewById(R.id.user_name_search_layout)
        var fullname:TextView = itemView.findViewById(R.id.user_fullname_search_layout)
       var supportbtn: TextView = itemView.findViewById(support_btn_search_layout)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_searchuser_name_search_layout)


    }


    private fun checkSupportingStatus(uid: String, supportbtn: TextView) {
        val supportingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Support").child(it1.toString())
                .child("Supporting")
        }

        supportingRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.child(uid).exists()){

                    supportbtn.text = "Supporting"
                }
                else{
                    supportbtn.text = "Support"

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun addNotification(userId:  String){

        val notiRef = FirebaseDatabase.getInstance().reference
            .child("Notifications")
            .child(userId)

        val notiMap = HashMap<String, Any>()
        notiMap["userid"] = firebaseUser!!.uid
        notiMap["text"] = "started supporting you"
        notiMap["postId"] = ""
        notiMap["ispost"] = false

        notiRef.push().setValue(notiMap)


    }

}