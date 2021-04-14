package br.com.arcom.signpad.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.util.IntentParameterUtils;
import br.com.arcom.signpad.util.UtilDate;
import br.com.arcom.signpad.util.UtilPhoto;

public class AssinaturaUsuarioActivity extends AppCompatActivity {

    private static String pathUsuarioFoto;
    private SignatureView mAssinaturaUsuario;
    private String mUsuarioNomeCom;
    private String mUsuarioCpf;
    private String pathUsuarioAss;
    private String titlePdf;
    private String usuarioFotoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assinatura_usuario_activity);
        recuperarParametros();
    }

    public void recuperarParametros() {
        mAssinaturaUsuario = findViewById(R.id.assinaturaUsuario_SignatureView);

        mUsuarioNomeCom = getIntent().getExtras().getString(IntentParameterUtils.USUARIO_NOME_COMPLETO);
        mUsuarioCpf = getIntent().getExtras().getString(IntentParameterUtils.USUARIO_CPF);
        usuarioFotoName = getIntent().getExtras().getString(IntentParameterUtils.USUARIO_FOTO_NAME);
        pathUsuarioFoto = getIntent().getExtras().getString(IntentParameterUtils.USUARIO_FOTO_PATH);
    }

    public void toActivtyAnterior(View view) {
        onBackPressed();
    }

    public void limparAssinatura(View view) {
        mAssinaturaUsuario.clearCanvas();
    }

    public void salvarDados(View view) {
        if (!validarAssinatura()) return;

        String imagemName = mUsuarioNomeCom.trim() + "-" + mUsuarioCpf.trim() + "-ASSINATURAUSUARIO";
        Bitmap bitmap = mAssinaturaUsuario.getSignatureBitmap();
        pathUsuarioAss = saveImage(bitmap, imagemName);
        Bitmap bitmapUsuarioFoto = BitmapFactory.decodeFile(pathUsuarioFoto);

        File photoFile = UtilPhoto.createPhotoFile(usuarioFotoName, AssinaturaUsuarioActivity.this);
        Uri photoUri = FileProvider.getUriForFile(AssinaturaUsuarioActivity.this, "br.com.arcom.signpad.fileprovider", photoFile);
        bitmapUsuarioFoto = UtilPhoto.rotateBitmap(AssinaturaUsuarioActivity.this, photoUri, bitmapUsuarioFoto, pathUsuarioFoto);

        pathUsuarioFoto = saveImage(bitmapUsuarioFoto, usuarioFotoName);
        titlePdf = mUsuarioNomeCom.trim() + "-" + mUsuarioCpf.trim();
        String pdfPath = criarPdf(pathUsuarioFoto, pathUsuarioAss, titlePdf);
        toNextActivity();
    }

    public String saveImage(Bitmap myBitmap, String imagemName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File wallpaperDirectory = new File(String.valueOf(ContextCompat.getExternalFilesDirs(getApplicationContext(), null)[0]));
        if (!wallpaperDirectory.exists()) wallpaperDirectory.mkdirs();

        try {
            File f = new File(wallpaperDirectory, imagemName + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(AssinaturaUsuarioActivity.this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public String criarPdf(String pathUsuarioFoto, String pathUsuarioAss, String titlePdf) {
        File pdfDirectory = new File(String.valueOf(ContextCompat.getExternalFilesDirs(getApplicationContext(), null)[0]));
        if (!pdfDirectory.exists()) pdfDirectory.mkdirs();

        Document document = new Document();
        try {
            File f = new File(pdfDirectory, titlePdf + ".pdf");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            PdfWriter.getInstance(document, fo);
            document.open();

            // Inserir titulo
            Paragraph p = new Paragraph("TERMO DE CONSENTIMENTO DE COLETA DE DADOS",
                    FontFactory.getFont(FontFactory.defaultEncoding, 16, Font.BOLD, BaseColor.BLACK));
            p.setLeading(1, 1);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            // Inserir espaço
            p = new Paragraph(" ");
            document.add(p);

            // Inserir texto informativo 1
            p = new Paragraph("Considerando o interesse expresso do signatário em adentrar nas dependências da empresa Arcom S/A, este termo" +
                    " tem por finalidade registrar a manifestação livre, informada e inequívoca pela qual o titular concorda com a utilização de" +
                    " seus dados pessoais abaixo identificados para finalidades específicas, nos moldes da Lei 13.709 de 14 de agosto de 2018 -" +
                    " Lei Geral de Proteção de Dados Pessoais (LGPD).",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.NORMAL, BaseColor.BLACK));
            p.setLeading(1, 1);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(p);

            // Inserir espaço
            p = new Paragraph(" ");
            document.add(p);

            // Inserir texto informativo 1
            p = new Paragraph("Com a assinatura, o titular autoriza a empresa Arcom S/A, inscrita no CNPJ sob o n° 25.769.266/0001-24 a" +
                    " registrar o acesso do signatário na portaria da empresa, em cumprimento às medidas de segurança interna" +
                    " adotadas pela sociedade empresária.",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.NORMAL, BaseColor.BLACK));
            p.setLeading(1, 1);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(p);

            // Inserir espaço
            p = new Paragraph(" ");
            document.add(p);

            // Inserir texto informativo 1
            p = new Paragraph("Estes dados não serão compartilhados com outras empresas, salvo por expressa determinação legal, permanecendo" +
                    " arquivados no banco de dados da Arcom S/A, seguindo todos os protocolos de segurança da informação necessários para " +
                    "a garantia de não violação destes elementos.",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.NORMAL, BaseColor.BLACK));
            p.setLeading(1, 1);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(p);

            // Inserir espaço
            p = new Paragraph(" ");
            document.add(p);

            // Inserir texto informativo 1
            p = new Paragraph("Com isso, a identificação abaixo, por meio de foto e assinatura pessoal, expressa a irrestrita" +
                    " concordância com o acima exposto.",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.NORMAL, BaseColor.BLACK));
            p.setLeading(1, 1);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(p);

            // Inserir espaço
            p = new Paragraph(" ");
            document.add(p);

            // Inserir dados fornecidos pelo usuario
            p.setLeading(1, 1);
            p.setAlignment(Element.ALIGN_LEFT);
            p = new Paragraph("Nome: " + mUsuarioNomeCom,
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            document.add(p);
            p = new Paragraph("CPF: " + mUsuarioCpf,
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            document.add(p);
            p = new Paragraph("Foto: ",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            document.add(p);

            Image figura = Image.getInstance(pathUsuarioFoto);
            figura.scalePercent(5, 5);
            Chunk chunk = new Chunk(figura, 0, 0, true);
            p = new Paragraph(chunk);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);
            p = new Paragraph(" ");
            document.add(p);
            p = new Paragraph(" ");
            document.add(p);
            p = new Paragraph(" ");
            document.add(p);
            p = new Paragraph(" ");
            document.add(p);

            figura = Image.getInstance(pathUsuarioAss);
            figura.scalePercent(10, 10);
            chunk = new Chunk(figura, 0, 0, true);
            p = new Paragraph(chunk);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            p = new Paragraph("_______________________________________________________________",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            p = new Paragraph("Assinatura.",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Uberlândia, " + UtilDate.buscarDataAtual(),
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            document.close();
            fo.close();
            return f.getAbsolutePath();
        } catch (DocumentException | IOException de) {
            de.printStackTrace();
        }
        return "";
    }

    public boolean validarAssinatura() {
        if (mAssinaturaUsuario.isBitmapEmpty()) {
            Toast.makeText(this, "A assinatura não pode estar vazia", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void toNextActivity() {
        Intent intent = new Intent(AssinaturaUsuarioActivity.this, ConclusaoActivity.class);
        startActivity(intent);
        finish();
    }

}