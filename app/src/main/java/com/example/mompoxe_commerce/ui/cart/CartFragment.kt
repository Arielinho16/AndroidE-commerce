package com.example.mompoxe_commerce.ui.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mompoxe_commerce.R
import com.example.mompoxe_commerce.data.model.CartItem
import com.example.mompoxe_commerce.databinding.FragmentCartBinding
import com.example.mompoxe_commerce.databinding.ItemCartBinding
import com.example.mompoxe_commerce.viewmodel.CartViewModel
import androidx.navigation.fragment.findNavController


class CartFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: CartAdapter

    private var pendingPayment = false


    private fun userIsLoggedIn(): Boolean {
        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("loggedIn", false)
    }

    private fun setPendingPayment(context: Context, pending: Boolean) {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("pendingPayment", pending).apply()
    }

    private fun isPendingPayment(context: Context): Boolean {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("pendingPayment", false)
    }


    private fun proceedToCheckout() {
        val totalAmount = cartViewModel.cartItems.value?.sumOf { it.product.price * it.quantity } ?: 0.0
        val action = CartFragmentDirections.actionCartFragmentToCheckoutFragment(totalAmount.toFloat())
        findNavController().navigate(action)
        pendingPayment = false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CartAdapter(cartViewModel)
        binding.recyclerViewCart.adapter = adapter
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())



        //  Este es el observer con total
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)

            val total = items.sumOf { it.product.price * it.quantity }
            binding.textViewTotal.text = "Total: $%.2f".format(total)

        }



        //  Botón pagar
        // Botón pagar
        binding.buttonPay.setOnClickListener {
            val totalAmount = cartViewModel.cartItems.value?.sumOf { it.product.price * it.quantity } ?: 0.0
            val action = CartFragmentDirections.actionCartFragmentToCheckoutFragment(totalAmount.toFloat())
            findNavController().navigate(action)
        }



        //  Botón atrás
        binding.toolbarCart.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbarCart.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()

        if (userIsLoggedIn() && isPendingPayment(requireContext())) {
            setPendingPayment(requireContext(), false)

            val totalAmount = cartViewModel.cartItems.value?.sumOf { it.product.price * it.quantity } ?: 0.0
            if (totalAmount > 0.0) {
                val action = CartFragmentDirections.actionCartFragmentToCheckoutFragment(totalAmount.toFloat())
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CartAdapter(
    private val cartViewModel: CartViewModel
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(DIFF_CALLBACK) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.textViewProductName.text = item.product.name
            binding.textViewQuantity.text = item.quantity.toString()
            val subtotal = item.product.price * item.quantity
            binding.textViewSubtotal.text = "Subtotal: $%,.2f".format(subtotal)

            Glide.with(binding.imageViewProduct.context)
                .load(item.product.imageUrl)
                .into(binding.imageViewProduct)

            binding.buttonIncrease.setOnClickListener {
                cartViewModel.increaseQuantity(item)
            }

            binding.buttonDecrease.setOnClickListener {
                cartViewModel.decreaseQuantity(item)
            }


            binding.buttonDelete.setOnClickListener {
                cartViewModel.removeItem(item)
            }
        }


    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
                oldItem.product.id == newItem.product.id

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
                oldItem == newItem
        }
    }
}

