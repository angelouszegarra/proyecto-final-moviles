package com.example.appfinanzas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem

class ConversorActivity : AppCompatActivity() {

    private lateinit var spinnerFromCurrency: Spinner
    private lateinit var spinnerToCurrency: Spinner
    private lateinit var editTextAmount: EditText
    private lateinit var buttonConvert: Button
    private lateinit var textViewResult: TextView

    // Lista de monedas disponibles para la conversión
    private val currencyList = listOf("Soles Peruanos (PEN)", "Dólares Estadounidenses (USD)", "Euros (EUR)")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversor)

        // Habilitar la flecha de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Referencias a los elementos del layout
        spinnerFromCurrency = findViewById(R.id.spinner_from_currency)
        spinnerToCurrency = findViewById(R.id.spinner_to_currency)
        editTextAmount = findViewById(R.id.editText_amount)
        buttonConvert = findViewById(R.id.button_convert)
        textViewResult = findViewById(R.id.textView_result)

        // Configurar Spinners con la lista de monedas
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFromCurrency.adapter = adapter
        spinnerToCurrency.adapter = adapter

        // Listener para el botón de conversión
        buttonConvert.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        // Obtener las monedas seleccionadas y la cantidad ingresada
        val fromCurrency = spinnerFromCurrency.selectedItem.toString().substringBefore(" (")
        val toCurrency = spinnerToCurrency.selectedItem.toString().substringBefore(" (")

        val amount = editTextAmount.text.toString().toDoubleOrNull()

        if (amount != null) {
            // Obtener la tasa de conversión y calcular el resultado
            val conversionRate = getConversionRate(fromCurrency, toCurrency)
            val result = amount * conversionRate

            // Mostrar el resultado en el TextView correspondiente
            textViewResult.text = String.format("%.2f %s", result, toCurrency)
        } else {
            Toast.makeText(this, "Por favor ingresa una cantidad válida", Toast.LENGTH_SHORT).show()
            textViewResult.text = ""
        }
    }

    private fun getConversionRate(fromCurrency: String, toCurrency: String): Double {
        return when (fromCurrency) {
            "Soles Peruanos" -> when (toCurrency) {
                "Dólares Estadounidenses" -> 0.27
                "Euros" -> 0.25
                else -> 1.0
            }
            "Dólares Estadounidenses" -> when (toCurrency) {
                "Soles Peruanos" -> 3.74
                "Euros" -> 0.95
                else -> 1.0
            }
            "Euros" -> when (toCurrency) {
                "Soles Peruanos" -> 3.96
                "Dólares Estadounidenses" -> 1.06
                else -> 1.0
            }
            else -> 1.0
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Cierra la actividad actual y regresa a la anterior
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
