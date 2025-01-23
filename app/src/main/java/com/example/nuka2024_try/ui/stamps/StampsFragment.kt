package com.example.nuka2024_try.ui.stamps

import android.content.Context
import android.content.SharedPreferences
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nuka2024_try.R
import com.google.android.flexbox.FlexboxLayout
import org.json.JSONArray
import javax.microedition.khronos.opengles.GL11

class StampsFragment : Fragment() {

    private var stampCount = 0
    private lateinit var stampTextView: TextView
    private lateinit var stampContainer: FlexboxLayout

    // 各スタンプのImageViewを保持する変数
    private lateinit var stamp1: ImageView
    private lateinit var stamp2: ImageView
    private lateinit var stamp3: ImageView
    private lateinit var stamp4: ImageView
    private lateinit var stamp5: ImageView
    private lateinit var stamp6: ImageView
    private lateinit var stamp7: ImageView
    private lateinit var stamp8: ImageView
    private lateinit var stamp9: ImageView
    private lateinit var stamp10: ImageView
    private lateinit var stamp11: ImageView
    private lateinit var stamp12: ImageView
    private lateinit var stamp13: ImageView
    private lateinit var stamp14: ImageView
    private lateinit var stamp15: ImageView
    private lateinit var stamp16: ImageView

    private lateinit var stampImageViews: List<ImageView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 引数から QRコード結果を取得
        val qrCodeResult = arguments?.getString("qrCodeResult")

        // QRコード結果が null でない場合、スタンプを追加
        if (qrCodeResult != null) {
            addStamp(qrCodeResult)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stamps, container, false)
        stampTextView = view.findViewById(R.id.stampCountTextView)
        stampContainer = view.findViewById(R.id.stampContainer)

        // 各スタンプのImageViewをリストで初期化
        stampImageViews = listOf(
            view.findViewById(R.id.stamp1),
            view.findViewById(R.id.stamp2),
            view.findViewById(R.id.stamp3),
            view.findViewById(R.id.stamp4),
            view.findViewById(R.id.stamp5),
            view.findViewById(R.id.stamp6),
            view.findViewById(R.id.stamp7),
            view.findViewById(R.id.stamp8),
            view.findViewById(R.id.stamp9),
            view.findViewById(R.id.stamp10),
            view.findViewById(R.id.stamp11),
            view.findViewById(R.id.stamp12),
            view.findViewById(R.id.stamp13),
            view.findViewById(R.id.stamp14),
            view.findViewById(R.id.stamp15),
            view.findViewById(R.id.stamp16)
        )

        // グレースケールのColorMatrixを設定
        val grayMatrix = ColorMatrix().apply { setSaturation(0f) }
        val grayFilter = ColorMatrixColorFilter(grayMatrix)

        // 全てのスタンプをグレースケールに設定
        stampImageViews.forEach { it.colorFilter = grayFilter }

        // 保存されているデータを読み込んでカラー表示を適用
        val savedStamps = loadNumberArray("stamps")
        stampTextView.text = "スタンプ枚数: ${savedStamps.size}"
        savedStamps.forEach { index ->
            if (index in stampImageViews.indices) {
                stampImageViews[index].clearColorFilter() // カラー表示に変更
            }
        }

        return view
    }

    private fun loadNumberArray(key: String): List<Int>{
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("Stamps",Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(key,null) ?:return emptyList()
        val jsonArray = JSONArray(jsonString)

        val numbers = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()){
            numbers.add((jsonArray.getInt(i)))
        }
        stampCount = numbers.size
        return  numbers
    }
    // スタンプを動的に表示するメソッド
    private fun showStamp(stamp: ImageView) {
        stamp.visibility = View.VISIBLE
    }

    // スタンプを動的に追加するメソッド
    fun addStamp(stampCode: String) {
        when (stampCode) {
            "aiueo" -> {
                showStamp(stamp1)
                stampCount++
            }
            "kakikukeko" -> {
                showStamp(stamp2)
                stampCount++
            }
            "STAMPS003" -> {
                showStamp(stamp3)
                stampCount++
            }
            "STAMPS004" -> {
                showStamp(stamp4)
                stampCount++
            }
            "STAMPS005" -> {
                showStamp(stamp5)
                stampCount++
            }
            "STAMPS006" -> {
                showStamp(stamp6)
                stampCount++
            }
            "STAMPS007" -> {
                showStamp(stamp7)
                stampCount++
            }
            "STAMPS008" -> {
                showStamp(stamp8)
                stampCount++
            }
            "STAMPS009" -> {
                showStamp(stamp9)
                stampCount++
            }
            "STAMPS010" -> {
                showStamp(stamp10)
                stampCount++
            }
            "STAMPS011" -> {
                showStamp(stamp11)
                stampCount++
            }
            "STAMPS012" -> {
                showStamp(stamp12)
                stampCount++
            }
            "STAMPS013" -> {
                showStamp(stamp13)
                stampCount++
            }
            "STAMPS014" -> {
                showStamp(stamp14)
                stampCount++
            }
            "STAMPS015" -> {
                showStamp(stamp15)  // 修正: 正しい ImageView を渡す
                stampCount++
            }
            "STAMPS016" -> {
                showStamp(stamp16)
                stampCount++
            }
            else -> {
                Toast.makeText(context, "無効なQRコードです", Toast.LENGTH_SHORT).show()
            }
        }

        updateStampUI()
        saveStamp(stampCode)  // スタンプを保存する
    }




    // UIをスタンプ数に応じて更新
    private fun updateStampUI() {
        stampTextView.text = "Stamps: $stampCount"
    }

    // スタンプをSharedPreferencesに保存する
    private fun saveStamp(stampCode: String) {
        val sharedPreferences = activity?.getSharedPreferences("stamps", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("stamp$stampCount", stampCode)  // スタンプコードを保存
        editor?.apply()
    }
}