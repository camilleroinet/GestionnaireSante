package com.example.gestionnairesante.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.databinding.RepasCardviewMenuBinding

class AdapterRecyclerMenu(private val clickListener: (MenuData) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerMenu.MyViewHolder>() {
    private val menuList = ArrayList<MenuData>()

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
        fun bind(data: MenuData, clickListener: (MenuData) -> Unit) {
            binding.nomMenu.text = data.nom_menu
            binding.glucides.text = data.totalGly.toString()
            binding.calories.text = data.totalCal.toString()
            binding.nbPlat.text = data.totalPlat.toString()

            binding.itemLayout3.setOnClickListener() {
                clickListener(data)
            }

        }
    }

    fun getDbObjet(position: Int): MenuData {
        return menuList.get(position)
    }
    override fun getItemCount(): Int {
        return menuList.size
    }
    fun setList(data: List<MenuData>) {
        menuList.clear()
        menuList.addAll(data)
    }
}