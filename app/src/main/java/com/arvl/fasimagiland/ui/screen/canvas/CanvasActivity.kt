package com.arvl.fasimagiland.ui.screen.canvas

import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.arvl.fasimagiland.R
import com.arvl.fasimagiland.canvas.UndoRedoListener
import com.arvl.fasimagiland.databinding.ActivityCanvasBinding
import com.arvl.fasimagiland.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class CanvasActivity : AppCompatActivity(), UndoRedoListener, ImageClassifierHelper.ClassifierListener {
    private val binding: ActivityCanvasBinding by lazy {
        ActivityCanvasBinding.inflate(layoutInflater)
    }

    private var isPencilIconClicked = false
    private var isEraserIconClicked = false

    companion object {
        var path = Path()
        var currentBrush = Color.WHITE
        var isEraserActive = false
    }

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Inisialisasi ImageClassifierHelper
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = this
        )

        val firstSentence = intent.getStringExtra("FIRST_SENTENCE")
        firstSentence?.let {
            binding.lstory.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.apply {
            drawPencil.undoRedoListener = this@CanvasActivity

            btnPencil.setOnClickListener {
                isPencilIconClicked = !isPencilIconClicked

                if (isPencilIconClicked) {
                    btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    drawPencil.stopErasing()
                    isEraserActive = false

                } else {
                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                }
            }

            btnEraser.setOnClickListener {
                isEraserIconClicked = !isEraserIconClicked
                if (isEraserIconClicked) {
                    btnEraser.setImageResource(R.drawable.ic_selected_eraser)
                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                    drawPencil.startErasing()
                    isEraserActive = true
                } else {
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    drawPencil.stopErasing()
                    isEraserActive = false
                }
            }

            btnRedo.setOnClickListener {
                drawPencil.redo()
                updateUndoRedoIcons()
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
            }

            btnUndo.setOnClickListener {
                drawPencil.undo()
                updateUndoRedoIcons()
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
            }

            binding.btnAnalyze.setOnClickListener {
                val bitmap = drawPencil.getBitmapFromCanvas()
                bitmap.let {
                    imageClassifierHelper.classifyCanvas(it)
                }
            }
        }
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        val resultText = results?.joinToString { classification ->
            classification.categories.joinToString { category ->
                "${category.label}: ${category.score}"
            }
        } ?: "Klasifikasi gagal"

        Toast.makeText(this, resultText, Toast.LENGTH_LONG).show()
    }

    override fun onError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    private fun updateUndoRedoIcons() {
        binding.apply {
            if (drawPencil.canUndo()) {
                btnUndo.setImageResource(R.drawable.ic_selected_undo)
            } else {
                btnUndo.setImageResource(R.drawable.ic_unselected_undo)
            }

            if (drawPencil.canRedo()) {
                btnRedo.setImageResource(R.drawable.ic_selected_redo)
            } else {
                btnRedo.setImageResource(R.drawable.ic_unselected_redo)
            }
        }
    }

    override fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean) {
        binding.btnUndo.isEnabled = canUndo
        binding.btnRedo.isEnabled = canRedo
        updateUndoRedoIcons()
    }
}
