package com.example.gestionnairesante.ui.poids

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterRecyclerPoids
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.database.dao.poids.PoidsRepo
import com.example.gestionnairesante.database.viewmodels.poids.VMPoids
import com.example.gestionnairesante.database.viewmodels.poids.VMPoidsFactory
import com.example.gestionnairesante.databinding.PoidBinding
import com.example.gestionnairesante.divers.MyDragShadowBuilder
import com.example.gestionnairesante.utils.createBarChart
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


class PoidsFragment : Fragment() {

    private var binding: PoidBinding? = null
    private lateinit var viewModel: VMPoids
    private lateinit var adapter: AdapterRecyclerPoids
    private var ind = 0
    private var lastPoua = 0f
    lateinit var targetMotion: LinearLayout
    lateinit var motionLayout: MotionLayout
    val dureeanimation: Long = 500

    private val onDragTV = View.OnDragListener { view, dragEvent ->
        (view as View).let {
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    if (!targetMotion.isVisible) {
                        //targetMotion.visibility = View.VISIBLE
                        createAnimation(0)
                    }
                    return@OnDragListener true
                }
                else -> return@OnDragListener false
            }
        }
    }

    private val onDragListener = View.OnDragListener { view, dragEvent ->
        //  TODO ATTENTION BIEN VERIFIER QUE LE TYPE CORRESPOND AU XML
        // si cible = linear, ou si cible = framelayout, ou si cible = motionlayout ou autre
        // sinon plante pour erreur de cast
        (view as LinearLayout).let {
            when (dragEvent.action) {

                DragEvent.ACTION_DRAG_STARTED -> {
                    return@OnDragListener true
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    return@OnDragListener true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    it.setBackgroundColor(Color.BLUE)
                    return@OnDragListener true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    return@OnDragListener true
                }

                DragEvent.ACTION_DROP -> {
                    if (targetMotion.isVisible) {
                        //it.setBackgroundColor(Color.BLUE)
                        createAnimation(1)
                    }
                    return@OnDragListener true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    return@OnDragListener true
                }

                else -> return@OnDragListener false
            }
        }
    }

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
            viewModel = viewModel
            binding?.fragPoids = this@PoidsFragment
        }

        //
        // databinding
        //
        val dao = DB_sante.getInstance(requireContext()).tabPoids
        val repository = PoidsRepo(dao)
        val factory = VMPoidsFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(VMPoids::class.java)


        //on declare que le linearlayout est droppable
        //le ll ecoute si qqc a été Drop
        //utilisation du findviewbyid à cause de l'utilisation du framework transition
        targetMotion = view.findViewById<LinearLayout>(R.id.targetMotion)
        targetMotion.setOnDragListener(onDragListener)

        motionLayout = view.findViewById<MotionLayout>(R.id.motionLayout)
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                // TODO
            }

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                //target.visibility = View.VISIBLE
                motionLayout?.transitionToStart()
                Toast.makeText(context, "----> transition débbutée", Toast.LENGTH_SHORT).show()

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                // TODO
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Toast.makeText(context, "transition terminée", Toast.LENGTH_SHORT).show()
            }
        })

        // TODO a cocher a la fin
        // Message de test du viewModel
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getValeurPoids().observe(viewLifecycleOwner) { it ->
            tabPoids.clear()
            tabPoids.addAll(it)
            recupDataBarChart()
            val p = tabPoids.size - 1
            if (p < 0) {
                Toast.makeText(requireContext(), "Aucunes données", Toast.LENGTH_SHORT).show()
            } else {
                lastPoua = tabPoids.get(p)
                val imc = calculerIMC(171, lastPoua)
                binding!!.rere.text = calculerIMC(171, lastPoua).toString()
                verifIMC(imc)
            }
        }

        binding!!.btnInsert.setOnClickListener {
            PoidsDialog.newInstance("titre", "subtitre", ind, 0, 0F)
                .show(childFragmentManager, PoidsDialog.TAG)
        }

        initRecycler()
        displayUser()
        touchRecycler()

    }

    fun listItemClicked(viewModel: VMPoids, data: PoidsData) {
        val id = viewModel.getPoidsToUpdate(data).id_poids
        val poids =  viewModel.getPoidsToUpdate(data).valeur_poids

        if (poids != null) {
            PoidsDialog.newInstance(
                "titre",
                "subtitre",
                ind,
                id, poids
            ).show(childFragmentManager, PoidsDialog.TAG)
        }
    }
