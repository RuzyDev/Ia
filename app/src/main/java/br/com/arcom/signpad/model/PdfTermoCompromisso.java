package br.com.arcom.signpad.model;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import br.com.arcom.signpad.utilities.UtilDate;

public class PdfTermoCompromisso {

    private final static Font tituloFont = FontFactory.getFont(FontFactory.defaultEncoding, 16, Font.BOLD, BaseColor.BLACK);
    private final static Font conteudoFont = FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.NORMAL, BaseColor.BLACK);
    private final static Font conteudoFontBold = FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK);

    public static String criarPdf(Context context, String nome, String cpf, String pathUsuarioFoto, String pathUsuarioAss, String titlePdf, Date dataPreechimento) {
        try {
            File pdfDirectory = new File(String.valueOf(ContextCompat.getExternalFilesDirs(context, null)[0]));
            if (!pdfDirectory.exists()) pdfDirectory.mkdirs();
            Document document = new Document();
            File file = new File(pdfDirectory, titlePdf + ".pdf");
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            PdfWriter.getInstance(document, fo);
            document.open();
            adicionarTitulo(document);
            adicionarTextoInfo(document);
            adicionarDadosVisitante(document, nome, cpf, pathUsuarioFoto, pathUsuarioAss);
            adicionarRodape(document, dataPreechimento);
            document.close();
            fo.close();
            return file.getAbsolutePath();
        } catch (DocumentException | IOException | ExceptionInInitializerError de) {
            de.printStackTrace();
        }
        return null;
    }

    private static void adicionarTitulo(Document document) throws DocumentException {
        Paragraph p = new Paragraph("TERMO DE CONSENTIMENTO DE COLETA DE DADOS", tituloFont);
        p.setLeading(1, 1);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        adicionarLinhaVazia(document, 1);
    }

    private static void adicionarTextoInfo(Document document) throws DocumentException {
        Paragraph p = new Paragraph("Considerando o interesse expresso do signatário em adentrar nas dependências da empresa Arcom S/A, este termo" +
                " tem por finalidade registrar a manifestação livre, informada e inequívoca pela qual o titular concorda com a utilização de" +
                " seus dados pessoais abaixo identificados para finalidades específicas, nos moldes da Lei 13.709 de 14 de agosto de 2018 -" +
                " Lei Geral de Proteção de Dados Pessoais (LGPD).",
                conteudoFont);
        p.setLeading(1, 1);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(p);

        adicionarLinhaVazia(document, 1);

        p = new Paragraph("Com a assinatura, o titular autoriza a empresa Arcom S/A, inscrita no CNPJ sob o n° 25.769.266/0001-24 a" +
                " registrar o acesso do signatário na portaria da empresa, em cumprimento às medidas de segurança interna" +
                " adotadas pela sociedade empresária.",
                conteudoFont);
        p.setLeading(1, 1);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(p);

        adicionarLinhaVazia(document, 1);

        p = new Paragraph("Estes dados não serão compartilhados com outras empresas, salvo por expressa determinação legal, permanecendo" +
                " arquivados no banco de dados da Arcom S/A, seguindo todos os protocolos de segurança da informação necessários para " +
                "a garantia de não violação destes elementos.",
                conteudoFont);
        p.setLeading(1, 1);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(p);

        adicionarLinhaVazia(document, 1);

        p = new Paragraph("Com isso, a identificação abaixo, por meio de foto e assinatura pessoal, expressa a irrestrita" +
                " concordância com o acima exposto.",
                conteudoFont);
        p.setLeading(1, 1);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(p);

        adicionarLinhaVazia(document, 1);
    }

    private static void adicionarDadosVisitante(Document document, String nome, String cpf, String pathUsuarioFoto, String pathUsuarioAss) throws DocumentException, IOException {
        Paragraph p = new Paragraph("Nome: " + nome, conteudoFontBold);
        p.setLeading(1, 1);
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);
        p = new Paragraph("CPF: " + cpf, conteudoFontBold);
        document.add(p);
        p = new Paragraph("Foto: ", conteudoFontBold);
        document.add(p);

        Image figura = Image.getInstance(pathUsuarioFoto);
        figura.scalePercent(20, 20);
        Chunk chunk = new Chunk(figura, 0, 0, true);
        p = new Paragraph(chunk);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);

        adicionarLinhaVazia(document, 5);

        figura = Image.getInstance(pathUsuarioAss);
        figura.scalePercent(15, 15);
        chunk = new Chunk(figura, 0, 0, true);
        p = new Paragraph(chunk);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);

        p = new Paragraph("_______________________________________________________________", conteudoFontBold);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        p = new Paragraph("Assinatura.", conteudoFontBold);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);

        adicionarLinhaVazia(document, 1);
    }

    private static void adicionarRodape(Document document, Date dataPreechimento) throws DocumentException {
        Paragraph p = new Paragraph("Uberlândia, " + UtilDate.buscarDataAtual(dataPreechimento, UtilDate.DATE_TIME), conteudoFontBold);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
    }

    private static void adicionarLinhaVazia(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) document.add(new Paragraph(" "));
    }
}