package supgro.com.Controller.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import supgro.com.Controller.Adapter.PostAdapter
import supgro.com.Controller.Model.Post
import supgro.com.R



class HomeFragment : Fragment() {

    private var postAdapter: PostAdapter? = null
    private var postList: MutableList<Post>? = null
    private var supportingList: MutableList<Post>? = null





           override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_home, fragment_container, false)

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

                        if (post!!.getPublisher() == id.toString()){

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
