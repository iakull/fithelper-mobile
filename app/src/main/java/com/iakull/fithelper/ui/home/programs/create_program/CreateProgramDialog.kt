package com.iakull.fithelper.ui.home.programs.create_program

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iakull.fithelper.databinding.DialogCreateProgramBinding
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.ui.home.programs.create_program.CreateProgramDialogDirections.Companion.toProgramDaysFragment
import com.iakull.fithelper.util.hideKeyboard
import com.iakull.fithelper.util.live_data.EventObserver
import com.iakull.fithelper.util.navigate
import com.iakull.fithelper.util.setNewValue
import kotlinx.android.synthetic.main.dialog_create_program.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateProgramDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCreateProgramBinding
    private val vm: CreateProgramViewModel by viewModel()
    private val sharedVM: SharedViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogCreateProgramBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        descriptionET.setOnFocusChangeListener { _, hasFocus ->
            descriptionTIL.isCounterEnabled = hasFocus
        }

        binding.createProgramBtn.setOnClickListener {
            if (validate()) vm.createProgram()
        }

        sharedVM.user.observe(viewLifecycleOwner) { vm.author.setNewValue(it) }
        vm.programCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard()
            navigate(toProgramDaysFragment(it))
        })
    }

    private fun validate(): Boolean {
        val nameIsEmpty = vm.name.value?.trim()?.isEmpty() ?: true
        nameTIL.error = "Заполните поле".takeIf { nameIsEmpty }
        return !nameIsEmpty
    }
}