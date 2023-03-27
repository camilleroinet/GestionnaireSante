package com.example.gestionnairesante.ui.poids

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.PoidsData
import com.example.gestionnairesante.database.viewmodels.VMPoids
import com.example.gestionnairesante.databinding.PoidsDialogBinding

class PoidsDialog : DialogFragment() {
    private var binding: PoidsDialogBinding? = null
    private val viewModel: VMPoids by viewModels ({ requireParentFragment() })

    companion object {
        const val TAG = "Poids_Dialog"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        var indice = 0

        val frag = PoidsDialog()
        var argFrag = frag.arguments

        fun newInstance(title: String, subTitle: String, indicefrag: Int): PoidsDialog {
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
        val dialogFragBinding = PoidsDialogBinding.inflate(inflater, container, false)
        binding = dialogFragBinding

        return dialogFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.dialogPoids = this@PoidsDialog
        }

        /**
         * Spinner
         */
        val spinner = binding!!.spinnerPeriode

        // TODO a decocher quand implementation du code

        val tabPeriode=resources.getStringArray(R.array.periodes)

        configSpinner(tabPeriode)


        // TODO a supprimer/cocher a la phase final
        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner){ it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        binding!!.btnDate.setOnClickListener(){
            binding!!.llDate.visibility = View.VISIBLE
            binding!!.btnDate.visibility = View.GONE
            binding!!.llTitredate.visibility = View.GONE

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
            val newInsert = PoidsData(0, note.toFloat())
            viewModel.insertPoids(newInsert)
        }
    }


    /**
     * Fonction de gestion du spinner
     */
    fun configSpinner(arrayCat: Array<String>){
        /* en simple java
        mInterpolatorSpinner = (Spinner) view.findViewById(R.id.interpolatorSpinner);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, mInterpolatorNames);
        mInterpolatorSpinner.setAdapter(spinnerAdapter);*/

        //val arrayCat = resources.getStringArray(R.array.categoriesfs)
        val adapt = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayCat)
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
