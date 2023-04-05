package com.example.gestionnairesante.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData

import com.example.gestionnairesante.databinding.DiabeteCardviewBinding


class AdapterRecyclerDiabete(private val clickListener: (GlycemieData) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerDiabete.MyViewHolder>() {

    private val noteList = ArrayList<GlycemieData>()
    private val listeNote = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: DiabeteCardviewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.diabete_cardview, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): GlycemieData {
        return noteList.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(noteList[position], clickListener)
    }

    fun setList(daousers: List<GlycemieData>) {
        noteList.clear()
        noteList.addAll(daousers)
    }
/*    fun setListNotes(dataNotes: List<DataNote>){
        listeNote.clear()
        listeNote.addAll(dataNotes)
    }*/

    class MyViewHolder(val binding: DiabeteCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataNote: GlycemieData, clickListener2: (GlycemieData) -> Unit) {

            val valeur = dataNote.valeur_glycemie
            if (valeur in 0..79) {
                binding.couleurAlerte.setBackgroundColor(Color.CYAN)
            }
            if (valeur in 80..179) {
                binding.couleurAlerte.setBackgroundColor(Color.GREEN)
            }
            if (valeur in 180..249) {
                binding.couleurAlerte.setBackgroundColor(Color.YELLOW)
            }
            if (valeur in 249..999) {
                binding.couleurAlerte.setBackgroundColor(Color.RED)
            }

            binding.poids.text = dataNote.valeur_glycemie.toString()
            //binding.periodegly.text = dataNote.periode.libelle_periode.toString()

            binding.itemLayout2.setOnClickListener {
                clickListener2(dataNote)
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}