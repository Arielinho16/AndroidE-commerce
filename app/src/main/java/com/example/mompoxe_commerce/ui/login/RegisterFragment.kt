package com.example.mompoxe_commerce.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mompoxe_commerce.R
import com.example.mompoxe_commerce.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar visual independiente con botón de volver
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back) // usa tu ícono aquí
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonCreateAccount.setOnClickListener {
            val nombre = binding.editTextNombre.text.toString().trim()
            val apellido = binding.editTextApellido.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val telefono = binding.editTextTelefono.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Email no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (LoginFragment.userExists(requireContext(), email)) {
                Toast.makeText(requireContext(), "El usuario ya está registrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar usuario
            LoginFragment.registerUser(requireContext(), email, password)

            // Guardar sesión
            LoginFragment.saveUserSession(requireContext(), email)

            Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_registerFragment_to_catalogFragment)
        }
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
