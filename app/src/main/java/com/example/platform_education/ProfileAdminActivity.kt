package com.example.platform_education

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileAdminActivity : AppCompatActivity() {

    private val apiService = RetrofitInstance.apiService
    private lateinit var etudiantAdapter: EtudiantAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_admin)
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
            val sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)

            val usernameMenuItem: MenuItem? = navigationView.menu.findItem(R.id.menu_username)

            if (usernameMenuItem != null && username != null) {
                usernameMenuItem.title = "Bonjour, $username"
            }
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this@ProfileAdminActivity, AdminActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_settings -> {
                    val formDialog = FormDialogFragment()
                    formDialog.show(supportFragmentManager, "ChangePasswordDialog")
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.Accepte -> {
                    val intent = Intent(this, ProfileAdminActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.ListeEtudiant -> {
                    val intent = Intent(this, CrudEtudiantActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.ListeEnseignant -> {
                    val intent = Intent(this, CrudEnseignantActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    // Gérez la déconnexion
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        //rest code
        setupRecyclerView()

        fetchEtudiantsList()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //etudiantAdapter = EtudiantAdapter(mutableListOf())
        etudiantAdapter = EtudiantAdapter(mutableListOf()) { etudiant ->
            // Handle the "Reject" button click
            deleteEtudiant(etudiant)
        }

        recyclerView.adapter = etudiantAdapter
    }

    private fun fetchEtudiantsList() {
        lifecycleScope.launch {
            try {
                val call: Call<List<Etudiant>> = apiService.getUsers()
                call.enqueue(object : Callback<List<Etudiant>> {
                    override fun onResponse(
                        call: Call<List<Etudiant>>,
                        response: Response<List<Etudiant>>
                    ) {
                        if (response.isSuccessful) {
                            val users = response.body()
                            updateEtudiantsList(users)
                        } else {
                            Log.d("ProfileAdminActivity", "API call failed with code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<Etudiant>>, t: Throwable) {
                        Log.e("ProfileAdminActivity", "Error during API call", t)
                        Toast.makeText(
                            this@ProfileAdminActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } catch (e: Exception) {
                Log.e("ProfileAdminActivity", "Error during API call", e)
                Toast.makeText(this@ProfileAdminActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateEtudiantsList(etudiants: List<Etudiant>?) {
        etudiants?.let {
            etudiantAdapter.updateEtudiantList(it)
        }
    }
    fun updateEtudiantState(userId: Int, newState: Int, userList: List<Etudiant>) {
        if (newState == 1) {
            lifecycleScope.launch {
                try {
                    val existingUser = userList.find { it.NumInscrit == userId }
                    if (existingUser != null) {

                        val updatedUser = existingUser.copyWithNewEtat(newState)

                        val response = apiService.updateEtudiantState(userId, updatedUser)

                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ProfileAdminActivity,
                                "État mis à jour avec succès",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun deleteEtudiant(etudiant: Etudiant) {
        lifecycleScope.launch {
            try {
                val response = apiService.delete(etudiant.NumInscrit)

                if (response.code() in 200 until 300) {
                    Toast.makeText(
                        this@ProfileAdminActivity,
                        "Étudiant supprimé avec succès",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Remove the student from the adapter's list
                    etudiantAdapter.removeEtudiant(etudiant)
                } else {
                    Toast.makeText(
                        this@ProfileAdminActivity,
                        "Erreur lors de la suppression de l'étudiant",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Handle exceptions
                Toast.makeText(
                    this@ProfileAdminActivity,
                    "Exception: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
