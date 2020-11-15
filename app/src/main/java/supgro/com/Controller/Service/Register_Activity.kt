package supgro.com.Controller.Service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register_activity_layout.*


class Register_Activity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(supgro.com.R.layout.register_activity_layout)

       /* val customList = listOf("first", "second", "third", "foruth")
        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, customList)
        struggles.adapter = adapter*/



      struggles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener  {

          override fun onNothingSelected(p0: AdapterView<*>?) {

          }

          override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {


              if(struggles.equals("Struggles") ){

                  null


              }else{

                 // val struggletext = struggles.selectedItem.toString()
                  Toast.makeText(this@Register_Activity, "You selected ${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_LONG).show()
              }


          }
      }

        val username = usernameRegEdTxt.text.toString()


        signupRegBtn.setOnClickListener {

            performRegister()


        }


    }


    private fun performRegister() {

        val username = usernameRegEdTxt.text.toString().trim()
        val email = emailRegEdTxt.text.toString().trim()
        val password = passwordRegEdTxt.text.toString().trim()
        val confirmemail = confirmEmailRegEdTxt
        val confirmpassword = comfirmPasswordRegEdTxt.text.toString()
        val fullname = fullnameRegEdTxt.text.toString()
       // val gender = genderRegEdTxt.text.toString().trim()
        val age = ageRegEdTxt.text.toString().trim()
       //val struggle = struggletext.text.toString()
        val struggle = struggles.selectedItem.toString()

        if (email.isEmpty() || password.isEmpty() || age.isEmpty() || fullname.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()

        }else if(!comfirmPasswordRegEdTxt.getText().toString().equals(comfirmPasswordRegEdTxt.getText().toString()) || !confirmEmailRegEdTxt.getText().toString().equals(confirmEmailRegEdTxt.getText().toString()) )
        {
            Toast.makeText(this, "Please make sure email and password match", Toast.LENGTH_SHORT).show()

        }else {
            Toast.makeText(
                baseContext,
                "You have successfully created an account!",
                Toast.LENGTH_SHORT
            ).show()
          /*val intent = Intent(this, MainFeedActivity::class.java)
            startActivity(intent)*/
        }
        Log.d("RegisterActivity", "Email is: $email")
        Log.d("RegisterActivity", "Password is: $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    saveUserToFireBaseDatabase( username, email, age, uid = String())

                //else
                Log.d("RegisterActivity", "Sucessfully creaded uid: ${it.result?.user?.uid}")

            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun saveUserToFireBaseDatabase( username: String, email: String, age: String, uid: String) {

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("username")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["username"] = usernameRegEdTxt.text.toString().toLowerCase()
        userMap["email"] = emailRegEdTxt.text.toString()
        //userMap["gender"] = genderRegEdTxt.text.toString().toLowerCase()
        userMap["age"] = ageRegEdTxt.text.toString()
        userMap["struggle"] = struggles.selectedItem.toString()
       // userMap["About"] = strugglesRegEdTxt.text.toString().toLowerCase()
        userMap["fullname"] = fullnameRegEdTxt.text.toString().toLowerCase()
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/supgro.appspot.com/o/add%20(2).png?alt=media&token=bbe56c23-ae7c-4c8f-bd41-1e875539473e"

        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {

                    /*FirebaseDatabase.getInstance().reference
                        .child("Support").child(currentUserID)
                        .child("Supporting").child(currentUserID)
                        .setValue(true)
*/


                        FirebaseDatabase.getInstance().reference.child("Support").child(currentUserID)
                            .child("Supporting").child(currentUserID)
                            .setValue(true)



                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                }
            }
    }
}



