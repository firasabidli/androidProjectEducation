package com.example.platform_education

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileAdminActivity : AppCompatActivity() {

    private val apiService = RetrofitInstance.apiService
    private lateinit var etudiantAdapter: EtudiantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_admin)

        setupRecyclerView()

        fetchEtudiantsList()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        etudiantAdapter = EtudiantAdapter(mutableListOf())
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
            etudiantAdapter.updateEtudiantList(it.toMutableList())
        }
    }
}
