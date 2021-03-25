package com.zhc.bizone.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.luozw.detection.Detection
import com.zhc.bizone.R
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.executors.Executor
import com.zhc.common.executors.runInMain
import com.zhc.common.vm.BaseViewModel
import com.zhc.facedetection.FaceDetectManager
import com.zhc.facedetection.FaceDetectManager.DetectAnalyzer
import kotlinx.android.synthetic.main.face_detection_activity_layout.*
import java.nio.ByteBuffer
typealias LumaListener = (luma: Double) -> Unit

@Route(path = Router.Pages.BizOneModule.BIZ_ONE_FACE_DETECTION_ACTIVITY)
class FaceDetectionActivity: BaseActivity<BaseViewModel>() {

    private var cameraClosed: Boolean = false
    private var cameraProvider: ProcessCameraProvider? = null

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun getLayoutId(): Int {
        return R.layout.face_detection_activity_layout
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        Detection.init(assets)

        cameraController.setOnClickListener {
            if (cameraClosed) {
                startCamera()
            } else {
                cameraProvider?.unbindAll()
                cameraClosed = true
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val detectAnalyzer = DetectAnalyzer {
                runInMain {
//                    faceRect.setImageBitmap(it)
                    Log.d(TAG, "startCamera: isTooNear: $it")
                    if (it) {
                        ToastUtils.showLong("too near")
                    }
                }
            }
            val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(Executor.ioExecutor, LuminosityAnalyzer { luma ->
                            Log.d(TAG, "zhc==== Average luminosity: $luma")
                        })
                    }
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    this, cameraSelector, preview, gainAnalyzer(detectAnalyzer)
//                    this, cameraSelector, preview, imageAnalyzer
                )
                cameraClosed = false
            } catch (exc: Exception) {
                cameraClosed = true
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }


    private fun gainAnalyzer(detectAnalyzer: DetectAnalyzer): UseCase? {
        val resolutionSize = Size(480, 640)
        return ImageAnalysis.Builder()
                .setTargetResolution(resolutionSize)
                .build()
                .also {
                    it.setAnalyzer(Executor.ioExecutor, detectAnalyzer)
                }
    }

    private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {
            Log.d(TAG, "analyze: zhc==== luminosityAnalyzer")
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listener(luma)

            image.close()
        }
    }
}