package com.test.pdfannotations

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pspdfkit.annotations.Annotation
import com.pspdfkit.configuration.PdfConfiguration
import com.pspdfkit.configuration.page.PageScrollDirection
import com.pspdfkit.document.PdfDocument
import com.pspdfkit.listeners.DocumentListener
import com.pspdfkit.ui.PdfFragment
import com.pspdfkit.ui.special_mode.controller.AnnotationSelectionController
import com.pspdfkit.ui.special_mode.manager.AnnotationManager

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val documentUri = Uri.parse("file:///android_asset/quran.pdf")
        val configuration = PdfConfiguration.Builder()
            .scrollDirection(PageScrollDirection.HORIZONTAL)
            .build()
        // First, try to restore a previously created fragment. If no fragment exists, create a new one.
        val pdfFragment = createFragment(documentUri, configuration)
        pdfFragment.addOnAnnotationSelectedListener(object :
            AnnotationManager.OnAnnotationSelectedListener {
            override fun onPrepareAnnotationSelection(
                controller: AnnotationSelectionController,
                annotation: Annotation,
                annotationCreated: Boolean
            ): Boolean {
                // Returning `false` here would prevent the annotation from being selected.
                return true
            }

            override fun onAnnotationSelected(annotation: Annotation, annotationCreated: Boolean) {
                //logging to the console the type of the tapped annotation
                Log.i(TAG, annotation.creator.toString())

            }
        })

        pdfFragment.addDocumentListener(object : DocumentListener {
            override fun onDocumentLoaded(document: PdfDocument) {
                super.onDocumentLoaded(document)
                // Retrieve the total number of pages in the document.
                val documentPageCount = document.pageCount

                // get the first annotation in the document
                val allAnnotations = (0..documentPageCount - 1)
                    .asSequence()
                    .mapNotNull { page ->
                        document.annotationProvider.getAnnotations(page).takeUnless { it.isEmpty() }
                    }

                val annotation = allAnnotations.firstOrNull()?.first()

                if (annotation != null) {
                    //navigate to the page of the annotation and select it
                    pdfFragment.setPageIndex(annotation.pageIndex, true)
                    pdfFragment.setSelectedAnnotation(annotation)

                    Log.i("Selected Annotation", annotation.type.toString())
                }
            }

        })

    }

    private fun createFragment(documentUri: Uri, configuration: PdfConfiguration): PdfFragment {
        val fragment = PdfFragment.newInstance(documentUri, configuration)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        return fragment
    }
}