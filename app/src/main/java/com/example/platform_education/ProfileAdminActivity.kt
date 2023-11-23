// ProfileAdminActivity.kt

package com.example.platform_education

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileAdminActivity : AppCompatActivity() {

    private val apiService = RetrofitInstance.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_admin)

        val tableLayout = findViewById<TableLayout>(R.id.tablelayout)

        // Appel de l'API dans la méthode onCreate
        val call = apiService.getUsers()
        call.enqueue(object : Callback<List<Etudiant>> {
            override fun onResponse(
                call: Call<List<Etudiant>>,
                response: Response<List<Etudiant>>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        for (user in users) {
                            // Création d'un CardView pour chaque étudiant
                            val cardView = layoutInflater.inflate(R.layout.item_etudiant, null) as CardView

                            // Récupération des vues dans le CardView
                            val numTextView = cardView.findViewById<TextView>(R.id.textViewNumInscrit)
                            val nomTextView = cardView.findViewById<TextView>(R.id.textViewNom)
                            val checkBoxAccepter = cardView.findViewById<CheckBox>(R.id.toggleButtonAccepter)

                            // Définition des valeurs dans les vues
                            numTextView.text = "${user.NumInscrit}"
                            nomTextView.text = user.Name

                            // Gestion du clic sur le bouton "Accepter"
//                            acceptButton.setOnClickListener {
//                                updateEtudiantState(user.NumInscrit, 1, users)
//                            }
                            checkBoxAccepter.setOnCheckedChangeListener { _, isChecked ->
                                updateEtudiantState(user.NumInscrit, if (isChecked) 1 else 0, users)
                            }

                            // Ajout du CardView à la TableLayout
                            tableLayout.addView(cardView)
                        }
                    }
                } else {
                    Log.d("ProfilERrer", "$response.code()")
                }
            }

            override fun onFailure(call: Call<List<Etudiant>>, t: Throwable) {
                Toast.makeText(
                    this@ProfileAdminActivity,
                    "Erreur de connexion : ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateEtudiantState(userId: Int, newState: Int, userList: List<Etudiant>) {
        lifecycleScope.launch {
            try {
                // Récupérez les informations de l'utilisateur existant
                val existingUser = userList.find { it.NumInscrit == userId }

                // Vérifiez si l'utilisateur existe
                if (existingUser != null) {
                    // Créez une nouvelle instance d'Etudiant avec le nouvel état
                    val updatedUser = existingUser.copyWithNewEtat(newState)

                    // Appel à l'API pour mettre à jour l'état de l'utilisateur
                    val response = apiService.updateEtudiantState(userId, updatedUser)

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ProfileAdminActivity,
                            "État mis à jour avec succès",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Actualisez l'affichage ou effectuez d'autres actions nécessaires après la mise à jour de l'état
                    } else {
                        Toast.makeText(
                            this@ProfileAdminActivity,
                            "Erreur lors de la mise à jour de l'état",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                // Gérer les exceptions
                Toast.makeText(
                    this@ProfileAdminActivity,
                    "Exception: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
