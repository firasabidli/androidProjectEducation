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
        val sharedPreference = getSharedPreferences("MonFichier", Context.MODE_PRIVATE)
        signupButton.setOnClickListener {
            val name = Editname.text.toString()
            val email = EditEmail.text.toString()
            val numero = EditNumero.text.toString()
            val classe = EditClasse.text.toString()
            val psd = EditPassword.text.toString()
            val confirmPassword = confirmPassword.text.toString()

            if (valider()) {
                val editor = sharedPreference.edit()
                val userCount = sharedPreference.getInt("userCount", 0)
                val newUserCount = userCount + 1

                if (psd != confirmPassword) {
                    Toast.makeText(this@SignUpActivity, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                } else {
                    editor.putString("numero$userCount", numero)
                    editor.putString("Name$userCount", name)
                    editor.putString("Email$userCount", email)
                    editor.putString("Classe$userCount", classe)
                    // Ne stockez pas les mots de passe en texte brut ici, c'est une mauvaise pratique de sécurité.
                    // Vous devriez utiliser une technique de hachage et de stockage sécurisé.
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