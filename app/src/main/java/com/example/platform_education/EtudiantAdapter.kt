// EtudiantAdapter.kt
package com.example.platform_education

import EtudiantDiffCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class EtudiantAdapter(private var etudiantList: MutableList<Etudiant>) :
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
        holder.checkBoxAccepter.isChecked = (etudiant.Etat == 1)
        holder.checkBoxAccepter.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // If CheckBox is checked, update Etat to 1
//                etudiant.Etat = 1
//            } else {
//                // If CheckBox is unchecked, update Etat to 0
//                etudiant.Etat = 0
//            }

            // Notify the activity about the state change
            (holder.itemView.context as? ProfileAdminActivity)?.updateEtudiantState(
                etudiant.NumInscrit,
                if (isChecked) 1 else 0,
                etudiantList
            )
            }
    }

    override fun getItemCount(): Int = etudiantList.size

    fun updateEtudiantList(newList: List<Etudiant>) {
        val diffCallback = EtudiantDiffCallback(etudiantList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        etudiantList.clear()
        etudiantList.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    class EtudiantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numTextView: TextView = itemView.findViewById(R.id.textViewNumInscrit)
        val nomTextView: TextView = itemView.findViewById(R.id.textViewName)
        val checkBoxAccepter: CheckBox = itemView.findViewById(R.id.checkBox)
    }
}
