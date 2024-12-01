package com.example.appfinanzas

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import android.app.AlertDialog

class PresupuestosActivity : AppCompatActivity() {
    private lateinit var etCategoria: EditText
    private lateinit var etMonto: EditText
    private lateinit var spinnerFrecuencia: Spinner
    private lateinit var etFechaInicio: EditText
    private lateinit var etFechaLimite: EditText
    private lateinit var tvDatosGuardados: TextView
    private lateinit var listView: ListView

    private val calendar = Calendar.getInstance()
    private val listaPresupuestos = mutableListOf<Presupuesto>()
    private lateinit var adapter: PresupuestoAdapter // Cambiado a PresupuestoAdapter
    private val sharedPreferences by lazy {
        getSharedPreferences("Presupuestos", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presupuesto)

        // Configurar el ActionBar para mostrar la flecha de retroceso
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar vistas
        etCategoria = findViewById(R.id.etCategoria)
        etMonto = findViewById(R.id.etMonto)
        spinnerFrecuencia = findViewById(R.id.spinnerFrecuencia)
        etFechaInicio = findViewById(R.id.etFechaInicio)
        etFechaLimite = findViewById(R.id.etFechaLimite)
        tvDatosGuardados = findViewById(R.id.tvDatosGuardados)
        listView = findViewById(R.id.listViewPresupuestos)

        // Configurar Spinner
        configurarSpinner()

        // Configurar el adaptador para el ListView usando el nuevo adaptador personalizado
        adapter = PresupuestoAdapter(this, listaPresupuestos)
        listView.adapter = adapter

        // Configurar el listener del ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            mostrarConfirmacionEliminacion(position)
        }

        // Cargar presupuestos desde SharedPreferences
        cargarPresupuestos()

        // Configurar listeners para las fechas
        configurarCamposFecha()
    }

    private fun configurarSpinner() {
        val frecuenciaArray = resources.getStringArray(R.array.frecuencia_array)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frecuenciaArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrecuencia.adapter = spinnerAdapter

        spinnerFrecuencia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mostrarCamposSegunFrecuencia()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun configurarCamposFecha() {
        etFechaInicio.setOnClickListener { mostrarCalendario(it as EditText) }
        etFechaLimite.setOnClickListener { mostrarCalendario(it as EditText) }
    }

    private fun mostrarCamposSegunFrecuencia() {
        val frecuencia = spinnerFrecuencia.selectedItem.toString()

        if (frecuencia == "Único") {
            etFechaInicio.visibility = View.VISIBLE
            etFechaLimite.visibility = View.VISIBLE
        } else {
            etFechaInicio.visibility = View.GONE

            // Calcular y establecer la fecha límite automáticamente si no es único.
            etFechaLimite.setText(calcularFechaLimite())
        }
    }

    private fun calcularFechaLimite(): String {
        calendar.add(Calendar.MONTH, 1) // Se añade un mes a la fecha actual.
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(calendar.time) // Devuelve la fecha en formato dd/MM/yyyy.
    }

    private fun mostrarCalendario(etFecha: EditText) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day) // Establece la fecha seleccionada.
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            etFecha.setText(format.format(selectedDate.time)) // Muestra la fecha en el EditText.
        }
        DatePickerDialog(this, dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show() // Muestra el diálogo de selección de fecha.
    }

    fun guardarPresupuesto(view: View) {
        val categoria = etCategoria.text.toString()
        val monto = etMonto.text.toString()
        val frecuencia = spinnerFrecuencia.selectedItem.toString()
        val fechaInicio = etFechaInicio.text.toString()
        val fechaLimite = etFechaLimite.text.toString()

        if (categoria.isEmpty() || monto.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return // Si hay campos vacíos, muestra un mensaje y retorna.
        }

        val presupuesto = Presupuesto(categoria, monto, frecuencia, fechaInicio, fechaLimite)
        listaPresupuestos.add(presupuesto) // Añade el presupuesto a la lista.
        guardarPresupuestos() // Guarda los presupuestos en SharedPreferences.

        adapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado.

        Toast.makeText(this, "Presupuesto guardado correctamente", Toast.LENGTH_SHORT).show()
        limpiarCampos() // Limpia los campos de entrada.
    }

    private fun guardarPresupuestos() {
        val json = Gson().toJson(listaPresupuestos) // Convierte la lista a JSON.
        sharedPreferences.edit().putString("presupuestos", json).apply() // Guarda el JSON en SharedPreferences.
    }

    private fun cargarPresupuestos() {
        val json = sharedPreferences.getString("presupuestos", null) // Carga el JSON desde SharedPreferences.

        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<Presupuesto>>() {}.type
            listaPresupuestos.clear()
            listaPresupuestos.addAll(Gson().fromJson(json, type)) // Convierte el JSON a una lista de presupuestos.
            adapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado.
        }
    }

    private fun mostrarConfirmacionEliminacion(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este presupuesto?")
            .setPositiveButton("Sí") { _, _ ->
                listaPresupuestos.removeAt(position) // Elimina el presupuesto seleccionado.
                guardarPresupuestos()
                adapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado.
                Toast.makeText(this, "Presupuesto eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    private fun limpiarCampos() {
        etCategoria.text.clear()
        etMonto.text.clear()
        spinnerFrecuencia.setSelection(0)
        etFechaInicio.text.clear()
        etFechaLimite.text.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

// Clase de datos Presupuesto debe estar incluida aquí también.
data class Presupuesto(
    val categoria: String,
    val monto: String,
    val frecuencia: String,
    val fechaInicio: String,
    val fechaLimite: String
)