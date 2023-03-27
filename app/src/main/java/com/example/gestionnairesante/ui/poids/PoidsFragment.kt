package com.example.gestionnairesante.ui.poids

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterRecyclerPoids
import com.example.gestionnairesante.adapter.ViewPagerChartsAdapter
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.PoidsData
import com.example.gestionnairesante.database.dao.PoidsRepo
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.viewmodels.VMPoids
import com.example.gestionnairesante.database.viewmodels.VMPoidsFactory
import com.example.gestionnairesante.databinding.PoidsBinding


class PoidsFragment : Fragment() {
    private var binding: PoidsBinding? = null
    private lateinit var viewModel: VMPoids
    private lateinit var adapter: AdapterRecyclerPoids
    private lateinit var viewPagerCharts: ViewPager

    private var ind = 0

    private var arrayTabCharts = arrayListOf<Int>(R.string.txt_chart1, R.string.txt_chart2, R.string.txt_chart3)
    private var arrayFragChart = arrayListOf<Fragment>(PoidsChartBar())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val poidsFrag = PoidsBinding.inflate(inflater, container, false)
        binding = poidsFrag
        return poidsFrag.root
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
            initRecycler()
            //recupDataBarChart()
            //binding.chart0.invalidate()
        }

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

        viewPagerCharts = binding?.viewpagercharts!!

        configViewPagerChart(viewPagerCharts, arrayFragChart, arrayTabCharts)
        initRecycler()
        displayUser()
        touchRecycler()
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
            viewPager.adapter = ViewPagerChartsAdapter(
                arrayFrag, arrayTab,
                childFragmentManager, context)
        }
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }




}