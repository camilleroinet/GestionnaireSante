package com.example.gestionnairesante.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasCardviewPlatBinding

class AdapterRecyclerPlat(private val clickListener: (PlatData) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerPlat.MyViewHolder>() {

    private val platList = ArrayList<PlatData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RepasCardviewPlatBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.repas_cardview_plat, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): PlatData {
        return platList.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(platList[position], clickListener)
    }

    fun setList(daousers: List<PlatData>) {
        platList.clear()
        platList.addAll(daousers)
    }

    class MyViewHolder(val binding: RepasCardviewPlatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PlatData, clickListener2: (PlatData) -> Unit) {

            binding.nomPlat.text = data.nom_plat.toString()
            binding.glucides.text = data.glucide_plat.toString()
            binding.calories.text = data.calorie_plat.toString()

            binding.itemLayout2.setOnClickListener {
                clickListener2(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return platList.size
    }
}