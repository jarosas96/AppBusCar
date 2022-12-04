package com.dpa.esan.appbuscar.fragments.model

import com.google.firebase.Timestamp
import java.util.*

data class RutaDtoBoleto(
    val destino: String = "",
    val nombreRuta: String = "",
    val origen: String = "",
    val costo: Double,
    val fechaInicio: Timestamp,
    val fechaFin: Timestamp
)
