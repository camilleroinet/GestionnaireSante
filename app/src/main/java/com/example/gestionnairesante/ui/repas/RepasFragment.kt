package com.example.gestionnairesante.ui.repas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterRecyclerMenuPlat
import com.example.gestionnairesante.adapter.AdapterRecyclerPlat
import com.example.gestionnairesante.adapter.AdapterViewPager
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuData
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.innerPlat.PlatInner
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasBinding
import com.example.gestionnairesante.ui.diabete.DiabeteChartLine
import com.example.gestionnairesante.ui.diabete.DiabeteChartPie
import com.example.gestionnairesante.ui.repas.vm.VmRepas
import com.example.gestionnairesante.ui.repas.vm.VmRepasFactory
import com.google.android.material.tabs.TabLayout

class RepasFragment : Fragment() {
    private var binding: RepasBinding? = null
    private lateinit var viewmodelrepas: VmRepas

    private lateinit var adapterPlat: AdapterRecyclerPlat
    private lateinit var adapterListePlatMenu: AdapterRecyclerMenuPlat

    private lateinit var tablayoutTabs: TabLayout
    private lateinit var viewPagerTabs: ViewPager
    private lateinit var viewPagerCharts: ViewPager

    private var ind = 0

    private var arrayTab = arrayListOf<Int>(R.string.txt_fragmenu, R.string.txt_fragrepas)
    private var arrayFragTab = arrayListOf<Fragment>(RepasTab1(), RepasTab2())

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
            binding?.fragRepas = this@RepasFragment
        }
        // databinding
        val daoPlat = DB_sante.getInstance(requireContext()).tabPlat
        val daoMenu = DB_sante.getInstance(requireContext()).tabMenu
        val daoRepas = DB_sante.getInstance(requireContext()).tabRelationnelPlat

        val repositoryPlatMenu = InnerPlatMenuRepo(daoPlat, daoMenu, daoRepas)
        val factory = VmRepasFactory(repositoryPlatMenu)
        viewmodelrepas = ViewModelProvider(this, factory).get(VmRepas::class.java)
        binding?.viewmodelrepas = viewmodelrepas

        val tabPlat = ArrayList<PlatData>()
        val tabListePlat = ArrayList<PlatInner>()

        viewmodelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewmodelrepas.getAllPlats().observe(viewLifecycleOwner) { it ->
            tabPlat.clear()
            tabPlat.addAll(it)
        }

        binding!!.btnMenu.setOnClickListener {
            binding!!.llVueChart.visibility = View.GONE
            binding!!.vueMenu.visibility = View.VISIBLE
        }

        //
        // Etape 1 du scenario
        // Je rentre le nom du menu
        //

        binding!!.validerNommenu.setOnClickListener() {
            val newMenu = MenuData(0, binding!!.etNommenu.text.toString())
            if(binding!!.etNommenu.text.isEmpty()){
                Toast.makeText(requireContext(), "Entrer un nom de menu", Toast.LENGTH_LONG).show()
            }else{
                viewmodelrepas.ajouterMenu(newMenu)
                binding!!.llEtape01.visibility = View.GONE
                binding!!.etapeInformation.visibility = View.VISIBLE
                binding!!.llEtape2.visibility = View.VISIBLE
            }
        }

        binding!!.validerMenu.setOnClickListener {
            binding!!.llVueChart.visibility = View.VISIBLE
            binding!!.llEtape01.visibility = View.GONE
            binding!!.etapeInformation.visibility = View.GONE
            binding!!.llEtape2.visibility = View.GONE


        }
        binding!!.annulerNommenu.setOnClickListener{
            binding!!.etNommenu.text.clear()
        }

        //
        // Etape 2
        // On affiche l'ecran des informations
        // l'utilsateur choisi ses plats
        //
        binding!!.btnInsertPlat.setOnClickListener {
            RepasDialogPlat.newInstance("titre", "subtitre", ind, 0, "", 0, 0)
                .show(childFragmentManager, RepasDialogPlat.TAG)
            //Toast.makeText(requireContext(), "youhou", Toast.LENGTH_LONG).show()
        }

        // Pour populate la db
        binding!!.annulerNommenu.setOnClickListener {
            val plat1 = PlatData(0,"Croissant", 25,200)
            val plat2 = PlatData(0,"Cafe", 0,0)
            val plat3 = PlatData(0,"Orange", 10,5)


            viewmodelrepas.ajouterPlat(plat1)
            viewmodelrepas.ajouterPlat(plat2)
            viewmodelrepas.ajouterPlat(plat3)

            val menu = MenuData(0, "monMenu")
            viewmodelrepas.ajouterMenu(menu)
            val inner = InnerPlatMenuData(0, 9,1)
            viewmodelrepas.ajouterInnerPlatMenu(inner)

        }
        viewPagerCharts = binding?.viewpagercharts!!

        tablayoutTabs = binding?.tabLayout!!
        viewPagerTabs = binding?.viewpagertabhost!!

        configTablelayout(arrayTab)
        //configViewPagerChart(viewPagerCharts, arrayFragChart, arrayTabCharts)
        configViewPager(viewPagerTabs, arrayFragTab, arrayTab, tablayoutTabs)

        initRecycler()
        displayUser()
    }

    fun initRecycler() {
        // Configuration du layout
        binding?.listeRepas?.layoutManager = LinearLayoutManager(context)
        // Configuration de l'adapter
        adapterPlat = AdapterRecyclerPlat {
            data: PlatData -> listItemClicked(viewmodelrepas, data)
        }
        binding?.listeRepas?.adapter = adapterPlat

        binding?.listeMenu?.layoutManager = LinearLayoutManager(context)
        adapterListePlatMenu = AdapterRecyclerMenuPlat {
            data: PlatInner -> listItemClicked2(viewmodelrepas, data)
        }
        binding?.listeMenu?.adapter = adapterListePlatMenu
    }


    fun listItemClicked(viewModel: VmRepas, data: PlatData) {
        viewModel.composerMenu(data)
        Toast.makeText(requireContext(), "item cliqué : ${data.nom_plat}", Toast.LENGTH_LONG).show()
        viewmodelrepas.totalPlats.value = viewmodelrepas.totalPlats.value?.plus(1)
    }

    fun listItemClicked2(viewModel: VmRepas, data: PlatInner) {
        //viewModel.composerMenu(data)
        Toast.makeText(requireContext(), "item cliqué : ${data.nomPlat}", Toast.LENGTH_LONG).show()
        viewmodelrepas.totalPlats.value = viewmodelrepas.totalPlats.value?.plus(1)
    }

    fun displayUser() {
        viewmodelrepas.getAllPlats().observe(viewLifecycleOwner, Observer {
            //Toast.makeText(requireContext(), "size ==>> ${it.size}", Toast.LENGTH_LONG).show()
            adapterPlat.setList(it)
            adapterPlat.notifyDataSetChanged()
        })

        viewmodelrepas.getPlatInMenu(viewmodelrepas.getLastMenuInCurrent()).observe(viewLifecycleOwner, Observer {
            //Toast.makeText(requireContext(), "size ==>> ${it.size}", Toast.LENGTH_LONG).show()
            adapterListePlatMenu.setList(it)
            adapterListePlatMenu.notifyDataSetChanged()

        })

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
                                binding!!.btnInsertPlat.visibility = View.INVISIBLE
                                binding!!.btnMenu.visibility = View.VISIBLE
                            }
                            1 -> {
                                binding!!.btnInsertPlat.visibility = View.VISIBLE
                                binding!!.btnMenu.visibility = View.INVISIBLE
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

}