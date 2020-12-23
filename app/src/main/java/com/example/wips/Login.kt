package com.example.wips

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class Login : AppCompatActivity() {
    lateinit var signup: Button
    lateinit var hideshowimg: ImageView
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signup = findViewById(R.id.Signup)
        var hideshow: Boolean = true
        hideshowimg = findViewById(R.id.hideshowimg2)
        password = findViewById(R.id.pwd_edittext)

        //Hiding and showing Password
        hideshowimg.setOnClickListener {
            if (hideshow) {
                hideshow = false
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                password.setSelection(password.getText().length)
                hideshowimg.alpha = 1F
            } else {
                hideshow = true
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                password.setSelection(password.getText().length)
                hideshowimg.alpha = 0.5F
            }
        }

        signup.setOnClickListener {
            startActivity(Intent(this@Login, Signup::class.java))
        }
    }
}