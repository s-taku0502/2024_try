package com.example.nuka2024_try.ui.stamps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.nuka2024_try.R
import com.google.android.flexbox.FlexboxLayout

class StampsFragment : Fragment() {

    private var stampCount = 0
    private lateinit var stampContainer: FlexboxLayout
    private var listener: StampListener? = null

    // リスナーを設定する
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as StampListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement StampListener")
        }
    }

    // リスナーをクリアする
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stamps, container, false)
        stampContainer = view.findViewById(R.id.stampContainer)

        // QRコード結果を処理
        val qrCodeResult = arguments?.getString("stampCode")
        if (qrCodeResult == "yakitori_zen_qr") {
            addStampWithIcon(R.drawable.yakitori_icon_stamp)
        }
        return view
    }

    // スタンプを追加し、カウントを更新
    private fun addStampWithIcon(iconResId: Int) {
        val imageView = ImageView(context)
        imageView.setImageResource(iconResId)
        imageView.layoutParams = FlexboxLayout.LayoutParams(100, 100)
        stampContainer.addView(imageView)

        stampCount++
        saveStampCount()
        listener?.onStampCountUpdated(stampCount) // リスナーを通じて通知
    }

    // スタンプ数を保存
    private fun saveStampCount() {
        val sharedPreferences = activity?.getSharedPreferences("stamps", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putInt("stampCount", stampCount)?.apply()
    }
}
