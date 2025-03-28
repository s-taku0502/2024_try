package com.example.nuka2024_try

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.nuka2024_try.databinding.ActivityMainBinding
import com.example.nuka2024_try.ui.qr_scanner.QRCodeCaptureActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // メールアイコン＋バッジ用
    private lateinit var mailContainer: FrameLayout
    private lateinit var mailIcon: ImageView
    private lateinit var textUnreadCount: TextView

    // Firestore リスナー
    var newsListenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ログインチェック
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

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

        // メールアイコンと未読数のTextViewを取得
        mailContainer = findViewById(R.id.mailContainer)
        mailIcon = findViewById(R.id.iconMail)
        textUnreadCount = findViewById(R.id.textUnreadCount)

        // メールアイコンタップで NewsActivity へ遷移
        mailIcon.setOnClickListener {
            startActivity(Intent(this, com.example.nuka2024_try.ui.news.NewsActivity::class.java))
        }

        // 未読数をリアルタイム監視
        observeUnreadCount()
    }

    private fun observeUnreadCount() {
        val currentUid = Firebase.auth.currentUser?.uid ?: return
        val db = Firebase.firestore

        newsListenerRegistration = db.collection("news")
            .addSnapshotListener { querySnapshot, e ->
                // エラー時の処理：ユーザーが既にログアウトしている場合はエラートーストを出さない
                if (e != null) {
                    if (Firebase.auth.currentUser == null) {
                        Log.d("MainActivity", "User is null; ignoring news error.")
                    } else {
                        Log.e("MainActivity", "observeUnreadCount error: ${e.message}")
                        // エラートーストを出さないようにする（または条件付きで出す）
                    }
                    return@addSnapshotListener
                }

                val docs = querySnapshot?.documents ?: emptyList()
                val unreadCount = docs.count { doc ->
                    val readUsers = doc.get("readUsers") as? List<String> ?: emptyList()
                    !readUsers.contains(currentUid)
                }
                Log.d("MainActivity", "unreadCount = $unreadCount")

                if (unreadCount > 0) {
                    textUnreadCount.text = unreadCount.toString()
                    textUnreadCount.visibility = View.VISIBLE
                } else {
                    textUnreadCount.visibility = View.GONE
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // カスタムアクションビューを使ってマイページボタンのクリック処理を設定
        val myPageItem = menu?.findItem(R.id.action_mypage)
        val actionView = myPageItem?.actionView
        actionView?.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_mypage)
        }
        return true
    }

    // onOptionsItemSelected は、カスタムアクションビューを使っているので必ず呼ばれるわけではありません。
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return when (item.itemId) {
            R.id.action_mypage -> {
                navController.navigate(R.id.nav_mypage)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        // リスナー解除
        newsListenerRegistration?.remove()
        newsListenerRegistration = null
    }
}
