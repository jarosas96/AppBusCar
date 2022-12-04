package com.dpa.esan.appbuscar.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpa.esan.appbuscar.R
import com.dpa.esan.appbuscar.adapter.ViajeAdapter
import com.dpa.esan.appbuscar.fragments.model.FlotaDtoViaje
import com.dpa.esan.appbuscar.fragments.model.RutaDtoBoleto
import com.dpa.esan.appbuscar.fragments.model.ViajeModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BuscarBoletosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuscarBoletosFragment : Fragment() {
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
        val rootView: View = inflater.inflate(R.layout.fragment_buscar_boletos, container, false)
        val spnOrigenBoleto: Spinner = rootView.findViewById(R.id.spnOrigenBoleto)
        val spnDestinoBoleto: Spinner = rootView.findViewById(R.id.spnDestinoBoleto)
        val btnBuscarViajes: Button = rootView.findViewById(R.id.btnBuscarViajes)
        val dateIda: TextView = rootView.findViewById(R.id.dateIda)
        val recyclerViewBoletos: RecyclerView = rootView.findViewById(R.id.recyclerViewViajes)
        var listViajesGeneric: ArrayList<ViajeModel> = ArrayList()
        var listViajes: ArrayList<ViajeModel> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        recyclerViewBoletos.layoutManager = LinearLayoutManager(requireContext())


        db.collection("viaje")
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
                            val rutaMap = dc.document.data["ruta"] as Map<*, *>
                            val ruta = parseRutaMap(rutaMap)
                            val flotaMap = dc.document.data["flota"] as Map<*, *>
                            val flota = parteFlotaMap(flotaMap)
                            listViajesGeneric.add(
                                ViajeModel(
                                    flota,
                                    dc.document.data["horaSalida"].toString(),
                                    ruta,
                                    dc.document.data["asientosLibres"].toString().toInt()
                                )
                            )
                        }
                    }
                }
            }

        dateIda.setOnClickListener {
            getDate(dateIda)
        }

        btnBuscarViajes.setOnClickListener {
            val fecha = parseStringToDate(dateIda.text.toString())
            val origen = spnOrigenBoleto.selectedItem.toString()
            val destino = spnDestinoBoleto.selectedItem.toString()
            for (viaje in listViajesGeneric)
            {
                if (parseTimeStampToDate(viaje.ruta!!.fechaInicio).isAfter(fecha)
                    && parseTimeStampToDate(viaje.ruta.fechaFin).isBefore(fecha)
                    && viaje.ruta.origen.equals(origen) && viaje.ruta.destino.equals(destino))
                {
                    listViajes.add(viaje)
                }
            }
            listViajes.add(listViajesGeneric[0])
            listViajes.add(listViajesGeneric[1])
            recyclerViewBoletos.adapter = ViajeAdapter(listViajes)
        }

        return rootView
    }

    private fun parseRutaMap(rutaMap: Map<*, *>): RutaDtoBoleto {
        return RutaDtoBoleto(
            rutaMap["destino"] as String, rutaMap["nombreRuta"] as String,
            rutaMap["origen"] as String, rutaMap["costo"] as Double,
            rutaMap["fechaInicio"] as Timestamp, rutaMap["fechaFin"] as Timestamp
        )
    }

    private fun parteFlotaMap(flotaMap: Map<*, *>): FlotaDtoViaje {
        return FlotaDtoViaje(
            flotaMap["cantAsientos"].toString().toInt(), flotaMap["placa"] as String,
            flotaMap["descripcion"] as String
        )
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
                val month = monthOfYear+1
                val fecha = "$dayOfMonth-0$month-$year"
                date.setText(fecha)

            }, yy, mm, dd
        )

        datePicker.show()
    }

    private fun parseStringToDate(fecha: String): LocalDate {
        return LocalDate.of(12, 12, 5)
    }

    private fun parseTimeStampToDate(timestamp: Timestamp): LocalDate {

        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val netDate = Date(milliseconds)
        val value = timestamp.toDate()
        return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BuscarBoletosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BuscarBoletosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}