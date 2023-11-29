package com.example.platform_education

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.platform_education.Enseignant
import com.example.platform_education.R
import com.example.platform_education.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateEnseignantActivity : AppCompatActivity() {

    private val apiService = RetrofitInstance.apiService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_enseignant)

        val uuid = intent.getIntExtra("uuid", -1)
        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val matiereEditText = findViewById<EditText>(R.id.editTextMatiere)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val updateButton = findViewById<Button>(R.id.buttonUpdate)

        val call = apiService.getEnseignantById(uuid)
        call.enqueue(object : Callback<Enseignant> {
            override fun onResponse(call: Call<Enseignant>, response: Response<Enseignant>) {
                if (response.isSuccessful) {
                    val enseignant = response.body()
                    if (enseignant != null) {
                        nameEditText.setText(enseignant.name)
                        matiereEditText.setText(enseignant.matiere)
                        passwordEditText.setText(enseignant.password)
                    }
                } else {
                    // Handle API request errors here
                    Toast.makeText(
                        this@UpdateEnseignantActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Close the activity if there's an error
                }
            }

            override fun onFailure(call: Call<Enseignant>, t: Throwable) {
                // Handle network errors here
                Toast.makeText(
                    this@UpdateEnseignantActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                finish() // Close the activity if there's an error
            }
        })

        updateButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val newMatiere = matiereEditText.text.toString()
            val newPassword = passwordEditText.text.toString()
            val updatedEnseignant = Enseignant(uuid, newName, newMatiere, newPassword)
            val updateCall = apiService.updateEnseignant(uuid, updatedEnseignant)
            updateCall.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UpdateEnseignantActivity,
                            "Enseignant updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Close the activity after successful update
                    } else {
                        // Handle API request errors here
                        Toast.makeText(
                            this@UpdateEnseignantActivity,
                            "Error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle network errors here
                    Toast.makeText(
                        this@UpdateEnseignantActivity,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
