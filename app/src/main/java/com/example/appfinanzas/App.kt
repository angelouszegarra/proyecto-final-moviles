package com.example.appfinanzas

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Cargar y aplicar el tema guardado al iniciar la aplicación
        loadTheme()
    }

    // Cargar el tema guardado en SharedPreferences
    private fun loadTheme() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)

        // Aplicar el tema según la preferencia guardada
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Tema oscuro
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  // Tema claro
        }
    }
}
