package com.dpa.esan.appbuscar.fragments.model

data class PasajeroModel(
    val age: Int,
    val cellNumber: String = "",
    val fatherLastName: String = "",
    val idc: String = "",
    val isAdmin: Boolean = false,
    val motherLastName: String = "",
    val names: String = "",
    val password: String = "",
    val sexo: String = ""
)
