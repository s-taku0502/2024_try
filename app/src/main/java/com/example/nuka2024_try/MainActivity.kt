package com.example.nuka2024_try

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Firestore の未読監視用リスナー
    private var newsListenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ログインチェック
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar をアクションバーとして設定
        setSupportActionBar(binding.appBarMain.toolbar)

        // Navigation Controller
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // DrawerLayout + NavigationView 設定
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // アクションバー左上に表示されるハンバーガーアイコン設定
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_stamps,    // スタンプ
                R.id.nav_coupons,   // クーポン
                R.id.nav_stores,    // 店舗一覧
                R.id.nav_contact,   // お問い合わせ
                R.id.nav_mypage,
                R.id.nav_news
                // 必要に応じて他のフラグメントを追加
            ),
            drawerLayout
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        // NavigationView のメニュー項目がタップされたとき
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_scan -> {
                    // QRコードスキャン画面へ
                    // (createIntent を定義していない場合は Intent(...) で直接呼ぶ)
                    startActivity(Intent(this, QRCodeCaptureActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    // Navigation Graph にある目的地へ
                    navController.navigate(menuItem.itemId)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }

        // 右下のメールアイコンと未読数バッジ
        val mailIcon = binding.appBarMain.iconMail
        val textUnreadCount = binding.appBarMain.textUnreadCount

        // メールアイコンをタップ → お知らせ一覧フラグメントへ
        mailIcon.setOnClickListener {
            navController.navigate(R.id.nav_news)
        }

        // Firestore のニュースコレクションを監視して未読数を更新
        observeUnreadCount(textUnreadCount)
    }

    /**
     * メニューを生成 (ここではマイページアイコンのみ)
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // カスタムアクションビューを持つメニューアイテムを取得
        val myPageItem = menu?.findItem(R.id.action_mypage)
        val myPageActionView = myPageItem?.actionView

        // ボタンがタップされたときにマイページへ遷移
        myPageActionView?.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_mypage)
        }
        return true
    }

    /**
     * メニューアイテムが選択された時の処理
     */
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

    /**
     * Firestore の "news" コレクションをリアルタイム監視して未読数を更新
     */
    private fun observeUnreadCount(textUnreadCount: TextView?) {
        val currentUid = Firebase.auth.currentUser?.uid ?: return
        val db = Firebase.firestore

        newsListenerRegistration = db.collection("news")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    if (Firebase.auth.currentUser == null) {
                        Log.d("MainActivity", "User is null; ignoring news error.")
                    } else {
                        Log.e("MainActivity", "observeUnreadCount error: ${e.message}")
                    }
                    return@addSnapshotListener
                }

                val docs = querySnapshot?.documents ?: emptyList()
                val unreadCount = docs.count { doc ->
                    val readUsers = doc.get("readUsers") as? List<*> ?: emptyList<Any>()
                    // 型安全のため filterIsInstance を使ってもOK
                    !readUsers.contains(currentUid)
                }
                Log.d("MainActivity", "unreadCount = $unreadCount")

                if (unreadCount > 0) {
                    textUnreadCount?.text = unreadCount.toString()
                    textUnreadCount?.visibility = View.VISIBLE
                } else {
                    textUnreadCount?.visibility = View.GONE
                }
            }
    }

    /**
     * ハンバーガーアイコン（またはUpボタン）動作
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Firestore リスナー解除
        newsListenerRegistration?.remove()
        newsListenerRegistration = null
    }
}
