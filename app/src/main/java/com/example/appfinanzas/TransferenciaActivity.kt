package com.example.appfinanzas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TransferenciaActivity : AppCompatActivity() {

    private lateinit var transactionAdapter: TransactionAdapter
    private val transacciones = mutableListOf<String>()  // Lista de transacciones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transferencia)

        // Habilitar el botón de regresar en la ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Referencias a los campos de entrada
        val spinnerPais = findViewById<Spinner>(R.id.spinnerPais)
        val inputCantidad = findViewById<EditText>(R.id.inputCantidad)
        val inputNombreDestinatario = findViewById<EditText>(R.id.inputNombreDestinatario)
        val inputNumeroDestinatario = findViewById<EditText>(R.id.inputNumeroDestinatario)
        val inputDniDestinatario = findViewById<EditText>(R.id.inputDniDestinatario)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val resultadoTransferencia = findViewById<TextView>(R.id.resultadoTransferencia)
        val igvTextView = findViewById<TextView>(R.id.igvTextView)
        val btnConfirmarTransaccion = findViewById<Button>(R.id.btnConfirmarTransaccion)
        val recyclerViewTransacciones = findViewById<RecyclerView>(R.id.recyclerViewTransacciones1)
        val btnVerTransacciones = findViewById<Button>(R.id.btnVerTransacciones)

        // Inicialización del RecyclerView
        transactionAdapter = TransactionAdapter(transacciones)
        recyclerViewTransacciones.layoutManager = LinearLayoutManager(this)
        recyclerViewTransacciones.adapter = transactionAdapter

        // Inicializa el RecyclerView oculto
        recyclerViewTransacciones.visibility = View.GONE

        // Llenar el Spinner con los países
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.countries_array, // Asegúrate de definir esta lista en strings.xml
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPais.adapter = adapter

        // Configuración para cuando se selecciona un país
        spinnerPais.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val pais = parentView.getItemAtPosition(position).toString()
                val igv = when (pais) {
                    "USA" -> 0.05
                    "México" -> 0.16
                    "Canadá" -> 0.13
                    else -> 0.18
                }
                igvTextView.text = "IGV: ${igv * 100}%"
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Puedes dejarlo vacío si no necesitas hacer nada cuando no hay selección
            }
        }

        // Configuración del botón Calcular
        btnCalcular.setOnClickListener {
            val pais = spinnerPais.selectedItem.toString()
            val cantidad = inputCantidad.text.toString().toDoubleOrNull()
            val nombreDestinatario = inputNombreDestinatario.text.toString()
            val numeroDestinatario = inputNumeroDestinatario.text.toString()
            val dniDestinatario = inputDniDestinatario.text.toString()

            // Validación de la cantidad y los campos de destinatario
            if (cantidad != null && nombreDestinatario.isNotEmpty() && numeroDestinatario.isNotEmpty() && dniDestinatario.isNotEmpty()) {
                // Límite de la transferencia por país
                val limite = when (pais) {
                    "USA" -> 5000
                    "México" -> 3000
                    "Canadá" -> 4000
                    else -> 2000
                }

                // Verificar si la cantidad excede el límite
                if (cantidad > limite) {
                    resultadoTransferencia.text = "Error: El monto excede el límite para $pais"
                } else {
                    // Calcular IGV dependiendo del país
                    val igv = when (pais) {
                        "USA" -> 0.05
                        "México" -> 0.16
                        "Canadá" -> 0.13
                        else -> 0.18
                    }

                    // Calcular el total con IGV
                    val igvMonto = cantidad * igv
                    val total = cantidad + igvMonto

                    // Mostrar el recibo con los detalles
                    val recibo = """
                        Recibo de Transferencia
                        ------------------------
                        País: $pais
                        Monto: $cantidad
                        IGV: $igvMonto
                        Total a enviar: $total

                        Destinatario: $nombreDestinatario
                        DNI: $dniDestinatario
                        Número: $numeroDestinatario
                    """.trimIndent()

                    resultadoTransferencia.text = recibo

                    // Mostrar el botón de confirmar
                    btnConfirmarTransaccion.visibility = View.VISIBLE
                }
            } else {
                resultadoTransferencia.text = "Por favor, ingrese todos los datos correctamente."
            }
        }

        // Configuración del botón Confirmar Transacción
        btnConfirmarTransaccion.setOnClickListener {
            val pais = spinnerPais.selectedItem.toString()
            val cantidad = inputCantidad.text.toString().toDoubleOrNull()
            val nombreDestinatario = inputNombreDestinatario.text.toString()
            val numeroDestinatario = inputNumeroDestinatario.text.toString()
            val dniDestinatario = inputDniDestinatario.text.toString()

            // Obtener la fecha y hora de la transacción
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val date = dateFormat.format(Date())

            // Agregar la transacción al historial, incluyendo la fecha y hora
            val transaccion = """
                Transacción a $pais
                ------------------------
                Monto: $cantidad
                Destinatario: $nombreDestinatario
                Número: $numeroDestinatario
                DNI: $dniDestinatario
                Fecha y hora: $date
            """.trimIndent()

            transacciones.add(transaccion)
            transactionAdapter.notifyDataSetChanged()  // Actualizar el RecyclerView

            // Mostrar mensaje de éxito
            Toast.makeText(this, "Transacción confirmada", Toast.LENGTH_SHORT).show()

            // Limpiar los campos después de confirmar
            inputCantidad.text.clear()
            inputNombreDestinatario.text.clear()
            inputNumeroDestinatario.text.clear()
            inputDniDestinatario.text.clear()

            // Ocultar el botón de confirmar después de la transacción
            btnConfirmarTransaccion.visibility = View.GONE

            // Mostrar el botón para ver las transacciones
            btnVerTransacciones.visibility = View.VISIBLE
        }

        // Mostrar las transacciones al hacer clic en "Ver Transacciones"
        btnVerTransacciones.setOnClickListener {
            recyclerViewTransacciones.visibility = View.VISIBLE  // Mostrar RecyclerView
            btnVerTransacciones.visibility = View.GONE  // Ocultar botón
        }
    }

    // Adapter para el RecyclerView
    class TransactionAdapter(private val transactions: MutableList<String>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.resultadotransferencia, parent, false)
            return TransactionViewHolder(view)
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            val transaction = transactions[position]
            holder.transactionText.text = transaction
        }

        override fun getItemCount(): Int = transactions.size

        class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val transactionText: TextView = view.findViewById(R.id.resultadoTransferencia)
        }
    }

    // Manejar el botón de regresar en la ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Regresar a la actividad anterior
                onBackPressed()  // Regresa a la actividad anterior
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
