package com.example.platform_education

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.platform_education.Etudiant
import com.example.platform_education.R
import com.example.platform_education.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserActivity : AppCompatActivity() {

    private val apiService = RetrofitInstance.apiService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        val numInscrit = intent.getIntExtra("numInscrit", -1)
        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextemail) // Add this line
        val classEditText = findViewById<EditText>(R.id.editTextclass) // Add this line
        val updateButton = findViewById<Button>(R.id.buttonUpdate)

        val call = apiService.getUserById(numInscrit)
        call.enqueue(object : Callback<Etudiant> {
            override fun onResponse(call: Call<Etudiant>, response: Response<Etudiant>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        nameEditText.setText(user.Name)
                        emailEditText.setText(user.Email) // Set email
                        classEditText.setText(user.Class) // Set class
                    }
                } else {
                    // Handle API request errors here
                    Toast.makeText(
                        this@UpdateUserActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Close the activity if there's an error
                }
            }

            override fun onFailure(call: Call<Etudiant>, t: Throwable) {
                // Handle network errors here
                Toast.makeText(
                    this@UpdateUserActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                finish() // Close the activity if there's an error
            }
        })

        updateButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()
            val newClass = classEditText.text.toString()
            val updatedUser = Etudiant(numInscrit, newName, newEmail, newClass, Password = null, Etat = 0)
            val updateCall = apiService.updateUser(numInscrit, updatedUser)
            updateCall.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UpdateUserActivity,
                            "User updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Close the activity after successful update
                    } else {
                        // Handle API request errors here
                        Toast.makeText(
                            this@UpdateUserActivity,
                            "Error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle network errors here
                    Toast.makeText(
                        this@UpdateUserActivity,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
