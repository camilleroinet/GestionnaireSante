package com.example.gestionnairesante.ui.repas

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.example.gestionnairesante.database.dao.innerMenu.InnerPeriodeMenuData
import com.example.gestionnairesante.database.dao.innerMenu.InnerPeriodeMenuRepo
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuData
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.innerPlat.PlatInner
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasBinding
import com.example.gestionnairesante.ui.diabete.DiabeteChartLine
import com.example.gestionnairesante.ui.diabete.DiabeteChartPie
import com.example.gestionnairesante.ui.repas.vm.VmRepas
import com.example.gestionnairesante.ui.repas.vm.VmRepasFactory
import com.google.android.material.tabs.TabLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    private var arrayTabCharts = arrayListOf<Int>(R.string.txt_chart1, R.string.txt_chart2, R.string.txt_chart3)
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
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoRepas = DB_sante.getInstance(requireContext()).tabRelationnelPlat
        val daoPeriodeMenu = DB_sante.getInstance(requireContext()).tabRelationnelMenu

        val repositoryPlatMenu = InnerPlatMenuRepo(daoPlat, daoMenu, daoRepas, daoPeriodeMenu)
        val repositoryPeriodeMenu = InnerPeriodeMenuRepo(daoMenu, daoPeriode, daoPeriodeMenu)

        val factory = VmRepasFactory(repositoryPlatMenu, repositoryPeriodeMenu)
        viewmodelrepas = ViewModelProvider(this, factory).get(VmRepas::class.java)
        binding?.viewmodelrepas = viewmodelrepas

        val tabPeriode = resources.getStringArray(R.array.periodes)
        configSpinner(tabPeriode)

        viewmodelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        displayUser()

        binding!!.llVueChart.visibility = View.VISIBLE
        binding!!.vueMenu.visibility = View.GONE

        binding!!.btnMenu.setOnClickListener {
            binding!!.llVueChart.visibility = View.GONE
            binding!!.vueMenu.visibility = View.VISIBLE
            binding!!.llEtape01.visibility = View.VISIBLE
            binding!!.llEtapeDate.visibility = View.GONE
            binding!!.etapeInformation.visibility = View.GONE
            binding!!.llEtape2.visibility = View.GONE
        }

        //
        // Etape 1 du scenario
        // Je rentre le nom du menu
        //

        binding!!.validerNommenu.setOnClickListener() {
            val newMenu = MenuData(0, binding!!.etNommenu.text.toString(), 0, 0, 0)
            if(binding!!.etNommenu.text.isEmpty()){
                Toast.makeText(requireContext(), "Entrer un nom de menu", Toast.LENGTH_LONG).show()
            }else{
                viewmodelrepas.ajouterMenu(newMenu)
                binding!!.llEtape01.visibility = View.GONE
                binding!!.llEtapeDate.visibility = View.VISIBLE
                binding!!.etapeInformation.visibility = View.GONE
            }
        }

        binding!!.validerMenu.setOnClickListener {
            binding!!.llVueChart.visibility = View.VISIBLE
            binding!!.llEtape01.visibility = View.GONE
            binding!!.etapeInformation.visibility = View.GONE
            binding!!.llEtape2.visibility = View.GONE

            viewmodelrepas.updateMenu(
                // TODO faire un test quand rv vide
                viewmodelrepas.getLastMenuInCurrent(),
                viewmodelrepas.totalPlats.value!!.toInt(),
                viewmodelrepas.totalGlucides.value!!.toInt(),
                viewmodelrepas.totalCalories.value!!.toInt())
        }

        binding!!.annulerNommenu.setOnClickListener{
            binding!!.etNommenu.text.clear()
        }

        //
        // Etape 2
        // On affiche l'ecran pour le choix de la date et de la periode
        //
        binding!!.btnValiderDate.setOnClickListener {
            binding!!.llEtapeDate.visibility = View.GONE
            binding!!.etapeInformation.visibility = View.VISIBLE
            // TODO fonction d'update de la date en Inner
            binding!!.llEtape2.visibility = View.VISIBLE
            binding!!.listeRepas.visibility = View.VISIBLE
        }

        binding!!.btnCancelDate.setOnClickListener {
            binding!!.llEtapeDate.visibility = View.GONE
            binding!!.llEtape01.visibility = View.VISIBLE
            // TODO fonction qui permet de recuperer le nom du dernioer menu
            // TODO gerer l'update au niveau des boutons et db

        }

        //
        // Etape 3
        // On affiche l'ecran des informations
        // l'utilsateur choisi ses plats
        //
        binding!!.btnInsertPlat.setOnClickListener {
            RepasDialogPlat.newInstance("titre", "subtitre", ind, 0, "", 0, 0)
                .show(childFragmentManager, RepasDialogPlat.TAG)
        }

        // Pour populate la db
        binding!!.annulerNommenu.setOnClickListener {
            val plat1 = PlatData(0,"Croissant", 25,200)
            viewmodelrepas.ajouterPlat(plat1)
            val plat2 = PlatData(0,"Cafe", 0,0)
            viewmodelrepas.ajouterPlat(plat2)
            val plat3 = PlatData(0,"Orange", 10,5)
            viewmodelrepas.ajouterPlat(plat3)

            val menu = MenuData(0, "monMenu", 0, 0, 0)
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

    fun listItemClicked(viewmodelrepas: VmRepas, data: PlatData) {
        viewmodelrepas.composerMenu(data)
        displayPlatInMenu()
    }

    fun listItemClicked2(viewModel: VmRepas, data: PlatInner) {
        viewModel.deletePlatInCurrent(data.idser)
        //Toast.makeText(requireContext(), "delete ok", Toast.LENGTH_LONG)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun displayUser() {
        viewmodelrepas.getAllPlats().observe(viewLifecycleOwner, Observer {
            //Toast.makeText(requireContext(), "size ==>> ${it.size}", Toast.LENGTH_LONG).show()
            val tabPlat = ArrayList<PlatData>()

            tabPlat.clear()
            tabPlat.addAll(it)

            adapterPlat.setList(it)
            adapterPlat.notifyDataSetChanged()
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    fun displayPlatInMenu() {

        viewmodelrepas.getPlatInMenu().observe(viewLifecycleOwner, Observer {
            val tabListePlat = ArrayList<PlatInner>()
            var calories = 0
            var glucides = 0

            tabListePlat.clear()
            tabListePlat.addAll(it)

            for(i in 0..tabListePlat.size-1){
                calories = calories + tabListePlat[i].calPlat
                glucides = glucides + tabListePlat[i].gluPlat
            }
            adapterListePlatMenu.setList(it)
            viewmodelrepas.totalPlats.value = tabListePlat.size.toString()
            viewmodelrepas.totalGlucides.value = glucides.toString()
            viewmodelrepas.totalCalories.value = calories.toString()
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

                        when (tab.position) {
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

    fun saveDate() {
        val val4 = binding!!.datepicker.dayOfMonth
        val val5 = binding!!.datepicker.month + 1
        val val6 = binding!!.datepicker.year
        val date = "$val4/$val5/$val6"

        val periode = binding!!.spinnerPeriode.selectedItem.toString()

        val current = LocalDateTime.now()
        val heure = DateTimeFormatter.ofPattern("HH:mm")
        val dateDuJour = Calendar.getInstance()
        dateDuJour.timeInMillis = System.currentTimeMillis()
        val heureDuJour = current.format(heure)

        val data = PeriodeData(0, periode, date, heureDuJour)
        viewmodelrepas.ajouterPeriode(data)
        viewmodelrepas.ajouterInnerPeriodeMenu(InnerPeriodeMenuData(viewmodelrepas.getLastMenuInCurrent(), viewmodelrepas.getLastPeriodeInCurrent()))
    }

    fun configSpinner(arrayCat: Array<String>) {
        /* en simple java
        mInterpolatorSpinner = (Spinner) view.findViewById(R.id.interpolatorSpinner);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, mInterpolatorNames);
        mInterpolatorSpinner.setAdapter(spinnerAdapter);*/

        //val arrayCat = resources.getStringArray(R.array.categoriesfs)
        val adapt = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayCat)
        binding?.let {
            with(it.spinnerPeriode) {
                adapter = adapt
                setSelection(0, false)
                prompt = "Selection catagorie"
                gravity = Gravity.CENTER
                //posAdapter = 0
                //nomCategorie = arrayCat[0]
                //gestionRecycler(0, nomCategorie)
            }
        }

        binding?.spinnerPeriode?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //posAdapter = position
                //nomCategorie = arrayCat[position]
                //gestionRecycler(position, nomCategorie)
                Toast.makeText(
                    requireContext(),
                    "spinner selection ======> $position",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}