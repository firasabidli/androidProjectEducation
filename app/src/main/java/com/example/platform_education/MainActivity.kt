package com.example.platform_education

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var pass: EditText
    lateinit var loginBtn: Button
    lateinit var signupText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signupText = findViewById(R.id.signupText)
        name=findViewById(R.id.username)
        pass=findViewById(R.id.password)
        loginBtn=findViewById(R.id.loginButton)
        // Set a click listener for the TextView
        signupText.setOnClickListener {
            // Create an intent to navigate to SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        val sharedPreference = getSharedPreferences("MonFichier", Context.MODE_PRIVATE)
        // initialiser les donnée du l'administrateur
        val editor = sharedPreference.edit()
        editor.putString("Adminname", "admin")
        editor.putString("Adminpassword", "111")
        editor.apply()
        loginBtn.setOnClickListener {
            val username = name.text.toString()
            val password = pass.text.toString()

            // Récupérer le compteur d'utilisateurs
            val userCount = sharedPreference.getInt("userCount", 0)

            // Vérifier l'authentification de l'utilisateur
            var isUserAuthenticated = false

            for (i in 1..userCount) {
                val registeredName = sharedPreference.getString("Name$i", "")
                val registeredPass = sharedPreference.getString("Password$i", "")

                if (username == registeredName && password == registeredPass) {
                    isUserAuthenticated = true
                    break
                }
            }

            // Vérifier l'authentification de l'administrateur
            val adminUsername = sharedPreference.getString("Adminname", "")
            val adminPassword = sharedPreference.getString("Adminpassword", "")

            if (username.isEmpty() || password.isEmpty() ) {
                // Authentification réussie pour l'utilisateur
                Toast.makeText(this, " Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show()

            } else if (isUserAuthenticated) {
                Toast.makeText(this, "connexion reésute ", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            }
            else if (username == adminUsername && password == adminPassword) {
                // Authentification réussie pour l'administrateur
                Toast.makeText(this, "Authentification de l'administrateur réussie", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, EtudiantActivity::class.java)
                intent.putExtra("username", adminUsername)
                startActivity(intent)
            } else {
                // Authentification échouée
                Toast.makeText(this, "connexion impossible", Toast.LENGTH_SHORT).show()
            }
        }
    }

}