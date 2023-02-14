package br.com.levez.challenge.delivery.ui.registration.mask

import android.text.Editable
import android.text.TextWatcher
import br.com.levez.challenge.delivery.extension.onlyDigits
import br.com.levez.challenge.delivery.extension.sanitize
import com.google.android.material.textfield.TextInputEditText

class SimpleMaskTextWatcher private constructor(
    private val editText: TextInputEditText,
    private val mask: String,
) : TextWatcher {
    companion object {
        private const val DATE_MASK = "##/##/####"
        private const val CEP_MASK = "#####-###"
        private const val CPF_MASK = "###.###.###-##"

        fun date(editText: TextInputEditText): SimpleMaskTextWatcher =
            SimpleMaskTextWatcher(editText, DATE_MASK)

        fun cep(editText: TextInputEditText): SimpleMaskTextWatcher =
            SimpleMaskTextWatcher(editText, CEP_MASK)

        fun cpf(editText: TextInputEditText): SimpleMaskTextWatcher =
            SimpleMaskTextWatcher(editText, CPF_MASK)
    }

    private var isRunning = false
    private var isDeleting = false
    private var old = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val str = s.toString().sanitize().onlyDigits()
        if (isRunning || isDeleting) {
            old = str
            isRunning = false
            return
        }

        val strWithMask = buildString {
            var i = 0
            for (char in mask.toCharArray()) {
                if (char != '#' && str.length > old.length) {
                    append(char)
                    continue
                }
                append(
                    try {
                        str[i]
                    } catch (e: Exception) {
                        break
                    }
                )
                i++
            }
        }

        isRunning = true
        editText.apply {
            setText(strWithMask)
            setSelection(strWithMask.length)
        }
    }

    override fun afterTextChanged(editable: Editable?) {
       // Do nothing
    }
}
