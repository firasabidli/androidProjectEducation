package com.example.platform_education

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Build
//import android.content.Context
//import android.content.Intent
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import androidx.core.app.NotificationCompat
//import android.app.PendingIntent
class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Gérer les clics sur les éléments du menu
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    // Gérer l'action pour "Profile"
                    // Exemple : startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                R.id.logout -> {
                    // Gérer l'action pour "Logout"
                    // Rediriger l'utilisateur vers l'activité de connexion (MainActivity)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        val tableLayout =
            findViewById<TableLayout>(R.id.tablelayout) // Récupération du TableLayout depuis le XML

        val sharedPrefs = getSharedPreferences("MonFichier", Context.MODE_PRIVATE)
        val userCount = sharedPrefs.getInt("userCount", 0)
        // Récupérez le nom de l'admin depuis l'intent
        val username = intent.getStringExtra("username")

        // Assurez-vous que le nom de l'utilisateur n'est pas nul
        if (username != null) {
            // Mise à jour de l'élément "Profile" avec le nom de l'utilisateur
            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            val profileMenuItem = bottomNavigation.menu.findItem(R.id.profile)
            profileMenuItem.title = "$username"
        }
//partie acceptation
        for (i in 1..userCount) {
            val cin = sharedPrefs.getString("numero$i", "")
            val nomUtilisateur = sharedPrefs.getString("Name$i", "")
            val email = sharedPrefs.getString("email$i", "")

            val row = TableRow(this)

            val cinTextView = TextView(this, null, 0, R.style.MyTextViewStyle)
            cinTextView.text = cin

            val nomTextView = TextView(this, null, 0, R.style.MyTextViewStyle)
            nomTextView.text = nomUtilisateur

            val acceptButton = Button(this)
            acceptButton.text = "Accepter"

            val refuseButton = Button(this)
            refuseButton.text = "Refuser"

            row.addView(cinTextView)
            row.addView(nomTextView)
            row.addView(acceptButton)
            row.addView(refuseButton)

            tableLayout.addView(row)

            acceptButton.setOnClickListener {
                val editor = sharedPrefs.edit()
                editor.putInt("Etat$i", 1)
                editor.apply()
                val username = sharedPrefs.getString("Name$i", "") // Remplacez par la vraie source de données

                if (username != null) {

                    sendNotificationToUser(username)
                    Toast.makeText(this@AdminActivity, "accepter", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AdminActivity, "Impossible de trouver l'e-mail de l'utilisateur", Toast.LENGTH_SHORT).show()
                }
            }

            refuseButton.setOnClickListener {
                // Traitement à effectuer lorsque le bouton "Refuser" est cliqué
                val editor = sharedPrefs.edit()
                editor.putInt("Etat$i", 0)
                editor.apply()
                Toast.makeText(this@AdminActivity, "refuser", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //partie du notification
    private fun sendNotificationToUser(username: String) {
        // Utilisez l'e-mail de l'utilisateur pour personnaliser le contenu de la notification
        val notificationContent = "Bienvenue, $username ! Vous pouvez maintenant accéder à votre compte."

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("mon_canal_id", "Mon Canal", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Description du canal"
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, "mon_canal_id")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Accès autorisé")
            .setContentText(notificationContent)
            .setAutoCancel(true)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        val notificationId = 1
        notificationManager.notify(notificationId, builder.build())
    }
}
