package com.arvl.fasimagiland.ui.screen.canvas

import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    private var isPaletteIconClicked = false

    companion object {
        var path = Path()
        var currentBrush = Color.BLACK
        var isEraserActive = false
    }

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Inisialisasi ImageClassifierHelper
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = this
        )

        binding.ivBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.no_animation, R.anim.slide_down)
        }

        binding.story.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.no_animation, R.anim.slide_down)
        }

        binding.apply {
            drawPencil.undoRedoListener = this@CanvasActivity

            btnPencil.setOnClickListener {
                isPencilIconClicked = !isPencilIconClicked

                if (isPencilIconClicked) {
                    btnPencil.setImageResource(R.drawable.ic_selected_pencil)

                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)

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
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)
                    colorPalate.visibility = View.INVISIBLE
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

            btnPallete.setOnClickListener {
                isPaletteIconClicked = !isPaletteIconClicked
                if (isPaletteIconClicked) {
                    colorPalate.visibility = View.VISIBLE
                    btnPallete.setImageResource(R.drawable.ic_selected_palette)
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                } else {
                    colorPalate.visibility = View.INVISIBLE
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)
                }
            }

            // Handle color selection
            btnBlue.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_blue)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnRed.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_red)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnYellow.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_yellow)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnGreen.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_green)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnBlack.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                val newColor = resources.getColor(R.color.palette_black)
                drawPencil.updateColor(newColor)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
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
