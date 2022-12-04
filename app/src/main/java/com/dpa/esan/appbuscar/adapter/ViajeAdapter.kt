package com.dpa.esan.appbuscar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dpa.esan.appbuscar.R
import com.dpa.esan.appbuscar.fragments.model.ViajeModel

class ViajeAdapter(private var listViaje: List<ViajeModel>)
    : RecyclerView.Adapter<ViajeAdapter.ViewHolder>() {

        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val ivViaje: ImageView = itemView.findViewById(R.id.ivBus)
            val lbDescription: TextView = itemView.findViewById(R.id.lblDescripcion)
            val lbCostoViaje: TextView = itemView.findViewById(R.id.lblCostoViaje)
            val lbAsientosLibres: TextView = itemView.findViewById(R.id.lblcantAsientosLibres)
            val lbSalidaValor: TextView = itemView.findViewById(R.id.lblSalidaValor)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.list_recycle_viajes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViaje = listViaje[position]
        holder.ivViaje.setImageResource(R.drawable.viaje)
        holder.lbDescription.text = itemViaje.flota!!.descripcion
        holder.lbCostoViaje.text = itemViaje.ruta!!.costo.toString()
        holder.lbAsientosLibres.text = itemViaje.asientosLibres.toString()
        holder.lbSalidaValor.text = itemViaje.horaSalida
    }

    override fun getItemCount(): Int {
        return listViaje.size
    }
}