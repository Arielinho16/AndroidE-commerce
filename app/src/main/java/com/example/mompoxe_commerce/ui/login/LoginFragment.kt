package com.example.mompoxe_commerce.ui.login

import android.content.Context
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
import com.example.mompoxe_commerce.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar visual sin afectar al MainActivity
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back) // usa el ícono que tengas
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val email = getUserSession(requireContext())
        if (email != null) {
            navigateToCatalog()
            return
        }

        binding.buttonLogin.setOnClickListener {
            val emailInput = binding.editTextEmail.text.toString().trim()
            val passwordInput = binding.editTextPassword.text.toString().trim()

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                Toast.makeText(requireContext(), "Email no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!userExists(requireContext(), emailInput)) {
                Toast.makeText(requireContext(), "La cuenta no existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isPasswordValid(requireContext(), emailInput, passwordInput)) {
                Toast.makeText(requireContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveUserSession(requireContext(), emailInput)
            navigateToCatalog()
        }

        binding.buttonRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }




    private fun navigateToCatalog() {
        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("pendingPayment", false).apply()

        val action = LoginFragmentDirections.actionLoginFragmentToCatalogFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Override for menu navigation")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val action = LoginFragmentDirections.actionLoginFragmentToCatalogFragment()
            findNavController().navigate(action)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun saveUserSession(context: Context, email: String) {
            val prefs = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
            prefs.edit().putString("email", email).apply()
        }

        fun getUserSession(context: Context): String? {
            val prefs = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
            return prefs.getString("email", null)
        }

        fun clearUserSession(context: Context) {
            val prefs = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        }

        fun registerUser(context: Context, email: String, password: String) {
            val prefs = context.getSharedPreferences("USERS_DB", Context.MODE_PRIVATE)
            prefs.edit().putString(email, password).apply()
        }

        fun userExists(context: Context, email: String): Boolean {
            val prefs = context.getSharedPreferences("USERS_DB", Context.MODE_PRIVATE)
            return prefs.contains(email)
        }

        fun isPasswordValid(context: Context, email: String, password: String): Boolean {
            val prefs = context.getSharedPreferences("USERS_DB", Context.MODE_PRIVATE)
            val storedPassword = prefs.getString(email, null)
            return storedPassword == password
        }
    }
}

