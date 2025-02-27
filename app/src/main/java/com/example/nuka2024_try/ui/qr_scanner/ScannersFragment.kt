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
        Log.d("ScannersFragment", "onCreateView called")

        if (!hasCameraPermission()) {
            Log.d("ScannersFragment", "Camera permission not granted, requesting permission")
            requestCameraPermission()
        } else {
            Log.d("ScannersFragment", "Camera permission granted, setting up barcode reader")
            setupBarcodeReader()
        }

        return binding.root
    }

    private fun setupBarcodeReader() {
        Log.d("ScannersFragment", "Setting up barcode reader")

        // デコード設定をカスタマイズ
        val decodeHints = mutableMapOf<DecodeHintType, Any>()
        decodeHints[DecodeHintType.TRY_HARDER] = true // より積極的にデコード
        decodeHints[DecodeHintType.POSSIBLE_FORMATS] = listOf(BarcodeFormat.QR_CODE) // QRコードのみ対象
        decodeHints[DecodeHintType.CHARACTER_SET] = "UTF-8" // UTF-8に対応

        val decoderFactory: DecoderFactory = DefaultDecoderFactory(
            listOf(BarcodeFormat.QR_CODE),
            decodeHints,
            null,
            0
        )

        binding.qrcodeReader.barcodeView.decoderFactory = decoderFactory

        binding.qrcodeReader.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                val qrCodeText = result.text
                Log.d("ScannersFragment", "QR Code scanned: $qrCodeText")
                if (qrCodeText.isNotEmpty()) {
                    Toast.makeText(requireContext(), "QR Code: $qrCodeText", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("ScannersFragment", "QR Code scan result was empty")
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
                Log.d("ScannersFragment", "Possible result points: $resultPoints")
            }
        })
    }

    private fun hasCameraPermission(): Boolean {
        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        Log.d("ScannersFragment", "Camera permission check: $hasPermission")
        return hasPermission
    }

    private fun requestCameraPermission() {
        Log.d("ScannersFragment", "Requesting camera permission")
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ScannersFragment", "Camera permission granted")
                setupBarcodeReader()
            } else {
                Log.d("ScannersFragment", "Camera permission denied")
                Toast.makeText(requireContext(), "カメラの許可が必要です", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("ScannersFragment", "onResume called")
        if (hasCameraPermission()) {
            binding.qrcodeReader.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("ScannersFragment", "onPause called")
        binding.qrcodeReader.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ScannersFragment", "onDestroyView called")
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ScannersFragment", "onViewCreated called")

        view.findViewById<Button>(R.id.ToHome).setOnClickListener {
            Log.d("ScannersFragment", "Navigating to HomeFragment")
            findNavController().navigate(R.id.action_qrCodeCaptureFragment_to_homeFragment)
        }
    }
}