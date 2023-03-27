package com.example.gestionnairesante.ui.repas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.ViewPagerAdapter
import com.example.gestionnairesante.adapter.ViewPagerChartsAdapter
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.PlatData
import com.example.gestionnairesante.database.dao.PlatRepo
import com.example.gestionnairesante.database.dao.glycemie.GlycemieRepo
import com.example.gestionnairesante.database.viewmodels.*
import com.example.gestionnairesante.databinding.RepasBinding
import com.example.gestionnairesante.ui.diabete.*
import com.google.android.material.tabs.TabLayout

class RepasFragment : Fragment() {
    private var binding: RepasBinding? = null
    private lateinit var viewModel: VMPLat
    //private lateinit var viewModelinsuline: VMInsuline

    private lateinit var tablayoutTabs: TabLayout
    private lateinit var viewPagerTabs: ViewPager
    private lateinit var viewPagerCharts: ViewPager

    private var ind = 0

    private var arrayTab = arrayListOf<Int>(R.string.txt_fragtab1, R.string.txt_fragtab2)
    private var arrayFragTab = arrayListOf<Fragment>(DiabeteTab1(), DiabeteTab2())

    private var arrayTabCharts =
        arrayListOf<Int>(R.string.txt_chart1, R.string.txt_chart2, R.string.txt_chart3)
    private var arrayFragChart = arrayListOf<Fragment>(DiabeteChartLine(), DiabeteChartPie())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val repasFrag = RepasBinding.inflate(inflater, container, false)
        binding = repasFrag
        return repasFrag.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            //viewModelinsuline = viewModelinsuline
            binding?.fragRepas = this@RepasFragment

        }
        // databinding
        val dao = DB_sante.getInstance(requireContext()).tabPlat
        //val dao2 = DB_sante.getInstance(requireContext()).tabInsuline

        val repository = PlatRepo(dao)
        //val repositoryInsuline = InsulineRepo(dao2)

        val factory = VMPlatFactory(repository)
       // val factoryInsuline = VMInsulineFactory(repositoryInsuline)

        viewModel = ViewModelProvider(this, factory).get(VMPLat::class.java)
        //viewModelinsuline = ViewModelProvider(this, factoryInsuline).get(VMInsuline::class.java)

        val tabPlat = ArrayList<PlatData>()
        val tabInsulineRapide = ArrayList<Int>()
        val tabInsulineLente = ArrayList<Int>()

        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                //Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getallPlat().observe(viewLifecycleOwner) { it ->
            tabPlat.clear()
            tabPlat.addAll(it)
            //recupDataBarChart()
            //binding.chart0.invalidate()
        }

        binding!!.btnPopulate.setOnClickListener {
            val plat1 = PlatData(0,"croissant", 40,40)
            val plat2 = PlatData(0,"café", 0,0)
            val plat3 = PlatData(0,"sandwich", 40,240)
            val plat4 = PlatData(0,"soupe", 5,18)

            viewModel.insertPlat(plat1)
            viewModel.insertPlat(plat2)
            viewModel.insertPlat(plat3)
            viewModel.insertPlat(plat4)


        }

        binding!!.btnInsert.setOnClickListener {
            RepasDialogPlat.newInstance("titre", "subtitre", ind)
                .show(childFragmentManager, RepasDialogPlat.TAG)
            //Toast.makeText(requireContext(), "youhou", Toast.LENGTH_LONG).show()
        }

        viewPagerCharts = binding?.viewpagercharts!!

        tablayoutTabs = binding?.tabLayout!!
        viewPagerTabs = binding?.viewpagertabhost!!

        configTablelayout(arrayTab)
        configViewPagerChart(viewPagerCharts, arrayFragChart, arrayTabCharts)
        configViewPager(viewPagerTabs, arrayFragTab, arrayTab, tablayoutTabs)

    }


    fun configTablelayout(array: ArrayList<Int>) {
        tablayoutTabs.apply {
            for (i in array.indices) {
                tablayoutTabs.addTab(tablayoutTabs.newTab().setText(array[i]))
            }

            tablayoutTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        viewPagerTabs.currentItem = it

                        when (tab?.position) {
                            0 -> {
                                binding!!.btnInsert.visibility = View.VISIBLE
                                binding!!.btnInsertRepas.visibility = View.INVISIBLE
                            }
                            1 -> {
                                binding!!.btnInsert.visibility = View.INVISIBLE
                                binding!!.btnInsertRepas.visibility = View.VISIBLE
                            }
                            else -> {
                                binding!!.btnInsert.visibility = View.VISIBLE
                                binding!!.btnInsertRepas.visibility = View.INVISIBLE
                            }
                        }

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

        }
    }

    fun configViewPager(
        viewPager: ViewPager,
        arrayFrag: ArrayList<Fragment>,
        arrayTab: ArrayList<Int>,
        tablayout: TabLayout
    ) {
        viewPager.apply {
            viewPager.adapter = ViewPagerAdapter(
                arrayFrag, arrayTab,
                childFragmentManager, tablayout.tabCount, context
            )
        }
        tablayout.setupWithViewPager(viewPager, true)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    fun configViewPagerChart(
        viewPager: ViewPager,
        arrayFrag: ArrayList<Fragment>,
        arrayTab: ArrayList<Int>
    ) {
        viewPager.apply {
            viewPager.adapter = ViewPagerChartsAdapter(
                arrayFrag, arrayTab,
                childFragmentManager, context
            )
        }
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}