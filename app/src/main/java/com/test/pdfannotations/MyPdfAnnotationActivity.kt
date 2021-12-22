package com.test.pdfannotations

import android.util.Log
import com.pspdfkit.annotations.Annotation
import com.pspdfkit.ui.PdfActivity
import com.pspdfkit.ui.PdfOutlineView

private const val TAG = "MyPdfAnnotationActivity"
class MyPdfAnnotationActivity: PdfActivity(), PdfOutlineView.OnAnnotationTapListener {
    override fun onAnnotationTap(pdfOutlineView: PdfOutlineView, annotation: Annotation) {
        Log.d(TAG, "onAnnotationTap: $pdfOutlineView")
        Log.d(TAG, "onAnnotationTap: $annotation")
    }
}