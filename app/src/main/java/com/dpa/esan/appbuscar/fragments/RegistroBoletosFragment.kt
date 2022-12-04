package com.dpa.esan.appbuscar.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.dpa.esan.appbuscar.R
import com.dpa.esan.appbuscar.fragments.model.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistroBoletosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistroBoletosFragment : Fragment() {
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
        val rootView: View = inflater.inflate(R.layout.fragment_registro_boletos, container, false)
        val flotaData: HashMap<String, FlotaDtoViaje> = hashMapOf()
        val rutaData: HashMap<String, RutaDtoBoleto> = hashMapOf()
        val spnFlota: Spinner = rootView.findViewById(R.id.spnFlota)
        val spnRuta: Spinner = rootView.findViewById(R.id.spnRuta)
        val spnHoraSalida: Spinner = rootView.findViewById(R.id.spnHoraSalida)
        val btnGuardarRegistroBoleta: Button = rootView.findViewById(R.id.btnGuardarRegistroBoleta)
        val btnCancelarRegistroBoleta: Button =
            rootView.findViewById(R.id.btnCancelarRegistroBoleta)
        val db = FirebaseFirestore.getInstance()

        db.collection("flota")
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    Log.w("Firebase error", "Error al listar las flotas....")
                    return@addSnapshotListener
                }
                for (dc in snap!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED,
                        DocumentChange.Type.REMOVED -> {
                            flotaData[dc.document.id] = FlotaDtoViaje(
                                dc.document.data["cantAsientos"].toString().toInt(),
                                dc.document.id,
                                dc.document.data["descripcion"].toString()
                            )
                        }
                    }

                }
                llenarSpinneFlota(flotaData, spnFlota)
            }

        db.collection("ruta")
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    Log.w("Firebase error", "Error al listar las rutas....")
                    return@addSnapshotListener
                }
                for (dc in snap!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED,
                        DocumentChange.Type.REMOVED -> {
                            rutaData[dc.document.id] = RutaDtoBoleto(
                                dc.document.data["destino"].toString(),
                                dc.document.id,
                                dc.document.data["origen"].toString(),
                                dc.document.data["costo"].toString().toDouble(),
                                dc.document.data["fechaIni"] as Timestamp,
                                dc.document.data["fechaFin"] as Timestamp
                            )
                        }
                    }

                }
                llenarSpinneRuta(rutaData, spnRuta)
            }

        btnGuardarRegistroBoleta.setOnClickListener {

            val flota: FlotaDtoViaje? = flotaData[spnFlota.selectedItem.toString()]
            val flotaDtoViaje = FlotaDtoViaje(
                flota?.cantAsientos,
                spnFlota.selectedItem.toString(),
                flota?.descripcion
            )
            val flotaDtoBoleto = FlotaDtoBoleto(
                flota?.cantAsientos,
                flota?.descripcion
            )
            val ruta: RutaDtoBoleto? = rutaData[spnRuta.selectedItem.toString()]
            val horaSalida = spnHoraSalida.selectedItem.toString()
            val nuevoViaje = ViajeModel(
                flotaDtoViaje, horaSalida, ruta,
                flotaDtoViaje.cantAsientos!!
            )


            db.collection("viaje").document(
                spnRuta.selectedItem.toString()
                    .plus(spnFlota.selectedItem.toString())
                    .plus(spnHoraSalida.selectedItem.toString())
            )
                .set(nuevoViaje)
            Toast.makeText(activity, "Viaje Registrado", Toast.LENGTH_LONG).show()
            val clienteInicial = PasajeroDtoBoleto("", "", "", "")
            val nuevoBoleto = BoletoModel(
                0, clienteInicial, flotaDtoBoleto, horaSalida, ruta,
                spnRuta.selectedItem.toString().plus(spnFlota.selectedItem.toString())
                    .plus(spnHoraSalida.selectedItem.toString()), false
            )
            var i = 0
            while (i < nuevoViaje.flota!!.cantAsientos!!) {
                i++
                nuevoBoleto.asiento = i
                db.collection("boletos").document().set(nuevoBoleto)
            }

        }

        btnCancelarRegistroBoleta.setOnClickListener {
            parentFragmentManager.commit {
                replace<ManueAdminFragment>(R.id.fragmentMenuAdminContainer)
                setReorderingAllowed(true)
                addToBackStack("principal")
            }
        }


        return rootView
    }

    private fun parseDate(fecha: String): Date {
        val formatter = SimpleDateFormat("dd/mm/yyyy")
        return formatter.parse(fecha) as Date
    }

    fun llenarSpinneFlota(
        flotaMap: HashMap<String, FlotaDtoViaje>,
        spnFlota: Spinner
    ) {
        val lista = ArrayList(flotaMap.keys)
        ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item, lista
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnFlota.adapter = arrayAdapter
        }
    }


    private fun llenarSpinneRuta(
        rutaMap: HashMap<String, RutaDtoBoleto>,
        spnRuta: Spinner
    ) {
        val lista = ArrayList(rutaMap.keys)
        ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item, lista
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnRuta.adapter = arrayAdapter
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistroBoletosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistroBoletosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}