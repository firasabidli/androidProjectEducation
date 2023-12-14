package com.example.platform_education

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class UpdateCoursActivity : AppCompatActivity() {

    private lateinit var queue: RequestQueue
    private lateinit var courseId: String
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonUpdate: Button

    // Variables to store previous values
    private var prevTitle: String = ""
    private var prevDescription: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_cours)

        queue = Volley.newRequestQueue(this)
        courseId = intent.getStringExtra("COURSE_ID") ?: ""

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonUpdate = findViewById(R.id.buttonUpdate)

        // Load course details from your backend
        loadCourseDetails()

        buttonUpdate.setOnClickListener {
            // Store previous values
            prevTitle = editTextTitle.text.toString()
            prevDescription = editTextDescription.text.toString()

            // Handle course update
            updateCourse()
        }
    }

    private fun loadCourseDetails() {
        val url = "http://192.168.56.1:8080/courses/$courseId"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Log the response to inspect the data
                Log.d("LoadCourseDetails", "Response: $response")

                // Load course details into the edit fields
                editTextTitle.setText(response.optString("title"))
                editTextDescription.setText(response.optString("description"))

                // Store initial values
                prevTitle = response.optString("title")
                prevDescription = response.optString("description")
            },
            Response.ErrorListener { error ->
                // Log the error for debugging
                Log.e("LoadCourseDetails", "Error loading course details", error)

                // Handle errors when loading course details
                Toast.makeText(this@UpdateCoursActivity, "Error loading course details", Toast.LENGTH_SHORT).show()
            })

        queue.add(jsonObjectRequest)
    }

    private fun updateCourse() {
        val url = "http://192.168.56.1:8080/courses/$courseId"

        val updatedTitle = editTextTitle.text.toString()
        val updatedDescription = editTextDescription.text.toString()

        val params = JSONObject()
        params.put("title", updatedTitle)
        params.put("description", updatedDescription)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, params,
            Response.Listener { response ->
                // Handle the course update response
                Toast.makeText(this@UpdateCoursActivity, "Update successful: $response", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@UpdateCoursActivity, AffichageCourActivity::class.java)
                startActivity(intent)
                finish()
            },
            Response.ErrorListener { error ->
                // Log the error for debugging
                Log.e("UpdateCourse", "Error updating course", error)

                // Handle errors when updating the course
                Toast.makeText(this@UpdateCoursActivity, "Error updating course", Toast.LENGTH_SHORT).show()

                // Restore previous values on error
                editTextTitle.setText(prevTitle)
                editTextDescription.setText(prevDescription)
            })

        queue.add(jsonObjectRequest)
    }
}