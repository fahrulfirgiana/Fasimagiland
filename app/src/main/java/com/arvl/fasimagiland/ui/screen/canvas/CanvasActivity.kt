package com.arvl.fasimagiland.ui.screen.canvas

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arvl.fasimagiland.R
import com.arvl.fasimagiland.canvas.UndoRedoListener
import com.arvl.fasimagiland.databinding.ActivityCanvasBinding
import com.arvl.fasimagiland.ui.component.AnalyzeFragment

class CanvasActivity : AppCompatActivity(), UndoRedoListener {
    private val binding: ActivityCanvasBinding by lazy {
        ActivityCanvasBinding.inflate(layoutInflater)
    }

    private var isPencilIconClicked = false
    private var isEraserIconClicked = false
    private var isPaletteIconClicked = false

    companion object {
        var path = Path()
        var paintBrush = Paint()
        var colorList = ArrayList<Int>()
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
            }

            btnUndo.setOnClickListener {
                drawPencil.undo()
            }

            btnPallete.setOnClickListener {
                isPaletteIconClicked = !isPaletteIconClicked

                if (isPaletteIconClicked) {
                    colorPalate.visibility = View.VISIBLE

                    btnPallete.setImageResource(R.drawable.ic_selected_palette)

                    btnPencil.setImageResource(R.drawable.ic_unselected_pencil)
                    btnEraser.setImageResource(R.drawable.ic_unselected_eraser)
                } else {
                    btnPallete.setImageResource(R.drawable.ic_unselected_palette)
                    colorPalate.visibility = View.INVISIBLE
                }
            }

            btnBlue.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                paintBrush.color = resources.getColor(R.color.palette_blue)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnRed.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                paintBrush.color = resources.getColor(R.color.palette_red)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnYellow.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                paintBrush.color = resources.getColor(R.color.palette_yellow)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnGreen.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                paintBrush.color = resources.getColor(R.color.palette_green)
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnBlack.setOnClickListener {
                btnPencil.setImageResource(R.drawable.ic_selected_pencil)
                paintBrush.color = Color.BLACK
                currentColor(paintBrush.color)
                colorPalate.visibility = View.INVISIBLE
                btnPallete.setImageResource(R.drawable.ic_unselected_palette)
            }

            btnAnalyze.setOnClickListener {
                AnalyzeFragment().show(supportFragmentManager, "AnalyzeFragment")
            }
        }
    }

    override fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean) {
        binding.btnUndo.apply {
            setImageResource(if (canUndo) R.drawable.ic_selected_undo else R.drawable.ic_unselected_undo)
        }

        binding.btnRedo.apply {
            setImageResource(if (canRedo) R.drawable.ic_selected_redo else R.drawable.ic_unselected_redo)
        }
    }

    private fun currentColor(color: Int) {
        currentBrush = color
        path = Path()
    }
}