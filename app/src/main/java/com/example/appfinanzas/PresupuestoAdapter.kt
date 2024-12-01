// PresupuestoAdapter.kt
package com.example.appfinanzas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PresupuestoAdapter(context: Context, private val listaPresupuestos: List<Presupuesto>) :
    ArrayAdapter<Presupuesto>(context, 0, listaPresupuestos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val presupuesto = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_presupuesto, parent, false)

        val tvCategoria = view.findViewById<TextView>(R.id.tvCategoria)
        val tvMonto = view.findViewById<TextView>(R.id.tvMonto)
        val tvFrecuencia = view.findViewById<TextView>(R.id.tvFrecuencia)
        val tvFechaInicio = view.findViewById<TextView>(R.id.tvFechaInicio)
        val tvFechaLimite = view.findViewById<TextView>(R.id.tvFechaLimite)

        tvCategoria.text = presupuesto?.categoria
        tvMonto.text = "Monto: ${presupuesto?.monto}"
        tvFrecuencia.text = "Frecuencia: ${presupuesto?.frecuencia}"
        tvFechaInicio.text = "Fecha de Inicio: ${presupuesto?.fechaInicio}"
        tvFechaLimite.text = "Fecha Límite: ${presupuesto?.fechaLimite}"

        return view
    }
}