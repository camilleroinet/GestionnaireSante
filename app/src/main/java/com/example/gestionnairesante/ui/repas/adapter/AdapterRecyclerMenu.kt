package com.example.gestionnairesante.ui.repas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.repas.RepasData
import com.example.gestionnairesante.databinding.RepasCardviewMenuBinding

class AdapterRecyclerMenu(private val clickListener: (RepasData) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerMenu.MyViewHolder>() {
    private val menuList = ArrayList<RepasData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RepasCardviewMenuBinding = DataBindingUtil.inflate(layoutInflater, R.layout.repas_cardview_menu, parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(menuList[position], clickListener)
    }

    class MyViewHolder(val binding: RepasCardviewMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RepasData, clickListener: (RepasData) -> Unit) {
            binding.nomMenu.text = data.nom_menu
            binding.glucides.text = data.totalGly.toString()
            binding.calories.text = data.totalCal.toString()
            binding.nbPlat.text = data.totalPlat.toString()

            binding.itemLayout3.setOnClickListener() {
                clickListener(data)
            }
        }
    }

    fun remove(position: Int){
        menuList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getDbObjet(position: Int): RepasData {
        return menuList.get(position)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    fun setList(data: List<RepasData>) {
        menuList.clear()
        menuList.addAll(data)
    }

}