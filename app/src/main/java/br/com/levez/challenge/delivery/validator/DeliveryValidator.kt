package br.com.levez.challenge.delivery.validator

import androidx.core.text.isDigitsOnly
import br.com.levez.challenge.delivery.exception.DeliveryException
import br.com.levez.challenge.delivery.exception.InvalidCPFDeliveryException
import br.com.levez.challenge.delivery.exception.RequiredFieldsNotFilledDeliveryException
import br.com.levez.challenge.delivery.extension.onlyDigits
import br.com.levez.challenge.delivery.model.Delivery

object DeliveryValidator {

    private const val CPF_LENGTH = 11
    private const val POSITION_CPF_LAST_DIGIT = 8
    private const val POSITION_VERIFYING_DIGIT_1 = 9
    private const val POSITION_VERIFYING_DIGIT_2 = 10
    private const val MAX_VALUE_DIGIT = 9
    private const val DEFAULT_DIGIT_VALUE = 0

    @Throws(DeliveryException::class)
    fun validate(delivery: Delivery) {
        if (delivery.isRequiredFieldsNotFilled()) {
            throw RequiredFieldsNotFilledDeliveryException()
        }
        if (delivery.isInvalidCPF()) {
            throw InvalidCPFDeliveryException()
        }
    }

    private fun Delivery.isRequiredFieldsNotFilled(): Boolean =
        externalId.isBlank() ||
            numberOfPackages.isBlank() ||
            deadline.isBlank() ||
            customerName.isBlank() ||
            customerCpf.isBlank() ||
            addressZipCode.isBlank() ||
            addressState.isBlank() ||
            addressCity.isBlank() ||
            addressNeighborhood.isBlank() ||
            addressStreet.isBlank() ||
            addressNumber.isBlank()

    private fun Delivery.isInvalidCPF(): Boolean {
        val cpf = customerCpf.onlyDigits()
        return when {
            cpf.isEmpty() -> true
            cpf.isDigitsOnly().not() -> true
            cpf.length != CPF_LENGTH -> true
            cpf.all { it == cpf.first() } -> true
            else -> {
                val rangeCpfNumber = IntRange(0, POSITION_CPF_LAST_DIGIT)

                val digit1 = rangeCpfNumber.sumOf {
                    (it + 1) * cpf[it].digitToInt()
                }.rem(CPF_LENGTH).takeIf { it <= MAX_VALUE_DIGIT } ?: DEFAULT_DIGIT_VALUE

                val digit2 = rangeCpfNumber.sumOf {
                    it * cpf[it].digitToInt()
                }.let {
                    (it + (digit1 * POSITION_VERIFYING_DIGIT_1)).rem(CPF_LENGTH)
                }.takeIf { it <= MAX_VALUE_DIGIT } ?: DEFAULT_DIGIT_VALUE

                cpf[POSITION_VERIFYING_DIGIT_1].digitToInt() != digit1 ||
                    cpf[POSITION_VERIFYING_DIGIT_2].digitToInt() != digit2
            }
        }
    }
}
