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
import com.dpa.esan.appbuscar.fragments.model.PasajeroModel
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistroPasajeroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistroPasajeroFragment : Fragment() {
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
        val rootView : View = inflater.inflate(R.layout.fragment_registro_pasajero, container, false)
        val txtDocumento: TextView = rootView.findViewById(R.id.txtDocumento)
        val txtNames: TextView = rootView.findViewById(R.id.txtNames)
        val txtFatherLastName: TextView = rootView.findViewById(R.id.txtFatherLastName)
        val txtMotherLastName: TextView = rootView.findViewById(R.id.txtMotherLastName)
        val txtEmail: TextView = rootView.findViewById(R.id.txtEmail)
        val txtTelefono: TextView = rootView.findViewById(R.id.txtTelefono)
        val txtEdad: TextView = rootView.findViewById(R.id.txtEdad)
        val txtContraseña: TextView = rootView.findViewById(R.id.txtContraseña)
        val spnSexo: Spinner = rootView.findViewById(R.id.spnSexo)
        val btnGuardarPasajero: Button = rootView.findViewById(R.id.btnGuardarPasajero)
        val btnCancelarRegistroPasajero: Button = rootView.findViewById(R.id.btnCancelarRegistroPasajero)
        val db = FirebaseFirestore.getInstance()


        btnGuardarPasajero.setOnClickListener{

            val age = txtEdad.text.toString().toInt()
            val cellNumber = txtTelefono.text.toString()
            val fatherLastName = txtFatherLastName.text.toString()
            val motherLastName = txtMotherLastName.text.toString()
            val idc = txtDocumento.text.toString()
            val names = txtNames.text.toString()
            val password = txtContraseña.text.toString()
            val sexo = spnSexo.selectedItem.toString()

            val nuevoPasajero = PasajeroModel(age, cellNumber, fatherLastName, idc,
                false, motherLastName, names, password, sexo)

            if(isValidForm(txtDocumento, txtNames, txtContraseña, txtFatherLastName,
                    txtMotherLastName, txtTelefono, txtEdad, txtEmail))
            {
                db.collection("users").document(txtEmail.text.toString())
                    .set(nuevoPasajero)
                Toast.makeText(activity, "Pasajero Registrado", Toast.LENGTH_LONG).show()
                cleanValues(txtDocumento, txtNames, txtContraseña, txtFatherLastName,
                    txtMotherLastName, txtTelefono, txtEdad, txtEmail)
            }
        }

        btnCancelarRegistroPasajero.setOnClickListener {
            parentFragmentManager.commit {
                replace<ManueAdminFragment>(R.id.fragmentMenuAdminContainer)
                setReorderingAllowed(true)
                addToBackStack("principal")
            }
        }

        return rootView
    }

    private fun isValidForm(idc: TextView, names: TextView, password: TextView,
                            fatherLastName: TextView, motherLastName: TextView,
                            cellNumber: TextView, age: TextView, email: TextView): Boolean
    {
        var response = true
        if (TextUtils.isEmpty(idc.text.toString()))
        {
            idc.error = "Ingrese su número de documento"
            response = false
        }
        if (TextUtils.isEmpty(names.text.toString())) {
            names.error = "Ingrese sus nombres"
            response = false
        }
        if (TextUtils.isEmpty(password.text.toString())) {
            password.error = "Ingrese la clave"
            response = false
        }
        if (TextUtils.isEmpty(fatherLastName.text.toString())) {
            fatherLastName.error = "Ingrese su apellido paterno"
            response = false
        }
        if (TextUtils.isEmpty(motherLastName.text.toString())) {
            motherLastName.error = "Ingrese su apellido materno"
            response = false
        }
        if (TextUtils.isEmpty(cellNumber.text.toString())) {
            cellNumber.error = "Ingrese su número de telefono"
            response = false
        }
        if (TextUtils.isEmpty(age.text.toString())) {
            age.error = "Ingrese su edad"
            response = false
        }
        if (TextUtils.isEmpty(email.text.toString())) {
            email.error = "Ingrese su correo"
            response = false
        }
        return response
    }

    private fun cleanValues(idc: TextView, names: TextView, password: TextView,
                            fatherLastName: TextView, motherLastName: TextView,
                            cellNumber: TextView, age: TextView, email: TextView)
    {
        idc.text = ""
        names.text = ""
        password.text = ""
        fatherLastName.text = ""
        motherLastName.text = ""
        cellNumber.text = ""
        age.text = ""
        email.text = ""
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistroPasajeroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistroPasajeroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}