package com.supgro.Controller.Service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_text_post.*

import com.supgro.R

class AddTextPostActivity : AppCompatActivity() {


    //text_post items
    //private var storageTextRef: StorageReference? = null
    private var firebaseUser: FirebaseUser? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_text_post)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        //save_post_Btn.setOnClickListener { uploadImage() }

    /*   post_text_preview_Btn.setOnClickListener {

            type_to_textpost.text.toString()

            type_to_textpost.setText(type_to_textpost.text)

        }*/


        add_post_btn.setOnClickListener {
            textPost()

        }




    }


   /* fun textPost(){



        *//*val textref = FirebaseDatabase.getInstance().reference.child("Text Post")
        val postId = textref.push().key

        val textpostMap = HashMap<String, Any>()


        textpostMap["postid"] = postId!!
        textpostMap["posttext"] = type_to_textpost.text.toString()
        textpostMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid


        textref.child(postId).updateChildren(textpostMap)*//*





        Toast.makeText(this, "Post loaded successfully", Toast.LENGTH_SHORT)
            .show()

        //after post is done it takes user to the page where they can see it.
        val intent = Intent(this@AddTextPostActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }*/

    fun textPost() {



              /*  val ref = FirebaseDatabase.getInstance().reference.child("Text Post")
                val postId = ref.push().key

                val textpostMap = HashMap<String, Any>()
                textpostMap["postid"] = postId!!
                textpostMap["posttext"] = type_to_textpost.text.toString()
                textpostMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

                ref.child(postId).updateChildren(textpostMap)
                Toast.makeText(this, "Post loaded successfully", Toast.LENGTH_SHORT)
                    .show()*/


        val ref = FirebaseDatabase.getInstance().reference.child("Post")
        val postId = ref.push().key + "_Text Post"

        val postMap = HashMap<String, Any>()
        postMap["postid"] = postId
        postMap["posttext"] = type_to_textpost.text.toString()
        postMap["publisher"] = firebaseUser!!.uid

        ref.child(postId).setValue(postMap)

        Toast.makeText(this, "Post loaded successfully", Toast.LENGTH_SHORT)
            .show()

        //after post is done it takes user to the page where they can see it.
        val intent = Intent(this@AddTextPostActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()




}

}