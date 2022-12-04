package com.dpa.esan.appbuscar.fragments.model

import java.util.Date

data class RutaModel (
    val costo: Double,
    val destino: String = "",
    val fechaFin: Date,
    val fechaIni: Date,
    val origen: String = ""
)