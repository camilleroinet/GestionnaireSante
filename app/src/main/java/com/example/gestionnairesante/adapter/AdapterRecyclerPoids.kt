package com.example.gestionnairesante.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.databinding.PoidsCardviewBinding

class AdapterRecyclerPoids (private val clickListener: (PoidsData) -> Unit) : RecyclerView.Adapter<AdapterRecyclerPoids.MyViewHolder>(){

    private val noteList = ArrayList<PoidsData>()
    private val listeNote = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: PoidsCardviewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.poids_cardview, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): PoidsData {
        return noteList.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.bind(noteList[position], clickListener)
    }

    fun setList(daousers: List<PoidsData>){
        noteList.clear()
        noteList.addAll(daousers)
    }
/*    fun setListNotes(dataNotes: List<DataNote>){
        listeNote.clear()
        listeNote.addAll(dataNotes)
    }*/

    class MyViewHolder(val binding: PoidsCardviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dataNote: PoidsData, clickListener: (PoidsData) -> Unit){


            binding.poids.text = dataNote.valeur_poids.toString()
            //binding.periodegly.text = dataNote.periode.libelle_periode.toString()

            binding.itemLayout2.setOnClickListener{
                clickListener(dataNote)
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}