package br.com.levez.challenge.delivery.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.levez.challenge.delivery.R
import br.com.levez.challenge.delivery.databinding.FragmentDeliveryListBinding
import br.com.levez.challenge.delivery.model.DeliveryMinimal
import br.com.levez.challenge.delivery.ui.common.crossFadeAnimation
import br.com.levez.challenge.delivery.ui.list.adapter.DeliveryListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveryListFragment : Fragment() {

    companion object {
        private const val ZERO = "0"

        fun newInstance() = DeliveryListFragment()
    }

    private val viewModel: DeliveryListViewModel by viewModel()

    private lateinit var binding: FragmentDeliveryListBinding

    private val deliveryListAdapter = DeliveryListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDeliveryListBinding.inflate(inflater).apply {
            lifecycleOwner = this@DeliveryListFragment.viewLifecycleOwner
            viewModel = this@DeliveryListFragment.viewModel

            recyclerDeliveries.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = deliveryListAdapter
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }

        viewModel.uiState.asLiveData().observe(viewLifecycleOwner, ::changeState)
        viewModel.openDeliveryRegistration.observe(viewLifecycleOwner, ::openDeliveryRegistration)

        return binding.root
    }

    private fun changeState(uiState: ListDeliveryUiState) {
        when (uiState) {
            is ListDeliveryUiState.Loading -> showLoadingContent()
            is ListDeliveryUiState.Empty -> showEmptyContent(R.string.message_empty_delivery_list)
            is ListDeliveryUiState.Loaded -> showLoadedContent(uiState.deliveries)
        }
    }

    private fun openDeliveryRegistration(@Suppress("UNUSED_PARAMETER") void: Void?) {
        findNavController().navigate(
            DeliveryListFragmentDirections
                .actionDeliveryListFragmentToDeliveryRegistrationFragment()
        )
    }

    private fun showLoadingContent() {
        binding.apply {
            recyclerDeliveries.visibility = View.GONE
            layoutEmptyList.root.visibility = View.GONE
            badgeCounterDeliveries.text = ZERO
            loading.visibility = View.VISIBLE
        }
    }

    private fun showEmptyContent(@StringRes stringId: Int) {
        binding.apply {
            layoutEmptyList.textEmptyList.text = getString(stringId)
            val viewForGone = if (recyclerDeliveries.isVisible) {
                recyclerDeliveries
            } else {
                loading
            }
            viewForGone.crossFadeAnimation(layoutEmptyList.root)
            badgeCounterDeliveries.text = ZERO
        }
    }

    private fun showLoadedContent(deliveries: List<DeliveryMinimal>) {
        binding.apply {
            if (recyclerDeliveries.isGone) {
                val viewForGone = if (loading.isVisible) {
                    loading
                } else {
                    layoutEmptyList.root
                }
                viewForGone.crossFadeAnimation(recyclerDeliveries)
            }
            badgeCounterDeliveries.text = deliveries.size.toString()
        }
        deliveryListAdapter.submitList(deliveries)
    }
}
