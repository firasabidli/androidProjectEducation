package com.example.platform_education

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SignUpActivity : AppCompatActivity() {

    private lateinit var EditEmail: EditText
    private lateinit var EditPassword: EditText
    private lateinit var Editname: EditText
    private lateinit var EditNumero: EditText
    private lateinit var EditClasse: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView
    private val apiService = RetrofitInstance.apiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize views
        EditEmail = findViewById(R.id.signup_email)
        EditPassword = findViewById(R.id.password)
        Editname = findViewById(R.id.username)
        EditNumero = findViewById(R.id.editNumeroinscrit)
        EditClasse = findViewById(R.id.editClass)
        confirmPassword = findViewById(R.id.confirmPassword)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        // Set click listener for login redirect text
        loginRedirectText.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        }

        // Set click listener for signup button
        signupButton.setOnClickListener {
            // Données à ajouter manuellement
            val name = Editname.text.toString()
            val email = EditEmail.text.toString()
            val numero = EditNumero.text.toString().toIntOrNull() ?: 0
            val classe = EditClasse.text.toString()
            val password = EditPassword.text.toString()
            val confirmpassword = confirmPassword.text.toString()
            if (valider()) {
                if (password != confirmpassword) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Les mots de passe ne correspondent pas",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val etat=0
                    // Création de l'instance de la classe Etudiant avec les données manuelles
                    val user = Etudiant(numero, name, email, classe, password,etat)

                    lifecycleScope.launch {
                        try {
                            val response = apiService.createPost(user)

                            if (response.isSuccessful) {
                                // Traitement de la réponse en cas de succès
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Inscription réussie!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                            } else {
                                // Traitement de l'erreur
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Erreur: ${response}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: HttpException) {
                            // Gérer les erreurs HTTP
                            val errorBody = e.response()?.errorBody()?.string()
                            Log.e("SignUpActivity", "HttpException: $errorBody", e)
                            Toast.makeText(
                                this@SignUpActivity,
                                "Erreur HTTP: $errorBody",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            // Gérer les autres exceptions
                            Log.e("SignUpActivity", "Exception: ${e.message}", e)
                            Toast.makeText(
                                this@SignUpActivity,
                                "Exception: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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
                AlertDialog.Builder(this).apply {
                    setMessage("Les champs ne doivent pas être vides")
                    setTitle("Error")
                    setIcon(android.R.drawable.btn_dialog)
                    setPositiveButton("yes") { dialogInterface, i -> finish() }
                    create().show()
                    return false
                }
            }
            return true
        }
}
