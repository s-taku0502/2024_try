package com.example.nuka2024_try.ui.qr_scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nuka2024_try.R
import com.example.nuka2024_try.databinding.FragmentQrcodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoderFactory
import com.journeyapps.barcodescanner.DefaultDecoderFactory


class ScannersFragment : Fragment() {

    private var _binding: FragmentQrcodeBinding? = null
    private val binding get() = _binding!!

    private val CAMERA_PERMISSION_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrcodeBinding.inflate(inflater, container, false)

        if (!hasCameraPermission()) {
            requestCameraPermission()
        } else {
            setupBarcodeReader()
        }

        return binding.root
    }

    private fun setupBarcodeReader() {
        // デコード設定をカスタマイズ
        val decodeHints = mutableMapOf<DecodeHintType, Any>()
        decodeHints[DecodeHintType.TRY_HARDER] = true // より積極的にデコード
        decodeHints[DecodeHintType.POSSIBLE_FORMATS] = listOf(BarcodeFormat.QR_CODE) // QRコードのみ対象
        decodeHints[DecodeHintType.CHARACTER_SET] = "UTF-8" // UTF-8に対応

        val decoderFactory: DecoderFactory = DefaultDecoderFactory(
            listOf(BarcodeFormat.QR_CODE), // サポートするフォーマットを設定
            decodeHints, // デコードヒントを設定
            null, // 文字エンコーディング（使わない場合は null）
            0 // ハードウェア設定。0 を渡してデフォルト動作を使用
        )



        binding.qrcodeReader.barcodeView.decoderFactory = decoderFactory

        binding.qrcodeReader.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                val qrCodeText = result.text
                if (qrCodeText.isNotEmpty()) {
                    Log.d("QRCode", qrCodeText)
                    Toast.makeText(requireContext(), "QR Code: $qrCodeText", Toast.LENGTH_SHORT).show()
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
                // 必要に応じて実装
            }
        })
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupBarcodeReader()
            } else {
                Toast.makeText(requireContext(), "カメラの許可が必要です", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            binding.qrcodeReader.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.qrcodeReader.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.ToHome).setOnClickListener {
            findNavController().navigate(R.id.action_qrCodeCaptureFragment_to_homeFragment)
        }
    }
}
