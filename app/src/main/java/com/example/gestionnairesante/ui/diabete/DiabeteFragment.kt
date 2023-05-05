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

    // Initialisation des view dans activuty main
    private lateinit var contain: CoordinatorLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var sheet: CircularRevealCardView
    private lateinit var scrim: View

    /**
     * Declaration de toutes les view du dialog
     */

    private lateinit var numberPicker1: NumberPicker
    private lateinit var numberPicker2: NumberPicker
    private lateinit var numberPicker3: NumberPicker
    private lateinit var pickerRapide1: NumberPicker
    private lateinit var pickerRapide2: NumberPicker
    private lateinit var pickerLente1: NumberPicker
    private lateinit var pickerLente2: NumberPicker
    private lateinit var datePicker: DatePicker
    private lateinit var spinnerPeriode: Spinner
    private lateinit var btnSave: Button


    // Init des includes dans la vue (activity_main.xml
    private lateinit var itemHolder: List<diabeteItemHolder>

    // Creation des objets de la vue : correspon aux includes
    private class diabeteItemHolder(parent: LinearLayout, listener: View.OnClickListener){
        // Bouton dans le dialogFragment (imgbutton)
        val btn: ImageButton = parent.findViewById(R.id.fermer)
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
        val diabeteFrag = DiabeteBinding.inflate(inflater, container, false)
        binding = diabeteFrag

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

        fab.setOnClickListener {
            val ll = requireActivity().findViewById<LinearLayout>(R.id.ajoutdiabete)
            ll.visibility = View.VISIBLE
            startAnimationFab(0, fab, 300, 0f, -400f)

            numberPicker1 = requireActivity().findViewById(R.id.picker1)
            numberPicker2 = requireActivity().findViewById(R.id.picker2)
            numberPicker3 = requireActivity().findViewById(R.id.picker3)
            pickerRapide1 = requireActivity().findViewById(R.id.pickerRapide1)
            pickerRapide2 = requireActivity().findViewById(R.id.pickerRapide2)
            pickerLente1 = requireActivity().findViewById(R.id.pickerLente1)
            pickerLente2 = requireActivity().findViewById(R.id.pickerLente2)
            datePicker = requireActivity().findViewById(R.id.datepicker)
            btnSave = requireActivity().findViewById(R.id.btn_saveGlycemie)

            val tabPeriode = resources.getStringArray(R.array.periodes)
            spinnerPeriode = requireActivity().findViewById(R.id.spinner_periode)

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

            configSpinner(tabPeriode)
            setupNumberPicker()

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
                                binding!!.btnInsert.visibility = View.GONE
                                binding!!.btnInsertInsuline.visibility = View.GONE
                            }
                            1 -> {
                                binding!!.btnInsert.visibility = View.GONE
                                binding!!.btnInsertInsuline.visibility = View.GONE
                            }
                            else -> {
                                binding!!.btnInsert.visibility = View.GONE
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

    fun configFabExpanded(){
        contain = requireActivity().findViewById(R.id.container)
        fab = requireActivity().findViewById(R.id.fab)
        sheet = requireActivity().findViewById(R.id.sheet)
        scrim = requireActivity().findViewById(R.id.scrim)
        itemHolder = listOf(
            diabeteItemHolder(requireActivity().findViewById(R.id.ajoutdiabete), itemOnClick)
        )

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
    /**
     * Fonction de gestion du spinner
     */

    fun configSpinner(arrayCat: Array<String>) {
        /* en simple java
        mInterpolatorSpinner = (Spinner) view.findViewById(R.id.interpolatorSpinner);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, mInterpolatorNames);
        mInterpolatorSpinner.setAdapter(spinnerAdapter);*/

        //val arrayCat = resources.getStringArray(R.array.categoriesfs)
        val adapt = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayCat)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.periodes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerPeriode.adapter = adapter
        }


        spinnerPeriode.onItemSelectedListener = object :
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
/*                Toast.makeText(
                    requireContext(),
                    "spinner selection ======> $position",
                    Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    private fun setupNumberPicker() {

        numberPicker1.minValue = 0
        numberPicker1.maxValue = 9
        numberPicker1.wrapSelectorWheel = false
        numberPicker1.setOnValueChangedListener { picker, oldVal, newVal ->
        }

        numberPicker2.minValue = 0
        numberPicker2.maxValue = 9
        numberPicker2.wrapSelectorWheel = true
        numberPicker2.setOnValueChangedListener { picker, oldVal, newVal ->
        }

        numberPicker3.minValue = 0
        numberPicker3.maxValue = 9
        numberPicker3.wrapSelectorWheel = true
        numberPicker3.setOnValueChangedListener { picker, oldVal, newVal ->
        }


        pickerRapide1.minValue = 0
        pickerRapide1.maxValue = 9
        pickerRapide1.wrapSelectorWheel = true
        pickerRapide1.setOnValueChangedListener { picker, oldVal, newVal ->
        }

        pickerRapide2.minValue = 0
        pickerRapide2.maxValue = 9
        pickerRapide2.wrapSelectorWheel = true
        pickerRapide2.setOnValueChangedListener { picker, oldVal, newVal ->
        }

        pickerLente1.minValue = 0
        pickerLente1.maxValue = 9
        pickerLente1.wrapSelectorWheel = true
        pickerLente1.setOnValueChangedListener { picker, oldVal, newVal ->
        }

        pickerLente2.minValue = 0
        pickerLente2.maxValue = 9
        pickerLente2.wrapSelectorWheel = true
        pickerLente2.setOnValueChangedListener { picker, oldVal, newVal ->
        }

    }

    fun save() {
        val val1 = numberPicker1.value
        val val2 = numberPicker2.value
        val val3 = numberPicker3.value
        val temp: String = val1.toString() + val2.toString() + val3.toString()

        val val4 = datePicker.dayOfMonth
        val val5 = datePicker.month + 1
        val val6 = datePicker.year
        val date = "$val4-$val5-$val6"

        val val7 = pickerRapide1.value
        val val8 = pickerRapide2.value
        val val9 = pickerLente1.value
        val val10 = pickerLente2.value
        val tempRapide: String = val7.toString() + val8.toString()
        val tempLente: String = val9.toString() + val10.toString()

        val periode = spinnerPeriode.selectedItem.toString()

        val current = LocalDateTime.now()
        val heure = DateTimeFormatter.ofPattern("HH:mm")
        val dateDuJour = Calendar.getInstance()
        dateDuJour.timeInMillis = System.currentTimeMillis()
        val heureDuJour = current.format(heure)

        vmdiabete.insertDiabete(
            periode, date, heureDuJour.toString(),
            temp.toInt(), tempRapide.toInt(), tempLente.toInt()
        )

    }

}

/**
 * RAPPEL :
 *
 * LinearInterpolator()
 * LinearOutSlowInterpolator()
 * AcceleratInterpolator()
 * DecelerateInterpolator()
 * AccelerateDecelerateInterpolator()
 *
 **/