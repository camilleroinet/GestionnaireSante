package com.example.gestionnairesante.ui.repas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterViewPager
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerMenu.InnerPeriodeMenuRepo
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.databinding.RepasBinding
import com.example.gestionnairesante.ui.diabete.DiabeteChartLine
import com.example.gestionnairesante.ui.diabete.DiabeteChartPie
import com.example.gestionnairesante.ui.repas.vm.VmRepas
import com.example.gestionnairesante.ui.repas.vm.VmRepasFactory
import com.google.android.material.tabs.TabLayout

class RepasFragment : Fragment() {
    private var binding: RepasBinding? = null
    private lateinit var viewmodelrepas: VmRepas

    private lateinit var tablayoutTabs: TabLayout
    private lateinit var viewPagerTabs: ViewPager
    private lateinit var viewPagerCharts: ViewPager

    private var arrayTab = arrayListOf<Int>(R.string.txt_fragmenu, R.string.txt_fragrepas)
    private var arrayFragTab = arrayListOf<Fragment>(RepasTab1(), RepasTab2())

    private var arrayTabCharts = arrayListOf<Int>(R.string.txt_chart1, R.string.txt_chart2, R.string.txt_chart3)
    private var arrayFragChart = arrayListOf<Fragment>(DiabeteChartLine(), DiabeteChartPie())

    private var indiceTab = 0

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
            binding?.fragRepas = this@RepasFragment
        }

        // databinding
        val daoPlat = DB_sante.getInstance(requireContext()).tabPlat
        val daoMenu = DB_sante.getInstance(requireContext()).tabMenu
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoRepas = DB_sante.getInstance(requireContext()).tabRelationnelPlat
        val daoPeriodeMenu = DB_sante.getInstance(requireContext()).tabRelationnelMenu

        val repositoryPlatMenu = InnerPlatMenuRepo(daoPlat, daoMenu, daoRepas, daoPeriodeMenu)
        val repositoryPeriodeMenu = InnerPeriodeMenuRepo(daoMenu, daoPeriode, daoPeriodeMenu)

        val factory = VmRepasFactory(repositoryPlatMenu, repositoryPeriodeMenu)
        viewmodelrepas = ViewModelProvider(requireActivity(), factory).get(VmRepas::class.java)
        binding?.viewmodelrepas = viewmodelrepas

        viewmodelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        binding!!.btnInsertPlat.setOnClickListener {
            Toast.makeText(requireContext(), "coucou", Toast.LENGTH_SHORT).show()
            RepasDialogPlat.newInstance(
                "Nouveau Plat","" ,
            0, 0, "", 0, 0).show(childFragmentManager, RepasDialogPlat.TAG)
        }

        binding!!.btnInsertmenu.setOnClickListener {
            Toast.makeText(requireContext(), "coucou2", Toast.LENGTH_SHORT).show()

            RepasDialogMenu.newInstance(
                "Nouveau Menu","" ,
                0 ).show(childFragmentManager, RepasDialogMenu.TAG)
        }

        /*        binding!!.btnMenu.setOnClickListener {
                    binding!!.llVueChart.visibility = View.GONE
                    binding!!.vueMenu.visibility = View.VISIBLE
                    binding!!.llEtape01.visibility = View.VISIBLE
                    binding!!.llEtapeDate.visibility = View.GONE
                    binding!!.etapeInformation.visibility = View.GONE
                    binding!!.llEtape2.visibility = View.GONE
                }*/


/*

 */
        viewPagerCharts = binding?.viewpagercharts!!

        tablayoutTabs = binding?.tabLayout!!
        viewPagerTabs = binding?.viewpagertabhost!!

        configTablelayout(arrayTab)
        //configViewPagerChart(viewPagerCharts, arrayFragChart, arrayTabCharts)
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

                        when (tab.position) {
                            0 -> {
                                indiceTab = 0
                                binding!!.btnInsertPlat.visibility = View.GONE
                                binding!!.btnInsertmenu.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "coucou", Toast.LENGTH_SHORT)

                            }
                            1 -> {
                                indiceTab = 1
                                binding!!.btnInsertPlat.visibility = View.VISIBLE
                                binding!!.btnInsertmenu.visibility = View.GONE
                            }

                            else -> {}
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}

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
            viewPager.adapter = AdapterViewPager(
                arrayFrag, arrayTab,
                childFragmentManager, tablayout.tabCount, context
            )
        }
        tablayout.setupWithViewPager(viewPager, true)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

}