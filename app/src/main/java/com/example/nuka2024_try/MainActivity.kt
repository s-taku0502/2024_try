package com.example.nuka2024_try

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.NavigationUI
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nuka2024_try.databinding.ActivityMainBinding
import com.example.nuka2024_try.ui.qr_scanner.QRCodeCaptureActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // トップレベルの目的地を設定
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_stamps
            ), drawerLayout
        )

        // アクションバーとナビゲーションコントローラを連携
        setupActionBarWithNavController(navController, appBarConfiguration)
        // NavigationViewとNavControllerを連携
        navView.setupWithNavController(navController)

        // メニューアイテムの選択リスナーを設定
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // アクティビティを作りたい場合はここに追記
                R.id.nav_scan -> {
                    // QRスキャンのアクティビティを起動
                    Toast.makeText(this, "QRスキャンのアクティビティを起動します", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, QRCodeCaptureActivity::class.java)
                    startActivity(intent)
                    // ドロワーを閉じる
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    // 他のメニューアイテムはNavControllerに委ねる
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                    if (handled) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    handled
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // メニューをインフレートする
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}