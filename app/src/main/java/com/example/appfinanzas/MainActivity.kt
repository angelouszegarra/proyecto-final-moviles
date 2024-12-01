package com.example.appfinanzas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Cargamos la nueva vista de inicio

        // Referencias a los CardView
        val cardPresupuestos = findViewById<CardView>(R.id.cardPresupuestos)
        val cardConversor = findViewById<CardView>(R.id.cardConversor)
        val cardCalculadora = findViewById<CardView>(R.id.cardCalculadora)
        val cardTemas = findViewById<CardView>(R.id.cardTemas)

        // Acciones al hacer clic en los CardView
        cardPresupuestos.setOnClickListener {
            val intent = Intent(this, PresupuestosActivity::class.java)
            startActivity(intent)
        }

        cardConversor.setOnClickListener {
            val intent = Intent(this, ConversorActivity::class.java)
            startActivity(intent)
        }

        cardCalculadora.setOnClickListener {
            val intent = Intent(this, CalculadoraActivity::class.java)
            startActivity(intent)
        }

        cardTemas.setOnClickListener {
            val intent = Intent(this, TemaActivity::class.java)
            startActivity(intent)
        }
    }
}
