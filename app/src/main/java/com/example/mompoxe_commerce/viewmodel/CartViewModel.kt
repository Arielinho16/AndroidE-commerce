package com.example.mompoxe_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mompoxe_commerce.data.model.CartItem
import com.example.mompoxe_commerce.data.model.Product

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    fun addToCart(product: Product, quantity: Int) {
        val current = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existing = current.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity += quantity
        } else {
            current.add(CartItem(product, quantity))
        }
        _cartItems.value = current
    }

    fun updateItem(item: CartItem) {
        val current = _cartItems.value?.toMutableList() ?: return
        val index = current.indexOfFirst { it.product.id == item.product.id }
        if (index != -1) {
            // ⚡️ Crear nueva instancia para romper la igualdad en DiffUtil
            val updatedItem = item.copy(quantity = item.quantity)
            current[index] = updatedItem
            _cartItems.value = current
        }
    }

    fun increaseQuantity(item: CartItem) {
        updateItem(item.copy(quantity = item.quantity + 1))
    }

    fun decreaseQuantity(item: CartItem) {
        if (item.quantity > 1) {
            updateItem(item.copy(quantity = item.quantity - 1))
        }
    }

    fun removeItem(item: CartItem) {
        val current = _cartItems.value?.toMutableList() ?: return
        current.removeAll { it.product.id == item.product.id }
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}