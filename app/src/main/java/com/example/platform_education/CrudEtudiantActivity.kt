package com.example.platform_education

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrudEtudiantActivity : AppCompatActivity() {
    private val apiService = RetrofitInstance.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_etudiant)

        val tableLayout = findViewById<TableLayout>(R.id.etudiantTableLayout)
        val addButton = findViewById<Button>(R.id.buttonAdd)

        // Set a click listener for the "Add" button
        addButton.setOnClickListener {
            // Handle the button click event to add a new student
            // You can start a new activity or show a dialog for user input
            // Example: Start a new activity for adding a new student
            val intent = Intent(this@CrudEtudiantActivity, AddEtudiantActivity::class.java)
            startActivity(intent)
        }

        // Call the API to get the list of users
        val call = apiService.getUsers()
        call.enqueue(object : Callback<List<Etudiant>> {
            override fun onResponse(
                call: Call<List<Etudiant>>,
                response: Response<List<Etudiant>>
            ) {
                if (response.isSuccessful) {
                    // If the API call is successful, parse the response and populate the table
                    val users = response.body()
                    if (users != null) {
                        for (user in users) {
                            // Create a new row for each user
                            val row = TableRow(this@CrudEtudiantActivity)

                            // Create TextViews to display user information
                            val cinTextView = TextView(
                                this@CrudEtudiantActivity,
                                null,
                                0,
                                R.style.MyTextViewStyle
                            )
                            cinTextView.text = "${user.NumInscrit}"

                            val nomTextView = TextView(
                                this@CrudEtudiantActivity,
                                null,
                                0,
                                R.style.MyTextViewStyle
                            )
                            nomTextView.text = "${user.Name}"

                            val emailTextView = TextView(
                                this@CrudEtudiantActivity,
                                null,
                                0,
                                R.style.MyTextViewStyle
                            )
                            emailTextView.text = "${user.Email}"

                            val classTextView = TextView(
                                this@CrudEtudiantActivity,
                                null,
                                0,
                                R.style.MyTextViewStyle
                            )
                            classTextView.text = "${user.Class}"

                            // Create buttons for update and delete operations
                            val updateButton = Button(this@CrudEtudiantActivity)
                            updateButton.text = "Update"
                            updateButton.setOnClickListener {
                                // Call the method to handle the update
                                handleUpdateButtonClick(user.NumInscrit)
                            }

                            val deleteButton = Button(this@CrudEtudiantActivity)
                            deleteButton.text = "Delete"
                            deleteButton.setOnClickListener {
                                // Call the method to handle the delete
                                handleDeleteButtonClick(user)
                            }

                            // Add TextViews and buttons to the row
                            row.addView(cinTextView)
                            row.addView(nomTextView)
                            row.addView(emailTextView)
                            row.addView(classTextView)
                            row.addView(updateButton)
                            row.addView(deleteButton)

                            // Add the row to the table layout
                            tableLayout.addView(row)
                        }
                    }
                } else {
                    // Handle API request errors here
                    Toast.makeText(
                        this@CrudEtudiantActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Etudiant>>, t: Throwable) {
                // Handle network errors here
                Toast.makeText(
                    this@CrudEtudiantActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun handleUpdateButtonClick(numInscrit: Int) {
        // Create an Intent to start the UpdateUserActivity
        val intent = Intent(this@CrudEtudiantActivity, UpdateUserActivity::class.java)
        // Pass the user ID to the UpdateUserActivity
        intent.putExtra("numInscrit", numInscrit)
        // Start the UpdateUserActivity
        startActivity(intent)
    }

    private fun handleDeleteButtonClick(etudiant: Etudiant) {
        // Call the method to handle the delete
        deleteEtudiant(etudiant)
    }

    private fun deleteEtudiant(etudiant: Etudiant) {
        // Call the API to delete the student
        val call = apiService.deleteEtudiant(etudiant.NumInscrit)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        // If the deletion is successful, show a success message
                        Toast.makeText(this@CrudEtudiantActivity, "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show()
                        // Do not finish the activity here
                    } else {
                        // If the server response is empty, show an error message
                        Toast.makeText(this@CrudEtudiantActivity, "Réponse du serveur vide", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If the deletion is not successful, show an error message with the response code
                    Toast.makeText(this@CrudEtudiantActivity, "Erreur: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle network errors here
                Log.e("DELETE_ERROR", "Network Error: ${t.message}", t)
                Toast.makeText(
                    this@CrudEtudiantActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}



//        if (username != null) {
//           // val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
//            val profileMenuItem = bottomNavigation.menu.findItem(R.id.profile)
//            profileMenuItem.title = username
//        }

//        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
//
//        bottomNavigation.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.profile -> {
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
//                R.id.logout -> {
//                    // Effacer les informations d'authentification
//                    val sharedPrefs = getSharedPreferences("MonFichier", Context.MODE_PRIVATE)
//                    val editor = sharedPrefs.edit()
//                    editor.remove("username") // Supprimer le nom d'utilisateur (ou autre clé d'authentification)
//                    editor.apply()
//
//                    // Rediriger l'utilisateur vers l'activité de connexion (MainActivity)
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish() // Fermer l'activité actuelle (EtudiantActivity)
//                    true
//                }
//                else -> false
//            }
//    fun startProfileAdmin(view: View) {
//        val intent = Intent(this, ProfileAdminActivity::class.java)
//        startActivity(intent)
//    }

