package com.example.wips.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wips.Activities.Admin.Buildings
import com.example.wips.Activities.Admin.Newbuilding
import com.example.wips.Activities.User.Search_Place
import com.example.wips.Activities.User.User_home
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
    lateinit var dloginuser :Button
    lateinit var dloginadmin :Button
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
        dloginuser = findViewById(R.id.demouser)
        dloginadmin = findViewById(R.id.demoadmin)
        var hideshow: Boolean = true
        hideshowimg = findViewById(R.id.hideshowimg2)
        passwordtext = findViewById(R.id.pwd_edittext)
        emailtext = findViewById(R.id.email_edittext)

        val isLoggedin = getSharedPreferences("Loggedin", Context.MODE_PRIVATE).getBoolean("isLoggedin", false)

        //If App has not allowed Wifi permission then ask for it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    1
            )

        }
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

        dloginuser.setOnClickListener {
            Login(View(this))
        }
        dloginadmin.setOnClickListener {
            startActivity(Intent(this@Login, Newbuilding::class.java))
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
                        getSharedPreferences("Loggedin", Context.MODE_PRIVATE).edit()
                                .putBoolean("isLoggedin", true).apply()
                        startActivity(Intent(this@Login, User_home::class.java))
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

    //Code for granting permission from user for wifi
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.count() > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                        this,
                        "Wifi Permission Granted",
                        Toast.LENGTH_SHORT
                )
                        .show()
            } else {
                Toast.makeText(
                        this,
                        "Wifi Permission Denied",
                        Toast.LENGTH_SHORT
                )
                        .show()
            }
        }
    }
}