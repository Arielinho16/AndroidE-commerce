package com.example.mompoxe_commerce.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mompoxe_commerce.data.model.Product
import com.example.mompoxe_commerce.databinding.FragmentFavoritosBinding
import com.example.mompoxe_commerce.ui.catalog.ProductAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritosFragment : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductAdapter { view, product ->
            val action = FavoritosFragmentDirections.actionFavoritesFragmentToProductDetailFragment(product)
            view.findNavController().navigate(action)
        }

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        val prefs = requireContext().getSharedPreferences("FAVORITES_DB", Context.MODE_PRIVATE)
        val json = prefs.getString("favorites", "[]")
        val type = object : TypeToken<List<Product>>() {}.type
        val list: List<Product> = Gson().fromJson(json, type) ?: emptyList()
        adapter.submitList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
