package br.com.levez.challenge.delivery.ui.common.extension

import android.os.Bundle

private const val ARGUMENT_SHOW_APP_BAR = "showAppBar"
private const val ARGUMENT_ID_DELIVERY = "idDelivery"

fun Bundle?.isAppBarVisible() =
    this?.getBoolean(ARGUMENT_SHOW_APP_BAR, false) == true

fun Bundle?.getIdDelivery() = this?.getString(ARGUMENT_ID_DELIVERY)
