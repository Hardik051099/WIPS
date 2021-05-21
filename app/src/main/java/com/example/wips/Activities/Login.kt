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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wips.Activities.Admin.*
import com.example.wips.Activities.User.User_home
import com.example.wips.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import showCustomToast

class Login : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    lateinit var signup: Button
    lateinit var login :Button

    lateinit var hideshowimg: ImageView
    lateinit var passwordtext: EditText
    lateinit var emailtext: EditText
    lateinit var progressBar: ProgressBar

    lateinit var email: String
    lateinit var password: String
    var role: String = ""
    lateinit var user: FirebaseUser
    var backpress:Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        signup = findViewById(R.id.Signup)
        login = findViewById(R.id.Login)

        var hideshow: Boolean = true
        hideshowimg = findViewById(R.id.hideshowimg2)
        passwordtext = findViewById(R.id.pwd_edittext)
        emailtext = findViewById(R.id.email_edittext)
        progressBar = findViewById(R.id.loginprogress)
        database = FirebaseDatabase.getInstance()
        databaseRef = database.reference
        progressBar.visibility = View.INVISIBLE

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
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
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

        signup.setOnClickListener {
            startActivity(Intent(this@Login, Signup::class.java))
        }

        /*login.setOnClickListener {
             startActivity(Intent(this@Login, Campus::class.java))

        }*/
    }

    fun loginAccount (email:String,password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("Login Email", "signInWithEmail:success")
                    user = auth.currentUser!!
                    progressBar.visibility = View.INVISIBLE
                    databaseRef.child("AllUsers").child("Users").child(user!!.uid).child("Role").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                        role = snapshot.value.toString()
                            Log.i("roles",role)
                            auth()
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })

                } else if(task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted."){
                    Toast(this).showCustomToast ("User Doesn't Exist.",false, this)
                    progressBar.visibility = View.INVISIBLE

                }
                else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") {
                    // If sign in fails, display a message to the user.
                    Log.i("Error of",task.exception.toString())
                    Toast(this).showCustomToast ("Email Address is Invalid!",false, this)
                    progressBar.visibility = View.INVISIBLE

                }
                else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password."){
                    Toast(this).showCustomToast ("Password is Incorrect!",false, this)
                    progressBar.visibility = View.INVISIBLE

                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.i("Error of", task.exception.toString())
                    Toast(this).showCustomToast ("Unknown Error Occurred",false, this)
                    // ...
                    progressBar.visibility = View.INVISIBLE

                }

                // ...
            }
    }

    fun Login (v: View){
        progressBar.visibility = View.VISIBLE
        if (emailtext.text.isNullOrEmpty() || passwordtext.text.isNullOrEmpty())
        {
            Toast(this).showCustomToast ("You can't leave a field empty!",false, this)
            progressBar.visibility = View.INVISIBLE
        }

        else {
            email = emailtext.text.toString()
            password = passwordtext.text.toString()
            loginAccount(email,password)
        }

    }
    fun auth(){
        if (user!!.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.
            Toast(this).showCustomToast(
                "Login successfull",
                true,
                this
            )
            getSharedPreferences("Loggedin", Context.MODE_PRIVATE).edit()
                .putBoolean("isLoggedin", true).apply()
            if (role.equals("user")) {
                startActivity(Intent(this@Login, User_home::class.java))
            }
            else if(role.equals("admin")){
                startActivity(Intent(this@Login, Campus::class.java))
            }
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            Toast(this).showCustomToast ("Email is not verified",false, this)
            progressBar.visibility = View.INVISIBLE
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
    override fun onBackPressed() {
        backpress = backpress + 1
        if (backpress > 1) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
        else{
            Toast(this).showCustomToast("Press Back again to Exit",true,this)

        }
    }
}