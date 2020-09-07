package supgro.com.Controller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.account_setting_activity.*
import supgro.com.Controller.Model.User
import supgro.com.R

class AccountSetting_Activity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    private var checker = ""
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setting_activity)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")



        //signing out of your account
        logout_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, Login_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


        change_image_txt_btn.setOnClickListener{
            checker = "clicked"
            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(this)
        }

        save_info_profile_Btn.setOnClickListener {

            if (checker == "clicked"){

                uploadImageAndUpdateInfo()

            }else{

                updateUserInfoOnly()

            }



        }


        userInfo()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            add_account_setting_image.setImageURI(imageUri)
        }

    }

    private fun updateUserInfoOnly() {
        val userRef = FirebaseDatabase.getInstance().getReference()
            .child("username")

        /*//I dont want this in there but good to know
        if (fullname_account_setting_txt.text.toString()== null){

            Toast.makeText(this, "please write fullname first", Toast.LENGTH_SHORT)
                .show()
        }*/

        val userMap = HashMap<String, Any>()
        userMap["username"] = username_account_setting_txt.text.toString().toLowerCase()
        userMap["about"] = about_account_setting_txt.text.toString().toLowerCase()
        userMap["fullname"] = fullname_account_setting_txt.text.toString().toLowerCase()
        /*userMap["gender"] = genderRegEdTxt.text.toString().toLowerCase()
        userMap["age"] = ageRegEdTxt.text.toString()
        userMap["struggle"] = strugglesRegEdTxt.text.toString().toLowerCase()*/

        userRef.child(firebaseUser.uid).updateChildren(userMap)

        Toast.makeText(this, "Account information has been updated successfully", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

    private fun userInfo(){

        val userRef = FirebaseDatabase.getInstance().reference
            .child("username")
            .child(firebaseUser.uid)

        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {



                if (p0.exists()){

                    val user = p0.getValue<User>(User::class.java)

                    //Picasso.get().load(user!!.getImage()).placeholder(R.drawable.add_profile_image).into(add_account_setting_image);
                    Glide.with(this@AccountSetting_Activity).load(user!!.getImage()).placeholder(R.drawable.add_profile_image).into(add_account_setting_image)

                    username_account_setting_txt.setText(user!!.getUsername())
                    fullname_account_setting_txt.setText(user!!.getFullName())
                    about_account_setting_txt.setText(user!!.getAbout())
                    //website_account_setting_txt.linksClickable = user.


                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun uploadImageAndUpdateInfo() {

      /*  //work on progress bar
        val progressDialog = ProgressBar(this)
        progressDialog.setTitle("Account Settings")*/


        val fileRef = storageProfilePicRef!!.child(firebaseUser!!.uid + "jpg")

        var uploadTask: StorageTask<*>
        uploadTask = fileRef.putFile(imageUri!!)


              uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful){

                    task.exception?.let {
                        throw  it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener (OnCompleteListener<Uri>{ task ->

                  if (task.isSuccessful){
                      val downloadUrl = task.result
                      myUrl = downloadUrl.toString()

                      val ref = FirebaseDatabase.getInstance().reference.child("username")

                      val userMap = HashMap<String, Any>()
                      userMap["username"] = username_account_setting_txt.text.toString().toLowerCase()
                      userMap["about"] = about_account_setting_txt.text.toString().toLowerCase()
                      userMap["fullname"] = fullname_account_setting_txt.text.toString().toLowerCase()
                      userMap["image"] = myUrl

                      ref.child(firebaseUser.uid).updateChildren(userMap)

                      Toast.makeText(this, "Account information has been updated successfully", Toast.LENGTH_SHORT)
                          .show()

                      val intent = Intent(this@AccountSetting_Activity, MainActivity::class.java)
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                      startActivity(intent)
                      finish()


                  }else{
                  }



            })

    }
}