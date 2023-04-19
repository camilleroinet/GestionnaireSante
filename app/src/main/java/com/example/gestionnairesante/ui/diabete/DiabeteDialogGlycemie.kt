package com.example.gestionnairesante.ui.diabete

import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.R
import com.example.gestionnairesante.databinding.DiabeteDialogBinding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DiabeteDialogGlycemie : DialogFragment() {
    private var binding: DiabeteDialogBinding? = null
    private val viewModel: VMDiabete by viewModels({ requireParentFragment() })

    companion object {
        const val TAG = "Dialog_Frag1"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        private var idtxtgly = "id_glycemie"
        private var idtxtper = "id_periode"
        private var idtxtins = "id_insuline"

        private var glytxt = "glycemie"

        private var rapidetxt = "rapide"
        private var lentetxt = "lente"

        private var datetxt = "date"
        private var heuretxt = "heure"
        private var periodetxt = "periode"

        var oldidgly: Int = 0
        var oldidper: Int = 0
        var oldidins: Int = 0
        var oldglycemie = 0
        var oldrapide = 0
        var oldlente = 0
        var olddate = ""
        var oldheure = ""
        var oldperiode = ""

        var indice = 0

        val frag = DiabeteDialogGlycemie()
        var argFrag = frag.arguments

        fun newInstance(
            title: String,
            subTitle: String,
            indicefrag: Int,
            idgly: Int, idins: Int, idper: Int,
            glycemie: Int,
            rapide: Int, lente: Int,
            date: String, heure: String, periode: String

        ): DiabeteDialogGlycemie {
            //permet le transfert de variables entre le parent et le fragment
            //seul les 2 premiers putstring sont importants
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putInt(keyg, indicefrag)

            args.putInt(idtxtgly, idgly)
            args.putInt(idtxtins, idins)
            args.putInt(idtxtper, idper)

            args.putInt(glytxt, glycemie)
            args.putInt(rapidetxt, rapide)
            args.putInt(lentetxt, lente)

            args.putString(datetxt, date)
            args.putString(heuretxt, heure)
            args.putString(periodetxt, periode)

            oldidgly = idgly
            oldidper = idper
            oldidins = idins
            oldglycemie = glycemie
            oldrapide = rapide
            oldlente = lente
            olddate = date
            oldheure = heure
            oldperiode = periode

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

        if (oldidgly == 0) {
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

        val val7 = binding!!.pickerRapide1.value
        val val8 = binding!!.pickerRapide2.value
        val val9 = binding!!.pickerLente1.value
        val val10 = binding!!.pickerLente2.value
        val tempRapide: String = val7.toString() + val8.toString()
        val tempLente: String = val9.toString() + val10.toString()

        val periode = binding!!.spinnerPeriode.selectedItem.toString()

        val current = LocalDateTime.now()
        val heure = DateTimeFormatter.ofPattern("HH:mm")
        val dateDuJour = Calendar.getInstance()
        dateDuJour.timeInMillis = System.currentTimeMillis()
        val heureDuJour = current.format(heure)

        viewModel.insertDiabete(
            periode, date, heureDuJour.toString(),
            temp.toInt(), tempRapide.toInt(), tempLente.toInt()
        )

    }

    fun update() {
        val val1 = binding!!.picker1.value
        val val2 = binding!!.picker2.value
        val val3 = binding!!.picker3.value
        val temp: String = val1.toString() + val2.toString() + val3.toString()

        val val4 = binding!!.datepicker.dayOfMonth
        val val5 = binding!!.datepicker.month + 1
        val val6 = binding!!.datepicker.year
        val date = "$val4/$val5/$val6"

        val val7 = binding!!.pickerRapide1.value
        val val8 = binding!!.pickerRapide2.value
        val val9 = binding!!.pickerLente1.value
        val val10 = binding!!.pickerLente2.value
        val tempRapide: String = val7.toString() + val8.toString()
        val tempLente: String = val9.toString() + val10.toString()

        val periode = binding!!.spinnerPeriode.selectedItem.toString()

        val current = LocalDateTime.now()
        val heure = DateTimeFormatter.ofPattern("HH:mm")
        val dateDuJour = Calendar.getInstance()
        dateDuJour.timeInMillis = System.currentTimeMillis()
        val heureDuJour = current.format(heure)

        viewModel.updateDiabete(
            oldidgly, oldidins, oldidper,
            temp.toInt(),tempRapide.toInt(), tempLente.toInt(),
            date, heureDuJour.toString(),periode

        )
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

        val pickerRapide1 = binding!!.pickerRapide1
        val pickerRapide2 = binding!!.pickerRapide2
        val pickerLente1 = binding!!.pickerLente1
        val pickerLente2 = binding!!.pickerLente2

        pickerRapide1.minValue = 0
        pickerRapide1.maxValue = 9
        pickerRapide1.wrapSelectorWheel = true
        pickerRapide1.setOnValueChangedListener { picker, oldVal, newVal ->
        }

        pickerRapide2.minValue = 0
        pickerRapide2.maxValue = 9
        pickerRapide2.wrapSelectorWheel = true
        numberPicker2.setOnValueChangedListener { picker, oldVal, newVal ->
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

}