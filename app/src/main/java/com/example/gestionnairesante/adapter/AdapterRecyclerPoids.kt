package com.example.gestionnairesante.adapter

import android.content.ClipData
import android.content.ClipDescription
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.databinding.PoidsCardviewBinding

class AdapterRecyclerPoids (private val longclickListener: (View) -> Unit) : RecyclerView.Adapter<AdapterRecyclerPoids.MyViewHolder>(){

    private val noteList = ArrayList<PoidsData>()
    var maposition : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: PoidsCardviewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.poids_cardview, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): PoidsData {
        return noteList.get(position)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        maposition = position
        holder.bind(noteList[position], longclickListener)
    }

    fun setList(daousers: List<PoidsData>){
        noteList.clear()
        noteList.addAll(daousers)
    }

    class MyViewHolder(val binding: PoidsCardviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dataNote: PoidsData, longclickListener: (View) -> Unit){
            binding.poids.text = dataNote.valeur_poids.toString()
            //binding.periodegly.text = dataNote.periode.libelle_periode.toString()

            binding.itemLayout2.setOnLongClickListener(){
                longclickListener(it)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}