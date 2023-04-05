package com.example.gestionnairesante.ui.repas

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.database.viewmodels.plat.VMPLat
import com.example.gestionnairesante.databinding.RepasDialogPlatBinding
import com.example.gestionnairesante.ui.diabete.DiabeteDialogInsuline

class RepasDialogPlat : DialogFragment() {
    private var binding: RepasDialogPlatBinding? = null
    private val viewModel: VMPLat by viewModels({ requireParentFragment() })

    companion object {
        const val TAG = "Dialog_plat"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"
        var indice = 0
        val frag = RepasDialogPlat()
        var argFrag = frag.arguments

        private var txtid = "id_plat"
        private var txtnom = "Nom plat"
        private var txtglucide = "txt Glucide"
        private var txtcalorie = "txt Calorie"

        var idPlat = 0
        var oldNom = ""
        var oldGlucide = 0
        var oldCalorie = 0

        fun newInstance(
            title: String,
            subTitle: String,
            indicefrag: Int,
            id: Int,
            nom: String,
            glucide: Int,
            calorie: Int

        ): RepasDialogPlat {
            //permet le transfert de variables entre le parent et le fragment
            //seul les 2 premiers putstring sont importants
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putInt(keyg, indicefrag)

            //
            // Parametre de la data a modifier
            //
            args.putInt(txtid, id)
            args.putString(txtnom, nom)
            args.putInt(txtglucide, glucide)
            args.putInt(txtcalorie, calorie)

            idPlat = id
            oldNom = nom
            oldGlucide = glucide
            oldCalorie = calorie

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
            binding?.dialogPlat = this@RepasDialogPlat
        }

        // TODO a supprimer/cocher a la phase final
        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        if (idPlat == 0) {
            binding!!.btnSavePlat.visibility = View.VISIBLE
            binding!!.btnUpdatePlat.visibility = View.GONE
        } else {
            binding!!.btnSavePlat.visibility = View.GONE
            binding!!.btnUpdatePlat.visibility = View.VISIBLE
        }

        binding!!.btnSavePlat.setOnClickListener {
            // TODO a decocher quand implementation du code
            save()
            dismiss()
        }

        binding!!.btnUpdatePlat.setOnClickListener {
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
        if (binding!!.etNomplat.text.isBlank()) {
            Toast.makeText(context, "youhou ya rien", Toast.LENGTH_LONG).show()
        } else {
            val nomPlat = binding!!.etNomplat.text.toString()
            val caloriePlat = binding!!.etCalories.text.toString()
            val glucidePlat = binding!!.etGlucide.text.toString()

            val newInsert = PlatData(0, nomPlat, caloriePlat.toInt(), glucidePlat.toInt())
            viewModel.insertPlat(newInsert)
        }
    }

    fun update() {
        if (binding!!.etNomplat.text.isBlank()) {
            Toast.makeText(context, "youhou ya rien", Toast.LENGTH_LONG).show()
        } else {
            val nomPlat = binding!!.etNomplat.text.toString()
            val caloriePlat = binding!!.etCalories.text.toString()
            val glucidePlat = binding!!.etGlucide.text.toString()
            viewModel.updatePlat(idPlat, nomPlat, glucidePlat.toInt(), caloriePlat.toInt() )
        }
    }

}

