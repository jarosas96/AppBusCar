package com.dpa.esan.appbuscar.fragments.model

data class ViajeModel(
    val flota: FlotaDtoViaje?,
    val horaSalida: String = "",
    val ruta: RutaDtoBoleto?,
    val asientosLibres: Int
)
