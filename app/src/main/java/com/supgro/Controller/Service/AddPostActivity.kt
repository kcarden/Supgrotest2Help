package com.supgro.Controller.Service

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.StorageReference
import com.supgro.R

class AddPostActivity : AppCompatActivity() {
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null

    //text_post items
    //private var storageTextRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

      /*  storagePostPicRef = FirebaseStorage.getInstance().reference.child("Posted Pictures")

        //save_post_Btn.setOnClickListener { uploadImage() }

        save_new_post_Btn.setOnClickListener {
            uploadImage()
            *//*if (caption_post.text.toString() != ""){
               save_new_post_Btn.setEnabled(false)

           }*//*
        }

        *//* add_post_btn.setOnClickListener {
            textPost()

        }*//*


        CropImage.activity()
            .setFixAspectRatio(true)
            .start(this)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            add_image_post.setImageURI(imageUri)
        } else {
            //maybe you can't post because of image size or something else
        }
    }



    private fun uploadImage() {

        when {

            imageUri == null -> Toast.makeText(
                this,
                "Please select image first.",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(caption_post.text.toString()) -> Toast.makeText(
                this,
                "Please write caption",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                //progress bar goes here!

                val fileRef =
                    storagePostPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {

                        task.exception?.let {
                            throw  it
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                })
                    .addOnCompleteListener(OnCompleteListener<Uri> { task ->

                        if (task.isSuccessful) {
                            val downloadUrl = task.result
                            myUrl = downloadUrl.toString()

                            val ref = FirebaseDatabase.getInstance().reference.child("Post")
                            val postId = ref. push().key + "_Image_Post"

                            val postMap = HashMap<String, Any>()
                            postMap["postid"] = postId!!
                            postMap["caption"] = caption_post.text.toString()
                            postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                            postMap["postimage"] = myUrl

                            ref.child(postId).updateChildren(postMap)

                            Toast.makeText(this, "Post loaded successfully", Toast.LENGTH_SHORT)
                                .show()

                            //after post is done it takes user to the page where they can see it.
                            val intent = Intent(this@AddPostActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()


                        } else {


                        }

                    })


            }
        }

*/
    }





}