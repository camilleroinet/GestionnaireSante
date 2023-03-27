package com.example.gestionnairesante.ui.diabete

import android.R
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.viewmodels.VMGlycemie
import com.example.gestionnairesante.database.viewmodels.VMInsuline
import com.example.gestionnairesante.databinding.DiabeteDialogInsulineBinding

class DiabeteDialogInsuline : DialogFragment() {
    private var binding: DiabeteDialogInsulineBinding? = null
    private val viewModel: VMGlycemie by viewModels ({ requireParentFragment() })
    private val viewModelinsuline: VMInsuline by viewModels ({ requireParentFragment() })

    companion object {
        const val TAG = "Dialog_Frag1"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        var indice = 0

        val frag = DiabeteDialogInsuline()
        var argFrag = frag.arguments

        fun newInstance(title: String, subTitle: String, indicefrag: Int): DiabeteDialogInsuline {
            //permet le transfert de variables entre le parent et le fragment
            //seul les 2 premiers putstring sont importants
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putInt(keyg, indicefrag)
            argFrag = args
            indice = indicefrag
            return frag
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogFragBinding = DiabeteDialogInsulineBinding.inflate(inflater, container, false)
        binding = dialogFragBinding

        return dialogFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            viewModelinsuline = viewModelinsuline
            binding?.dialogInsuline = this@DiabeteDialogInsuline

        }

        /**
         * Spinner
         */
        val spinner = binding!!.spinnerPeriode

        // TODO a decocher quand implementation du code
/*        viewModel.getAllPeriode().observe(viewLifecycleOwner){
            val tabPeriode= ArrayList<String>()
            tabPeriode.clear()
            tabPeriode.addAll(it)
            configSpinner(tabPeriode)
        }*/

        // TODO a supprimer/cocher a la phase final
        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner){ it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        binding!!.btnSaveMalade.setOnClickListener{
            // TODO a decocher quand implementation du code
            save()
            dismiss()
        }
        binding!!.btnClearMalade.setOnClickListener{
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

    fun save(){

        if (binding!!.etPoids.text.isBlank() ) {
            Toast.makeText(context, "youhou ya rien", Toast.LENGTH_LONG).show()
        }else{
            val note = binding!!.etPoids.text.toString()
            val note2 = binding!!.etPoids.text.toString()

            val newInsert = InsulineData(0, note.toInt(), note2.toInt())
            viewModelinsuline.insertInsuline(newInsert)
        }
    }

    /**
     * Fonction de gestion du spinner
     */
    fun configSpinner(arrayCat: ArrayList<String>){
        /* en simple java
        mInterpolatorSpinner = (Spinner) view.findViewById(R.id.interpolatorSpinner);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, mInterpolatorNames);
        mInterpolatorSpinner.setAdapter(spinnerAdapter);*/

        //val arrayCat = resources.getStringArray(R.array.categoriesfs)
        val adapt = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, arrayCat)
        binding?.let {
            with(it.spinnerPeriode){
                adapter = adapt
                setSelection(0, false)
                prompt= "Selection catagorie"
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
                Toast.makeText(requireContext(), "spinner selection ======> $position", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}



