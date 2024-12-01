package com.example.appfinanzas

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.view.MenuItem

class TemaActivity : AppCompatActivity() {

    private lateinit var switchTemaClaro: Switch
    private lateinit var switchTemaOscuro: Switch
    private lateinit var textTemaClaro: TextView
    private lateinit var textTemaOscuro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargar el tema antes de setContentView
        loadTheme()

        setContentView(R.layout.activity_tema)

        // Habilitar la flecha de regreso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Referencias a los elementos del layout
        switchTemaClaro = findViewById(R.id.switch_tema_claro)
        switchTemaOscuro = findViewById(R.id.switch_tema_oscuro)
        textTemaClaro = findViewById(R.id.text_tema_claro)
        textTemaOscuro = findViewById(R.id.text_tema_oscuro)

        // Cargar el estado del switch basado en el tema actual
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)

        // Configurar el estado inicial de los switches
        switchTemaClaro.isChecked = !isDarkMode
        switchTemaOscuro.isChecked = isDarkMode

        // Listener para el switch de Tema Claro
        switchTemaClaro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Si se activa Tema Claro, desactiva Tema Oscuro
                switchTemaOscuro.isChecked = false
                toggleTheme(false) // Cambiar a Tema Claro
            }
        }

        // Listener para el switch de Tema Oscuro
        switchTemaOscuro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Si se activa Tema Oscuro, desactiva Tema Claro
                switchTemaClaro.isChecked = false
                toggleTheme(true) // Cambiar a Tema Oscuro
            }
        }
    }

    // Cargar el tema guardado
    private fun loadTheme() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)

        // Aplicar el tema basado en la preferencia almacenada
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // Cambiar el tema y guardar la preferencia
    private fun toggleTheme(isDarkMode: Boolean) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Guardar el estado del tema
        editor.putBoolean("DARK_MODE", isDarkMode)
        editor.apply()

        // Aplicar el nuevo tema
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            updateThemeText(true)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            updateThemeText(false)
        }

        // Reiniciar la actividad para aplicar cambios
        recreate()
    }

    // Actualizar el color del texto dependiendo del tema
    private fun updateThemeText(isDarkMode: Boolean) {
        if (isDarkMode) {
            textTemaClaro.setTextColor(resources.getColor(android.R.color.darker_gray))
            textTemaOscuro.setTextColor(resources.getColor(android.R.color.black))
        } else {
            textTemaClaro.setTextColor(resources.getColor(android.R.color.black))
            textTemaOscuro.setTextColor(resources.getColor(android.R.color.darker_gray))
        }
    }

    // Manejar la acción de la flecha de regreso
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()  // Esto vuelve a la actividad anterior
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
