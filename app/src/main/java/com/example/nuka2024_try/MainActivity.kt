package com.example.nuka2024_try

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.nuka2024_try.databinding.ActivityMainBinding
import com.example.nuka2024_try.ui.news.NewsBadgeHelper
import com.example.nuka2024_try.ui.qr_scanner.QRCodeCaptureActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsBadgeHelper: NewsBadgeHelper // NewsBadgeHelper を保持するプロパティ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Drawer & Navigation の設定
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_stamps, R.id.nav_contact, R.id.nav_mypage),
            drawerLayout
        )

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_scan -> {
                    startActivity(Intent(this, QRCodeCaptureActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    navController.navigate(menuItem.itemId)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }

        // メールアイコン (ImageView) の設定
        val mailIcon = findViewById<ImageView>(R.id.iconMail)
        mailIcon.setOnClickListener {
            startActivity(Intent(this, com.example.nuka2024_try.ui.news.NewsActivity::class.java))
        }

        // NewsBadgeHelper の初期化と設定
        newsBadgeHelper = NewsBadgeHelper()
        newsBadgeHelper.setupBadge(this, mailIcon)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val myPageItem = menu?.findItem(R.id.action_mypage)
        val actionView = myPageItem?.actionView
        actionView?.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_mypage)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}
