package br.com.levez.challenge.delivery.ui.common.extension

import androidx.core.content.ContextCompat
import br.com.levez.challenge.delivery.R
import com.google.android.material.appbar.MaterialToolbar

fun MaterialToolbar.applyNavigationBackIcon() {
    setNavigationIcon(R.drawable.ic_navigation_back)
    setNavigationIconTint(ContextCompat.getColor(context, R.color.gray_60))
}
