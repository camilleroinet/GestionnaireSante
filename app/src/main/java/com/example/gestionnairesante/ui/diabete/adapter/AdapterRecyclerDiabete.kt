package com.example.gestionnairesante.ui.diabete.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.innerDiabete.DataInner

import com.example.gestionnairesante.databinding.DiabeteCardviewBinding

class AdapterRecyclerDiabete(private val clickListener: (DataInner) -> Unit) : RecyclerView.Adapter<AdapterRecyclerDiabete.MyViewHolder>() {

    private val dataList = ArrayList<DataInner>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: DiabeteCardviewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.diabete_cardview, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position], clickListener)
    }

    class MyViewHolder(val binding: DiabeteCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataInner, clickListener: (DataInner) -> Unit) {
            val valeur = data.glycemie

            binding.couleurAlerte.setBackgroundColor(checkGly(valeur))
            binding.gly.text = data.glycemie.toString()
            binding.dateGlycemie.text = data.date
            binding.rapide.text = data.rapide.toString()
            binding.lente.text = data.lente.toString()

            binding.itemLayout2.setOnClickListener {
                clickListener(data)
            }
        }

        fun checkGly(valeur: Int) : Int{
            var intColor = 0
            if(valeur in 0..79) {
                return Color.CYAN
            } else if(valeur in 80..179) {
                return Color.GREEN
            } else if (valeur in 180..249) {
                return Color.YELLOW
            } else {
                return Color.RED
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getDbObjet(position: Int): DataInner {
        return dataList.get(position)
    }

    fun update(position: Int){
        dataList[position] = getDbObjet(position)
        dataList.removeAt(position)
        dataList.add(position, getDbObjet(position))
        notifyItemChanged(position)
    }

    fun remove(position: Int){
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setList(data: List<DataInner>) {
        dataList.clear()
        dataList.addAll(data)
    }

}
