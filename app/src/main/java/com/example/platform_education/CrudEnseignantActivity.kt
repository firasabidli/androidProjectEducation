package com.example.platform_education

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrudEnseignantActivity : AppCompatActivity() {
    private val apiService = RetrofitInstance.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_enseignant)

        val tableLayout = findViewById<TableLayout>(R.id.enseignantTableLayout)
        val addButton = findViewById<Button>(R.id.buttonAddEnseignant)

        addButton.setOnClickListener {
            // Handle the button click event to add a new enseignant
            // You can start a new activity or show a dialog for user input
            // Example: Start a new activity for adding a new enseignant
            val intent = Intent(this@CrudEnseignantActivity, AddEnseignantActivity::class.java)
            startActivity(intent)
        }

        // Call the API to get the list of enseignants
        val call = apiService.getEnseignants()
        call.enqueue(object : Callback<List<Enseignant>> {
            override fun onResponse(
                call: Call<List<Enseignant>>,
                response: Response<List<Enseignant>>
            ) {
                if (response.isSuccessful) {
                    val enseignants = response.body()
                    if (enseignants != null) {
                        for (enseignant in enseignants) {
                            Log.d("API_SUCCESS", "Enseignant: $enseignant")

                            val row = TableRow(this@CrudEnseignantActivity)

                            val nameTextView = TextView(
                                this@CrudEnseignantActivity,
                                null,
                                0,
                                R.style.MyTextViewStyle
                            )
                            nameTextView.text = "${enseignant.name}"

                            val matiereTextView = TextView(
                                this@CrudEnseignantActivity,
                                null,
                                0,
                                R.style.MyTextViewStyle
                            )
                            matiereTextView.text = "${enseignant.matiere}"

                            val updateButton = Button(this@CrudEnseignantActivity)
                            updateButton.text = "Update"
                            updateButton.setOnClickListener {
                                // Call the method to handle the update
                                handleUpdateButtonClick(enseignant.id)
                            }

                            val deleteButton = Button(this@CrudEnseignantActivity)
                            deleteButton.text = "Delete"
                            deleteButton.setOnClickListener {
                                // Call the method to handle the delete
                                handleDeleteButtonClick(enseignant)
                            }

                            row.addView(nameTextView)
                            row.addView(matiereTextView)
                            row.addView(updateButton)
                            row.addView(deleteButton)

                            tableLayout.addView(row)
                        }
                    } else {
                        Log.d("API_SUCCESS", "Enseignants list is null")
                    }
                } else {
                    // Handle API request errors here
                    Log.e("API_ERROR", "Error: ${response.code()}")
                    Toast.makeText(
                        this@CrudEnseignantActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


            override fun onFailure(call: Call<List<Enseignant>>, t: Throwable) {
                // Handle network errors here
                Toast.makeText(
                    this@CrudEnseignantActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun handleUpdateButtonClick(id: Int) {
        // Create an Intent to start the UpdateEnseignantActivity
        val intent = Intent(this@CrudEnseignantActivity, UpdateEnseignantActivity::class.java)
        // Pass the enseignant id to the UpdateEnseignantActivity
        intent.putExtra("id", id)
        // Start the UpdateEnseignantActivity
        startActivity(intent)
    }


    private fun handleDeleteButtonClick(enseignant: Enseignant) {
        // Call the method to handle the delete
        deleteEnseignant(enseignant)
    }

    private fun deleteEnseignant(enseignant: Enseignant) {
        // Faites appel à l'API pour supprimer l'enseignant
        val call = apiService.deleteEnseignant(enseignant.id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Toast.makeText(
                            this@CrudEnseignantActivity,
                            "Enseignant supprimé avec succès",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Do not finish the activity here
                    } else {
                        Toast.makeText(
                            this@CrudEnseignantActivity,
                            "Réponse du serveur vide",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@CrudEnseignantActivity,
                        "Erreur: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle network errors here
                Log.e("DELETE_ERROR", "Network Error: ${t.message}", t)
                Toast.makeText(
                    this@CrudEnseignantActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
