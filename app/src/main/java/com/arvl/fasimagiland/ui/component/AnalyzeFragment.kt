package com.arvl.fasimagiland.ui.component

import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.arvl.fasimagiland.R

class AnalyzeFragment : DialogFragment() {

    companion object {
        private const val IMAGE_PATH = "image_path"

        fun newInstance(imagePath: String): AnalyzeFragment {
            val args = Bundle()
            args.putString(IMAGE_PATH, imagePath)
            val fragment = AnalyzeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analyze, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagePath = arguments?.getString(IMAGE_PATH)
        val ivAnalyze = view.findViewById<ImageView>(R.id.iv_analyze)
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            ivAnalyze.setImageBitmap(bitmap)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

}
