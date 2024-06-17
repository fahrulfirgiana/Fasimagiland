package com.arvl.fasimagiland.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import com.arvl.fasimagiland.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private var maxResults: Int = 10,
    private val modelName: String = "model_baru.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            val errorMessage = context.getString(R.string.image_classifier_failed)
            Log.e(TAG, "Failed to create ImageClassifier: $errorMessage", e)
            classifierListener?.onError(errorMessage)
        }
    }

    fun classifyCanvas(bitmap: Bitmap) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        // Update the image processor for grayscale images
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(28, 28, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .add(GrayscaleOp())
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val imageProcessingOptions = ImageProcessingOptions.builder().build()

        var inferenceTime = SystemClock.uptimeMillis()
        val results = imageClassifier?.classify(tensorImage, imageProcessingOptions)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        classifierListener?.onResults(
            results,
            inferenceTime
        )
    }


    class GrayscaleOp : TensorOperator {
        override fun apply(input: TensorBuffer): TensorBuffer {
            val flatArray = input.floatArray
            for (i in flatArray.indices step 3) {
                val gray =
                    0.2989f * flatArray[i] + 0.5870f * flatArray[i + 1] + 0.1140f * flatArray[i + 2]
                flatArray[i] = gray
                flatArray[i + 1] = gray
                flatArray[i + 2] = gray
            }
            return input
        }
    }


    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}

