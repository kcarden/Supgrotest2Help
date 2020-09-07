package supgro.com.Controller.Service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_text_post.*
import supgro.com.Controller.MainActivity
import supgro.com.R

class AddTextPostActivitytest : AppCompatActivity() {

    /* var textUri: Uri? = null
    var myUrl = ""*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_text_post)




        post_text_preview_Btn.setOnClickListener {

            type_to_textpost.text.toString()

            add_text_post.setText(type_to_textpost.text)

        }

        add_post_btn.setOnClickListener {
            AddToPostActivity()







        }


    }

    private fun AddToPostActivity() {



/*when {
            add_text_post == null -> Toast.makeText(this, "example field is empty ", Toast.LENGTH_LONG).show()
            type_to_textpost == null -> Toast.makeText(this, "example field is empty ", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(type_to_textpost.text.toString()) -> Toast.makeText(this, "Please type text first ", Toast.LENGTH_LONG).show()


            else -> {




                        if(task.isSuccessful){*/


                            val ref = FirebaseDatabase.getInstance().reference.child("Text Post")
                            val postId = ref.push().key

                            val postMap = HashMap<String, Any>()
                            postMap["postid"] = postId!!
                            //postMap["caption"] = caption_post.text.toString().toLowerCase()
                            postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                            postMap["posttext"] = type_to_textpost.text.toString()

                            ref.child(postId).updateChildren(postMap)

                            Toast.makeText(this, "Post loaded sucessfully ", Toast.LENGTH_LONG).show()

                            val intent = Intent (this@AddTextPostActivitytest, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()

                        /*}else {}*/





     }
}