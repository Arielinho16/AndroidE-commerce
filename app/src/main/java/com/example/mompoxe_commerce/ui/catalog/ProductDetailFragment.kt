package com.example.mompoxe_commerce.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mompoxe_commerce.databinding.FragmentProductDetailBinding
import com.example.mompoxe_commerce.data.model.Product
import com.example.mompoxe_commerce.viewmodel.CartViewModel

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()

    private var quantity = 1

    private val args: ProductDetailFragmentArgs by navArgs()  // <- safe args

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        // Bind info
        binding.textViewName.text = product.name
        binding.textViewPrice.text = "$${product.price}"
        binding.textViewDescription.text = product.description
        binding.textViewQuantity.text = quantity.toString()

        Glide.with(requireContext())
            .load(product.imageUrl)
            .into(binding.imageView)

        binding.buttonIncrease.setOnClickListener {
            quantity++
            binding.textViewQuantity.text = quantity.toString()
        }

        binding.buttonDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.textViewQuantity.text = quantity.toString()
            }
        }

        binding.buttonAddToCart.setOnClickListener {
            cartViewModel.addToCart(product, quantity)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}