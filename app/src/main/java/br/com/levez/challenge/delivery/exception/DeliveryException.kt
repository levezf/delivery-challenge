package br.com.levez.challenge.delivery.exception

import androidx.annotation.StringRes

abstract class DeliveryException(@StringRes val messageId: Int) : Exception()