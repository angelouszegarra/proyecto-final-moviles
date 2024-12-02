package com.example.appfinanzas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Cargamos la vista de inicio

        // Referencias a los CardView
        val cardPresupuestos = findViewById<CardView>(R.id.cardPresupuestos)
        val cardConversor = findViewById<CardView>(R.id.cardConversor)
        val cardCalculadora = findViewById<CardView>(R.id.cardCalculadora)
        val cardTemas = findViewById<CardView>(R.id.cardTemas)
        val cardTransferencia = findViewById<CardView>(R.id.cardTransferencia)

        // Configuración de listeners
        cardPresupuestos.setOnClickListener {
            navigateToActivity(PresupuestosActivity::class.java)
        }

        cardConversor.setOnClickListener {
            navigateToActivity(ConversorActivity::class.java)
        }

        cardCalculadora.setOnClickListener {
            navigateToActivity(CalculadoraActivity::class.java)
        }

        cardTemas.setOnClickListener {
            navigateToActivity(TemaActivity::class.java)
        }

        cardTransferencia.setOnClickListener {
            navigateToActivity(TransferenciaActivity::class.java)
        }
    }

    // Función para manejar la navegación a otras actividades
    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
