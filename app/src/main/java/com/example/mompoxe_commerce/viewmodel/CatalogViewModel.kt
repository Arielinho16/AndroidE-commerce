package com.example.mompoxe_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mompoxe_commerce.data.MockData
import com.example.mompoxe_commerce.data.model.Product

class CatalogViewModel : ViewModel() {

    private val _allProducts = MutableLiveData<List<Product>>(MockData.productList)
    private val _products = MutableLiveData<List<Product>>(MockData.productList)
    val products: LiveData<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = MockData.productList
    }

    fun filterProducts(query: String, filter: String) {
        val allProducts = MockData.productList

        // 1. Buscar por nombre
        val searched = if (query.isNotBlank()) {
            allProducts.filter { it.name.contains(query, ignoreCase = true) }
        } else {
            allProducts
        }

        // 2. Aplicar filtro
        val result = when (filter) {
            "Precio Ascendente" -> searched.sortedBy { it.price }
            "Precio Descendente" -> searched.sortedByDescending { it.price }
            "Todos" -> searched
            else -> searched.filter { it.category.equals(filter, ignoreCase = true) }
        }

        _products.value = result
    }

}
