package com.example.gestionnairesante.ui.diabete

import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.viewmodels.glycemie.VMGlycemie
import com.example.gestionnairesante.databinding.DiabeteDialogBinding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.ui.poids.PoidsDialog
import java.sql.Time
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class DiabeteDialogGlycemie : DialogFragment() {
    private var binding: DiabeteDialogBinding? = null
    private val viewModel: VMDiabete by viewModels({ requireParentFragment() })


    companion object {
        const val TAG = "Dialog_Frag1"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        private var idtxt = "id"
        private var glytxt = "glycemie"

        var oldid: Int = 0
        var oldglycemie = 0

        var indice = 0

        val frag = DiabeteDialogGlycemie()
        var argFrag = frag.arguments

        fun newInstance(title: String, subTitle: String, indicefrag: Int, id: Int, glycemie: Int): DiabeteDialogGlycemie {
            //permet le transfert de variables entre le parent et le fragment
            //seul les 2 premiers putstring sont importants
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putInt(keyg, indicefrag)

            args.putInt(idtxt, id)
            args.putInt(glytxt, glycemie)

            oldid = id
            oldglycemie = glycemie

            argFrag = args
            indice = indicefrag
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dialogFrag1Binding = DiabeteDialogBinding.inflate(inflater, container, false)
        binding = dialogFrag1Binding

        return dialogFrag1Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.dialogDiabete = this@DiabeteDialogGlycemie
        }

        val tabPeriode = resources.getStringArray(R.array.periodes)

        configSpinner(tabPeriode)
        setupNumberPicker()

        if (oldid == 0) {
            binding!!.btnSaveGlycemie.visibility = View.VISIBLE
            binding!!.btnUpdateGlycemie.visibility = View.GONE
        } else {
            binding!!.btnSaveGlycemie.visibility = View.GONE
            binding!!.btnUpdateGlycemie.visibility = View.VISIBLE
        }

        // TODO a supprimer/cocher a la phase final
        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        binding!!.btnSaveGlycemie.setOnClickListener {
            // TODO a decocher quand implementation du code
            save()
            dismiss()
        }

        binding!!.btnUpdateGlycemie.setOnClickListener {
            // TODO a decocher quand implementation du code
            update()
            dismiss()
        }

        binding!!.btnClearGlycemie.setOnClickListener {
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    fun save() {
        val val1 = binding!!.picker1.value
        val val2 = binding!!.picker2.value
        val val3 = binding!!.picker3.value
        val temp: String = val1.toString() + val2.toString() + val3.toString()
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

        viewModel.insertDiabete(periode, date, heureDuJour.toString(),"dd", temp.toInt() )

    }

    fun update() {
        val val1 = binding!!.picker1.value
        val val2 = binding!!.picker2.value
        val val3 = binding!!.picker3.value
        val temp: String = val1.toString() + val2.toString() + val3.toString()
        //viewModel.updateGlycemie(oldid, temp.toInt())
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

    private fun setupNumberPicker() {
        val numberPicker1 = binding!!.picker1
        val numberPicker2 = binding!!.picker2
        val numberPicker3 = binding!!.picker3

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

    fun gestionCalendar(){
        val dateDuJour= Calendar.getInstance()
        dateDuJour.timeInMillis = System.currentTimeMillis()

        val jourDuJour = dateDuJour.get(Calendar.DAY_OF_MONTH)
        val moisDuJour = dateDuJour.get(Calendar.MONTH)
        val yearDuJour = dateDuJour.get(Calendar.YEAR)
        Toast.makeText(context,
            "la date daujourdhui est $jourDuJour / $moisDuJour / $yearDuJour",
            Toast.LENGTH_LONG).show()


        binding?.datepicker?.init(yearDuJour, moisDuJour, jourDuJour, DatePicker.OnDateChangedListener() { view, year, monthOfYear, dayOfMonth ->
            val monthreel = monthOfYear + 1
            recupDate(
                year,
                monthreel,
                dayOfMonth)
        })
    }

    fun recupDate(year: Int, month: Int, day: Int) : String{
        /*autre façon de recuperer le detail de la date
        * a utiliser directement dans le fragment par exemple
        val jour = binding.datepicker.dayOfMonth
        val mois = binding.datepicker.month
        val moisreel = mois + 1
        val annee = binding.datepicker.year
        */

        //Un petit exemple tout simple de
        // gestion des jours et mois inferieur à 10
        //seulement utile si on veut l'afficher dans un textview
        var jourString: String = day.toString()
        var monthString: String = month.toString()
        //var yearString: String = year.toString()

        if (month<10){
            monthString = "0$month"
        }
        if (day<10){
            jourString = "0$day"
        }
        Toast.makeText(context,
            "la date séléctionnée est " +
                    "$jourString / " +
                    "$monthString / " +
                    "$year",
            Toast.LENGTH_LONG).show()

        //un petit lien avec le databinding
        val dateenstring = "$jourString/$monthString/$year"
        return dateenstring
    }


}