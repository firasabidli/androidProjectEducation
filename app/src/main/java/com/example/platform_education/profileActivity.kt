package com.example.platform_education

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.content.Context
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    lateinit var usernameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var confirmPasswordEditText: EditText
    lateinit var modifierButton: Button
    lateinit var nameTextView: TextView
    lateinit var emailTextView: TextView
    lateinit var back: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Associer les éléments de l'interface utilisateur aux variables
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.signup_email)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirmPassword)
        modifierButton = findViewById(R.id.modifier)
        nameTextView = findViewById(R.id.name)
        emailTextView = findViewById(R.id.email)
        back = findViewById(R.id.back)

        // Récupérer les informations de l'utilisateur depuis les préférences partagées
        val sharedPrefs = getSharedPreferences("MonFichier", Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("Name", "")
        val userEmail = sharedPrefs.getString("Email", "")

        // Afficher le nom d'utilisateur et l'e-mail dans les TextViews
        nameTextView.text = userName
        emailTextView.text = userEmail

        modifierButton.setOnClickListener {
            // Récupérer les nouvelles informations
            val newUsername = usernameEditText.text.toString()
            val newEmail = emailEditText.text.toString()
            val newPassword = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            // Valider les champs
            if (newUsername.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this@ProfileActivity, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                Toast.makeText(this@ProfileActivity, "L'adresse e-mail n'est pas valide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this@ProfileActivity, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enregistrer les nouvelles informations dans les préférences partagées
            val editor = sharedPrefs.edit()
            editor.putString("Name", newUsername)
            editor.putString("Email", newEmail)
            editor.putString("Password", newPassword)
            editor.apply()

            Toast.makeText(this@ProfileActivity, "Profil mis à jour", Toast.LENGTH_SHORT).show()

            // Rediriger vers l'activité précédente
            onBackPressed()
        }

        back.setOnClickListener {
            onBackPressed() // Comportement de retour
        }
    }
}
