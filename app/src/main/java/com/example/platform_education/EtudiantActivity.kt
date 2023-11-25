package com.example.platform_education

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class EtudiantActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etudiant)
        val username = intent.getStringExtra("username")
//navigation
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Trouver l'élément du menu avec le nom d'utilisateur
            val usernameMenuItem: MenuItem? = navigationView.menu.findItem(R.id.menu_username)
            val username = intent.getStringExtra("username")

            if (usernameMenuItem != null && username != null) {
                usernameMenuItem.title = "Bonjour, $username"
            }

            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_settings -> {
                    // Gérer l'élément du menu Settings
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
        //rest code
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

