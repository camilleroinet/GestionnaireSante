package com.example.gestionnairesante.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.databinding.DiabeteCardviewInsulineBinding

class AdapterRecyclerInsuline (private val clickListener: (InsulineData) -> Unit) : RecyclerView.Adapter<AdapterRecyclerInsuline.MyViewHolder>(){

    private val noteList = ArrayList<InsulineData>()
    private val listeNote = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: DiabeteCardviewInsulineBinding = DataBindingUtil.inflate(layoutInflater, R.layout.diabete_cardview_insuline, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): InsulineData {
        return noteList.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.bind(noteList[position], clickListener)
    }

    fun setList(daousers: List<InsulineData>){
        noteList.clear()
        noteList.addAll(daousers)
    }
/*    fun setListNotes(dataNotes: List<DataNote>){
        listeNote.clear()
        listeNote.addAll(dataNotes)
    }*/

    class MyViewHolder(val binding: DiabeteCardviewInsulineBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dataNote: InsulineData, clickListener: (InsulineData) -> Unit){


            binding.poids.text = dataNote.insuline_rapide.toString()
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