package com.example.gestionnairesante.ui.repas

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
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
import com.example.gestionnairesante.adapter.AdapterViewPager
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerMenu.InnerPeriodeMenuRepo
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasBinding
import com.example.gestionnairesante.ui.diabete.DiabeteChartLine
import com.example.gestionnairesante.ui.diabete.DiabeteChartPie
import com.example.gestionnairesante.ui.repas.vm.VmRepas
import com.example.gestionnairesante.ui.repas.vm.VmRepasFactory
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    // Initialisation des view dans activuty main
    private lateinit var contain: CoordinatorLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var sheet: CircularRevealCardView
    private lateinit var scrim: View

    private var indiceTab = 0
    /**
     * Declaration de toutes les view du dialog
     */
    private lateinit var et_nom: EditText
    private lateinit var et_cal: EditText
    private lateinit var et_glu: EditText

    private lateinit var btnSave: Button


    // Init des includes dans la vue (activity_main.xml
    private lateinit var itemHolder: List<repasItemHolder>
    private lateinit var itemHolder2: List<repasItemHolder2>

    // Creation des objets de la vue : correspon aux includes
    private class repasItemHolder(parent: LinearLayout, listener: View.OnClickListener){
        // Bouton dans le dialogFragment (imgbutton)
        val btn: ImageButton = parent.findViewById(R.id.btncoycyc)
        init {
            btn.setOnClickListener(listener)
        }
    }
    private class repasItemHolder2(parent: LinearLayout, listener: View.OnClickListener){
        // Bouton dans le dialogFragment (imgbutton)
        val btn: ImageButton = parent.findViewById(R.id.fermer_plat)

        init {
            btn.setOnClickListener(listener)
        }
    }

    // Clique sur le bouton
    private val itemOnClick = View.OnClickListener { v ->
        fab.isExpanded = false
        startAnimationFab(1, fab, 300, -400f, 0f)
    }

    // Init de l'animation
    var animator = ObjectAnimator()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val repasFrag = RepasBinding.inflate(inflater, container, false)
        binding = repasFrag

        // Affichage de la vue du dialogFrag
        configFabExpanded()

        // Gestion de la touche retour du telephone
        // TODO le retour du fab a sa place initiale n'est pas codé
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fab.isExpanded = false
                startAnimationFab(1, fab, 300, -400f, 0f)

            }
        })

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

/*        binding!!.btnMenu.setOnClickListener {
            binding!!.llVueChart.visibility = View.GONE
            binding!!.vueMenu.visibility = View.VISIBLE
            binding!!.llEtape01.visibility = View.VISIBLE
            binding!!.llEtapeDate.visibility = View.GONE
            binding!!.etapeInformation.visibility = View.GONE
            binding!!.llEtape2.visibility = View.GONE
        }*/

        fab.setOnClickListener {
            when(indiceTab){
                0 -> {
                    val ll = requireActivity().findViewById<LinearLayout>(R.id.ajoutrepas)
                    ll.visibility = View.VISIBLE
                    startAnimationFab(0, fab, 300, 0f, -400f)

                    // La partie noire qui entoure la vue
                    // TODO le clic dessus est desactivé
                    scrim.setOnClickListener {
                        fab.isExpanded = false
                        startAnimationFab(1, fab, 300, -400f, 0f)
                    }

                    btnSave.setOnClickListener {
                        save()
                        ll.visibility = View.GONE
                        fab.isExpanded = false
                        startAnimationFab(1, fab, 300, -400f, 0f)

                    }
                }
                1 -> {
                    val ll = requireActivity().findViewById<LinearLayout>(R.id.ajoutplats)
                    ll.visibility = View.VISIBLE
                    startAnimationFab(0, fab, 300, 0f, -400f)

                    et_nom = requireActivity().findViewById(R.id.et_nomplat)
                    et_cal = requireActivity().findViewById(R.id.et_calories)
                    et_glu = requireActivity().findViewById(R.id.et_glucide)

                    btnSave = requireActivity().findViewById(R.id.btn_savePlat)


                    // La partie noire qui entoure la vue
                    // TODO le clic dessus est desactivé
                    scrim.setOnClickListener {
                        fab.isExpanded = false
                        startAnimationFab(1, fab, 300, -400f, 0f)
                    }

                    btnSave.setOnClickListener {
                        save()
                        ll.visibility = View.GONE
                        fab.isExpanded = false
                        startAnimationFab(1, fab, 300, -400f, 0f)

                    }
                }
            }


        }

/*
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


        }

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
                                binding!!.btnInsertPlat.visibility = View.INVISIBLE
                                binding!!.btnMenu.visibility = View.VISIBLE
                            }
                            1 -> {
                                indiceTab = 1
                                binding!!.btnInsertPlat.visibility = View.VISIBLE
                                binding!!.btnMenu.visibility = View.INVISIBLE
                                //fab.visibility= View.GONE
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


    fun configFabExpanded(){
        contain = requireActivity().findViewById(R.id.container)
        fab = requireActivity().findViewById(R.id.fab)
        sheet = requireActivity().findViewById(R.id.sheet)
        scrim = requireActivity().findViewById(R.id.scrim)
        when(indiceTab){
            0 -> {
                itemHolder = listOf(
                    RepasFragment.repasItemHolder(
                        requireActivity().findViewById(R.id.ajoutrepas),
                        itemOnClick
                    )
                )
            }
            1 -> {
                itemHolder2 = listOf(

                    RepasFragment.repasItemHolder2(
                        requireActivity().findViewById(R.id.ajoutplats),
                        itemOnClick
                    )
                )
            }
            else -> {

            }
        }


        val fabMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(contain) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            fab.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                leftMargin = fabMargin + systemBars.left
                rightMargin = fabMargin + systemBars.right
                bottomMargin = fabMargin + systemBars.bottom
            }
            sheet.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                leftMargin = fabMargin + systemBars.left
                rightMargin = fabMargin + systemBars.right
                bottomMargin = fabMargin + systemBars.bottom
            }
            insets
        }
    }

    fun startAnimationFab(indice: Int, view: View, duration: Long, debut: Float, fin: Float): ObjectAnimator {
        /** Note : mouvement suivant l'axe Y = Height
         * mettre "translationX" pour un mouvement horizontal Axe X = width
         * parametre fin
         * --> positif le btn va vers le bas
         * --> negatif btn va vers le haut
         * parametre debut et fin
         * --> inverse les float pour revenir en position
         **/

        animator = ObjectAnimator.ofFloat(view, "translationY", debut, fin)
        animator.duration = duration

        /**
         * Possibilite de mettre une autre interpolation
         * voir rappel a la fin
         */
        animator.interpolator = LinearInterpolator()

        /**
         * Important
         * laisser les 4 etats de l'animation
         */
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) { }
            override fun onAnimationCancel(animation: Animator) { }
            override fun onAnimationRepeat(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                if( indice == 0) {
                    fab.isExpanded = true
                }
            }
        })

        animator.start()
        return animator
    }

    override fun onDetach() {
        fab.isExpanded = false
        super.onDetach()
    }

    fun save() {
        if (et_nom.text.isBlank()) {
            Toast.makeText(context, "youhou ya rien", Toast.LENGTH_LONG).show()
        } else {
            val nomPlat = et_nom.text.toString()
            val caloriePlat = et_cal.text.toString()
            val glucidePlat = et_glu.text.toString()

            val newInsert = PlatData(0, nomPlat, caloriePlat.toInt(), glucidePlat.toInt())
            viewmodelrepas.ajouterPlat(newInsert)
        }
    }

}