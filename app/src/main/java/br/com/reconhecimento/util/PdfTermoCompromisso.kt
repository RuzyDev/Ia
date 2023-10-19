package br.com.reconhecimento.util

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.*


object PdfTermoCompromisso {
    private val tituloFont =
        FontFactory.getFont(FontFactory.defaultEncoding, 16f, Font.BOLD, BaseColor.BLACK)
    private val conteudoFont =
        FontFactory.getFont(FontFactory.defaultEncoding, 12f, Font.NORMAL, BaseColor.BLACK)
    private val conteudoFontBold =
        FontFactory.getFont(FontFactory.defaultEncoding, 12f, Font.BOLD, BaseColor.BLACK)

    fun criarPdf(
        context: Context,
        nome: String,
        cpf: String,
        pathUsuarioFoto: String,
        pathUsuarioAss: String,
        titlePdf: String
    ): String? {
        try {
            val pdfDirectory = File(context.filesDir, "arcom/midia")
            if (!pdfDirectory.exists()) pdfDirectory.mkdirs()
            val document = Document()
            val file = File(pdfDirectory, "$titlePdf.pdf")
            file.createNewFile()
            val fo = FileOutputStream(file)
            PdfWriter.getInstance(document, fo)
            document.open()
            adicionarTitulo(document)
            adicionarTextoInfo(document)
            adicionarVisitantesCadastrados(document, nome, cpf, pathUsuarioFoto, pathUsuarioAss)
            adicionarRodape(document)
            document.close()
            fo.close()
            return file.absolutePath
        } catch (de: DocumentException) {
            de.printStackTrace()
        } catch (de: IOException) {
            de.printStackTrace()
        } catch (de: ExceptionInInitializerError) {
            de.printStackTrace()
        }
        return null
    }

    @Throws(DocumentException::class)
    private fun adicionarTitulo(document: Document) {
        val p = Paragraph("TERMO DE CONSENTIMENTO DE COLETA DE DADOS", tituloFont)
        p.setLeading(1f, 1f)
        p.alignment = Element.ALIGN_CENTER
        document.add(p)
        adicionarLinhaVazia(document, 1)
    }

    @Throws(DocumentException::class)
    private fun adicionarTextoInfo(document: Document) {
        var p = Paragraph(
            "Considerando o interesse expresso do signatário em adentrar nas dependências da empresa Arcom S/A, este termo" +
                    " tem por finalidade registrar a manifestação livre, informada e inequívoca pela qual o titular concorda com a utilização de" +
                    " seus dados pessoais abaixo identificados para finalidades específicas, nos moldes da Lei 13.709 de 14 de agosto de 2018 -" +
                    " Lei Geral de Proteção de Dados Pessoais (LGPD).",
            conteudoFont
        )
        p.setLeading(1f, 1f)
        p.alignment = Element.ALIGN_JUSTIFIED
        document.add(p)
        adicionarLinhaVazia(document, 1)
        p = Paragraph(
            ("Com a assinatura, o titular autoriza a empresa Arcom S/A, inscrita no CNPJ sob o n° 25.769.266/0001-24 a" +
                    " registrar o acesso do signatário na portaria da empresa, em cumprimento às medidas de segurança interna" +
                    " adotadas pela sociedade empresária."),
            conteudoFont
        )
        p.setLeading(1f, 1f)
        p.alignment = Element.ALIGN_JUSTIFIED
        document.add(p)
        adicionarLinhaVazia(document, 1)
        p = Paragraph(
            ("Estes dados não serão compartilhados com outras empresas, salvo por expressa determinação legal, permanecendo" +
                    " arquivados no banco de dados da Arcom S/A, seguindo todos os protocolos de segurança da informação necessários para " +
                    "a garantia de não violação destes elementos."),
            conteudoFont
        )
        p.setLeading(1f, 1f)
        p.alignment = Element.ALIGN_JUSTIFIED
        document.add(p)
        adicionarLinhaVazia(document, 1)
        p = Paragraph(
            "Com isso, a identificação abaixo, por meio de foto e assinatura pessoal, expressa a irrestrita" +
                    " concordância com o acima exposto.",
            conteudoFont
        )
        p.setLeading(1f, 1f)
        p.alignment = Element.ALIGN_JUSTIFIED
        document.add(p)
        adicionarLinhaVazia(document, 1)
    }

    @Throws(DocumentException::class, IOException::class)
    private fun adicionarVisitantesCadastrados(
        document: Document,
        nome: String,
        cpf: String,
        pathUsuarioFoto: String,
        pathUsuarioAss: String
    ) {
        var p = Paragraph("Nome: $nome", conteudoFontBold)
        p.setLeading(1f, 1f)
        p.alignment = Element.ALIGN_LEFT
        document.add(p)
        p = Paragraph("CPF: $cpf", conteudoFontBold)
        document.add(p)
        p = Paragraph("Foto: ", conteudoFontBold)
        document.add(p)
        var figura = Image.getInstance(pathUsuarioFoto)
        figura.scalePercent(15f, 15f)
        var chunk: Chunk = Chunk(figura, 0f, 0f, true)
        p = Paragraph(chunk)
        p.alignment = Element.ALIGN_CENTER
        document.add(p)
        adicionarLinhaVazia(document, 2)
        figura = Image.getInstance(pathUsuarioAss)
        figura.scalePercent(15f, 15f)
        chunk = Chunk(figura, 0f, 0f, true)
        p = Paragraph(chunk)
        p.alignment = Element.ALIGN_CENTER
        document.add(p)
        p = Paragraph(
            "_______________________________________________________________",
            conteudoFontBold
        )
        p.alignment = Element.ALIGN_CENTER
        document.add(p)
        p = Paragraph("Assinatura.", conteudoFontBold)
        p.alignment = Element.ALIGN_CENTER
        document.add(p)
        adicionarLinhaVazia(document, 1)
    }

    @Throws(DocumentException::class)
    private fun adicionarRodape(document: Document) {
        val p = Paragraph(
            "Uberlândia, ${LocalDateTime.now().formatData("dd/MM/y HH:mm:ss")}",
            conteudoFontBold
        )
        p.alignment = Element.ALIGN_CENTER
        document.add(p)
    }

    @Throws(DocumentException::class)
    private fun adicionarLinhaVazia(document: Document, number: Int) {
        for (i in 0 until number) document.add(Paragraph(" "))
    }
}