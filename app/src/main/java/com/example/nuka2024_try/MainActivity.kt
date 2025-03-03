package com.example.nuka2024_try

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nuka2024_try.databinding.ActivityMainBinding
import com.example.nuka2024_try.ui.qr_scanner.QRCodeCaptureActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            Toast.makeText(this, "Fab clicked", Toast.LENGTH_SHORT).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // トップレベルの目的地に nav_home, nav_stamps, nav_contact を指定
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_stamps, R.id.nav_contact), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        // NavigationView は setupWithNavController を利用して自動的に各画面へ遷移させる
        navView.setupWithNavController(navController)

        // 個別処理が必要な nav_scan だけカスタム処理し、他は自動処理に任せる
        navView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_scan) {
                Toast.makeText(this, "QRスキャンのアクティビティを起動します", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, QRCodeCaptureActivity::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            } else {
                // それ以外は自動処理（nav_contact も含む）
                val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                if (handled) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                handled
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}
