package com.example.gestionnairesante.ui.repas

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.PlatData
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.viewmodels.VMGlycemie
import com.example.gestionnairesante.database.viewmodels.VMPLat
import com.example.gestionnairesante.databinding.DiabeteDialogBinding
import com.example.gestionnairesante.databinding.RepasDialogPlatBinding
import com.example.gestionnairesante.ui.diabete.DiabeteDialogGlycemie

class RepasDialogPlat : DialogFragment() {
    private var binding: RepasDialogPlatBinding? = null
    private val viewModel: VMPLat by viewModels ({ requireParentFragment() })

    companion object {
        const val TAG = "Dialog_plat"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        var indice = 0

        val frag = RepasDialogPlat()
        var argFrag = frag.arguments

        fun newInstance(title: String, subTitle: String, indicefrag: Int): RepasDialogPlat {
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
        val dialogFrag1Binding = RepasDialogPlatBinding.inflate(inflater, container, false)
        binding = dialogFrag1Binding

        return dialogFrag1Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.dialogPlat = this@RepasDialogPlat
        }

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

        if (binding!!.etNomplat.text.isBlank() ) {
            Toast.makeText(context, "youhou ya rien", Toast.LENGTH_LONG).show()
        }else{
            val nomPlat = binding!!.etNomplat.text.toString()
            val caloriePlat = binding!!.etCalories.text.toString()
            val glucidePlat = binding!!.etGlucide.text.toString()

            val newInsert = PlatData(0, nomPlat, caloriePlat.toInt(), glucidePlat.toInt())
            viewModel.insertPlat(newInsert)
        }
    }

}

