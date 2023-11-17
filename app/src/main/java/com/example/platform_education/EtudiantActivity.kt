package com.example.platform_education
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.platform_education.R

class EtudiantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etudiant)
        val username = intent.getStringExtra("username")

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
        }

    }

//    fun startProfileAdmin(view: View) {
//        val intent = Intent(this, ProfileAdminActivity::class.java)
//        startActivity(intent)
//    }

