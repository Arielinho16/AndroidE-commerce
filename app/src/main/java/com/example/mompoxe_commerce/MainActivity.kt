package com.example.mompoxe_commerce

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mompoxe_commerce.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.catalogFragment,
                R.id.ordersFragment,
                R.id.favoritesFragment
            ),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // Cambiar el texto de login/logout dinámicamente
        val prefs = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
        val loggedIn = prefs.contains("email")
        val menu = binding.navigationView.menu
        val loginLogoutItem = menu.findItem(R.id.nav_login_logout)

        if (loggedIn) {
            loginLogoutItem.title = "Cerrar sesión"
            loginLogoutItem.setIcon(R.drawable.ic_logout)
        } else {
            loginLogoutItem.title = "Iniciar sesión"
            loginLogoutItem.setIcon(R.drawable.ic_login)
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_catalog -> {
                    if (navController.currentDestination?.id != R.id.catalogFragment) {
                        navController.navigate(R.id.catalogFragment)
                    }
                }
                R.id.nav_orders -> {
                    if (navController.currentDestination?.id != R.id.ordersFragment) {
                        navController.navigate(R.id.ordersFragment)
                    }
                }
                R.id.nav_favorites -> {
                    if (navController.currentDestination?.id != R.id.favoritesFragment) {
                        navController.navigate(R.id.favoritesFragment)
                    }
                }
                R.id.nav_login_logout -> {
                    if (loggedIn) {
                        prefs.edit().clear().apply()
                        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        recreate() // Recargar actividad para actualizar el menú
                    } else {
                        navController.navigate(R.id.loginFragment)
                    }
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Mostrar/ocultar toolbar y drawer en login y register
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    binding.toolbar.visibility = View.GONE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}