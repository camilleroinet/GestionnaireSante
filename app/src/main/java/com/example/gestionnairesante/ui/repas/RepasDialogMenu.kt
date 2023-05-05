package com.example.gestionnairesante.ui.repas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.databinding.RepasDialogMenuBinding
import com.example.gestionnairesante.ui.repas.vm.VmRepas


class RepasDialogMenu : DialogFragment() {
    private var binding: RepasDialogMenuBinding? = null
    private val viewmodelrepas: VmRepas by activityViewModels()

    companion object {
        const val TAG = "Dialog_menu"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private var keyg = "indice"

        var indice = 0

        val frag = RepasDialogMenu()
        var argFrag = frag.arguments

        fun newInstance(title: String, subTitle: String, indicefrag: Int): RepasDialogMenu {
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
    ): View {
        val dialogFrag1Binding = RepasDialogMenuBinding.inflate(inflater, container, false)
        binding = dialogFrag1Binding

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodelrepas = viewmodelrepas
            binding?.dialogMenu = this@RepasDialogMenu
        }

        binding?.viewmodelrepas = viewmodelrepas

        return dialogFrag1Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO a supprimer/cocher a la phase final
        //creation de message pout l'utilisateur si qqc est arrivé
        viewmodelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

/*        binding!!.btnValiderDate.setOnClickListener {
            // TODO a decocher quand implementation du code
            save()
            dismiss()
        }
        binding!!.btnClearMalade.setOnClickListener {
            dismiss()
        }*/

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    fun save() {

    }

}