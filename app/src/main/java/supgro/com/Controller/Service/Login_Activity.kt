package supgro.com.Controller.Service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity_layout.*
import supgro.com.R

class Login_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_layout)

        //FORGOT PASSWORD BUTTON
        forgotPwBtn.setOnClickListener {
           /* val passwordresetIntent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(passwordresetIntent)*/
                    }

    //BUTTON TO GO TO REGITRATION PAGE***
        CreateAccountBtn.setOnClickListener {
            val registerPageIntent = Intent(this, Register_Activity::class.java)
            startActivity(registerPageIntent)
        }

        //LOGIN BUTTON***
        loginBtn.setOnClickListener {
            signin()
        }






    }

    private fun signin(){
        val email = usernameLoginEdTxt.text.toString().trim()
        val password = passwordLoginEdTxt.text.toString().trim()



        if (email.isEmpty() || password.isEmpty() ){
            Toast.makeText(this, "Please input correct information", Toast.LENGTH_SHORT).show()
            return

            /*}else if (email!= null){
                Toast.makeText(this, "Incorrect EMAIL or PASSWORD, Please try again", Toast.LENGTH_LONG).show()
                return
            }else if (password != null){
                Toast.makeText(this, "Incorrect EMAIL or PASSWORD, Please try again", Toast.LENGTH_LONG).show()
                return*/
        }else {
            Toast.makeText(this, "You have successfully Logged in", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful)
                    return@addOnCompleteListener

                Log.d("SignupActivity", "Successfully created user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT ).show()

            }



    }

   override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser !=null){
            val intent = Intent(this@Login_Activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }
}
