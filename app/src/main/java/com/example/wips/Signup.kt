package com.example.wips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import showCustomToast

class Signup : AppCompatActivity() {

    lateinit var hideshowimg:ImageView
    lateinit var password:EditText
    lateinit var signup:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var hideshow:Boolean = true
        hideshowimg = findViewById(R.id.hideshowimg)
        password = findViewById(R.id.pwd_edittext)
        signup = findViewById(R.id.Signup)

        //Hiding and showing Password
        hideshowimg.setOnClickListener {
            if (hideshow){
                hideshow = false
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                password.setSelection(password.getText().length)
                hideshowimg.alpha = 1F
            }
            else{
                hideshow = true
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                password.setSelection(password.getText().length)
                hideshowimg.alpha = 0.5F
            }
        }

        // apply an onClickListener() method
        signup.setOnClickListener{
            Toast(this).showCustomToast (
                "A confirmation mail has been sent!",
                this
            )
        }
    }
}