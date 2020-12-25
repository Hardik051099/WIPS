package com.example.wips.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.wips.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import showCustomToast

class Signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    lateinit var hideshowimg:ImageView
    lateinit var passwordtext:EditText
    lateinit var emailtext:EditText
    lateinit var usernametext:EditText
    lateinit var signup:Button

    lateinit var email: String
    lateinit var password: String
    lateinit var name: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()


        var hideshow:Boolean = true
        hideshowimg = findViewById(R.id.hideshowimg)
        passwordtext = findViewById(R.id.pwd_edittext)
        emailtext = findViewById(R.id.email_edittext)
        usernametext =  findViewById(R.id.edittext_username)
        signup = findViewById(R.id.Signup)

        //Hiding and showing Password
        hideshowimg.setOnClickListener {
            if (hideshow){
                hideshow = false
                passwordtext.transformationMethod = HideReturnsTransformationMethod.getInstance()
                passwordtext.setSelection(passwordtext.getText().length)
                hideshowimg.alpha = 1F
            }
            else{
                hideshow = true
                passwordtext.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordtext.setSelection(passwordtext.getText().length)
                hideshowimg.alpha = 0.5F
            }
        }

        // apply an onClickListener() method
        signup.setOnClickListener{
            Signup(View(this))
        }
    }

    fun createAccount (email:String ,password:String,name:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Successful", "createUserWithEmail:success")
                    val user = auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast(this).showCustomToast ("A confirmation mail has been sent!",true, this)
                                Log.d("Email", "Email sent.")
                                //Opens email app
                                Handler().postDelayed(Runnable {
                                    val intent =
                                        packageManager.getLaunchIntentForPackage("com.google.android.gm")
                                    startActivity(intent)
                                    finish()
                                }, 2000)
                            }
                        }


                } else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") {
                    // If sign in fails, display a message to the user.
                    Log.i("Error of",task.exception.toString())
                    Toast(this).showCustomToast ("Email Address is Invalid!",false, this)
                }
                else if(task.exception.toString() == "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]"){
                    Log.i("Error of",task.exception.toString())
                    Toast(this).showCustomToast ("Password is too Small! (At least 6 characters)",false, this)
                }
                else {
                    Toast(this).showCustomToast ("Unknown Error Occurred!",false, this)
                }

                // ...
            }

    }
    fun Signup (v: View){
        if (emailtext.text.isNullOrEmpty() || passwordtext.text.isNullOrEmpty() || usernametext.text.isNullOrEmpty())
        {
            Toast(this).showCustomToast ("You can't Leave a Field Empty!",false, this)
        }
        else {
            email = emailtext.text.toString()
            password = passwordtext.text.toString()
            name = usernametext.text.toString()
            createAccount(email,password,name)
        }

    }
}