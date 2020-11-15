 package supgro.com.Controller.Service

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.view.get
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
import kotlinx.android.synthetic.main.register_activity_layout.*
import kotlinx.android.synthetic.main.user_item_layout.*
import supgro.com.Controller.Model.Post
import supgro.com.Controller.Model.User
import supgro.com.R

class AccountSetting_Activity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    private var checker = ""
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null
    var userList: List<User>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setting_activity)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")


     /*   val strugglesList = arrayListOf<String>( "Struggle", "self-harm", "Depression", "Adhd", "Bipolar Disorder", "(ED)Eating Disorder", "(PTSD)Post Tramatic Stress Disorder", "(ACE)Adverse childhood experiences", "Stress")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strugglesList )

        struggles_spinner.adapter = arrayAdapter*/

        val user =


       /* goAnonymousSw.setOnCheckedChangeListener { compoundButton, onSwitch ->
            if (onSwitch){
            }

        }*/


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

       /*struggles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener  {

            override fun onNothingSelected(p0: AdapterView<*>?) {

               // spinner_results.text = "Struggle"


            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {


             *//*   spinner_results.text = null
                spinner_results.text = strugglesList.get(position)
*//*



            }
        }*/



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

       //I dont want this in there but good to know
        if (fullname_account_setting_txt.text.toString().equals(null)|| username_account_setting_txt.equals(null)){

            Toast.makeText(this, "please write fullname first", Toast.LENGTH_SHORT)
                .show()
        }


            val userMap = HashMap<String, Any>()
        userMap["username"] = username_account_setting_txt.text.toString().toLowerCase()
        userMap["about"] = about_account_setting_txt.text.toString().toLowerCase()
        userMap["fullname"] = fullname_account_setting_txt.text.toString().toLowerCase()
       //serMap["struggle"] = struggles_spinner.selectedItem.toString()






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

                    username_account_setting_txt.setText(user.getUsername())
                    fullname_account_setting_txt.setText(user.getFullName())
                    about_account_setting_txt.setText(user.getAbout())

                    /*struggles_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                            // struggles_spinner[position]
                            //struggles_spinner.selectedItem.toString()

                        }


                    }*/

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
                      //rebaseAuth.getInstance().currentUser!!.uid
                      val userMap = HashMap<String, Any>()
                      userMap["username"] = username_account_setting_txt.text.toString().toLowerCase()
                      userMap["about"] = about_account_setting_txt.text.toString().toLowerCase()
                      //userMap["fullname"] = fullname_account_setting_txt.text.toString().toLowerCase()
                     //serMap["struggle"] =
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

private fun Any.toString(struggle: String) {

}
