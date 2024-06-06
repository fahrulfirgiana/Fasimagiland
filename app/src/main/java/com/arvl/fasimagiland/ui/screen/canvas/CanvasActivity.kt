package com.arvl.fasimagiland.ui.screen.canvas

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arvl.fasimagiland.R
import com.arvl.fasimagiland.canvas.UndoRedoListener
import com.arvl.fasimagiland.databinding.ActivityCanvasBinding
import com.arvl.fasimagiland.model.Pencil
import com.arvl.fasimagiland.ui.component.AnalyzeFragment
import java.io.File
import java.io.FileOutputStream

class CanvasActivity : AppCompatActivity(), UndoRedoListener {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

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

            btnAnalyze.setOnClickListener {
                AnalyzeFragment().apply {
                    show(supportFragmentManager, AnalyzeFragment::class.java.simpleName)
                }
            }

            btnAnalyze.setOnClickListener {
                val bitmap = Bitmap.createBitmap(drawPencil.width, drawPencil.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawPencil.draw(canvas)
                val imagePath = saveBitmap(bitmap)
                val analyzeFragment = AnalyzeFragment.newInstance(imagePath)
                analyzeFragment.show(supportFragmentManager, AnalyzeFragment::class.java.simpleName)
            }
        }
    }

    fun calculateBounds(dataPencil: List<Pencil>): RectF {
        var left = Float.MAX_VALUE
        var top = Float.MAX_VALUE
        var right = Float.MIN_VALUE
        var bottom = Float.MIN_VALUE

        for (pencil in dataPencil) {
            val pathBounds = RectF()
            pencil.path?.computeBounds(pathBounds, true)

            if (pathBounds.left < left) {
                left = pathBounds.left
            }
            if (pathBounds.top < top) {
                top = pathBounds.top
            }
            if (pathBounds.right > right) {
                right = pathBounds.right
            }
            if (pathBounds.bottom > bottom) {
                bottom = pathBounds.bottom
            }
        }

        return RectF(left, top, right, bottom)
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

    private fun saveBitmap(bitmap: Bitmap): String {
        val file = File(externalCacheDir, "sketch.png")
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file.absolutePath
    }

}
