// EtudiantAdapter.kt
package com.example.platform_education

import EtudiantDiffCallback
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class EtudiantAdapter(
    private var etudiantList: MutableList<Etudiant>,
    private val onRejectClickListener: (Etudiant) -> Unit
) : RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtudiantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_etudiant, parent, false)
        return EtudiantViewHolder(view)
    }

    private var onItemClickListener: ((Etudiant) -> Unit)? = null

    fun setOnItemClickListener(listener: (Etudiant) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: EtudiantViewHolder, position: Int) {
        val etudiant = etudiantList[position]

        holder.numTextView.text = "${etudiant.NumInscrit}"
        holder.nomTextView.text = "${etudiant.Name}"

        // Enable the button based on the current state
        holder.checkBoxAccepter.isEnabled = etudiant.Etat != 1
        holder.itemView.setOnClickListener {
            val etudiant = etudiantList[position]
            onItemClickListener?.invoke(etudiant)
        }
        holder.checkBoxAccepter.setOnClickListener {
            val updatedEtudiant = etudiant.copyWithNewEtat(1)
            showNotification(
                holder.itemView.context,
                "permission SIGN IN ",
                "You are accepted"
            )

            // Debugging Toast
            (holder.itemView.context as? ProfileAdminActivity)?.runOnUiThread {
                Toast.makeText(
                    holder.itemView.context,
                    "Before Update - NumInscrit: ${etudiant.NumInscrit}, Etat: ${etudiant.Etat}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Update the button state
            updateButton(holder.checkBoxAccepter, updatedEtudiant.Etat)

            // Debugging Toast
            (holder.itemView.context as? ProfileAdminActivity)?.runOnUiThread {
                Toast.makeText(
                    holder.itemView.context,
                    "After Update - NumInscrit: ${etudiant.NumInscrit}, Etat: ${updatedEtudiant.Etat}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Notify the activity about the state change
            (holder.itemView.context as? ProfileAdminActivity)?.updateEtudiantState(
                etudiant.NumInscrit,
                updatedEtudiant.Etat,
                etudiantList
            )
        }

        holder.rejeter.setOnClickListener {
            // Invoke the callback when "Reject" button is clicked
            onRejectClickListener.invoke(etudiant)
        }
    }

    override fun getItemCount(): Int = etudiantList.size

    class EtudiantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numTextView: TextView = itemView.findViewById(R.id.textViewNumInscrit)
        val nomTextView: TextView = itemView.findViewById(R.id.textViewName)
        val checkBoxAccepter: Button = itemView.findViewById(R.id.checkBox)
        val rejeter: Button = itemView.findViewById(R.id.rejeter)
    }

    fun updateEtudiantList(newList: List<Etudiant>) {
        val diffCallback = EtudiantDiffCallback(etudiantList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        etudiantList.clear()
        etudiantList.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    private fun updateButton(button: Button, etat: Int) {
        // Assume you have a function to determine the button state based on etat
        val buttonState = determineButtonState(etat)

        // Update the button state based on the logic
        updateButtonState(button, buttonState)
    }

    private fun determineButtonState(etat: Int): ButtonState {
        return if (etat == 1) ButtonState.DISABLED else ButtonState.ENABLED
    }

    private fun updateButtonState(button: Button, buttonState: ButtonState) {
        button.isEnabled = buttonState == ButtonState.ENABLED
    }

    enum class ButtonState {
        ENABLED,
        DISABLED
    }

    fun removeEtudiant(etudiant: Etudiant) {
        // Remove the student from the list and notify the adapter
        val position = etudiantList.indexOf(etudiant)
        if (position != -1) {
            etudiantList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Ajoutez cette constante dans l'adaptateur
    private val PERMISSION_REQUEST_CODE = 123 // Remplacez 123 par la valeur de votre choix

    // Modifiez la fonction showNotification dans l'adaptateur
    companion object {
        fun showNotification(context: Context, title: String, message: String) {
            val notificationManager = NotificationManagerCompat.from(context)

            // Vérifier la permission VIBRATE avant d'afficher la notification
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.VIBRATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val channel = NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(R.drawable.baseline_notifications_active_24)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                notificationManager.notify(1, channel)
            } else {
                // Si la permission n'est pas accordée, demandez-la
                // Cela nécessite une activité pour demander la permission, assurez-vous donc d'appeler cette fonction depuis une activité.
                val PERMISSION_REQUEST_CODE = 123
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.VIBRATE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }


    }
}



