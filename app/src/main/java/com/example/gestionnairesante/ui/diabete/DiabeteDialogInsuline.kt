package com.example.gestionnairesante.ui.diabete

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.viewmodels.glycemie.VMGlycemie
import com.example.gestionnairesante.database.viewmodels.insuline.VMInsuline
import com.example.gestionnairesante.databinding.DiabeteDialogInsulineBinding

class DiabeteDialogInsuline : DialogFragment() {
    private var binding: DiabeteDialogInsulineBinding? = null
    private val viewModel: VMGlycemie by viewModels({ requireParentFragment() })
    private val viewModelinsuline: VMInsuline by viewModels({ requireParentFragment() })

    companion object {
        const val TAG = "dial_insuline"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        private var idIns = "id_insuline"
        private var txtrapide = "txt Rapide"
        private var txtlente = "txt Lente"

        var indice = 0

        val frag = DiabeteDialogInsuline()
        var argFrag = frag.arguments

        var idInsuline = 0
        var oldLente = 0
        var oldRapide = 0

        fun newInstance(
            title: String,
            subTitle: String,
            indicefrag: Int,
            id: Int,
            rapide: Int,
            lente: Int
        ): DiabeteDialogInsuline {
            //permet le transfert de variables entre le parent et le fragment
            //seul les 2 premiers putstring sont importants
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putInt(keyg, indicefrag)

            //
            // Parametre de la data a modifier
            //
            args.putInt(idIns, id)
            args.putInt(txtrapide, rapide)
            args.putInt(txtlente, lente)

            idInsuline = id
            oldRapide = rapide
            oldLente = lente

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
        val dialogFragBinding = DiabeteDialogInsulineBinding.inflate(inflater, container, false)
        binding = dialogFragBinding

        return dialogFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.dialogInsuline = this@DiabeteDialogInsuline

        }

        val tabPeriode = resources.getStringArray(R.array.periodes)

        configSpinner(tabPeriode)
        setupNumberPicker()

        if (idInsuline == 0) {
            binding!!.btnInsertInsuline.visibility = View.VISIBLE
            binding!!.btnModifInsuline.visibility = View.GONE
        } else {
            binding!!.btnInsertInsuline.visibility = View.GONE
            binding!!.btnModifInsuline.visibility = View.VISIBLE
        }

        // TODO a supprimer/cocher a la phase final
        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        binding!!.btnInsertInsuline.setOnClickListener {
            // TODO a decocher quand implementation du code
            save()
            dismiss()
        }
        binding!!.btnModifInsuline.setOnClickListener {
            // TODO a decocher quand implementation du code
            update()
            dismiss()
        }
        binding!!.btnClearMalade.setOnClickListener {
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
        val val1 = binding!!.pickerRapide1.value
        val val2 = binding!!.pickerRapide2.value
        val val3 = binding!!.pickerLente1.value
        val val4 = binding!!.pickerLente2.value

        val tempRapide: String = val1.toString() + val2.toString()
        val tempLente: String = val3.toString() + val4.toString()

        val newInsert = InsulineData(0, tempRapide.toInt(), tempLente.toInt())
        viewModelinsuline.insertInsuline(newInsert)
    }

    fun update() {
        val val1 = binding!!.pickerRapide1.value
        val val2 = binding!!.pickerRapide2.value
        val val3 = binding!!.pickerLente1.value
        val val4 = binding!!.pickerLente2.value

        val tempRapide: String = val1.toString() + val2.toString()
        val tempLente: String = val3.toString() + val4.toString()
        viewModelinsuline.updateInsuline(idInsuline, tempRapide.toInt(), tempLente.toInt())

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
        val numberPicker1 = binding!!.pickerRapide1
        val numberPicker2 = binding!!.pickerRapide2
        val numberPicker3 = binding!!.pickerLente1
        val numberPicker4 = binding!!.pickerLente2

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

        numberPicker4.minValue = 0
        numberPicker4.maxValue = 9
        numberPicker4.wrapSelectorWheel = true
        numberPicker4.setOnValueChangedListener { picker, oldVal, newVal ->
        }

    }

}