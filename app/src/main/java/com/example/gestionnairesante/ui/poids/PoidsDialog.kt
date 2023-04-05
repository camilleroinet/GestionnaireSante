package com.example.gestionnairesante.ui.poids

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.database.viewmodels.poids.VMPoids
import com.example.gestionnairesante.databinding.PoidsDialogBinding

class PoidsDialog : DialogFragment() {
    private var binding: PoidsDialogBinding? = null
    private val viewModel: VMPoids by viewModels({ requireParentFragment() })


    // Configuration de dialogfrag
    companion object {
        const val TAG = "Poids_Dialog"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"
        private var idtxt = "id"
        private var poidstxt = "poids"

        var oldid: Int = 0
        var oldpoids: Float = 0F
        var indice = 0
        val frag = PoidsDialog()
        var argFrag = frag.arguments

        fun newInstance(
            title: String,
            subTitle: String,
            indicefrag: Int,
            id: Int,
            poids: Float
        ): PoidsDialog {
            //permet le transfert de variables entre le parent et le fragment
            //seuls les 2 premiers putstring sont importants
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putInt(keyg, indicefrag)
            args.putInt(idtxt, id)
            args.putFloat(poidstxt, poids)

            oldid = id
            oldpoids = poids

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
            //viewModel = viewModel
            binding?.dialogPoids = this@PoidsDialog
        }
        /**
         * Spinner
         */
        // TODO a decocher quand implementation du code

        val tabPeriode = resources.getStringArray(R.array.periodes)

        configSpinner(tabPeriode)
        setupNumberPicker()

        if (oldid == 0 && oldpoids == 0F) {
            binding!!.btnInsertPoids.visibility = View.VISIBLE
            binding!!.btnUpdatePoids.visibility = View.GONE
        } else {
            binding!!.btnInsertPoids.visibility = View.GONE
            binding!!.btnUpdatePoids.visibility = View.VISIBLE
        }

        // TODO a supprimer/cocher a la phase final
        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        binding!!.btnInsertPoids.setOnClickListener {
            // TODO a decocher quand implementation du code
            // Sauvegrade des données et fermeture de la dialog
            save()
            dismiss()
        }
        binding!!.btnUpdatePoids.setOnClickListener {
            // TODO a decocher quand implementation du code
            // Sauvegrade des données et fermeture de la dialog
            update()
            dismiss()
        }
        binding!!.btnCancel.setOnClickListener {
            // Fermeture de la dialog sans tansfert de données
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
        val newInsert = PoidsData(0, temp.toFloat())
        viewModel.insertPoids(newInsert)
    }

    fun update() {
        val val1 = binding!!.picker1.value
        val val2 = binding!!.picker2.value
        val val3 = binding!!.picker3.value
        val temp: String = val1.toString() + val2.toString() + val3.toString()
        viewModel.updatePoids(oldid, temp.toFloat())
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
                //  posAdapter = position
                //  nomCategorie = arrayCat[position]
                //  gestionRecycler(position, nomCategorie)
                //  Toast.makeText(requireContext(), "spinner selection ======> $position", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupNumberPicker() {
        val numberPicker1 = binding!!.picker1
        val numberPicker2 = binding!!.picker2
        val numberPicker3 = binding!!.picker3

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
