package com.dpa.esan.appbuscar.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.dpa.esan.appbuscar.R
import com.dpa.esan.appbuscar.fragments.model.FlotaModel
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistroFlotaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistroFlotaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView : View =  inflater.inflate(R.layout.fragment_registro_flota, container, false)
        val txtPlaca: TextView = rootView.findViewById(R.id.txtPlaca)
        val txtMarca: TextView = rootView.findViewById(R.id.txtMarca)
        val txtModelo: TextView = rootView.findViewById(R.id.txtModelo)
        val txtAsientos: TextView = rootView.findViewById(R.id.txtAsientos)
        val txtPoliza: TextView = rootView.findViewById(R.id.txtPoliza)
        val txtAñoFabricacion: TextView = rootView.findViewById(R.id.txtAñoFabricacion)
        val txtDescription: TextView = rootView.findViewById(R.id.txtDescription)
        val spnEstado: Spinner = rootView.findViewById(R.id.spnEstado)
        val btnGuardarFlota: Button = rootView.findViewById(R.id.btnGuardarFlota)
        val btnCancelarRegistroFlota: Button = rootView.findViewById(R.id.btnCancelarRegistroFlota)
        val db = FirebaseFirestore.getInstance()

        btnGuardarFlota.setOnClickListener{

            val marca = txtMarca.text.toString()
            val modelo = txtModelo.text.toString()
            val cantAsientos = txtAsientos.text.toString().toInt()
            val numPoliza = txtPoliza.text.toString()
            val yearFabricacion = txtAñoFabricacion.text.toString().toInt()
            val estado = spnEstado.selectedItem.toString()
            val descripcion = txtDescription.text.toString()

            val nuevaFlota = FlotaModel(marca, modelo, cantAsientos, numPoliza, yearFabricacion,
                estado, descripcion)

            if(isValidForm(txtPlaca, txtMarca, txtModelo, txtAsientos, txtPoliza,
                    txtAñoFabricacion))
            {
                db.collection("flota").document(txtPlaca.text.toString())
                    .set(nuevaFlota)
                Toast.makeText(activity, "Flota Registrado", Toast.LENGTH_LONG).show()
                cleanValues(txtPlaca, txtMarca, txtModelo, txtAsientos, txtPoliza,
                    txtAñoFabricacion)
            }
        }

        btnCancelarRegistroFlota.setOnClickListener {
            parentFragmentManager.commit {
                replace<ManueAdminFragment>(R.id.fragmentMenuAdminContainer)
                setReorderingAllowed(true)
                addToBackStack("principal")
            }
        }

        return rootView
    }

    private fun isValidForm(txtPlaca: TextView, txtMarca: TextView, txtModelo: TextView,
                            txtAsientos: TextView, txtPoliza: TextView,
                            txtAñoFabricacion: TextView): Boolean
    {
        var response = true
        if (TextUtils.isEmpty(txtPlaca.text.toString()))
        {
            txtPlaca.error = "Ingrese el número de placa"
            response = false
        }
        if (TextUtils.isEmpty(txtMarca.text.toString())) {
            txtMarca.error = "Ingrese la marca del bus"
            response = false
        }
        if (TextUtils.isEmpty(txtModelo.text.toString())) {
            txtModelo.error = "Ingrese el modelo del bus"
            response = false
        }
        if (TextUtils.isEmpty(txtAsientos.text.toString())) {
            txtAsientos.error = "Ingrese la cantidad de asientos"
            response = false
        }
        if (TextUtils.isEmpty(txtPoliza.text.toString())) {
            txtPoliza.error = "Ingrese el número de poliza"
            response = false
        }
        if (TextUtils.isEmpty(txtAñoFabricacion.text.toString())) {
            txtAñoFabricacion.error = "Ingrese el año de fabricación"
            response = false
        }
        return response
    }

    private fun cleanValues(txtPlaca: TextView, txtMarca: TextView, txtModelo: TextView,
                            txtAsientos: TextView, txtPoliza: TextView,
                            txtAñoFabricacion: TextView)
    {
        txtPlaca.text = ""
        txtMarca.text = ""
        txtModelo.text = ""
        txtAsientos.text = ""
        txtPoliza.text = ""
        txtAñoFabricacion.text = ""
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistroFlotaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistroFlotaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}