package com.dpa.esan.appbuscar.fragments.model

data class FlotaModel(
    val marca: String = "",
    val modelo: String = "",
    val cantAsientos: Int,
    val numPoliza: String = "",
    val yearFabricacion: Int,
    val estado: String = "",
    val descripcion: String = ""
)
