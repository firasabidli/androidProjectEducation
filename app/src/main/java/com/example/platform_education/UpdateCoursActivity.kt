package com.example.platform_education
// UpdateCoursActivity.kt
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
            // Handle course update
            updateCourse()
        }
    }

    private fun loadCourseDetails() {
        val url = "http://192.168.56.1:8080/courses/$courseId"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, null,
            Response.Listener { response ->
                // Log the response to inspect the data
                Log.d("LoadCourseDetails", "Response: $response")

                // Load course details into the edit fields
                editTextTitle.setText(response.optString("title"))
                editTextDescription.setText(response.optString("description"))
            },
            Response.ErrorListener { error ->
                // Log the error for debugging
                Log.e("LoadCourseDetails", "Error loading course details", error)
                Toast.makeText(this@UpdateCoursActivity,"erreur:$error",Toast.LENGTH_SHORT).show()
                // Handle errors when loading course details
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
                Toast.makeText(this@UpdateCoursActivity, "Update successful: $response",Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Log the error for debugging
                Log.e("UpdateCourse", "Error updating course", error)

                // Handle errors when updating the course
            })

        queue.add(jsonObjectRequest)
    }


}
