package com.dpa.esan.appbuscar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.dpa.esan.appbuscar.fragments.*

class MenuPassengerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_passenger)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<HeaderAdminFragment>(R.id.fragmentHeaderPasajeroContainer)
            add<MenuPasajeroFragment>(R.id.fragmentMenuPasajeroContainer)
            add<FlooterFragment>(R.id.fragmentFooterPasajeroContainer)
        }
    }
}