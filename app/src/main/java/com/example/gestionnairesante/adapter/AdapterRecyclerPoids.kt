package com.example.gestionnairesante.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.databinding.PoidsCardviewBinding

class AdapterRecyclerPoids(private val clickListener: (PoidsData) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerPoids.MyViewHolder>() {

    private val poidsList = ArrayList<PoidsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: PoidsCardviewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.poids_cardview, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): PoidsData {
        return poidsList[position]
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(poidsList[position], clickListener)
    }

    class MyViewHolder(val binding: PoidsCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PoidsData, clickListener: (PoidsData) -> Unit) {
            val id = data.id_poids
            binding.poids.text = data.valeur_poids.toString()

            binding.itemLayout2.setOnClickListener {
                clickListener(data)
            }
        }
    }

    fun setList(daousers: List<PoidsData>) {
        poidsList.clear()
        poidsList.addAll(daousers)
    }

    override fun getItemCount(): Int {
        return poidsList.size
    }
}