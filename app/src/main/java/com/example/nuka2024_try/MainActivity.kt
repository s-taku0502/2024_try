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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ※ ここでは、ユーザー登録・ログイン後にMainActivityが起動する前提としています。
        // Firebase Authentication による認証状態のチェックは、起動画面やLaunchActivityで行う想定です。

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Toast.makeText(this, "Fab clicked", Toast.LENGTH_SHORT).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // ナビゲーションドロワーのトップレベルの目的地を設定
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_stamps), drawerLayout
        )

        // アクションバーとナビゲーションコントローラを連携
        setupActionBarWithNavController(navController, appBarConfiguration)
        // NavigationViewとNavControllerを連携
        navView.setupWithNavController(navController)

        // メニューアイテムの選択リスナー
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_scan -> {
                    Toast.makeText(this, "QRスキャンのアクティビティを起動します", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, QRCodeCaptureActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
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
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}
