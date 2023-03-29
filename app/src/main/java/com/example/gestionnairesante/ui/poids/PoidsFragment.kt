package com.example.gestionnairesante.ui.poids

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterRecyclerPoids
import com.example.gestionnairesante.adapter.AdapterViewPagerCharts
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.database.dao.poids.PoidsRepo
import com.example.gestionnairesante.database.viewmodels.VMPoids
import com.example.gestionnairesante.database.viewmodels.VMPoidsFactory
import com.example.gestionnairesante.databinding.PoidBinding
import com.example.gestionnairesante.utils.createBarChart
import com.github.mikephil.charting.data.BarEntry
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt


class PoidsFragment : Fragment() {
    private var binding: PoidBinding? = null
    private lateinit var viewModel: VMPoids
    private lateinit var adapter: AdapterRecyclerPoids
    private var ind = 0
    private var lastPoua = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val poidsFrg = PoidBinding.inflate(inflater, container, false)
        binding = poidsFrg

        //viewModel.recupLastPoids()
        return poidsFrg.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.fragPoids = this@PoidsFragment
        }



        // databinding
        val dao = DB_sante.getInstance(requireContext()).tabPoids
        val repository = PoidsRepo(dao)
        val factory = VMPoidsFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(VMPoids::class.java)

        val tabPoids = ArrayList<Float>()
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
            //Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }


        viewModel.getValeurPoids().observe(viewLifecycleOwner) { it ->
            tabPoids.clear()
            tabPoids.addAll(it)
            recupDataBarChart()
            val p = tabPoids.size-1
            if(p < 0) {
                Toast.makeText(requireContext(), "Aucunes données",Toast.LENGTH_SHORT).show()
            }else{
                lastPoua = tabPoids.get(p)
                val imc = calculerIMC(171,lastPoua)
                binding!!.rere.text = calculerIMC(171,lastPoua).toString()
                verifIMC(imc)
            }

            //recupDataBarChart()
            //binding.chart0.invalidate()
        }

       // Toast.makeText(requireContext(), "monpoids ="+viewModel.inputLastPoid.value,Toast.LENGTH_LONG).show()

        binding!!.btnPopulate.setOnClickListener {
            val poids1 = PoidsData(0, 66.0F)
            val poids2 = PoidsData(0, 68.0F)
            val poids3 = PoidsData(0, 122.0F)
            val poids4 = PoidsData(0, 66.0F)

            viewModel.insertPoids(poids1)
            viewModel.insertPoids(poids2)
            viewModel.insertPoids(poids3)
            viewModel.insertPoids(poids4)

        }

        binding!!.btnInsert.setOnClickListener{
            PoidsDialog.newInstance("titre", "subtitre", ind).show(childFragmentManager, PoidsDialog.TAG)
            //Toast.makeText(requireContext(), "youhou", Toast.LENGTH_LONG).show()
        }

        initRecycler()
        displayUser()
        touchRecycler()


        //viewModel.afficherLastPoids()
            //val d = viewModel.inputLastPoid.value





    }

    fun calculerIMC(taille: Int, poids: Float) : Float {
        //  IMC = poids en kg/taille²
        val poids = poids.toDouble()
        val taille = (taille.toDouble() /100)

        return  (poids / (taille*taille)).toFloat()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun verifIMC(imc : Float){
        if(imc <= 18.5F){
            binding?.imgImc?.setImageDrawable(getResources().getDrawable(R.drawable.imc1))
            binding?.resultat?.text = "Poids insuffisant"
        }
        if(imc > 18.5F && imc < 25){
            binding?.imgImc?.setImageDrawable(getResources().getDrawable(R.drawable.imc2))
            binding?.resultat?.text = "Poids normal"
        }
        if(imc >= 25F && imc < 30){
            binding?.imgImc?.setImageDrawable(getResources().getDrawable(R.drawable.imc3))
            binding?.resultat?.text = "Surpoids"
        }
        if(imc >= 30F && imc < 35){
            binding?.imgImc?.setImageDrawable(getResources().getDrawable(R.drawable.imc4))
            binding?.resultat?.text = "Obésité"
        }
        if(imc >= 35F){
            binding?.imgImc?.setImageDrawable(getResources().getDrawable(R.drawable.imc5))
            binding?.resultat?.text = "Obésité morbide"
        }

    }
    fun initRecycler(){
        // Configuration du layout
        binding?.recyclerPoids?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapter = AdapterRecyclerPoids { daouser: PoidsData -> listItemClicked(viewModel, daouser)}
        binding?.recyclerPoids?.adapter = adapter

    }
    fun listItemClicked(viewModel: VMPoids, daouser: PoidsData){
        viewModel.initUpdateAndDelete(daouser)
        viewModel.clearallOrdelete()
    }

    fun displayUser(){
        viewModel.getAllPoids().observe(viewLifecycleOwner, Observer {
            //Toast.makeText(requireContext(), "size ==>> ${it.size}", Toast.LENGTH_LONG).show()
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = binding?.recyclerPoids?.adapter as AdapterRecyclerPoids
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    //DialogFragHomeSuppr.newInstance("titre", "subtitre", ind).show(childFragmentManager, DialogFragHomeSuppr.TAG)
                    viewModel.deleteGlycemie(obj)
                }

                override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == ItemTouchHelper.RIGHT){
                        viewHolder?.itemView?.alpha = 0.5F
                    }
                }

                override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.alpha = 1.0F
                }
            }
            ItemTouchHelper(simplecall)
        }
        itemTouchHelper.attachToRecyclerView(binding?.recyclerPoids)
    }
    fun configViewPagerChart(
        viewPager: ViewPager,
        arrayFrag: ArrayList<Fragment>,
        arrayTab: ArrayList<Int>){
        viewPager.apply {
            viewPager.adapter = AdapterViewPagerCharts(
                arrayFrag, arrayTab,
                childFragmentManager, context)
        }
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun recupDataBarChart(): ArrayList<BarEntry>{
        val valu = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Float>()
        val barChart = binding!!.chart0
        val stringValue = ArrayList<String>()

        viewModel.getValeurPoids().observe(viewLifecycleOwner){
            tabValeur.clear()
            tabValeur.addAll(it)

            val r = tabValeur.size - 1

            for (i in 0..r){
                stringValue.add("")
                valu.add(BarEntry(i.toFloat(), tabValeur[i].toFloat()))
            }
            createBarChart(barChart,valu,stringValue ,"Poids"  )
        }

        return valu
    }



}