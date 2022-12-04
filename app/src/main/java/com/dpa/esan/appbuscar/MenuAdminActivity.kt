package com.dpa.esan.appbuscar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.dpa.esan.appbuscar.fragments.FooterAdminFragment
import com.dpa.esan.appbuscar.fragments.HeaderAdminFragment
import com.dpa.esan.appbuscar.fragments.ManueAdminFragment

class MenuAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_admin)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<HeaderAdminFragment>(R.id.fragmentHeaderAdminContainer)
            add<ManueAdminFragment>(R.id.fragmentMenuAdminContainer)
            add<FooterAdminFragment>(R.id.fragmentFooterAdminContainer)
        }

    }
}