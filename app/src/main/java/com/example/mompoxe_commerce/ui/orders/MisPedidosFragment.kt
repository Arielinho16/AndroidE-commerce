package com.example.mompoxe_commerce.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mompoxe_commerce.data.model.Order
import com.example.mompoxe_commerce.databinding.FragmentMisPedidosBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MisPedidosFragment : Fragment() {

    private var _binding: FragmentMisPedidosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisPedidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar pedidos guardados
        val orders = getSavedOrders(requireContext())
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOrders.adapter = OrdersAdapter(orders)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSavedOrders(context: Context): List<Order> {
        val prefs = context.getSharedPreferences("ORDERS_DB", Context.MODE_PRIVATE)
        val json = prefs.getString("orders", "[]")
        return Gson().fromJson(json, object : TypeToken<List<Order>>() {}.type)
    }
}
