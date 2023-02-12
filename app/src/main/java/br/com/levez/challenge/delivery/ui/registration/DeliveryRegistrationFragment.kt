package br.com.levez.challenge.delivery.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import br.com.levez.challenge.delivery.R
import br.com.levez.challenge.delivery.databinding.FragmentDeliveryRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryRegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveryRegistrationFragment()
    }

    private val viewModel: DeliveryRegistrationViewModel by viewModel()
    private lateinit var binding: FragmentDeliveryRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDeliveryRegistrationBinding.inflate(inflater).apply {
            lifecycleOwner = this@DeliveryRegistrationFragment.viewLifecycleOwner
            viewModel = this@DeliveryRegistrationFragment.viewModel

            includeBottomButton.bottomActionButton.setOnClickListener {
                this@DeliveryRegistrationFragment.viewModel.validateAndRegisterDelivery()
            }
        }

        with(viewModel) {
            uiState.observe(viewLifecycleOwner, ::changeState)
            failure.observe(viewLifecycleOwner, ::showFailure)
        }

        return binding.root
    }

    private fun showFailure(@StringRes failureRes: Int?) {
        failureRes?.let { res ->
            Snackbar.make(binding.root, res, Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.includeBottomButton.viewLine).show()
        }
    }

    private fun changeState(uiState: DeliveryRegistrationUiState) {
        when (uiState) {
            DeliveryRegistrationUiState.Editing -> showContentEditing()
            DeliveryRegistrationUiState.Registering -> showContentRegistering()
            DeliveryRegistrationUiState.Registered -> finish()
        }
    }

    private fun showContentRegistering() {
        binding.includeBottomButton.bottomActionButton.text = getString(R.string.text_registering)
    }

    private fun showContentEditing() {
        binding.includeBottomButton.bottomActionButton.text = getString(R.string.text_create)
    }

    private fun finish() {
//        findNavController().popBackStack()
    }
}
