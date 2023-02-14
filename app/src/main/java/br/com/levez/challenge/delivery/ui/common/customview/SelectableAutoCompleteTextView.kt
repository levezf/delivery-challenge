package br.com.levez.challenge.delivery.ui.common.customview

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class SelectableAutoCompleteTextView : MaterialAutoCompleteTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(
        context: Context,
        attributeSet:
            AttributeSet,
        defStyleAttr: Int,
    ) : super(context, attributeSet, defStyleAttr)

    init {
        threshold = 0
        setOnClickListener {
            if (adapter != null) {
                performFiltering("", 0)
                showDropDown()
            }
        }
    }

    override fun enoughToFilter(): Boolean = true
}
