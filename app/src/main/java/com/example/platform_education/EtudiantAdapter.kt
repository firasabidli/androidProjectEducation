package com.example.platform_education

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EtudiantAdapter(private val etudiantList: List<Etudiant>) :
    RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder>() {

    class EtudiantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewNom)
        val textViewNumInscrit: TextView = itemView.findViewById(R.id.textViewNumInscrit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtudiantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_etudiant, parent, false)
        return EtudiantViewHolder(view)
    }

    override fun onBindViewHolder(holder: EtudiantViewHolder, position: Int) {
        val etudiant = etudiantList[position]

        holder.textViewName.text = etudiant.Name
        holder.textViewNumInscrit.text = "Numéro d'inscription : ${etudiant.NumInscrit}"
    }

    override fun getItemCount(): Int {
        return etudiantList.size
    }
}
