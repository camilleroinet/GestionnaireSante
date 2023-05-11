package com.example.gestionnairesante.ui.diabete

import android.animation.Animator
import android.animation.ObjectAnimator
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterRecyclerDiabete
import com.example.gestionnairesante.adapter.AdapterViewPager
import com.example.gestionnairesante.adapter.AdapterViewPagerCharts
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteRepo
import com.example.gestionnairesante.database.dao.insuline.ParamStyloData
import com.example.gestionnairesante.databinding.DiabeteBinding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.ui.diabete.vm.VMDiabeteFactory
import com.example.gestionnairesante.ui.poids.PoidsDialog
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DiabeteFragment : Fragment() {
    private var binding: DiabeteBinding? = null
    private lateinit var vmdiabete: VMDiabete

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
        val daoGlycemie = DB_sante.getInstance(requireContext()).tabGlycemie
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoInsuline = DB_sante.getInstance(requireContext()).tabInsuline
        val daoDiabete = DB_sante.getInstance(requireContext()).tabRelationnelDiabete
        val daoStylo = DB_sante.getInstance(requireContext()).tabStylo

        val repoDiabete =
            InnerDiabeteRepo(daoGlycemie, daoPeriode, daoInsuline, daoDiabete, daoStylo)
        val factoryDiabete = VMDiabeteFactory(repoDiabete)
        vmdiabete = ViewModelProvider(requireActivity(), factoryDiabete).get(VMDiabete::class.java)
        binding?.vmdiabete = vmdiabete


        binding?.btnInsert?.setOnClickListener {
            DiabeteDialogGlycemie.newInstance(
                "titre",
                "subtitre",
                ind,
                0, 0,
                0,
                0, 0, 0, "", "", ""
            ).show(childFragmentManager, PoidsDialog.TAG)
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
                                binding!!.btnInsertInsuline.visibility = View.GONE
                            }

                            1 -> {
                                binding!!.btnInsert.visibility = View.GONE
                                binding!!.btnInsertInsuline.visibility = View.GONE
                            }

                            else -> {
                                binding!!.btnInsert.visibility = View.VISIBLE
                                binding!!.btnInsertInsuline.visibility = View.GONE
                            }
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
