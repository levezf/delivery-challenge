package br.com.levez.challenge.delivery.ui.registration.binding

import androidx.databinding.BindingAdapter
import br.com.levez.challenge.delivery.model.City
import br.com.levez.challenge.delivery.ui.common.customview.SelectableAutoCompleteTextView

@BindingAdapter("app:availableCities")
fun SelectableAutoCompleteTextView.setAvailableCities(cities: List<City>?) {
    setSimpleItems(cities?.map(City::name)?.toTypedArray() ?: emptyArray<String>())
}
