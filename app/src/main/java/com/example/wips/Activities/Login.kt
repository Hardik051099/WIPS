package com.example.wips.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.wips.Activities.Admin.Buildings
import com.example.wips.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import showCustomToast

class Login : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    lateinit var signup: Button
    lateinit var login :Button
    lateinit var dlogin :Button
    lateinit var hideshowimg: ImageView
    lateinit var passwordtext: EditText
    lateinit var emailtext: EditText

    lateinit var email: String
    lateinit var password: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        signup = findViewById(R.id.Signup)
        login = findViewById(R.id.Login)
        dlogin = findViewById(R.id.demo)
        var hideshow: Boolean = true
        hideshowimg = findViewById(R.id.hideshowimg2)
        passwordtext = findViewById(R.id.pwd_edittext)
        emailtext = findViewById(R.id.email_edittext)

        //Hiding and showing Password
        hideshowimg.setOnClickListener {
            if (hideshow) {
                hideshow = false
                passwordtext.transformationMethod = HideReturnsTransformationMethod.getInstance()
                passwordtext.setSelection(passwordtext.getText().length)
                hideshowimg.alpha = 1F
            } else {
                hideshow = true
                passwordtext.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordtext.setSelection(passwordtext.getText().length)
                hideshowimg.alpha = 0.5F
            }
        }

        dlogin.setOnClickListener {
            startActivity(Intent(this@Login, Buildings::class.java))
        }




        signup.setOnClickListener {
            startActivity(Intent(this@Login, Signup::class.java))
        }
        login.setOnClickListener {
            startActivity(Intent(this@Login, Buildings::class.java))
        }
    }

    fun loginAccount (email:String,password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("Login Email", "signInWithEmail:success")
                    val user = auth.currentUser
                    if (user!!.isEmailVerified())
                    {
                        // user is verified, so you can finish this activity or send user to activity which you want.
                        Toast(this).showCustomToast ("Login successfull",true, this)

                    }
                    else
                    {
                        // email is not verified, so just prompt the message to the user and restart this activity.
                        Toast(this).showCustomToast ("Email is not verified",false, this)
                    }
                } else if(task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted."){
                    Toast(this).showCustomToast ("User Doesn't Exist.",false, this)
                }
                else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") {
                    // If sign in fails, display a message to the user.
                    Log.i("Error of",task.exception.toString())
                    Toast(this).showCustomToast ("Email Address is Invalid!",false, this)
                }
                else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password."){
                    Toast(this).showCustomToast ("Password is Incorrect!",false, this)
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.i("Error of", task.exception.toString())
                    Toast(this).showCustomToast ("Unknown Error Occurred",false, this)
                    // ...
                }

                // ...
            }
    }

    fun Login (v: View){
        if (emailtext.text.isNullOrEmpty() || passwordtext.text.isNullOrEmpty())
        {
            Toast(this).showCustomToast ("You can't leave a field empty!",false, this)
        }

        else {
            email = emailtext.text.toString()
            password = passwordtext.text.toString()
            loginAccount(email,password)
        }

    }
}