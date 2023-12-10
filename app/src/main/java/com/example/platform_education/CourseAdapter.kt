package com.example.platform_education
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CourseAdapter(private val context: Context, private val courses: JSONArray) :
    RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewFile: TextView = itemView.findViewById(R.id.textViewFile)
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


    }



    override fun getItemCount(): Int {
        return courses.length()
    }
}

