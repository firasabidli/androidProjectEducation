package com.example.platform_education

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class SignUpActivity : AppCompatActivity() {
    lateinit var EditEmail: EditText
    lateinit var EditPassword: EditText
    lateinit var Editname: EditText
    lateinit var EditNumero: EditText
    lateinit var EditClasse: EditText
    lateinit var confirmPassword: EditText
    lateinit var signupButton: Button
    lateinit var loginRedirectText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        EditEmail = findViewById(R.id.signup_email)
        EditPassword = findViewById(R.id.password)
        Editname=findViewById(R.id.username)
        EditNumero=findViewById(R.id.editNumeroinscrit)
        EditClasse=findViewById(R.id.editClass)
        confirmPassword=findViewById(R.id.confirmPassword)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)
        loginRedirectText.setOnClickListener {
            // Navigate to MainActivity upon clicking "Not yet registered? SignUp Now"
            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        }

        signupButton.setOnClickListener {
            val name = Editname.text.toString()
            val email = EditEmail.text.toString()
            val numero = EditNumero.text.toString()
            val classe = EditClasse.text.toString()
            val psd = EditPassword.text.toString()
            val confirmPassword = confirmPassword.text.toString()

            if (valider()) {
            }
        }
    }

    private fun valider(): Boolean {
        val name = Editname.text.toString()
        val email = EditEmail.text.toString()
        val password = EditPassword.text.toString()
        val numero = EditNumero.text.toString()
        val classe = EditClasse.text.toString()
        val confirmPassword = confirmPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || numero.isEmpty() || classe.isEmpty() || confirmPassword.isEmpty()) {
            val ad: AlertDialog.Builder
            ad = AlertDialog.Builder(this)
            ad.setMessage("Les champs ne doivent pas être vide")
            ad.setTitle("Error")
            ad.setIcon(android.R.drawable.btn_dialog)
            ad.setPositiveButton(
                "yes"
            ) { dialogInterface, i -> finish() }
            val a = ad.create()
            a.show()
            return false
        }
        return true
    }
}