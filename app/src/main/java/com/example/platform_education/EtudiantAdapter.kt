// EtudiantAdapter.kt
package com.example.platform_education

import EtudiantDiffCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class EtudiantAdapter(private var etudiantList: MutableList<Etudiant>,
                      private val onRejectClickListener: (Etudiant) -> Unit) :
    RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtudiantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_etudiant, parent, false)
        return EtudiantViewHolder(view)
    }

    override fun onBindViewHolder(holder: EtudiantViewHolder, position: Int) {
        val etudiant = etudiantList[position]

        holder.numTextView.text = "${etudiant.NumInscrit}"
        holder.nomTextView.text = "${etudiant.Name}"

        // Enable the button based on the current state
        holder.checkBoxAccepter.isEnabled = etudiant.Etat != 1

        holder.checkBoxAccepter.setOnClickListener{
            val updatedEtudiant = etudiant.copyWithNewEtat(1)

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
        val rejeter:Button=itemView.findViewById(R.id.rejeter)
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
}
