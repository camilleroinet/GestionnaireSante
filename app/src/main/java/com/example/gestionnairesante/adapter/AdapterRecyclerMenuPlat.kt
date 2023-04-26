package com.example.gestionnairesante.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.innerPlat.PlatInner
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasCardviewmListeplatBinding

class AdapterRecyclerMenuPlat (
    private val clickListener: (PlatInner) -> Unit ) : RecyclerView.Adapter<AdapterRecyclerMenuPlat.MyViewHolder>() {

    private val platList = ArrayList<PlatInner>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RepasCardviewmListeplatBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.repas_cardviewm_listeplat, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): PlatInner {
        return platList.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(platList[position], clickListener)
    }

    fun setList(daousers: List<PlatInner>) {
        platList.clear()
        platList.addAll(daousers)
    }

    class MyViewHolder(val binding: RepasCardviewmListeplatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PlatInner, clickListener: (PlatInner) -> Unit) {

            binding.nomPlat.text = data.nomPlat

            binding.itemLayout2.setOnClickListener {
                clickListener(data)

            }
        }
    }

    override fun getItemCount(): Int {
        return platList.size
    }
}