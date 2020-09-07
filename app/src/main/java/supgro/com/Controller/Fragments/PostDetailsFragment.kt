package supgro.com.Controller.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import supgro.com.Controller.Adapter.PostAdapter
import supgro.com.Controller.Model.Post
import supgro.com.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailsFragment : Fragment() {

    private var postAdapter: PostAdapter? = null
    private var postList: MutableList<Post>? = null
    private var postId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_post_details, container, false)

        val preference = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (preference != null){

            //may not work due to .toString or !!
            postId = preference.getString("postid", "none")!!
        }

        var recyclerView: RecyclerView
        recyclerView = view.findViewById(R.id.recycler_View_post_details)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        postAdapter = context?.let { PostAdapter(it, postList as ArrayList<Post>) }
        recyclerView.adapter = postAdapter

       retrieveImagePost()
        //retrieveTextPost()
        return view
    }

    private fun retrieveImagePost() {
        val postRef = FirebaseDatabase.getInstance().reference
            .child("Posts")
            .child(postId)

        postRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                postList?.clear()

                val post = p0.getValue(Post::class.java)

                postList!!.add(post!!)

                postAdapter!!.notifyDataSetChanged()




            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    /*private fun retrieveTextPost() {
        val postRef = FirebaseDatabase.getInstance().reference
            .child("Text Post")
            .child(postId)

        postRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                postList?.clear()

                val post = p0.getValue(Post::class.java)

                postList!!.add(post!!)

                postAdapter!!.notifyDataSetChanged()




            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }*/

    }
