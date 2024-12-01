package com.example.appfinanzas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem // Importar MenuItem

class CalculadoraActivity : AppCompatActivity() {

    private lateinit var editTextTargetAmount: EditText
    private lateinit var editTextMonthlySavings: EditText
    private lateinit var editTextInterestRate: EditText
    private lateinit var buttonCalculate: Button
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora)

        // Habilitar la flecha de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Referencias a los elementos del layout
        editTextTargetAmount = findViewById(R.id.editText_target_amount)
        editTextMonthlySavings = findViewById(R.id.editText_monthly_savings)
        editTextInterestRate = findViewById(R.id.editText_interest_rate)
        buttonCalculate = findViewById(R.id.button_calculate)
        textViewResult = findViewById(R.id.textView_result)

        // Listener para el botón de cálculo
        buttonCalculate.setOnClickListener {
            calculateSavings()
        }
    }

    private fun calculateSavings() {
        val targetAmount = editTextTargetAmount.text.toString().toDoubleOrNull()
        val monthlySavings = editTextMonthlySavings.text.toString().toDoubleOrNull()
        val interestRate = editTextInterestRate.text.toString().toDoubleOrNull()

        if (targetAmount != null && monthlySavings != null) {
            var months = 0.0
            var totalSaved = 0.0

            while (totalSaved < targetAmount) {
                totalSaved += monthlySavings
                months++
                // Agregar interés si se proporciona
                if (interestRate != null && interestRate > 0) {
                    totalSaved += (totalSaved * (interestRate / 100) / 12) // Interés mensual
                }
            }

            // Calcular intereses generados si aplica
            val interestEarned = if (interestRate != null && interestRate > 0) {
                totalSaved - (monthlySavings * months)
            } else {
                0.0
            }

            // Mostrar resultados en el TextView correspondiente
            textViewResult.text = "Resultados:\n" +
                    "Monto total ahorrado: $${String.format("%.2f", totalSaved)}\n" +
                    "Intereses generados: $${String.format("%.2f", interestEarned)}\n" +
                    "Tiempo necesario para alcanzar el objetivo: ${months.toInt()} meses aproximadamente"

            clearFields()

        } else {
            textViewResult.text = "Por favor ingresa valores válidos."
        }
    }

    private fun clearFields() {
        editTextTargetAmount.text.clear()
        editTextMonthlySavings.text.clear()
        editTextInterestRate.text.clear()
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