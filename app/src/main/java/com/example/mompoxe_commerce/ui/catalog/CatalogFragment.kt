package com.example.mompoxe_commerce.ui.catalog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mompoxe_commerce.databinding.FragmentCatalogBinding
import com.example.mompoxe_commerce.viewmodel.CatalogViewModel
import androidx.navigation.fragment.findNavController
import com.example.mompoxe_commerce.R
import com.example.mompoxe_commerce.data.model.Product
import com.example.mompoxe_commerce.ui.catalog.CatalogFragmentDirections.Companion.actionCatalogFragmentToLoginFragment

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModels()
    private var adapter: ProductAdapter? = null  // ✅ ahora nullable

    private fun userIsLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
        return prefs.contains("email")
    }

    private fun clearUserSession(context: Context) {
        val prefs = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    private fun toggleFavorite(context: Context, product: Product) {
        val prefs = context.getSharedPreferences("FAVORITES", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val current = prefs.getStringSet("favorite_ids", mutableSetOf()) ?: mutableSetOf()

        if (current.contains(product.id.toString())) {
            current.remove(product.id.toString())
            Toast.makeText(context, "${product.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
        } else {
            current.add(product.id.toString())
            Toast.makeText(context, "${product.name} agregado a favoritos", Toast.LENGTH_SHORT).show()
        }

        editor.putStringSet("favorite_ids", current)
        editor.apply()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        //  solo inicializar adapter si es null
        if (adapter == null) {
            adapter = ProductAdapter { view, product ->
                val action = CatalogFragmentDirections
                    .actionCatalogFragmentToProductDetailFragment(product)
                view.findNavController().navigate(action)
            }
        }

        //  Callback para favoritos
        adapter?.onFavoriteClick = { product ->
            toggleFavorite(requireContext(), product)
        }


        // Configurar el spinner
        val adapterSpinner = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_options,
            android.R.layout.simple_spinner_item
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilter.adapter = adapterSpinner

        binding.buttonSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString().trim()
            val selectedFilter = binding.spinnerFilter.selectedItem.toString()

            if (query.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, escribe el producto que deseas buscar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.filterProducts(query, selectedFilter)
        }


        // Observador para cambios
        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val searchQuery = binding.editTextSearch.text.toString()
                val filter = parent.getItemAtPosition(position).toString()
                viewModel.filterProducts(searchQuery, filter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.recyclerViewCatalog.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@CatalogFragment.adapter
        }

        binding.buttonCart.setOnClickListener {
            val action = CatalogFragmentDirections.actionCatalogFragmentToCartFragment()
            findNavController().navigate(action)
        }

        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter?.submitList(products)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_catalog, menu)
        val item = menu.findItem(R.id.action_login_logout)
        if (userIsLoggedIn(requireContext())) {
            item.title = "Cerrar sesión"
        } else {
            item.title = "Iniciar sesión"
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_login_logout -> {
                if (userIsLoggedIn(requireContext())) {
                    clearUserSession(requireContext())
                    Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    requireActivity().invalidateOptionsMenu()
                } else {
                    val action = CatalogFragmentDirections.actionCatalogFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
