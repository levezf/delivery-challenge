package br.com.levez.challenge.delivery.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.levez.challenge.delivery.R
import br.com.levez.challenge.delivery.databinding.ItemDeliveryBinding
import br.com.levez.challenge.delivery.model.DeliveryMinimal

typealias OnDeliveryClick = (Long) -> Unit

class DeliveryListAdapter(
    private val listener: OnDeliveryClick,
) :
    ListAdapter<DeliveryMinimal, DeliveryListAdapter.DeliveryViewHolder>(DeliveryComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder =
        DeliveryViewHolder.create(parent)

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(listener, getItem(position))
    }

    class DeliveryViewHolder(
        private val binding: ItemDeliveryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: OnDeliveryClick, delivery: DeliveryMinimal) {
            binding.apply {
                this.delivery = delivery
                this.root.setOnClickListener { listener.invoke(delivery.id) }
            }
        }

        companion object {
            fun create(parent: ViewGroup): DeliveryViewHolder {
                val binding = DataBindingUtil.inflate<ItemDeliveryBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_delivery,
                    parent,
                    false
                )
                return DeliveryViewHolder(binding)
            }
        }
    }

    class DeliveryComparator : DiffUtil.ItemCallback<DeliveryMinimal>() {
        override fun areItemsTheSame(oldItem: DeliveryMinimal, newItem: DeliveryMinimal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DeliveryMinimal,
            newItem: DeliveryMinimal,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
