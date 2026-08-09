package com.artem.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.core.content.FileProvider

fun generatePdfReport(context: Context, studentName: String, group: String, practiceBase: String) {
    val pdfDocument = PdfDocument()

    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    val paint = Paint()
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.textSize = 16f

    canvas.drawText("МІНІСТЕРСТВО ОСВІТИ І НАУКИ УКРАЇНИ", 120f, 100f, paint)
    canvas.drawText("ДЕРЖАВНИЙ УНІВЕРСИТЕТ «ЖИТОМИРСЬКА ПОЛІТЕХНІКА»", 70f, 130f, paint)

    paint.textSize = 24f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("ЗВІТ", 270f, 300f, paint)

    paint.textSize = 16f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    canvas.drawText("з інженерної практики", 210f, 330f, paint)

    canvas.drawText("Виконав:", 350f, 480f, paint)
    canvas.drawText("Студент групи $group", 350f, 510f, paint)
    canvas.drawText(studentName, 350f, 540f, paint)
    canvas.drawText("База: $practiceBase", 350f, 570f, paint)

    canvas.drawText("Житомир - 2026", 240f, 780f, paint)

    pdfDocument.finishPage(page)

    try {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, "Report_$group.pdf")
        pdfDocument.writeTo(FileOutputStream(file))

        Toast.makeText(context, "PDF створено!", Toast.LENGTH_SHORT).show()

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "На телефоні не встановлено програму для читання PDF", Toast.LENGTH_LONG).show()
        }

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Помилка: ${e.message}", Toast.LENGTH_SHORT).show()
    } finally {
        pdfDocument.close()
    }
}