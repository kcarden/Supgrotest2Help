package supgro.com.Controller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.account_setting_activity.*
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_add_text_post.*
import supgro.com.R

class AddTextPostActivity : AppCompatActivity() {


    //text_post items
    //private var storageTextRef: StorageReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_text_post)


        //save_post_Btn.setOnClickListener { uploadImage() }

        /*post_text_preview_Btn.setOnClickListener {

            type_to_textpost.text.toString()

            add_text_post.setText(type_to_textpost.text)

        }

        add_post_btn.setOnClickListener {
            AddToPostActivity()*/
        add_post_btn.setOnClickListener {
            textPost()

        }


    }


    fun textPost(){

       /* .addOnCompleteListener (OnCompleteListener<Uri>{ task ->

            if (task.isSuccessful){
                val downloadUrl = task.result
                myUrl = downloadUrl.toString()*/

        val textref = FirebaseDatabase.getInstance().reference.child("Text Post")
        val postId = textref.push().key

        val textpostMap = HashMap<String, Any>()
        textpostMap["postid"] = postId!!
        textpostMap["posttext"] = type_to_textpost.text.toString()
        textpostMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

        textref.child(postId).updateChildren(textpostMap)


        Toast.makeText(this, "Post loaded successfully", Toast.LENGTH_SHORT)
            .show()

        //after post is done it takes user to the page where they can see it.
        val intent = Intent(this@AddTextPostActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

            /*}else{

            }

        })*/
    }

    /*fun textPost() {

        .addOnCompleteListener(OnCompleteListener<Uri> { task ->


            if (task.isSuccessful) {


                val ref = FirebaseDatabase.getInstance().reference.child("Text Post")
                val postId = ref.push().key

                val textpostMap = HashMap<String, Any>()
                textpostMap["postid"] = postId!!
                textpostMap["posttext"] = type_to_textpost.text.toString()
                textpostMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

                ref.child(postId).updateChildren(textpostMap)
                Toast.makeText(this, "Post loaded successfully", Toast.LENGTH_SHORT)
                    .show()


            } else {

            }

        })

} */

}