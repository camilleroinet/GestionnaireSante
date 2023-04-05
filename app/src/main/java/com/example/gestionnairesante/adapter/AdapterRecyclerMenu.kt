package com.example.gestionnairesante.adapter

import android.annotation.SuppressLint
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
        val binding: RepasCardviewMenuBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.repas_cardview_menu, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): MenuData {
        return menuList.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(menuList[position], clickListener)
    }

    fun setList(daousers: List<MenuData>) {
        menuList.clear()
        menuList.addAll(daousers)
    }

    class MyViewHolder(val binding: RepasCardviewMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataNote: MenuData, clickListener: (MenuData) -> Unit) {
            binding.nomMenu.text = dataNote.nom_menu


            binding.itemLayout3.setOnClickListener {
                clickListener(dataNote)
            }
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
}