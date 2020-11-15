package supgro.com.Controller.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_post_selection.view.*
import supgro.com.Controller.Service.AddPostActivity
import supgro.com.Controller.Service.AddTextPostActivity
import supgro.com.Controller.Service.AddTypeTwoTextPostActivity
import supgro.com.R



class PostSelectionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_selection, container, false)

//Convert to different text post

      /* view.image_btn_add_post.setOnClickListener {
           val imageIntent = Intent(context, AddPostActivity::class.java)
           startActivity(imageIntent)

       }*/

        view.text_btn_add_post.setOnClickListener {
            val textIntent = Intent(context, AddTextPostActivity::class.java)
            startActivity(textIntent)

        }

        view.text_btn_add_secondType_post.setOnClickListener {
            val textIntent = Intent(context, AddTypeTwoTextPostActivity::class.java)
            startActivity(textIntent)

        }

        return view
    }


}