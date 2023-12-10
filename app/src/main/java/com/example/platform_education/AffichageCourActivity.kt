package com.example.platform_education

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class AffichageCourActivity : AppCompatActivity() {

    private val API_BASE_URL = "http://192.168.56.1:8080/courses"
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affichage_cour)

        Toast.makeText(this@AffichageCourActivity, "affiche", Toast.LENGTH_SHORT).show()

        queue = Volley.newRequestQueue(this)

        // Reference to the RecyclerView in your layout
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Specify the URL for the JSON endpoint on your server.
        val jsonUrl = "http://192.168.56.1:8080/courses"

        // Request a JSON array response from the provided URL.
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, jsonUrl, null,
            Response.Listener { response ->
                // Process the JSON array of courses.
                try {
                    val adapter = CourseAdapter(this, response)
                    recyclerView.adapter = adapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Handle errors.
                Log.e("Volley Error", "Error occurred", error)
            })

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }
}
