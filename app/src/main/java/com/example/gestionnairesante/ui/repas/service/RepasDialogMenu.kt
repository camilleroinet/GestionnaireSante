package com.example.gestionnairesante.ui.repas.service

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionnairesante.ui.repas.adapter.AdapterRecyclerMenuPlat
import com.example.gestionnairesante.ui.repas.adapter.AdapterRecyclerPlat
import com.example.gestionnairesante.database.dao.innerRepas.InnerPeriodeRepasData
import com.example.gestionnairesante.database.dao.innerPlat.PlatInner
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasDialogMenuBinding
import com.example.gestionnairesante.ui.repas.vm.VmRepas
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class RepasDialogMenu : BottomSheetDialogFragment() {
    private var binding: RepasDialogMenuBinding? = null
    private val viewmodelrepas: VmRepas by activityViewModels()

    private lateinit var adapterPlat: AdapterRecyclerPlat
    private lateinit var adapterListePlatMenu: AdapterRecyclerMenuPlat

    companion object {
        const val TAG = "Dialog_menu"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        var indice = 0

        val frag = RepasDialogMenu()
        var argFrag = frag.arguments

        fun newInstance(title: String, subTitle: String, indicefrag: Int): RepasDialogMenu {
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
    ): View {
        val dialogFrag1Binding = RepasDialogMenuBinding.inflate(inflater, container, false)
        binding = dialogFrag1Binding

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.dialogMenu = this@RepasDialogMenu
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

        //
        // Etape 1 du scenario
        // Je rentre le nom du menu
        //
        binding!!.validerNommenu.setOnClickListener() {
            if(binding!!.etNommenu.text.isEmpty()){
                Toast.makeText(requireContext(), "Entrer un nom de menu", Toast.LENGTH_LONG).show()
            }else{
                //val newMenu = MenuData(0, binding!!.etNommenu.text.toString(), 0, 0f, 0f)
                viewmodelrepas.ajouterMenu(binding!!.etNommenu.text.toString(), 0, 0, 0)
                binding!!.llEtape01.visibility = View.GONE
                binding!!.llEtape02.visibility = View.VISIBLE
                binding!!.etapeInformation.visibility = View.GONE
                binding!!.llEtape03Compo.visibility = View.GONE

            }
        }

        //
        // Etape 2
        // On affiche l'ecran pour le choix de la date et de la periode
        //
        binding!!.btnValiderDate.setOnClickListener {
            saveDate()
            binding!!.llEtape02.visibility = View.GONE
            binding!!.etapeInformation.visibility = View.VISIBLE
            binding!!.llEtape03.visibility = View.VISIBLE
            binding!!.listeRepas.visibility = View.VISIBLE
            binding!!.llEtape03Compo.visibility = View.VISIBLE
            binding!!.llBoutons.visibility = View.VISIBLE

            viewmodelrepas.totalPlats.value = "0"
            viewmodelrepas.totalGlucides.value = "0"
            viewmodelrepas.totalCalories.value = "0"

        }

        binding!!.btnCancelDate.setOnClickListener {
            binding!!.llEtape02.visibility = View.GONE
            binding!!.llEtape01.visibility = View.VISIBLE

        }

        //
        // Etape 3
        // On affiche l'ecran des informations
        // l'utilsateur choisi ses plats
        //
        binding!!.validerMenu.setOnClickListener {
            // TODO a decocher quand implementation du code
            viewmodelrepas.updateMenu(
                // TODO faire un test quand rv vide
                viewmodelrepas.getLastMenuInCurrent(),
                viewmodelrepas.totalPlats.value!!.toInt(),
                viewmodelrepas.totalGlucides.value!!.toInt(),
                viewmodelrepas.totalCalories.value!!.toInt()
            )

            val temp = InnerPeriodeRepasData(0,viewmodelrepas.getLastMenuInCurrent(), viewmodelrepas.getLastPeriodeInCurrent())
            Toast.makeText(requireContext(), "last Menu = ${viewmodelrepas.getLastMenuInCurrent()} , last plat = ${viewmodelrepas.getLastPeriodeInCurrent()}", Toast.LENGTH_LONG).show()
            viewmodelrepas.ajouterInnerPeriodeMenu(temp)

            dismiss()
        }


        binding!!.annulerNommenu.setOnClickListener{
            binding!!.etNommenu.text.clear()
        }
        binding!!.annulerMenu.setOnClickListener {
            dismiss()
        }

        binding!!.annulerNommenu.setOnClickListener {
            val plat1 = PlatData(0,"Croissant", 25,200)
            viewmodelrepas.ajouterPlat(plat1)
            val plat2 = PlatData(0,"Cafe", 0,0)
            viewmodelrepas.ajouterPlat(plat2)
            val plat3 = PlatData(0,"Orange", 10,5)
            viewmodelrepas.ajouterPlat(plat3)
        }

        initRecycler()
        displayUser()
    }
    fun initRecycler() {
        // Configuration du layout
        binding?.listeRepas?.layoutManager = LinearLayoutManager(context)
        // Configuration de l'adapter
        adapterPlat = AdapterRecyclerPlat {
                data: PlatData -> listItemClicked(viewmodelrepas, data)
        }
        binding?.listeRepas?.adapter = adapterPlat

        binding?.listeMenu?.layoutManager = LinearLayoutManager(context)
        adapterListePlatMenu = AdapterRecyclerMenuPlat {
                data: PlatInner -> listItemClicked2(viewmodelrepas, data)
        }
        binding?.listeMenu?.adapter = adapterListePlatMenu
    }

    fun listItemClicked(viewmodelrepas: VmRepas, data: PlatData) {
        viewmodelrepas.composerMenu(data)
        displayPlatInMenu()
    }

    fun listItemClicked2(viewModel: VmRepas, data: PlatInner) {
        viewModel.deletePlatInCurrent(data.idser)
    }

    fun displayUser() {
        viewmodelrepas.getAllPlats().observe(viewLifecycleOwner, Observer {
            val tabPlat = ArrayList<PlatData>()
            tabPlat.clear()
            tabPlat.addAll(it)

            adapterPlat.setList(it)
            adapterPlat.notifyDataSetChanged()
        })
    }
    @SuppressLint("NotifyDataSetChanged")
    fun displayPlatInMenu() {

        viewmodelrepas.getPlatInMenu().observe(viewLifecycleOwner, Observer {
            val tabListePlat = ArrayList<PlatInner>()
            var calories = 0
            var glucides = 0

            tabListePlat.clear()
            tabListePlat.addAll(it)

            for(i in 0..tabListePlat.size-1){
                calories = calories + tabListePlat[i].calPlat
                glucides = glucides + tabListePlat[i].gluPlat
            }
            adapterListePlatMenu.setList(it)
            viewmodelrepas.totalPlats.value = tabListePlat.size.toString()
            viewmodelrepas.totalGlucides.value = glucides.toString()
            viewmodelrepas.totalCalories.value = calories.toString()
            adapterListePlatMenu.notifyDataSetChanged()

        })

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    fun saveDate() {
        val val4 = binding!!.datepicker.dayOfMonth
        val val5 = binding!!.datepicker.month + 1
        val val6 = binding!!.datepicker.year
        val date = "$val4-$val5-$val6"

       // val periode = binding!!.spinnerPeriode.selectedItem.toString()

        val current = LocalDateTime.now()
        val heure = DateTimeFormatter.ofPattern("HH:mm")
        val dateDuJour = Calendar.getInstance()
        dateDuJour.timeInMillis = System.currentTimeMillis()
        val heureDuJour = current.format(heure)

        val data = PeriodeData(0, "periode", date, heureDuJour)
        viewmodelrepas.ajouterPeriode(data)


    }

    fun save() {

    }

}