package br.com.levez.challenge.delivery.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.StringRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.levez.challenge.delivery.R
import br.com.levez.challenge.delivery.databinding.FragmentDeliveryRegistrationBinding
import br.com.levez.challenge.delivery.network.manager.ConnectionState
import br.com.levez.challenge.delivery.ui.registration.mask.SimpleMaskTextWatcher
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryRegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveryRegistrationFragment()
    }

    private val args: DeliveryRegistrationFragmentArgs by navArgs()
    private val viewModel: DeliveryRegistrationViewModel by viewModel()
    private lateinit var binding: FragmentDeliveryRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.idDelivery = args.idDelivery?.toLongOrNull()
        }
        requireActivity().title = args.idDelivery?.let { id ->
            getString(R.string.title_editing_delivery, id)
        } ?: getString(R.string.title_registration_delivery)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDeliveryRegistrationBinding.inflate(inflater).apply {
            initializeMasks()

            lifecycleOwner = this@DeliveryRegistrationFragment.viewLifecycleOwner
            viewModel = this@DeliveryRegistrationFragment.viewModel

            initializeListeners()
        }

        with(viewModel) {
            uiState.observe(viewLifecycleOwner, ::changeState)
            failure.observe(viewLifecycleOwner, ::showFailure)
            internetConnectionState.observe(viewLifecycleOwner, ::changeInternetConnection)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.idDelivery != null) {
            initializeMenu()
        }
    }

    private fun FragmentDeliveryRegistrationBinding.initializeListeners() {
        autoCompleteState.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            this@DeliveryRegistrationFragment.viewModel.refreshCity()
        }
        includeBottomButton.bottomActionButton.setOnClickListener {
            this@DeliveryRegistrationFragment.viewModel.validateAndRegisterDelivery()
        }
    }

    private fun FragmentDeliveryRegistrationBinding.initializeMasks() {
        editTextDeadline.addTextChangedListener(
            SimpleMaskTextWatcher.date(editTextDeadline)
        )
        editTextZipCode.addTextChangedListener(
            SimpleMaskTextWatcher.cep(editTextZipCode)
        )
        editTextCustomerCpf.addTextChangedListener(
            SimpleMaskTextWatcher.cpf(editTextCustomerCpf)
        )
    }

    private fun changeInternetConnection(connectionState: ConnectionState) {
        if (connectionState == ConnectionState.NOT_CONNECTED) {
            findNavController().navigate(
                DeliveryRegistrationFragmentDirections
                    .actionDeliveryRegistrationFragmentToNoConnectionActivity()
            )
        }
    }

    private fun showFailure(@StringRes failureRes: Int?) {
        failureRes?.let { res ->
            Snackbar.make(binding.root, res, Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.includeBottomButton.viewLine).show()
        }
    }

    private fun changeState(uiState: DeliveryRegistrationUiState) {
        when (uiState) {
            DeliveryRegistrationUiState.Editing -> {
                setBottomButtonState(true, R.string.text_create)
            }
            DeliveryRegistrationUiState.Registering -> {
                setBottomButtonState(false, R.string.text_registering)
            }
            DeliveryRegistrationUiState.NoInternetConnection -> {
                setBottomButtonState(false)
            }
            DeliveryRegistrationUiState.Loading -> {
                setBottomButtonState(false, R.string.text_loading)
            }
            DeliveryRegistrationUiState.Finished -> {
                finish()
            }
        }
    }

    private fun initializeMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_delivery, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    if (menuItem.itemId == R.id.action_delete_delivery) {
                        viewModel.deleteDelivery()
                        true
                    } else {
                        false
                    }
            },
            viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    private fun setBottomButtonState(enable: Boolean = false, @StringRes stringRes: Int? = null) {
        binding.includeBottomButton.bottomActionButton.apply {
            text = stringRes?.let { getString(it) }
            isEnabled = enable
        }
    }

    private fun finish() {
        findNavController().popBackStack()
    }
}
