package com.example.mompoxe_commerce.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mompoxe_commerce.R
import com.example.mompoxe_commerce.databinding.FragmentPaymentMethodBinding

class PaymentMethodFragment : Fragment() {

    private var _binding: FragmentPaymentMethodBinding? = null
    private val binding get() = _binding!!

    private val args: PaymentMethodFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTotalAmount.text = "Total: $%.2f".format(args.totalAmount)

        binding.radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonCash -> {
                    binding.editTextCashAmount.visibility = View.VISIBLE
                    binding.layoutCardFields.visibility = View.GONE
                    binding.textViewTransferInfo.visibility = View.GONE
                }
                R.id.radioButtonCard -> {
                    binding.editTextCashAmount.visibility = View.GONE
                    binding.layoutCardFields.visibility = View.VISIBLE
                    binding.textViewTransferInfo.visibility = View.GONE
                }
                R.id.radioButtonTransfer -> {
                    binding.editTextCashAmount.visibility = View.GONE
                    binding.layoutCardFields.visibility = View.GONE
                    binding.textViewTransferInfo.visibility = View.VISIBLE
                }
            }
        }

        binding.buttonConfirmPayment.setOnClickListener {
            when (binding.radioGroupPayment.checkedRadioButtonId) {
                R.id.radioButtonCash -> {
                    val amountGiven = binding.editTextCashAmount.text.toString().toFloatOrNull()
                    if (amountGiven == null || amountGiven < args.totalAmount) {
                        Toast.makeText(requireContext(), "Monto inválido", Toast.LENGTH_SHORT).show()
                    } else {
                        val change = amountGiven - args.totalAmount
                        Toast.makeText(requireContext(), "Pago en efectivo confirmado. Cambio: $%.2f".format(change), Toast.LENGTH_LONG).show()
                        navigateToCatalog()
                    }
                }
                R.id.radioButtonCard -> {
                    val card = binding.editTextCardNumber.text.toString()
                    if (card.isBlank()) {
                        Toast.makeText(requireContext(), "Ingrese datos de tarjeta", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Pago con tarjeta confirmado", Toast.LENGTH_LONG).show()
                        navigateToCatalog()
                    }
                }
                R.id.radioButtonTransfer -> {
                    Toast.makeText(requireContext(), "Transferencia confirmada. Verifique los datos enviados.", Toast.LENGTH_LONG).show()
                    navigateToCatalog()
                }
                else -> Toast.makeText(requireContext(), "Seleccione un método de pago", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToCatalog() {
        findNavController().navigate(
            R.id.catalogFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
