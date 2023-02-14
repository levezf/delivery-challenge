package br.com.levez.challenge.delivery.extension

fun String.onlyDigits(): String = replace("\\D".toRegex(), "")

fun String?.sanitize(): String = orEmpty().trim()
