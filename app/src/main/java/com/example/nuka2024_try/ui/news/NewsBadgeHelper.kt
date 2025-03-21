package com.example.nuka2024_try.ui.news

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Newsパッケージ側で「バッジ」を管理するためのヘルパークラス
 */
@OptIn(ExperimentalBadgeUtils::class)
class NewsBadgeHelper {

    private var badgeDrawable: BadgeDrawable? = null

    /**
     * メールアイコンにバッジをアタッチし、Firestoreを監視して未読数を反映する
     */
    @androidx.annotation.OptIn(ExperimentalBadgeUtils::class)
    fun setupBadge(context: Context, mailIcon: ImageView) {
        // まだBadgeDrawableを作っていないなら作成
        if (badgeDrawable == null) {
            badgeDrawable = BadgeDrawable.create(context).apply {
                // バッジの色設定（お好みで変更）
                backgroundColor = context.getColor(android.R.color.holo_red_dark)
                badgeTextColor = context.getColor(android.R.color.white)

                // ★ バッジの位置を右上にし、オフセットを調整
                badgeGravity = BadgeDrawable.TOP_END
                horizontalOffset = 12
                verticalOffset = 8
            }
        }

        // ImageViewにバッジをアタッチ
        badgeDrawable?.let {
            BadgeUtils.attachBadgeDrawable(it, mailIcon)
        }

        // Firestoreから未読数を監視
        observeUnreadCount()
    }

    private fun observeUnreadCount() {
        val currentUid = Firebase.auth.currentUser?.uid
        if (currentUid == null) {
            Log.d("NewsBadgeHelper", "currentUid is null → 未読数監視を中断")
            return
        }

        val db = Firebase.firestore
        db.collection("news")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.e("NewsBadgeHelper", "SnapshotListener error: ${e.message}")
                    return@addSnapshotListener
                }

                val docs = querySnapshot?.documents ?: emptyList()
                // デバッグ用ログ：各ドキュメントの readUsers を出力
                docs.forEach { doc ->
                    val readUsers = doc.get("readUsers") as? List<String> ?: emptyList()
                    Log.d("NewsBadgeHelper", "docId=${doc.id}, readUsers=$readUsers")
                }

                // 未読: readUsersにcurrentUidが含まれていないニュース
                val unreadCount = docs.count { doc ->
                    val readUsers = doc.get("readUsers") as? List<String> ?: emptyList()
                    !readUsers.contains(currentUid)
                }
                Log.d("NewsBadgeHelper", "unreadCount = $unreadCount")

                // バッジに反映
                if (unreadCount > 0) {
                    badgeDrawable?.number = unreadCount
                    badgeDrawable?.isVisible = true
                } else {
                    badgeDrawable?.clearNumber()
                    badgeDrawable?.isVisible = false
                }
            }
    }
}
