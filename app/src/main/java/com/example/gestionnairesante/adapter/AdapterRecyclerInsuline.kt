package com.example.gestionnairesante.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.databinding.DiabeteCardviewInsulineBinding

class AdapterRecyclerInsuline (private val clickListener: (InsulineData) -> Unit) : RecyclerView.Adapter<AdapterRecyclerInsuline.MyViewHolder>(){
    private val dataList = ArrayList<InsulineData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: DiabeteCardviewInsulineBinding = DataBindingUtil.inflate(layoutInflater, R.layout.diabete_cardview_insuline, parent, false)
        return MyViewHolder(binding)
    }

    fun getDbObjet(position: Int): InsulineData {
        return dataList.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.bind(dataList[position], clickListener)
    }

    fun setList(daousers: List<InsulineData>){
        dataList.clear()
        dataList.addAll(daousers)
    }
/*    fun setListNotes(dataNotes: List<DataNote>){
        listeNote.clear()
        listeNote.addAll(dataNotes)
    }*/

    class MyViewHolder(val binding: DiabeteCardviewInsulineBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: InsulineData, clickListener: (InsulineData) -> Unit){

            if(data.insuline_lente == 0){
                binding.llLente.visibility = View.INVISIBLE
            }
            if(data.insuline_rapide == 0){
                binding.llRapide.visibility = View.INVISIBLE
            }

            binding.rapide.text = data.insuline_rapide.toString()
            binding.lente.text = data.insuline_lente.toString()

            binding.itemLayout2.setOnClickListener{
                clickListener(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}