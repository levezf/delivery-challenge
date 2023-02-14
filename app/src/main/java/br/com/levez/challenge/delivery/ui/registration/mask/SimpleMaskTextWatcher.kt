package br.com.levez.challenge.delivery.ui.registration.mask

import android.text.Editable
import android.text.TextWatcher

class SimpleMaskTextWatcher private constructor(private val mask: String) : TextWatcher {
    companion object {
        private const val DATE_MASK = "##/##/####"
        private const val CEP_MASK = "#####-###"

        fun date(): SimpleMaskTextWatcher = SimpleMaskTextWatcher(DATE_MASK)

        fun cep(): SimpleMaskTextWatcher = SimpleMaskTextWatcher(CEP_MASK)
    }

    private var isRunning = false
    private var isDeleting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Not implemented
    }

    override fun afterTextChanged(editable: Editable?) {
        if (editable == null || isRunning || isDeleting) return

        isRunning = true

        val editableLength = editable.length

        if (editableLength == 0 || editableLength >= mask.length) {
            isRunning = false
            return
        }

        if (mask[editableLength] != '#') {
            editable.append(mask[editableLength])
        } else if (mask[editableLength - 1] != '#') {
            editable.insert(
                editableLength - 1,
                mask,
                editableLength - 1,
                editableLength
            )
        }

        isRunning = false
    }
}
