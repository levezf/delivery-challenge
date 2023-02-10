package br.com.levez.challenge.delivery.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.levez.challenge.delivery.databinding.FragmentDeliveryRegistrationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryRegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveryRegistrationFragment()
    }

    private val viewModel: DeliveryRegistrationViewModel by viewModel()
    private lateinit var binding: FragmentDeliveryRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDeliveryRegistrationBinding.inflate(inflater).apply {
            viewModel = this@DeliveryRegistrationFragment.viewModel
        }

        return binding.root
    }
}