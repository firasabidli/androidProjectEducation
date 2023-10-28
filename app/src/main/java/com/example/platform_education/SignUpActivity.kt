package com.example.platform_education

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

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
        val sharedPreference = getSharedPreferences("MonFichier", Context.MODE_PRIVATE)
        signupButton.setOnClickListener {
            val name = Editname.editableText.toString()
            val email = EditEmail.text.toString()
            val numero = EditNumero.editableText.toString()
            val classe = EditClasse.editableText.toString()
            val confirmPassword = confirmPassword.editableText.toString()
            val psd = EditPassword.editableText.toString()

            val editor = sharedPreference.edit()

            if (name.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "Le nom est obligatoire", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "L'adresse e-mail est obligatoire", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this@SignUpActivity, "L'adresse e-mail n'est pas valide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (numero.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "Le numéro est obligatoire", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            val numero: Int
//            try {
//                numero = numeroStr.toInt()
//            } catch (e: NumberFormatException) {
//                Toast.makeText(this@SignUpActivity, "Le numéro n'est pas valide", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            if (classe.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "La classe est obligatoire", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (psd.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "Le mot de passe est obligatoire", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "La confirmation du mot de passe est obligatoire", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (psd != confirmPassword) {
                Toast.makeText(this@SignUpActivity, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userCount = sharedPreference.getInt("userCount", 0)
            val newUserCount = userCount + 1

            editor.putString("numero$userCount", numero)
            editor.putString("Name$userCount", name)
            editor.putString("Email$userCount", email)
            editor.putString("Classe$userCount", classe)
            editor.putString("Password$userCount", psd)
            editor.putInt("Etat", 0)
            editor.putInt("userCount", newUserCount)

            editor.apply()

            Toast.makeText(this@SignUpActivity, "Enregistrement effectué", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}