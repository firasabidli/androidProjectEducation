package com.example.platform_education
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CourseAdapter(private val context: Context, private val courses: JSONArray) :
    RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewFile: TextView = itemView.findViewById(R.id.textViewFile)
        val editButton: Button = itemView.findViewById(R.id.btnedit)
        val deleteButton: Button = itemView.findViewById(R.id.btndelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val courseObject: JSONObject = courses.getJSONObject(position)
            val title = courseObject.getString("title")
            val description = courseObject.optString("description", "")  // Utilisez optString pour gérer les valeurs nulles
            val fichierPath = courseObject.getString("file")

            holder.textViewName.text = title
            holder.textViewDescription.text = description
            holder.textViewFile.text = fichierPath
        holder.editButton.setOnClickListener {
            try {
                val courseId = courseObject.getString("_id")
                val intent = Intent(context, UpdateCoursActivity::class.java)
                intent.putExtra("COURSE_ID", courseId)
                context.startActivity(intent)
                Toast.makeText(
                    context,
                    "Clicked Edit for ${courseObject.getString("title")}",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(context, "Error editing course: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "An unexpected error occurred", Toast.LENGTH_SHORT).show()
            }
        }
        holder.deleteButton.setOnClickListener {
            // Handle delete button click
            val courseId = courseObject.getString("_id")
            deleteCourse(courseId, position)
        }
    }
    private fun deleteCourse(courseId: String, position: Int) {
        val url = "http://192.168.56.1:8080/courses/$courseId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, null,
            Response.Listener { response ->
                // Handle the course deletion response
                // Remove the item from the list and notify the adapter
                courses.remove(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
                Toast.makeText(context, "Course deleted successfully", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Handle errors when deleting the course
                Toast.makeText(context, "Error deleting course", Toast.LENGTH_SHORT).show()
            })

        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(jsonObjectRequest)
    }


    override fun getItemCount(): Int {
        return courses.length()
    }
}

