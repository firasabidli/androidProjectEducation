// ProfileAdminActivity.kt

package com.example.platform_education

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope

import org.json.JSONArray
import org.json.JSONObject
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class ProfileAdminActivity : AppCompatActivity() {

    lateinit var Rtext: TextView
    private val apiService = RetrofitInstance.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_admin)
        Rtext = findViewById(R.id.textView)
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
                            val row = TableRow(this@ProfileAdminActivity)

                            val cinTextView = TextView(
                                this@ProfileAdminActivity,
                                null,
                                0,
                                R.style.MyTextViewStyle
                            )
                            cinTextView.text = "${user.NumInscrit}"

                            val nomTextView = TextView(
                                this@ProfileAdminActivity,
                                null,
                                0,
                                R.style.MyTextViewStyle
                            )
                            nomTextView.text = "${user.Name}"

                            val acceptButton = Button(this@ProfileAdminActivity)
                            acceptButton.text = "Accepter"
                            acceptButton.setOnClickListener {
                                updateEtudiantState(user.NumInscrit, 1, users)
                            }
                            row.addView(cinTextView)
                            row.addView(nomTextView)
                            row.addView(acceptButton)

                            tableLayout.addView(row)
                        }

                        // Affichez la chaîne dans le TextView
                        //  Rtext.text = "NumInscrit:\n$cin\n\nName:\n$nomUtilisateur"
                    }
                    // Traitez les données des utilisateurs ici
                    Toast.makeText(this@ProfileAdminActivity, "marche:$users", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Gérez les erreurs de requête ici
                    Log.d("ProfilERrer", "$response.code()")
                }
            }

            override fun onFailure(call: Call<List<Etudiant>>, t: Throwable) {
                // Gérez les erreurs de connexion ici
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

//my Code
        //val tableLayout = findViewById<TableLayout>(R.id.tablelayout) // Récupération du TableLayout depuis le XML
//
//        val sharedPrefs = getSharedPreferences("MonFichier", Context.MODE_PRIVATE)
//        val userCount = sharedPrefs.getInt("userCount", 0)
//        // Récupérez le nom de l'admin depuis l'intent
//        //val username = intent.getStringExtra("username")
//
//        val profileButton = findViewById<ImageButton>(R.id.imageButtonProfile)
//        profileButton.setOnClickListener {
//            val popupMenu = PopupMenu(this, profileButton)
//            val inflater: MenuInflater = popupMenu.menuInflater
//            inflater.inflate(R.menu.navigation_menu, popupMenu.menu)
//
//            val profileMenuItem = popupMenu.menu.findItem(R.id.profile)
//            val sharedPrefs = getSharedPreferences("MonFichier", Context.MODE_PRIVATE)
//            val username = sharedPrefs.getString("username", null)
//
//
//            profileMenuItem.title = "admin"
//
//
//            popupMenu.setOnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.profile -> {
//                        val intent = Intent(this, ProfileActivity::class.java)
//                        startActivity(intent)
//                        true
//                    }
//
//                    R.id.logout -> {
//                        // Effacer les informations d'authentification
//                        val editor = sharedPrefs.edit()
//                        editor.remove("username")
//                        editor.apply()
//
//                        // Rediriger l'utilisateur vers l'activité de connexion (MainActivity)
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                        true
//                    }
//
//                    else -> false
//                }
//            }
//
//            popupMenu.show()
//
//        }
//
////partie acceptation
//        for (i in 0..userCount) {
//            val cin = sharedPrefs.getString("Name$i", "")
//            val nomUtilisateur = sharedPrefs.getString("Name$i", "")
//            val email = sharedPrefs.getString("email$i", "")
//
//            val row = TableRow(this)
//
//            val cinTextView = TextView(this, null, 0, R.style.MyTextViewStyle)
//            cinTextView.text = cin
//
//            val nomTextView = TextView(this, null, 0, R.style.MyTextViewStyle)
//            nomTextView.text = nomUtilisateur
//
//            val acceptButton = Button(this)
//            acceptButton.text = "Accepter"
//
//            val refuseButton = Button(this)
//            refuseButton.text = "Refuser"
//
//            row.addView(cinTextView)
//            row.addView(nomTextView)
//            row.addView(acceptButton)
//            row.addView(refuseButton)
//
//            tableLayout.addView(row)

//            acceptButton.setOnClickListener {
//                val editor = sharedPrefs.edit()
//                editor.putInt("Etat$i", 1)
//                editor.apply()
//                val username =
//                    sharedPrefs.getString("Name$i", "") // Remplacez par la vraie source de données
//
//                if (username != null) {
//
//                    sendNotificationToUser(username)
//                    Toast.makeText(this@ProfileAdminActivity, "accepter", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(
//                        this@ProfileAdminActivity,
//                        "Impossible de trouver l'e-mail de l'utilisateur",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            refuseButton.setOnClickListener {
//                // Traitement à effectuer lorsque le bouton "Refuser" est cliqué
//                val editor = sharedPrefs.edit()
//                editor.putInt("Etat$i", 0)
//                editor.apply()
//                Toast.makeText(this@ProfileAdminActivity, "refuser", Toast.LENGTH_SHORT).show()
//            }



//    //partie du notification
//    private fun sendNotificationToUser(username: String) {
//
//        // Utilisez l'e-mail de l'utilisateur pour personnaliser le contenu de la notification
//        val notificationContent =
//            "Bienvenue, $username ! Vous pouvez maintenant accéder à votre compte."
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "mon_canal_id",
//                "Mon Canal",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            channel.description = "Description du canal"
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val builder = NotificationCompat.Builder(this, "mon_canal_id")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle("Accès autorisé")
//            .setContentText(notificationContent)
//            .setAutoCancel(true)
//
//        val intent = Intent(this, MainActivity::class.java)
//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        builder.setContentIntent(pendingIntent)
//
//        val notificationId = 1
//        notificationManager.notify(notificationId, builder.build())
//
//    }


