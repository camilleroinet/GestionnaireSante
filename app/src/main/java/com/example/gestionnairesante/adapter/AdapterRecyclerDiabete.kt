package com.example.gestionnairesante.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.innerDiabete.DataInner

import com.example.gestionnairesante.databinding.DiabeteCardviewBinding

class AdapterRecyclerDiabete(private val clickListener: (DataInner) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerDiabete.MyViewHolder>() {
    private val dataList = ArrayList<DataInner>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: DiabeteCardviewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.diabete_cardview, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): DataInner {
        return dataList.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position], clickListener)
    }

    fun setList(daousers: List<DataInner>) {
        dataList.clear()
        dataList.addAll(daousers)
    }

    class MyViewHolder(val binding: DiabeteCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataInner, clickListener2: (DataInner) -> Unit) {
            val valeur = data.glycemie
            if (valeur in 0..79) {
                binding.couleurAlerte.setBackgroundColor(Color.CYAN)
            }
            if (valeur in 80..179) {
                binding.couleurAlerte.setBackgroundColor(Color.GREEN)
            }
            if (valeur in 180..249) {
                binding.couleurAlerte.setBackgroundColor(Color.YELLOW)
            }
            if (valeur in 250..999) {
                binding.couleurAlerte.setBackgroundColor(Color.RED)
            }
            binding.gly.text = data.glycemie.toString()
            binding.periode.text = data.periode.toString()
            binding.heure.text = data.heure.toString()
            binding.dateGlycemie.text = data.date.toString()
            binding.rapide.text = data.rapide.toString()
            binding.lente.text = data.lente.toString()

            binding.itemLayout2.setOnClickListener {
                clickListener2(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}