package com.example.gestionnairesante.ui.poids

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterRecyclerPoids
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsRepo
import com.example.gestionnairesante.database.dao.innerPoids.PoidsInner
import com.example.gestionnairesante.databinding.PoidBinding
import com.example.gestionnairesante.ui.poids.vm.VmPoids
import com.example.gestionnairesante.ui.poids.vm.VmPoidsFactory
import com.example.gestionnairesante.utils.createBarChart
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PoidsFragment : Fragment() {

    private var binding: PoidBinding? = null
    private lateinit var viewModel: VmPoids
    private lateinit var adapter: AdapterRecyclerPoids
    private var ind = 0
    private var lastPoua = 0f

    // Initialisation des view dans activity main
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
    private lateinit var datePicker: DatePicker
    private lateinit var spinnerPeriode: Spinner
    private lateinit var btnSave: Button

    val tabPoids = ArrayList<Float>()

    // Init des includes dans la vue (activity_main.xml
    private lateinit var itemHolder: List<poidsItemHolder>

    // Creation des objets de la vue : correspond aux includes
    private class poidsItemHolder(parent: LinearLayout, listener: View.OnClickListener) {
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
        val poidsFrg = PoidBinding.inflate(inflater, container, false)
        binding = poidsFrg

        // Affichage de la vue du dialogFrag
        configFabExpanded()

        // Gestion de la touche retour du telephone
        // TODO le retour du fab a sa place initiale n'est pas codé
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fab.isExpanded = false
                    startAnimationFab(1, fab, 300, -400f, 0f)
                }
            })
        return poidsFrg.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.fragPoids = this@PoidsFragment
        }

        //
        // databinding
        //
        val daoPoids = DB_sante.getInstance(requireContext()).tabPoids
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoPoidsInner = DB_sante.getInstance(requireContext()).tabRelationnelPoids

        val repository = InnerPoidsRepo(daoPoids, daoPeriode, daoPoidsInner)
        val factory = VmPoidsFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(VmPoids::class.java)

        binding?.viewModel = viewModel

        // TODO a cocher a la fin
        // Message de test du viewModel
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getAllValeurPoids().observe(viewLifecycleOwner) { it ->
            tabPoids.clear()
            tabPoids.addAll(it)

            val p = tabPoids.size

            if (p > 0) {
                binding?.llAvertChart?.visibility = View.GONE
                binding?.chart0?.visibility = View.VISIBLE
                binding?.tabLayout?.visibility = View.VISIBLE

                lastPoua = tabPoids.get(p - 1)

                val imc = (calculerIMC(171, lastPoua))

                val decimal = BigDecimal(imc).setScale(2, RoundingMode.HALF_EVEN)

                binding!!.rere.text = decimal.toString()
                verifIMC(decimal.toDouble())

                initRecycler()
                touchRecycler()
                displayUser()

                recupDataBarChart()

            } else {
                Toast.makeText(requireContext(), "Aucunes données", Toast.LENGTH_SHORT).show()
                binding?.llAvertChart?.visibility = View.VISIBLE
                binding?.chart0?.visibility = View.GONE
                binding?.tabLayout?.visibility = View.GONE
            }
        }

        fab.setOnClickListener {
            val ll = requireActivity().findViewById<LinearLayout>(R.id.ajoutpoids)
            ll.visibility = View.VISIBLE
            startAnimationFab(0, fab, 300, 0f, -400f)

            numberPicker1 = requireActivity().findViewById(R.id.poids_picker1)
            numberPicker2 = requireActivity().findViewById(R.id.poids_picker2)
            numberPicker3 = requireActivity().findViewById(R.id.poids_picker3)
            datePicker = requireActivity().findViewById(R.id.datepickerpoids)
            btnSave = requireActivity().findViewById(R.id.btn_insertPoids)

            val tabPeriode = resources.getStringArray(R.array.periodes)
            spinnerPeriode = requireActivity().findViewById(R.id.spinner_periodepoids)

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

        /*        binding!!.btnInsert.setOnClickListener {
                    PoidsDialog.newInstance("titre", "subtitre", ind, 0, 0, 0F, "", "", "")
                        .show(childFragmentManager, PoidsDialog.TAG)
                }*/

    }

    fun listItemClicked(viewModel: VmPoids, data: PoidsInner) {


    }

    // TODO Apres implementation du profil modifier la valeur
    //  par defaut de la taille
    // Calcule de l'imc
    fun calculerIMC(taille: Int, poids: Float): Double {
        //  IMC = poids en kg/taille²
        val weight = poids.toDouble()
        val height = (taille.toDouble() / 100)

        return (weight / (height * height))
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    // Test et retour du traitement en fonction de la valeur de l'imc
    fun verifIMC(imc: Double) {
        when (imc) {
            in 0.0F..18.5F -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc1))
                binding?.resultat?.text = "Poids insuffisant"
            }

            in 18.5F..25F -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc2))
                binding?.resultat?.text = "Poids normal"
            }

            in 25F..30F -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc3))
                binding?.resultat?.text = "Surpoids"
            }

            in 30F..35F -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc4))
                binding?.resultat?.text = "Obésité"
            }

            else -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc5))
                binding?.resultat?.text = "Obésité morbide"
            }
        }

    }

    // Initialisation du recylcer
    fun initRecycler() {
        // Configuration du layout
        binding?.recyclerPoids?.layoutManager = LinearLayoutManager(context)
        // Configuration de l'adapter
        // adapter = AdapterSportD { h: View -> longclickListener(h) }
        adapter =
            AdapterRecyclerPoids { daouser: PoidsInner -> listItemClicked(viewModel, daouser) }
        binding?.recyclerPoids?.adapter = adapter

    }

    // Afficher les données dans le recycler

    fun displayUser() {
        viewModel.getPoidsPeriode().observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    // Gestion tactile du recycler view
    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    when (direction) {
                        ItemTouchHelper.RIGHT -> {
                            viewModel.deletePoidPeriode(obj.idpoi, obj.idper)
                            adapter.remove(sp)
                        }

                        ItemTouchHelper.LEFT -> {
                            val idpoi = obj.idpoi
                            val idper = obj.idper
                            val poids = obj.poids
                            val date = obj.date
                            val heure = obj.heure
                            val periode = obj.periode

                            PoidsDialog.newInstance(
                                "titre",
                                "subtitre",
                                ind,
                                idpoi, idper,
                                poids,
                                date, heure, periode
                            ).show(childFragmentManager, PoidsDialog.TAG)
                        }

                    }

                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == ItemTouchHelper.RIGHT) {
                        viewHolder?.itemView?.alpha = 0.5F
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.alpha = 1.0F
                }
            }
            ItemTouchHelper(simplecall)
        }
        itemTouchHelper.attachToRecyclerView(binding?.recyclerPoids)
    }

    // Traitement du bar chart
    fun recupDataBarChart(): ArrayList<BarEntry> {
        val data = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Float>()
        val barChart = binding!!.chart0
        val stringValue = ArrayList<String>()

        viewModel.getAllValeurPoids().observe(viewLifecycleOwner) {
            tabValeur.clear()
            tabValeur.addAll(it)

            val r = tabValeur.size - 1
            for (i in 0..r) {
                stringValue.add("")
                data.add(BarEntry(i.toFloat(), tabValeur[i]))
            }
            createBarChart(barChart, data, stringValue, "Poids")
        }

        return data
    }

    fun configFabExpanded() {
        contain = requireActivity().findViewById(R.id.container)
        fab = requireActivity().findViewById(R.id.fab)
        sheet = requireActivity().findViewById(R.id.sheet)
        scrim = requireActivity().findViewById(R.id.scrim)
        itemHolder = listOf(
            PoidsFragment.poidsItemHolder(
                requireActivity().findViewById(R.id.ajoutdiabete),
                itemOnClick
            )
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

    fun startAnimationFab(
        indice: Int,
        view: View,
        duration: Long,
        debut: Float,
        fin: Float
    ): ObjectAnimator {
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
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                if (indice == 0) {
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
        numberPicker1.wrapSelectorWheel = true
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

    }

    fun save() {
        //
        // Poids
        //
        val val1 = numberPicker1.value
        val val2 = numberPicker2.value
        val val3 = numberPicker3.value
        val poids: String = val1.toString() + val2.toString() + val3.toString()

        //
        // Date
        //
        val val4 = datePicker.dayOfMonth
        val val5 = datePicker.month + 1
        val val6 = datePicker.year
        val date = "$val4-$val5-$val6"

        //
        // Periode
        //
        val periode = spinnerPeriode.selectedItem.toString()

        //
        // Heure
        //
        val current = LocalDateTime.now()
        val heure = DateTimeFormatter.ofPattern("HH:mm")
        val dateDuJour = Calendar.getInstance()
        dateDuJour.timeInMillis = System.currentTimeMillis()
        val heureDuJour = current.format(heure)

        viewModel.insertPoids(poids.toFloat(), date, heureDuJour.toString(), periode)


    }

}