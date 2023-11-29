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

class AddEnseignantActivity : AppCompatActivity() {

    private lateinit var editId: EditText
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editMatiere: EditText
    private lateinit var editPassword: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var addButton: Button
    private val apiService = RetrofitInstance.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_enseignant)

        // Initialize views
        editId = findViewById(R.id.editId)
        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editMatiere = findViewById(R.id.editMatiere)
        editPassword = findViewById(R.id.editPassword)
        confirmPassword = findViewById(R.id.confirmPassword)
        addButton = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            if (validate()) {
                val id = editId.text.toString().toIntOrNull() ?: 0
                val name = editName.text.toString()
                val email = editEmail.text.toString()
                val matiere = editMatiere.text.toString()
                val password = editPassword.text.toString()
                val confirmPasswordValue = confirmPassword.text.toString()

                if (password != confirmPasswordValue) {
                    Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                } else {
                    val teacher = Enseignant( id ,name, matiere, password)

                    lifecycleScope.launch {
                        try {
                            val response = apiService.addEnseignant(teacher)

                            if (response.isSuccessful) {
                                Toast.makeText(this@AddEnseignantActivity, "Enseignant ajouté avec succès", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Toast.makeText(this@AddEnseignantActivity, "Erreur: ${response.code()} - $errorBody", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: HttpException) {
                            Log.e("AddEnseignantActivity", "HttpException: ${e.response()?.errorBody()?.string()}", e)
                            Toast.makeText(this@AddEnseignantActivity, "Erreur HTTP: ${e.response()?.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e("AddEnseignantActivity", "Exception: ${e.message}", e)
                            Toast.makeText(this@AddEnseignantActivity, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        val id = editId.text.toString()
        val name = editName.text.toString()
        val email = editEmail.text.toString()
        val matiere = editMatiere.text.toString()
        val password = editPassword.text.toString()
        val confirmPasswordValue = confirmPassword.text.toString()

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || matiere.isEmpty() || password.isEmpty() || confirmPasswordValue.isEmpty()) {
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
