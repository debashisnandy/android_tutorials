package com.example.notes.foundations

import android.text.Editable
import android.text.TextWatcher

open class StateChangeTextWatcher: TextWatcher {

    var previousVale:String? = null

    override fun afterTextChanged(s: Editable?) {
        previousVale = s?.toString()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       // no op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
       // op
    }
}