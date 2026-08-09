package com.artem.myapplication

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream

fun generateWordReport(context: Context, studentName: String, group: String, practiceBase: String) {
    try {
        val document = XWPFDocument()

        fun addParagraph(text: String, align: ParagraphAlignment, isBold: Boolean = false, fontSize: Int = 14) {
            val paragraph = document.createParagraph()
            paragraph.alignment = align
            val run = paragraph.createRun()
            run.fontFamily = "Times New Roman"
            run.fontSize = fontSize
            run.isBold = isBold
            run.setText(text)
        }

        fun addEmptyLine() {
            document.createParagraph().createRun().setText("")
        }

        addParagraph("МІНІСТЕРСТВО ОСВІТИ І НАУКИ УКРАЇНИ", ParagraphAlignment.CENTER)
        addParagraph("ДЕРЖАВНИЙ УНІВЕРСИТЕТ «ЖИТОМИРСЬКА ПОЛІТЕХНІКА»", ParagraphAlignment.CENTER)

        addEmptyLine()
        addEmptyLine()

        addParagraph("ЗВІТ", ParagraphAlignment.CENTER, isBold = true, fontSize = 24)
        addParagraph("з інженерної практики", ParagraphAlignment.CENTER, fontSize = 16)

        addEmptyLine()
        addEmptyLine()

        addParagraph("Виконав: студент групи $group", ParagraphAlignment.RIGHT)
        addParagraph(studentName, ParagraphAlignment.RIGHT)
        addParagraph("База: $practiceBase", ParagraphAlignment.RIGHT)

        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, "Report_$group.docx")

        FileOutputStream(file).use { out ->
            document.write(out)
        }

        Toast.makeText(context, "Word-документ створено!", Toast.LENGTH_SHORT).show()

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent)

    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "На телефоні не встановлено програму для читання Word", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Помилка збереження Word: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}