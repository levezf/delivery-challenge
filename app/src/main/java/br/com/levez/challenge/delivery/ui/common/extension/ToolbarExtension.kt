package br.com.levez.challenge.delivery.ui.common.extension

import android.os.Bundle
import androidx.core.content.ContextCompat
import br.com.levez.challenge.delivery.R
import com.google.android.material.appbar.MaterialToolbar

fun MaterialToolbar.applyNavigationBackIcon() {
    setNavigationIcon(R.drawable.ic_navigation_back)
    setNavigationIconTint(ContextCompat.getColor(context, R.color.gray_60))
}

fun MaterialToolbar.applyTitle(arguments: Bundle?) {
    title = arguments?.getIdDelivery()?.let { id ->
        context.getString(R.string.title_editing_delivery, id)
    } ?: context.getString(R.string.title_registration_delivery)
}
