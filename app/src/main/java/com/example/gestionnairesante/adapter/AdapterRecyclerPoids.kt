package com.example.gestionnairesante.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.databinding.PoidsCardviewBinding

class AdapterRecyclerPoids(private val longclickListener: (View) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerPoids.MyViewHolder>() {

    private val poidsList = ArrayList<PoidsData>()
    private val id: Int = 0
    var maPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: PoidsCardviewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.poids_cardview, parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: PoidsCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataNote: PoidsData, longclickListener: (View) -> Unit) {
            val id = dataNote.id_poids
            binding.poids.text = dataNote.valeur_poids.toString()

        }
    }

    override fun onBindViewHolder(holder: AdapterRecyclerPoids.MyViewHolder, position: Int) {
        holder.bind(poidsList[position], longclickListener)
        holder.itemView.setOnLongClickListener() {
            maPos = holder.adapterPosition
            longclickListener(it)
            true
        }
    }

    fun setList(daousers: List<PoidsData>) {
        poidsList.clear()
        poidsList.addAll(daousers)
    }

    fun getId(): Int {
        return id
    }

    fun getDbObjet(position: Int): PoidsData {
        return poidsList[position]
    }

    override fun getItemCount(): Int {
        return poidsList.size
    }
}