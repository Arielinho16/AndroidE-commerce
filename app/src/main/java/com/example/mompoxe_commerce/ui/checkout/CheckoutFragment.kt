package com.example.mompoxe_commerce.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mompoxe_commerce.R
import com.example.mompoxe_commerce.data.model.Order
import com.example.mompoxe_commerce.databinding.FragmentCheckoutBinding
import com.example.mompoxe_commerce.viewmodel.CartViewModel
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val args: CheckoutFragmentArgs by navArgs()
    private val cartViewModel: CartViewModel by activityViewModels()
    private var pinCode: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.textViewTotalAmount.text = "Total: $%.2f".format(args.totalAmount)

        binding.radioGroupDelivery.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonDelivery -> {
                    binding.editTextAddress.visibility = View.VISIBLE
                    binding.textViewPickupPin.visibility = View.GONE
                }
                R.id.radioButtonPickup -> {
                    binding.editTextAddress.visibility = View.GONE

                }
            }
        }
        binding.buttonConfirm.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val surname = binding.editTextSurname.text.toString().trim()
            val phone = binding.editTextPhone.text.toString().trim()
            val idNumber = binding.editTextId.text.toString().trim()

            if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || idNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.radioButtonDelivery.isChecked) {
                val address = binding.editTextAddress.text.toString()
                if (address.isBlank()) {
                    Toast.makeText(requireContext(), "Escribe tu dirección", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // Limpiar carrito
                cartViewModel.clearCart()
                Toast.makeText(requireContext(), "¡Pedido para delivery confirmado!", Toast.LENGTH_LONG).show()

            } else if (binding.radioButtonPickup.isChecked) {
                val pin = generatePin(name, surname, idNumber)
                pinCode = pin
                binding.textViewPickupPin.visibility = View.VISIBLE
                binding.textViewPickupPin.text = "Tu PIN de retiro: $pin"
                // Limpiar carrito
                cartViewModel.clearCart()
                Toast.makeText(requireContext(), "¡Retiro en tienda con PIN: $pin!", Toast.LENGTH_LONG).show()
            }


            // Ir al PaymentMethodFragment y pasarle los argumentos
            val action = CheckoutFragmentDirections
                .actionCheckoutFragmentToPaymentMethodFragment(
                    totalAmount = args.totalAmount,
                    pickupPin = pinCode
                )
            findNavController().navigate(action)

        }

    }

    private fun generatePin(name: String, surname: String, idNumber: String): String {
        val firstLetterName = if (name.isNotEmpty()) name[0] else 'X'
        val firstLetterSurname = if (surname.isNotEmpty()) surname[0] else 'Y'
        val randomNumbers = (10000..99999).random()

        val idSuffix = if (idNumber.length >= 3) {
            idNumber.takeLast(3)
        } else {
            idNumber.padStart(3, '0')
        }

        return "$firstLetterName$firstLetterSurname$randomNumbers$idSuffix".uppercase()
    }

    private fun saveOrder(order: Order) {
        val prefs = requireContext().getSharedPreferences("ORDERS_DB", Context.MODE_PRIVATE)
        val existing = prefs.getString("orders", "[]")
        val type = object : TypeToken<MutableList<Order>>() {}.type
        val list: MutableList<Order> = Gson().fromJson(existing, type) ?: mutableListOf()
        list.add(order)
        prefs.edit().putString("orders", Gson().toJson(list)).apply()
    }



    @Deprecated("Override for menu navigation")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
