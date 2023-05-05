@file: JvmName("Pickers")
@file: JvmMultifileClass

package com.example.gestionnairesante.utils

import android.widget.NumberPicker


fun setupNumberPicker(picker: NumberPicker, min: Int, max: Int) : NumberPicker {

    picker.minValue = min
    picker.maxValue = max
    picker.wrapSelectorWheel = false
    picker.setOnValueChangedListener { picker, oldVal, newVal ->

    }
    return picker
}

/*
    numberPicker2.minValue = 0
    numberPicker2.maxValue = 9
    numberPicker2.wrapSelectorWheel = true
    numberPicker2.setOnValueChangedListener { picker, oldVal, newVal ->
    }

    numberPicker3.minValue = 0
    numberPicker3.maxValue = 9
    numberPicker3.wrapSelectorWheel = true
    numberPicker3.setOnValueChangedListener { picker, oldVal, newVal ->
    }


    pickerRapide1.minValue = 0
    pickerRapide1.maxValue = 9
    pickerRapide1.wrapSelectorWheel = true
    pickerRapide1.setOnValueChangedListener { picker, oldVal, newVal ->
    }

    pickerRapide2.minValue = 0
    pickerRapide2.maxValue = 9
    pickerRapide2.wrapSelectorWheel = true
    numberPicker2.setOnValueChangedListener { picker, oldVal, newVal ->
    }

    pickerLente1.minValue = 0
    pickerLente1.maxValue = 9
    pickerLente1.wrapSelectorWheel = true
    pickerLente1.setOnValueChangedListener { picker, oldVal, newVal ->
    }

    pickerLente2.minValue = 0
    pickerLente2.maxValue = 9
    pickerLente2.wrapSelectorWheel = true
    pickerLente2.setOnValueChangedListener { picker, oldVal, newVal ->
    }
*/
