package com.example.gestionnairesante.ui.repas.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasDialogPlatBinding
import com.example.gestionnairesante.ui.repas.vm.VmRepas
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RepasDialogPlat : BottomSheetDialogFragment() {
    private var binding: RepasDialogPlatBinding? = null
    private val viewmodelrepas: VmRepas by activityViewModels()
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
    ): View {
        val dialogFrag1Binding = RepasDialogPlatBinding.inflate(inflater, container, false)
        binding = dialogFrag1Binding
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.dialogPlat = this@RepasDialogPlat
        }

        binding?.viewmodelrepas = viewmodelrepas

        return dialogFrag1Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        when (idPlat) {
            0-> {
                binding!!.btnSavePlat.visibility = View.VISIBLE
                binding!!.btnUpdatePlat.visibility = View.GONE
            }

            else -> {
                binding!!.btnSavePlat.visibility = View.GONE
                binding!!.btnUpdatePlat.visibility = View.VISIBLE
            }

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

        binding!!.btnAnnulerPlat.setOnClickListener {
            binding!!.etNomplat.setText("")
            binding!!.etCalories.setText("")
            binding!!.etGlucide.setText("")
        }

        binding!!.fermerPlat.setOnClickListener {
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
            viewmodelrepas.ajouterPlat(newInsert)
        }
    }

    fun update() {
        if (binding!!.etNomplat.text.isBlank()) {
            Toast.makeText(context, "youhou ya rien", Toast.LENGTH_LONG).show()
        } else {
            val nomPlat = binding!!.etNomplat.text.toString()
            val caloriePlat = binding!!.etCalories.text.toString()
            val glucidePlat = binding!!.etGlucide.text.toString()
            viewmodelrepas.updatePlat(idPlat, nomPlat, glucidePlat.toInt(), caloriePlat.toInt() )
        }
    }

}

