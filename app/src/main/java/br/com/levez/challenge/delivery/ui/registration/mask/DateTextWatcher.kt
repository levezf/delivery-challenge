package br.com.levez.challenge.delivery.ui.registration.mask

import android.text.Editable
import android.text.TextWatcher

class DateTextWatcher : TextWatcher {
    companion object {
        private const val DATE_MASK = "##/##/####"
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
        if (editable == null || isRunning || isDeleting) {
            return
        }

        isRunning = true

        val editableLength = editable.length

        if (editableLength >= DATE_MASK.length) {
            isRunning = false
            return
        }

        if (DATE_MASK[editableLength] != '#') {
            editable.append(DATE_MASK[editableLength])
        } else if (DATE_MASK[editableLength - 1] != '#') {
            editable.insert(
                editableLength - 1,
                DATE_MASK,
                editableLength - 1,
                editableLength
            )
        }

        isRunning = false
    }
}
