package com.arvl.fasimagiland.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.arvl.fasimagiland.model.Pencil
import com.arvl.fasimagiland.CanvasActivity.Companion.currentBrush
import android.util.Log

class DrawPencil @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TOUCH_TOLERANCE = 4f

    private var mX = 0f
    private var mY = 0f

    private val dataPencil = mutableListOf<Pencil>()
    private val undoList = mutableListOf<Pencil>()
    private val redoList = mutableListOf<Pencil>()

    private val paintBrush = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = currentBrush
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 16f
        alpha = 0xff
    }

    var undoRedoListener: UndoRedoListener? = null

    fun updateColor(newColor: Int) {
        paintBrush.color = newColor
    }

    private fun touchStart(x: Float, y: Float) {
        Log.d("DrawPencil", "touchStart: redoList size before clearing: ${redoList.size}")
        redoList.clear()
        Log.d("DrawPencil", "touchStart: redoList size after clearing: ${redoList.size}")

        val path = Path()
        val p = Pencil(currentBrush, path)
        dataPencil.add(p)
        path.moveTo(x, y)
        mX = x
        mY = y

        undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            val path = dataPencil.last().path
            path?.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y

            Log.d("DrawPencil", "touchMove: redoList size before clearing: ${redoList.size}")
            redoList.clear()
            Log.d("DrawPencil", "touchMove: redoList size after clearing: ${redoList.size}")

            undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
        }
    }

    private fun touchUp() {
        val path = dataPencil.last().path
        path?.lineTo(mX, mY)
        undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())

        Log.d("DrawPencil", "touchUp: redoList size before clearing: ${redoList.size}")
        redoList.clear()
        Log.d("DrawPencil", "touchUp: redoList size after clearing: ${redoList.size}")

        undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        for (p in dataPencil) {
            paintBrush.color = p.color
            p.path?.let { canvas.drawPath(it, paintBrush) }
        }
    }

    fun undo() {
        if (dataPencil.isNotEmpty()) {
            val removedPencil = dataPencil.removeAt(dataPencil.size - 1)
            undoList.add(removedPencil)

            Log.d("DrawPencil", "undo: redoList size before clearing: ${redoList.size}")
            redoList.clear()
            Log.d("DrawPencil", "undo: redoList size after clearing: ${redoList.size}")

            invalidate()
            undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
        }
    }

    fun redo() {
        if (undoList.isNotEmpty()) {
            val restoredPencil = undoList.removeAt(undoList.size - 1)
            dataPencil.add(restoredPencil)
            invalidate()
            undoRedoListener?.onUndoRedoStateChanged(canUndo(), canRedo())
        }
    }

    fun canUndo(): Boolean {
        return dataPencil.isNotEmpty()
    }

    fun canRedo(): Boolean {
        return undoList.isNotEmpty()
    }

}
