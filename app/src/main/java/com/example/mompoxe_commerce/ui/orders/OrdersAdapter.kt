package com.example.mompoxe_commerce.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mompoxe_commerce.data.model.Order
import com.example.mompoxe_commerce.databinding.ItemOrderBinding

class OrdersAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(inflater, parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.binding.textViewOrderInfo.text =
            "${order.nombre} ${order.apellido}\nTel: ${order.telefono}\nTotal: $${order.total}\nDelivery: ${if (order.delivery) "Sí" else "No"}"
    }

    override fun getItemCount(): Int = orders.size
}
