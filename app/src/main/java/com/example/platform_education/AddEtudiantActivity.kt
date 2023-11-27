package com.example.platform_education


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddEtudiantActivity : AppCompatActivity() {

    private lateinit var EditName: EditText
    private lateinit var EditEmail: EditText
    private lateinit var EditNumero: EditText
    private lateinit var EditClasse: EditText
    private lateinit var EditPassword: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var addButton: Button
    private val apiService = RetrofitInstance.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_etudiant)

        // Initialize views
        EditName = findViewById(R.id.editName)
        EditEmail = findViewById(R.id.editEmail)
        EditNumero = findViewById(R.id.editNumero)
        EditClasse = findViewById(R.id.editClasse)
        EditPassword = findViewById(R.id.editPassword)
        confirmPassword = findViewById(R.id.confirmPassword)
        addButton = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            if (validate()) {
                val name = EditName.text.toString()
                val email = EditEmail.text.toString()
                val numero = EditNumero.text.toString().toIntOrNull() ?: 0
                val classe = EditClasse.text.toString()
                val password = EditPassword.text.toString()
                val confirmPasswordValue = confirmPassword.text.toString()

                if (password != confirmPasswordValue) {
                    Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                } else {
                    val etat = 0
                    val student = Etudiant(numero, name, email, classe, password, etat)

                    lifecycleScope.launch {
                        try {
                            val response = apiService.createPost(student)

                            if (response.isSuccessful) {
                                Toast.makeText(this@AddEtudiantActivity, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Toast.makeText(this@AddEtudiantActivity, "Erreur: ${response.code()} - $errorBody", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: HttpException) {
                            Log.e("AddEtudiantActivity", "HttpException: ${e.response()?.errorBody()?.string()}", e)
                            Toast.makeText(this@AddEtudiantActivity, "Erreur HTTP: ${e.response()?.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e("AddEtudiantActivity", "Exception: ${e.message}", e)
                            Toast.makeText(this@AddEtudiantActivity, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        val name = EditName.text.toString()
        val email = EditEmail.text.toString()
        val password = EditPassword.text.toString()
        val numero = EditNumero.text.toString()
        val classe = EditClasse.text.toString()
        val confirmPasswordValue = confirmPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || numero.isEmpty() || classe.isEmpty() || confirmPasswordValue.isEmpty()) {
            AlertDialog.Builder(this).apply {
                setMessage("Les champs ne doivent pas être vides")
                setTitle("Error")
                setIcon(android.R.drawable.btn_dialog)
                setPositiveButton("yes") { dialogInterface, i -> }
                create().show()
                return false
            }
        }
        return true
    }
}
