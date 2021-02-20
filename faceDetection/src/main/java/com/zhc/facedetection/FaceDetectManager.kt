package com.zhc.facedetection

import android.graphics.*
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.luozw.detection.Detection
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DecimalFormat

object FaceDetectManager {

    private const val MODEL_TYPE = 1 // 0 yolo 1 face

    /////////////////////////////////
    private var people_flag = false
    private var distance_near_flag = false
    private const val set_max_area = 120000
    private const val width_ratio_max = 0.8 //宽度和高度比例设置
    private var threshold = 0.7
    private var nms_threshold = 0.3


    init {
        System.loadLibrary("ncnn_detection")
    }

    private fun detectOnModel(
        image: ImageProxy,
        rotationDegrees: Int,
        tooNearCallback: (tooNear: Boolean) -> Unit
    ) {                        //void  改为boolean[]
        val bitmapsrc = imageToBitmap(image) // 格式转换
        //        int rotate = readPictureDegree(image);
        val lastBitmap = adjustPhotoRotation(bitmapsrc, 270)
        val start = System.currentTimeMillis()
        val result =
            Detection.detect(lastBitmap, threshold, nms_threshold)
        val end = System.currentTimeMillis()
        val time = end - start
        //Toast.makeText(MainActivity.this, "time：" + time + "ms", Toast.LENGTH_SHORT).show();
//        Bitmap mutableBitmap = bitmapsrc.copy(Bitmap.Config.ARGB_8888, true);
        val mutableBitmap = lastBitmap!!.copy(Bitmap.Config.ARGB_8888, true)

        //Bitmap lastBitmap = adjustPhotoRotation(mutableBitmap ,270);
        val canvas = Canvas(mutableBitmap)
        val boxPaint = Paint()
        boxPaint.alpha = 200
        boxPaint.style = Paint.Style.STROKE
        val size = image.width / 800.toFloat()
        boxPaint.strokeWidth = 4 * size
        boxPaint.textSize = 40 * size
        for (box in result) {
            boxPaint.color = box.color
            boxPaint.style = Paint.Style.FILL
            if (MODEL_TYPE == 0) {
                canvas.drawText(box.vocLabel, box.x0, box.y0, boxPaint)
            } else if (MODEL_TYPE == 1) {
                canvas.drawText(box.faceLabel, box.x0, box.y0, boxPaint)
            }
            val decimalFormat = DecimalFormat("0.00")
            val score = decimalFormat.format(box.score.toDouble())
            canvas.drawText(score, box.x0, box.y0 + 40 * size, boxPaint)
            boxPaint.style = Paint.Style.STROKE
            canvas.drawRect(box.rect, boxPaint)
        }
        var max_score = 0f
        var people_id = 0
        val area = 0
        var box_width = 0
        var box_height = 0
        val box_ratio = 0f
        var width_ratio = 0f
        var height_ratio = 0f
        var max_ratio = 0.0
        var pic_width = 480
        var pic_height = 640
        if (result.size >= 1) {
            people_flag = true
            max_score = result[0].score
            for (j in result.indices) {
                if (result[j].score > max_score) {
                    max_score = result[j].score
                    people_id = j
                } else {
                    continue
                }
            }
            if (result[people_id].y0 < 0) {
                result[people_id].y0 = 0f
            }
            if (result[people_id].x0 < 0) {
                result[people_id].x0 = 0f
            }
            if (result[people_id].x1 > 480) {
                result[people_id].x0 = 480f
            }
            if (result[people_id].y1 < 640) {
                result[people_id].y1 = 640f
            }
            box_height = (result[people_id].y1 - result[people_id].y0).toInt()
            box_width = (result[people_id].x1 - result[people_id].x0).toInt()
            pic_width = bitmapsrc.height
            pic_height = bitmapsrc.width
            width_ratio = box_width.toFloat() / pic_width //图片宽度取480
            height_ratio = box_height.toFloat() / pic_height //图片高度取640
            //            area=box_height*box_width;
            //Toast.makeText(MainActivity.this, "ratio：" + box_ratio , Toast.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.this, "area：" + area , Toast.LENGTH_SHORT).show();
//            if (area> set_max_area || width_ratio > width_ratio_max ||  height_ratio > height_ratio_max){
            Log.e(
                "ratio",
                "widthratio:$width_ratio-heightratio:$height_ratio"
            )
            //Log.e("height_ratio" ,"height_ratio");
            max_ratio = Math.max(width_ratio, height_ratio).toDouble()
            if (max_ratio > width_ratio_max) {
                distance_near_flag = true
//                Toast.makeText(this "you are too close", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "detectOnModel: zhc==== you are too close")
            } else {
                distance_near_flag = false
            }
        } else {
            people_flag = false
            distance_near_flag = false
        }
        tooNearCallback.invoke(distance_near_flag)
//        resultImageView.setImageBitmap(mutableBitmap)


//        final_result[0]=people_flag;
//        final_result[1]=distance_near_flag;
//
//        return final_result;


//        if (detectService == null) {
//            detectCamera.set(false);
//            return;
//        }
//        detectService.execute(new Runnable() {
//            @Override
//            public void run() {
//                Matrix matrix = new Matrix();
//                matrix.postRotate(rotationDegrees);
//                width = bitmapsrc.getWidth();
//                height = bitmapsrc.getHeight();
//                Bitmap bitmap = Bitmap.createBitmap(bitmapsrc, 0, 0, width, height, matrix, false);
//
//                detectAndDraw(bitmap);
//
//            }
//        });
    }

    private fun imageToBitmap(image: ImageProxy): Bitmap {
        val nv21: ByteArray = imagetToNV21(image)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val imageBytes = out.toByteArray()
        try {
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun imagetToNV21(image: ImageProxy): ByteArray {
        val planes = image.planes
        val y = planes[0]
        val u = planes[1]
        val v = planes[2]
        val yBuffer = y.buffer
        val uBuffer = u.buffer
        val vBuffer = v.buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        // U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        return nv21
    }

    fun adjustPhotoRotation(bm: Bitmap, orientationDegree: Int): Bitmap? {
        val m = Matrix()
        m.setRotate(orientationDegree.toFloat(), bm.width.toFloat() / 2, bm.height.toFloat() / 2)
        try {
            return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
        } catch (ex: OutOfMemoryError) {
        }
        return null
    }

    class DetectAnalyzer(private val tooNearCallback: (tooNear: Boolean) -> Unit) : ImageAnalysis.Analyzer {
        override fun analyze(image: ImageProxy) {
            detectOnModel(image!!, 0, tooNearCallback)
            image.close()
        }
    }
}
