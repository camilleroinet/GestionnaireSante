package com.example.gestionnairesante.ui.poids

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterRecyclerPoids
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsRepo
import com.example.gestionnairesante.database.dao.innerPoids.PoidsInner
import com.example.gestionnairesante.databinding.PoidBinding
import com.example.gestionnairesante.ui.poids.vm.VmPoids
import com.example.gestionnairesante.ui.poids.vm.VmPoidsFactory
import com.example.gestionnairesante.utils.createBarChart
import com.github.mikephil.charting.data.BarEntry
import java.math.BigDecimal
import java.math.RoundingMode

class PoidsFragment : Fragment() {

    private var binding: PoidBinding? = null
    private lateinit var viewModel: VmPoids
    private lateinit var adapter: AdapterRecyclerPoids
    private var ind = 0
    private var lastPoua = 0f

    val tabPoids = ArrayList<Float>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val poidsFrg = PoidBinding.inflate(inflater, container, false)
        binding = poidsFrg

        return poidsFrg.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.fragPoids = this@PoidsFragment
        }

        //
        // databinding
        //
        val daoPoids = DB_sante.getInstance(requireContext()).tabPoids
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoPoidsInner = DB_sante.getInstance(requireContext()).tabRelationnelPoids

        val repository = InnerPoidsRepo(daoPoids, daoPeriode, daoPoidsInner)
        val factory = VmPoidsFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(VmPoids::class.java)

        binding?.viewModel = viewModel

        // TODO a cocher a la fin
        // Message de test du viewModel
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        binding?.btnInsert?.setOnClickListener {
            PoidsDialog.newInstance(
                "titre",
                "subtitre",
                ind,
                0, 0,
                0F,
                "", "", ""
            ).show(childFragmentManager, PoidsDialog.TAG)
        }

        viewModel.getAllValeurPoids().observe(viewLifecycleOwner) { it ->
            tabPoids.clear()
            tabPoids.addAll(it)

            val p = tabPoids.size

            if (p > 0) {
                binding?.llAvertChart?.visibility = View.GONE
                binding?.chart0?.visibility = View.VISIBLE
                binding?.tabLayout?.visibility = View.VISIBLE

                lastPoua = tabPoids.get(p - 1)

                val imc = (calculerIMC(171, lastPoua))

                val decimal = BigDecimal(imc).setScale(2, RoundingMode.HALF_EVEN)

                binding!!.rere.text = decimal.toString()
                verifIMC(decimal.toDouble())

                initRecycler()
                touchRecycler()
                displayUser()

                recupDataBarChart()

            } else {
                Toast.makeText(requireContext(), "Aucunes données", Toast.LENGTH_SHORT).show()
                binding?.llAvertChart?.visibility = View.VISIBLE
                binding?.chart0?.visibility = View.GONE
                binding?.tabLayout?.visibility = View.GONE
            }
        }
    }

    fun listItemClicked(viewModel: VmPoids, data: PoidsInner) {


    }

    // TODO Apres implementation du profil modifier la valeur
    //  par defaut de la taille
    // Calcule de l'imc
    fun calculerIMC(taille: Int, poids: Float): Double {
        //  IMC = poids en kg/taille²
        val weight = poids.toDouble()
        val height = (taille.toDouble() / 100)

        return (weight / (height * height))
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    // Test et retour du traitement en fonction de la valeur de l'imc
    fun verifIMC(imc: Double) {
        when (imc) {
            in 0.0F..18.5F -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc1))
                binding?.resultat?.text = "Poids insuffisant"
            }

            in 18.5F..25F -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc2))
                binding?.resultat?.text = "Poids normal"
            }

            in 25F..30F -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc3))
                binding?.resultat?.text = "Surpoids"
            }

            in 30F..35F -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc4))
                binding?.resultat?.text = "Obésité"
            }

            else -> {
                binding?.imgImc?.setImageDrawable(resources.getDrawable(R.drawable.imc5))
                binding?.resultat?.text = "Obésité morbide"
            }
        }

    }

    // Initialisation du recylcer
    fun initRecycler() {
        // Configuration du layout
        binding?.recyclerPoids?.layoutManager = LinearLayoutManager(context)
        // Configuration de l'adapter
        // adapter = AdapterSportD { h: View -> longclickListener(h) }
        adapter =
            AdapterRecyclerPoids { daouser: PoidsInner -> listItemClicked(viewModel, daouser) }
        binding?.recyclerPoids?.adapter = adapter

    }

    // Afficher les données dans le recycler

    fun displayUser() {
        viewModel.getPoidsPeriode().observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    // Gestion tactile du recycler view
    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    when (direction) {
                        ItemTouchHelper.RIGHT -> {
                            viewModel.deletePoidPeriode(obj.idpoi, obj.idper)
                            adapter.remove(sp)
                        }

                        ItemTouchHelper.LEFT -> {
                            val idpoi = obj.idpoi
                            val idper = obj.idper
                            val poids = obj.poids
                            val date = obj.date
                            val heure = obj.heure
                            val periode = obj.periode

                            PoidsDialog.newInstance(
                                "titre",
                                "subtitre",
                                ind,
                                idpoi, idper,
                                poids,
                                date, heure, periode
                            ).show(childFragmentManager, PoidsDialog.TAG)
                        }

                    }

                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == ItemTouchHelper.RIGHT) {
                        viewHolder?.itemView?.alpha = 0.5F
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.alpha = 1.0F
                }
            }
            ItemTouchHelper(simplecall)
        }
        itemTouchHelper.attachToRecyclerView(binding?.recyclerPoids)
    }

    // Traitement du bar chart
    fun recupDataBarChart(): ArrayList<BarEntry> {
        val data = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Float>()
        val barChart = binding!!.chart0
        val stringValue = ArrayList<String>()

        viewModel.getAllValeurPoids().observe(viewLifecycleOwner) {
            tabValeur.clear()
            tabValeur.addAll(it)

            val r = tabValeur.size - 1
            for (i in 0..r) {
                stringValue.add("")
                data.add(BarEntry(i.toFloat(), tabValeur[i]))
            }
            createBarChart(barChart, data, stringValue, "Poids")
        }

        return data
    }



}