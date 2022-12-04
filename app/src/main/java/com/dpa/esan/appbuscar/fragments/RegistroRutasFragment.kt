package com.dpa.esan.appbuscar.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.dpa.esan.appbuscar.R
import com.dpa.esan.appbuscar.fragments.model.RutaModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistroRutasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistroRutasFragment : Fragment() {
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
        val rootView : View = inflater.inflate(R.layout.fragment_registro_rutas, container, false)
        val fecIni: TextView = rootView.findViewById(R.id.fecIni)
        val fecFin: TextView = rootView.findViewById(R.id.fecFin)
        val spnOrigen: Spinner = rootView.findViewById(R.id.spnOrigen)
        val spnDestino: Spinner = rootView.findViewById(R.id.spnDestino)
        val txtCostoTramo: TextView = rootView.findViewById(R.id.txtCostoTramo)
        val txtNombreRuta: TextView = rootView.findViewById(R.id.txtNombreRuta)
        val btnGuardarRegistroRutas: Button = rootView.findViewById(R.id.btnGuardarRegistroRutas)
        val btnCancelarRegistroRutas: Button = rootView.findViewById(R.id.btnCancelarRegistroRutas)

        val db = FirebaseFirestore.getInstance()

        btnGuardarRegistroRutas.setOnClickListener{

            val costo = txtCostoTramo.text.toString().toDouble()
            val destino = spnDestino.selectedItem.toString()
            val fechaIni = parseDate(fecIni.text.toString())
            val fechaFin = parseDate(fecFin.text.toString())
            val origen = spnOrigen.selectedItem.toString()

            val nuevaRuta = RutaModel(costo, destino, fechaFin, fechaIni, origen)

            if(isValidForm(fecIni, fecFin, spnOrigen, spnDestino,
                    txtCostoTramo, txtNombreRuta))
            {
                db.collection("ruta").document(txtNombreRuta.text.toString())
                    .set(nuevaRuta)
                Toast.makeText(activity, "Ruta Registrada", Toast.LENGTH_LONG).show()
                cleanValues(fecIni, fecFin, spnOrigen, spnDestino,
                    txtCostoTramo, txtNombreRuta)
            }
        }

        fecIni.setOnClickListener {
            getDate(fecIni)
        }

        fecFin.setOnClickListener {
            getDate(fecFin)
        }

        btnCancelarRegistroRutas.setOnClickListener {
            parentFragmentManager.commit {
                replace<ManueAdminFragment>(R.id.fragmentMenuAdminContainer)
                setReorderingAllowed(true)
                addToBackStack("principal")
            }
        }

        return rootView
    }

    private fun parseDate(fecha: String): Date
    {
        val formatter = SimpleDateFormat("dd/mm/yyyy")
        return formatter.parse(fecha) as Date
    }

    private fun getDate(date: TextView)
    {
        val calendario = Calendar.getInstance()
        val yy = calendario[Calendar.YEAR]
        val mm = calendario[Calendar.MONTH]
        val dd = calendario[Calendar.DAY_OF_MONTH]


        val datePicker = DatePickerDialog(
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->
                var month = monthOfYear+1
                val fecha = "$dayOfMonth/$month/$year"
                date.setText(fecha)
            }, yy, mm, dd
        )

        datePicker.show()
    }

    private fun isValidForm(fecIni: TextView, fecFin: TextView, spnOrigen: Spinner,
                            spnDestino: Spinner,
                            txtCostoTramo: TextView, txtNombreRuta: TextView): Boolean
    {
        var response = true
        if (TextUtils.isEmpty(fecIni.text.toString()))
        {
            fecIni.error = "Seleccione una Fecha de Inicio"
            response = false
        }
        if (TextUtils.isEmpty(fecFin.text.toString())) {
            fecFin.error = "eleccione una Fecha de Fin"
            response = false
        }
        if (TextUtils.isEmpty(txtCostoTramo.text.toString())) {
            txtCostoTramo.error = "Ingrese el costo de la ruta"
            response = false
        }
        if (TextUtils.isEmpty(txtNombreRuta.text.toString())) {
            txtNombreRuta.error = "Ingrese nombre de la ruta"
            response = false
        }
        if (spnOrigen.selectedItemPosition == 0) {
            response = false
        }
        if (spnDestino.selectedItemPosition == 0) {
            response = false
        }
        return response
    }

    private fun cleanValues(fecIni: TextView, fecFin: TextView, spnOrigen: Spinner,
                            spnDestino: Spinner,
                            txtCostoTramo: TextView, txtNombreRuta: TextView)
    {
        val position = 0
        fecIni.text = ""
        fecFin.text = ""
        txtCostoTramo.text = ""
        txtNombreRuta.text = ""
        spnOrigen.setSelection(position)
        spnDestino.setSelection(position)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistroRutasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistroRutasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

