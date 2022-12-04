package com.dpa.esan.appbuscar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dpa.esan.appbuscar.fragments.RegistroPasajeroFragment
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = FirebaseFirestore.getInstance()
        val txtCorreo: TextView = findViewById(R.id.txtCorreo)
        val txtPassword: TextView = findViewById(R.id.txtPassword)
        val btnIngresar: Button = findViewById(R.id.btnIngresar)
        val btnCrearCuenta: Button = findViewById(R.id.btnCrear)
        var clave: String = ""
        var isAdmin = false

        btnIngresar.setOnClickListener {

            if (isValidForm(txtCorreo, txtPassword)) {
                db.collection("users").document(txtCorreo.text.toString()).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            clave = document.get("password") as String
                            if (clave.equals(txtPassword.text.toString())) {
                                isAdmin = document.get("isAdmin") as Boolean
                                redirectForm(isAdmin)
                            } else {
                                Toast.makeText(
                                    this, "“EL USUARIO Y/O CLAVE\n" +
                                            "NO EXISTE EN EL SISTEMA",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this, "“EL USUARIO Y/O CLAVE\n" +
                                        "NO EXISTE EN EL SISTEMA",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        btnCrearCuenta.setOnClickListener {
            val intentPassenger = Intent(this, RegistroPasajeroFragment::class.java)
            startActivity(intentPassenger)
        }
    }

    private fun isValidForm(email: TextView, clave: TextView): Boolean
    {
        var response = true
        if (TextUtils.isEmpty(email.text.toString()))
        {
            email.error = "El correo no puede estar vacío"
            response = false
        }
        if (TextUtils.isEmpty(clave.text.toString())) {
            clave.error = "La clave no puede estas vacía"
            response = false
        }
        return response
    }

    private fun redirectForm(isAdmin: Boolean)
    {
        if (isAdmin)
        {
            val intentAdmin = Intent(this, MenuAdminActivity::class.java)
            startActivity(intentAdmin)
        } else
        {
            val intentPassenger = Intent(this, MenuPassengerActivity::class.java)
            startActivity(intentPassenger)
        }
    }
}