/*    fun undoSwipe(adapt: AdapterSportD, pos: Int){
        adapt.notifyItemRemoved(pos)
        adapt.notifyItemInserted(pos)
    }*/
    // TODO Apres implementation du profil modifier la valeur
    //  par defaut de la taille
    // Calcule de l'imc
    fun calculerIMC(taille: Int, poids: Float): Float {
        //  IMC = poids en kg/taille²
        val weight = poids.toDouble()
        val height = (taille.toDouble() / 100)

        return (weight / (height * height)).toFloat()
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    // Test et retour du traitement en fonction de la valeur de l'imc
    fun verifIMC(imc: Float) {
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
        adapter = AdapterRecyclerPoids { daouser: PoidsData -> listItemClicked(viewModel, daouser) }
        binding?.recyclerPoids?.adapter = adapter

    }

    // Afficher les données dans le recycler
    @SuppressLint("NotifyDataSetChanged")
    fun displayUser() {
        viewModel.getAllPoids().observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    // Gestion tactile du recycler view
    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = binding?.recyclerPoids?.adapter as AdapterRecyclerPoids
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
/*                    obj.valeur_poids?.let {
                        PoidsDialog.newInstance("titre", "subtitre", ind, obj.id_poids,
                            it
                        ).show(childFragmentManager, PoidsDialog.TAG)
                    }*/
                    viewModel.deletePoids(obj)
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

        viewModel.getValeurPoids().observe(viewLifecycleOwner) {
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

    // Animation du dragNdop
    fun createAnimation(indice: Int) {
        when (indice) {
            0 -> {
                targetMotion.setBackgroundColor(Color.BLACK)
                targetMotion.visibility = View.VISIBLE
                startAnimation(
                    targetMotion,
                    AccelerateDecelerateInterpolator(),
                    dureeanimation,
                    indice
                )
            }
            1 -> {
                attendre()
                startAnimation2(targetMotion, AccelerateInterpolator(), dureeanimation, indice)
                targetMotion.visibility = View.GONE
                targetMotion.setBackgroundColor(Color.BLACK)
            }
        }
    }

    /**
     * Gestion de l'animation
     */
    fun startAnimation(
        view: View,
        interpolator: AccelerateDecelerateInterpolator,
        duration: Long,
        indice: Int
    ): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(view, View.SCALE_X, View.SCALE_Y, initPath(indice))
        animator.duration = duration
        animator.interpolator = interpolator
        animator.start()
        return animator
    }

    // Ou
    fun startAnimation2(
        view: View,
        interpolator: AccelerateInterpolator,
        duration: Long,
        indice: Int
    ): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(view, View.SCALE_X, View.SCALE_Y, initPath(indice))
        animator.duration = duration
        animator.interpolator = interpolator
        animator.start()
        return animator
    }

    fun attendre() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(dureeanimation))
            withContext(Dispatchers.Main) {
                //createAnimation(1)
                targetMotion.visibility = View.GONE
                targetMotion.setBackgroundColor(Color.BLACK)
            }
        }
    }

    private fun initPath(indice: Int): Path {
        val path = Path()

        if (indice == 0) {
            path.moveTo(0.2f, 0.2f)
            path.lineTo(1f, 1f)
        } else if (indice == 1) {
            path.moveTo(1f, 1f)
            path.lineTo(0.2f, 0.2f)
        }
        return path
    }

    /**
     * Gestion du dragNdrop
     */
    fun longclickListener(f: View) {
        f.setOnDragListener(onDragTV)
        gestionDrag(f)
    }

    fun gestionDrag(v: View) {
        val item = ClipData.Item(v.tag as? CharSequence)
        val dragData = ClipData(
            v.tag as? CharSequence,
            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
            item
        )
        val myShadow = MyDragShadowBuilder(v)

        v.startDragAndDrop(dragData, myShadow, null, 0)
    }
}