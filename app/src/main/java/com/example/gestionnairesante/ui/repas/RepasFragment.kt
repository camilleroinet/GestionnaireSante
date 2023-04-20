package com.example.gestionnairesante.ui.repas

import android.animation.ObjectAnimator
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterRecyclerPlat
import com.example.gestionnairesante.adapter.AdapterViewPager
import com.example.gestionnairesante.adapter.AdapterViewPagerCharts
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasBinding
import com.example.gestionnairesante.divers.MyDragShadowBuilder
import com.example.gestionnairesante.ui.diabete.*
import com.example.gestionnairesante.ui.repas.vm.VmRepas
import com.example.gestionnairesante.ui.repas.vm.VmRepasFactory
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class RepasFragment : Fragment() {
    private var binding: RepasBinding? = null
    private lateinit var viewmodelrepas: VmRepas
    private lateinit var adapterPlat: AdapterRecyclerPlat

    private lateinit var tablayoutTabs: TabLayout
    private lateinit var viewPagerTabs: ViewPager
    private lateinit var viewPagerCharts: ViewPager

    private var ind = 0

    private var arrayTab = arrayListOf<Int>(R.string.txt_fragmenu, R.string.txt_fragrepas)
    private var arrayFragTab = arrayListOf<Fragment>(RepasTab1(), RepasTab2())

    private var arrayTabCharts =
        arrayListOf<Int>(R.string.txt_chart1, R.string.txt_chart2, R.string.txt_chart3)
    private var arrayFragChart = arrayListOf<Fragment>(DiabeteChartLine(), DiabeteChartPie())

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
                        Toast.makeText(context, "Ajout reussi", Toast.LENGTH_SHORT).show()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val repasFrag = RepasBinding.inflate(inflater, container, false)
        binding = repasFrag
        return repasFrag.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodelrepas = viewmodelrepas
            binding?.fragRepas = this@RepasFragment

        }
        // databinding
        val daoPlat = DB_sante.getInstance(requireContext()).tabPlat
        val daoMenu = DB_sante.getInstance(requireContext()).tabMenu
        val daoRepas = DB_sante.getInstance(requireContext()).tabRelationnelPlat

        val repositoryPlatMenu = InnerPlatMenuRepo(daoPlat, daoMenu, daoRepas)
        val factory = VmRepasFactory(repositoryPlatMenu)
        viewmodelrepas = ViewModelProvider(this, factory).get(VmRepas::class.java)

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


        val tabPlat = ArrayList<PlatData>()


        viewmodelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                //Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewmodelrepas.getAllPlats().observe(viewLifecycleOwner) { it ->
            tabPlat.clear()
            tabPlat.addAll(it)
        }

        binding!!.btnMenu.setOnClickListener {
            binding!!.vueChart.visibility = View.GONE
            binding!!.vueMenu.visibility = View.VISIBLE
        }

        binding!!.btnInsertPlat.setOnClickListener {
            RepasDialogPlat.newInstance("titre", "subtitre", ind, 0, "", 0, 0)
                .show(childFragmentManager, RepasDialogPlat.TAG)
            //Toast.makeText(requireContext(), "youhou", Toast.LENGTH_LONG).show()
        }

        binding!!.validerMenu.setOnClickListener {
            val plat1 = PlatData(0,"Croissant", 25,200)
            viewmodelrepas.ajouterPlat(plat1)
        }
        viewPagerCharts = binding?.viewpagercharts!!

        tablayoutTabs = binding?.tabLayout!!
        viewPagerTabs = binding?.viewpagertabhost!!

        configTablelayout(arrayTab)
        //configViewPagerChart(viewPagerCharts, arrayFragChart, arrayTabCharts)
        configViewPager(viewPagerTabs, arrayFragTab, arrayTab, tablayoutTabs)


        initRecycler()
        displayUser()
    }

    fun initRecycler() {
        // Configuration du layout
        binding?.listeRepas?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapterPlat = AdapterRecyclerPlat {
                h: View -> longclickListener(h) }
        binding?.listeRepas?.adapter = adapterPlat

    }
    fun listItemClicked(viewModel: VmRepas, data: PlatData) {

    }

    fun longclickListener(f: View) {
        f.setOnDragListener(onDragTV)
        gestionDrag(f)
    }

    fun displayUser() {
        viewmodelrepas.getAllPlats().observe(viewLifecycleOwner, Observer {
            //Toast.makeText(requireContext(), "size ==>> ${it.size}", Toast.LENGTH_LONG).show()
            adapterPlat.setList(it)
            adapterPlat.notifyDataSetChanged()
        })
    }

    fun configTablelayout(array: ArrayList<Int>) {
        tablayoutTabs.apply {
            for (i in array.indices) {
                tablayoutTabs.addTab(tablayoutTabs.newTab().setText(array[i]))
            }

            tablayoutTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        viewPagerTabs.currentItem = it

                        when (tab?.position) {
                            0 -> {
                                binding!!.btnInsertPlat.visibility = View.INVISIBLE
                                binding!!.btnMenu.visibility = View.VISIBLE
                            }
                            1 -> {
                                binding!!.btnInsertPlat.visibility = View.VISIBLE
                                binding!!.btnMenu.visibility = View.INVISIBLE
                            }

                        }

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}

            })

        }
    }

    fun doDelete(adapt: AdapterRecyclerPlat, pos: Int){
        adapt.notifyItemRemoved(pos)
        viewmodelrepas.deletePlat(pos)
        //adapt.notifyItemInserted(pos)
    }
    fun configViewPager(
        viewPager: ViewPager,
        arrayFrag: ArrayList<Fragment>,
        arrayTab: ArrayList<Int>,
        tablayout: TabLayout
    ) {
        viewPager.apply {
            viewPager.adapter = AdapterViewPager(
                arrayFrag, arrayTab,
                childFragmentManager, tablayout.tabCount, context
            )
        }
        tablayout.setupWithViewPager(viewPager, true)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    fun configViewPagerChart(
        viewPager: ViewPager,
        arrayFrag: ArrayList<Fragment>,
        arrayTab: ArrayList<Int>
    ) {
        viewPager.apply {
            viewPager.adapter = AdapterViewPagerCharts(
                arrayFrag, arrayTab,
                childFragmentManager, context
            )
        }
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

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