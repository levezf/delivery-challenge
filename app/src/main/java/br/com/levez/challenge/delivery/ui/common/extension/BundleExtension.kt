package br.com.levez.challenge.delivery.ui.common.extension

import android.os.Bundle

private const val ARGUMENT_SHOW_APP_BAR = "ShowAppBar"

fun Bundle?.isAppBarVisible() =
    this?.getBoolean(ARGUMENT_SHOW_APP_BAR, false) == true
