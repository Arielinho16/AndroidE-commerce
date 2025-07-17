package com.example.mompoxe_commerce.ui.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mompoxe_commerce.data.model.Product
import com.example.mompoxe_commerce.databinding.ItemProductBinding

class ProductAdapter(
    private val onClick: (View, Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    // Callback para favoritos
    var onFavoriteClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textViewName.text = product.name
            binding.textViewPrice.text = "$${product.price}"

            Glide.with(binding.imageView.context)
                .load(product.imageUrl)
                .into(binding.imageView)

            binding.root.setOnClickListener { view ->
                onClick(view, product)
            }

            //Acción del botón favorito
            binding.buttonFavorite.setOnClickListener {
                onFavoriteClick?.invoke(product)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}

