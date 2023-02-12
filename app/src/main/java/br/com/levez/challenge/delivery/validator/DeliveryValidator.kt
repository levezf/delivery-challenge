package br.com.levez.challenge.delivery.validator

import br.com.levez.challenge.delivery.exception.DeliveryException
import br.com.levez.challenge.delivery.exception.InvalidCPFDeliveryException
import br.com.levez.challenge.delivery.exception.RequiredFieldsNotFilledDeliveryException
import br.com.levez.challenge.delivery.model.Delivery

object DeliveryValidator {

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
        if (customerCpf.isEmpty()) return true
        if (customerCpf.contains("[ˆ0-9]")) return true
        if (customerCpf.length != 11) return true
        if (customerCpf.all { it == customerCpf[0] }) return true

        val rangeCpfNumber = IntRange(0, 8)

        val digit1 = rangeCpfNumber.sumOf {
            (it + 1) * customerCpf[it].digitToInt()
        }.rem(11).takeIf { it < 10 } ?: 0

        val digit2 = rangeCpfNumber.sumOf {
            it * customerCpf[it].digitToInt()
        }.let {
            (it + (digit1 * 9)).rem(11)
        }.takeIf { it < 10 } ?: 0

        return customerCpf[9].digitToInt() != digit1 || customerCpf[10].digitToInt() != digit2
    }
}
