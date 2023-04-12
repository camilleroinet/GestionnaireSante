package com.example.gestionnairesante.ui.diabete

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
import com.example.gestionnairesante.adapter.AdapterViewPagerCharts
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.InnerDiabete.InnerDiabeteRepo
import com.example.gestionnairesante.database.dao.glycemie.GlycemieRepo
import com.example.gestionnairesante.database.dao.insuline.InsulineRepo
import com.example.gestionnairesante.database.viewmodels.glycemie.VMGlycemie
import com.example.gestionnairesante.database.viewmodels.glycemie.VMGlycemieFactory
import com.example.gestionnairesante.database.viewmodels.insuline.VMInsuline
import com.example.gestionnairesante.database.viewmodels.insuline.VMInsulineFactory
import com.example.gestionnairesante.databinding.DiabeteBinding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.ui.diabete.vm.VMDiabeteFactory
import com.google.android.material.tabs.TabLayout

class DiabeteFragment : Fragment() {
    private var binding: DiabeteBinding? = null
    private lateinit var viewModel: VMGlycemie
    private lateinit var viewModelinsuline: VMInsuline
    private lateinit var tablayoutTabs: TabLayout
    private lateinit var viewPagerTabs: ViewPager
    private lateinit var viewPagerCharts: ViewPager

    private var ind = 0

    private var arrayTab = arrayListOf<Int>(R.string.txt_fragtab1, R.string.txt_fragtab2)
    private var arrayFragTab = arrayListOf<Fragment>(DiabeteTab1(), DiabeteTab2())

    private var arrayTabCharts =
        arrayListOf<Int>(R.string.txt_chart1, R.string.txt_chart2, R.string.txt_chart3)
    private var arrayFragChart =
        arrayListOf<Fragment>(DiabeteChartLine(), DiabeteChartPie())

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
        val dao = DB_sante.getInstance(requireContext()).tabGlycemie
        val dao2 = DB_sante.getInstance(requireContext()).tabInsuline

        val daoGlycemie = DB_sante.getInstance(requireContext()).tabGlycemie
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoDiabete = DB_sante.getInstance(requireContext()).tabRelationnelDiabete
        val repoDiabete = InnerDiabeteRepo(daoGlycemie, daoPeriode, daoDiabete)
        val factoryDiabete = VMDiabeteFactory(repoDiabete)

        val vmdiabete = ViewModelProvider(this, factoryDiabete).get(VMDiabete::class.java)
        binding?.viewModel = vmdiabete

        val repository = GlycemieRepo(dao)
        val repositoryInsuline = InsulineRepo(dao2)

        val factory = VMGlycemieFactory(repository)
        val factoryInsuline = VMInsulineFactory(repositoryInsuline)

        viewModel = ViewModelProvider(this, factory).get(VMGlycemie::class.java)
        viewModelinsuline = ViewModelProvider(this, factoryInsuline).get(VMInsuline::class.java)

        val tabGlycemie = ArrayList<Int>()
        val tabInsulineRapide = ArrayList<Int>()
        val tabInsulineLente = ArrayList<Int>()

        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        viewModelinsuline.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModelinsuline.getallRapide().observe(viewLifecycleOwner) { it ->
            tabInsulineRapide.clear()
            tabInsulineRapide.addAll(it)
        }

        viewModelinsuline.getallLente().observe(viewLifecycleOwner) { it ->
            tabInsulineLente.clear()
            tabInsulineLente.addAll(it)
        }

        viewModel.getAllValeurGlycemie().observe(viewLifecycleOwner) { it ->
            tabGlycemie.clear()
            tabGlycemie.addAll(it)
        }


        binding!!.btnInsert.setOnClickListener {
//            vmdiabete.insertDiabete()

            DiabeteDialogGlycemie.newInstance("titre", "subtitre", ind, 0, 0)
                .show(childFragmentManager, DiabeteDialogGlycemie.TAG)
            //Toast.makeText(requireContext(), "youhou", Toast.LENGTH_LONG).show()
        }
        binding!!.btnInsertInsuline.setOnClickListener {
            DiabeteDialogInsuline.newInstance("titre", "subtitre", ind, 0, 0, 0)
                .show(childFragmentManager, DiabeteDialogInsuline.TAG)
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

                        when (tab.position) {
                            0 -> {
                                binding!!.btnInsert.visibility = View.VISIBLE
                                binding!!.btnInsertInsuline.visibility = View.INVISIBLE
                            }
                            1 -> {
                                binding!!.btnInsert.visibility = View.INVISIBLE
                                binding!!.btnInsertInsuline.visibility = View.VISIBLE
                            }
                            else -> {
                                binding!!.btnInsert.visibility = View.VISIBLE
                                binding!!.btnInsertInsuline.visibility = View.INVISIBLE
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