package com.dpa.esan.appbuscar.fragments.model

data class BoletoModel(
    var asiento: Int?,
    val cliente: PasajeroDtoBoleto,
    val flota: FlotaDtoBoleto?,
    val horaSalida: String = "",
    val ruta: RutaDtoBoleto?,
    val viaje: String = "",
    val isSold: Boolean
)