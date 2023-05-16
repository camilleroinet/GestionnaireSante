package com.example.gestionnairesante.ui.poids.service

import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.gestionnairesante.R
import com.example.gestionnairesante.databinding.PoidsDialogBinding
import com.example.gestionnairesante.ui.poids.vm.VmPoids
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PoidsDialog : BottomSheetDialogFragment() {
    private var binding: PoidsDialogBinding? = null
    private val viewModel: VmPoids by activityViewModels()

    // Configuration de dialogfrag
    companion object {
        const val TAG = "Poids_Dialog"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        private var idpoitxt = "idgly"
        private var idpertxt = "idper"

        private var poidstxt = "poids"
        private var datetxt = "date"
        private var heuretxt = "heure"
        private var periodetxt = "periode"

        var oldidper: Int = 0
        var oldidpoi: Int = 0
        var oldpoids = 0F
        var olddate = ""
        var oldheure = ""
        var oldperiode = ""

        var indice = 0
        val frag = PoidsDialog()
        var argFrag = frag.arguments

        fun newInstance(
            title: String,
            subTitle: String,
            indicefrag: Int,
            idpoi: Int,
            idper: Int,
            poids: Float,
            date: String,
            heure: String,
            periode: String
        ): PoidsDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putInt(keyg, indicefrag)
            args.putString(idpoitxt, idpoitxt)
            args.putString(idpertxt, idpertxt)
            args.putString(poidstxt, poidstxt)
            args.putString(datetxt, datetxt)
            args.putString(heuretxt, heuretxt)
            args.putString(periodetxt, periodetxt)

            oldidpoi = idpoi
            oldidper = idper
            oldpoids = poids
            olddate = date
            oldheure = heure
            oldperiode = periode

            argFrag = args
            indice = indicefrag
            return frag
        }
    }

    // Creation de la vue
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dialogFragBinding = PoidsDialogBinding.inflate(inflater, container, false)
        binding = dialogFragBinding

        return dialogFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.dialogPoids = this@PoidsDialog
        }

        val tabPeriode = resources.getStringArray(R.array.periodes)

        configSpinner(tabPeriode)
        setupNumberPicker()

        if (oldidpoi == 0 && oldidper == 0) {
            binding!!.btnInsertPoids.visibility = View.VISIBLE
            binding!!.btnUpdatePoids.visibility = View.GONE
        } else {
            binding!!.btnInsertPoids.visibility = View.GONE
            binding!!.btnUpdatePoids.visibility = View.VISIBLE
        }

        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        binding!!.btnInsertPoids.setOnClickListener {
            save()
            dismiss()
        }
        binding!!.btnUpdatePoids.setOnClickListener {
            update()
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
        //
        // Poids
        //
        val val1 = binding!!.poidsPicker1.value
        val val2 = binding!!.poidsPicker2.value
        val val3 = binding!!.poidsPicker3.value
        val poids: String = val1.toString() + val2.toString() + val3.toString()

        //
        // Date
        //
        val val4 = binding!!.datepickerpoids.dayOfMonth
        val val5 = binding!!.datepickerpoids.month + 1
        val val6 = binding!!.datepickerpoids.year
        val date = "$val4-$val5-$val6"

        //
        // Periode
        //
        val periode = binding!!.spinnerPeriodepoids.selectedItem.toString()

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

    fun update() {
        //
        // Poids
        //
        val val1 = binding!!.poidsPicker1.value
        val val2 = binding!!.poidsPicker2.value
        val val3 = binding!!.poidsPicker3.value
        val poids: String = val1.toString() + val2.toString() + val3.toString()

        //
        // Date
        //
        val val4 = binding!!.datepickerpoids.dayOfMonth
        val val5 = binding!!.datepickerpoids.month + 1
        val val6 = binding!!.datepickerpoids.year
        val date = "$val4/$val5/$val6"

        //
        // Periode
        //
        val periode = binding!!.spinnerPeriodepoids.selectedItem.toString()

        //
        // Heure
        //
        val current = LocalDateTime.now()
        val heure = DateTimeFormatter.ofPattern("HH:mm")
        val dateDuJour = Calendar.getInstance()
        dateDuJour.timeInMillis = System.currentTimeMillis()
        val heureDuJour = current.format(heure)

        viewModel.updatePoids(oldidpoi, oldidper, poids.toFloat(), date, heureDuJour.toString(), periode)
    }

    /**
     * Fonction de gestion du spinner
     */
    fun configSpinner(arrayCat: Array<String>) {
        val adapt = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayCat)
        binding?.let {
            with(it.spinnerPeriodepoids) {
                adapter = adapt
                setSelection(0, false)
                prompt = "Selection catagorie"
                gravity = Gravity.CENTER
                //posAdapter = 0
                //nomCategorie = arrayCat[0]
                //gestionRecycler(0, nomCategorie)
            }
        }
        binding?.spinnerPeriodepoids?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //  Toast.makeText(requireContext(), "spinner selection ======> $position", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupNumberPicker() {
        val numberPicker1 = binding!!.poidsPicker1
        val numberPicker2 = binding!!.poidsPicker2
        val numberPicker3 = binding!!.poidsPicker3

        // Picker des centaines
        numberPicker1.minValue = 0
        numberPicker1.maxValue = 9
        numberPicker1.wrapSelectorWheel = true
        numberPicker1.setOnValueChangedListener { picker, oldVal, newVal ->
        }
        // Picker des dizaines
        numberPicker2.minValue = 0
        numberPicker2.maxValue = 9
        numberPicker2.wrapSelectorWheel = true
        numberPicker2.setOnValueChangedListener { picker, oldVal, newVal ->
        }
        // Picker des unites
        numberPicker3.minValue = 0
        numberPicker3.maxValue = 9
        numberPicker3.wrapSelectorWheel = true
        numberPicker3.setOnValueChangedListener { picker, oldVal, newVal ->
        }
    }

}
