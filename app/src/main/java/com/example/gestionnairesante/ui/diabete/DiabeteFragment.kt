package com.example.gestionnairesante.ui.diabete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.MainActivity
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterViewPager
import com.example.gestionnairesante.adapter.AdapterViewPagerCharts
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteRepo
import com.example.gestionnairesante.database.dao.insuline.ParamStyloData
import com.example.gestionnairesante.databinding.DiabeteBinding
import com.example.gestionnairesante.ui.diabete.service.DiabeteChartLine
import com.example.gestionnairesante.ui.diabete.service.DiabeteChartPie
import com.example.gestionnairesante.ui.diabete.service.DiabeteDialogGlycemie
import com.example.gestionnairesante.ui.diabete.service.DiabeteTab1
import com.example.gestionnairesante.ui.diabete.service.DiabeteTab2
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.ui.diabete.vm.VMDiabeteFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class DiabeteFragment : Fragment() {
    private var binding: DiabeteBinding? = null
    private lateinit var vmdiabete: VMDiabete

    private lateinit var tablayoutTabs: TabLayout
    private lateinit var viewPagerTabs: ViewPager
    private lateinit var viewPagerCharts: ViewPager

    private var arrayTab = arrayListOf(R.string.txt_fragtab1, R.string.txt_fragtab2)
    private var arrayFragTab = arrayListOf(DiabeteTab1(), DiabeteTab2())

    private var arrayTabCharts = arrayListOf(R.string.txt_chart1, R.string.txt_chart2, R.string.txt_chart3)
    private var arrayFragChart = arrayListOf(DiabeteChartLine(), DiabeteChartPie())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val diabeteFrag = DiabeteBinding.inflate(inflater, container, false)
        binding = diabeteFrag

        return diabeteFrag.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.fragDiabete = this@DiabeteFragment
        }

        // databinding
        val daoGlycemie = DB_sante.getInstance(requireContext()).tabGlycemie
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoInsuline = DB_sante.getInstance(requireContext()).tabInsuline
        val daoDiabete = DB_sante.getInstance(requireContext()).tabRelationnelDiabete
        val daoStylo = DB_sante.getInstance(requireContext()).tabStylo

        val repoDiabete = InnerDiabeteRepo(daoGlycemie, daoPeriode, daoInsuline, daoDiabete, daoStylo)
        val factoryDiabete = VMDiabeteFactory(repoDiabete)
        vmdiabete = ViewModelProvider(requireActivity(), factoryDiabete).get(VMDiabete::class.java)
        binding?.vmdiabete = vmdiabete

        lifecycleScope.launch {
            lifecycle.withStarted {
                val fab: FloatingActionButton = (activity as MainActivity).findViewById(R.id.fab)
                fab.show()
                fab.setOnClickListener {
                    val diabeteDialogGlycemie = DiabeteDialogGlycemie()
                    diabeteDialogGlycemie.show(childFragmentManager, DiabeteDialogGlycemie.TAG)
                }
            }
        }

        vmdiabete.nbStylo.value = vmdiabete.getAllStylo()
        if (vmdiabete.nbStylo.value == 0) {
            vmdiabete.insertStylo(ParamStyloData(0, 300, 300, 2))
            vmdiabete.insertStylo(ParamStyloData(0, 500, 500, 2))
        }

        vmdiabete.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        /**
         * view binding pour les view pagers et le tab host
         */
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

    fun configViewPagerChart(
        viewPager: ViewPager,
        arrayFrag: ArrayList<Fragment>,
        arrayTab: ArrayList<Int>
    ) {
        viewPager.apply {
            viewPager.adapter = AdapterViewPagerCharts(
                arrayFrag, arrayTab,
                childFragmentManager, context
            )
        }
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

}
