package br.com.arcom.signpad.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.util.IntentParameterUtils;
import br.com.arcom.signpad.util.UtilDate;

public class AssinaturaUsuarioActivity extends AppCompatActivity {

    private SignatureView mAssinaturaUsuario;
    private String mUsuarioNomeCom;
    private String mUsuarioCpf;
    private String pathUsuarioFoto;
    private String pathUsuarioAss;
    private String titlePdf;

    private String usuarioFotoName;
    private Bitmap bitmapUsuarioFoto;

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
        bitmapUsuarioFoto = (Bitmap) getIntent().getExtras().getParcelable(IntentParameterUtils.USUARIO_FOTO_BITMAP);
    }

    public void toActivtyAnterior(View view) {
        onBackPressed();
    }

    public void limparAssinatura(View view) {
        mAssinaturaUsuario.clearCanvas();
    }

    public void salvarDados(View view) {
        String imagemName = mUsuarioNomeCom.trim() + "-" + mUsuarioCpf.trim() + "-ASSINATURAUSUARIO";
        Bitmap bitmap = mAssinaturaUsuario.getSignatureBitmap();
        pathUsuarioAss = saveImage(bitmap, imagemName);
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
            p = new Paragraph("Este termo tem por finalidade registrar a manifestação livre, informada e inequívoca pela qual o " +
                    "titular concorda com o termo de seus dados pessoais para finalidades especificas em conformidade com a Lei Nº 13.709 - " +
                    "Lei Geral de proteção de dados Pessoais (LGPD). Ao aceitar o presente termo, manifesta-se o consentimento de que " +
                    "a empresa Arcom S/A, inscrita no CNPJ sob o N° 25.769.266/0001-24, determina os seguintes dados pessoais do visitante:",
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
            p = new Paragraph("    Nome: " + mUsuarioNomeCom,
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            document.add(p);
            p = new Paragraph("    CPF: " + mUsuarioCpf,
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            document.add(p);
            p = new Paragraph("    Foto: ",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD, BaseColor.BLACK));
            document.add(p);

            Image figura = Image.getInstance(pathUsuarioFoto);
            figura.scalePercent(70, 70);
            Chunk chunk = new Chunk(figura, 0, 0, true);
            p = new Paragraph(chunk);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Estes dados tem por finalidade única o registro de acesso do indivíduo na portaria da empresa, colaborando " +
                    "nas medidas de segurança interna adotadas por esta sociedade de empresa os dados não serão compartilhados com outras empresas, " +
                    "permanecendo arquivos no banco de dados da Arcom S/A seguindo os protocolos de segurança necessários para a garantia de não " +
                    "violação das informações existentes.  Assim, pela ciência dos Termos deste documento, a assinatura representa a concordância " +
                    "com informações prestadas.",
                    FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.NORMAL, BaseColor.BLACK));
            p.setLeading(1, 1);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
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

    public void toNextActivity() {
        Intent intent = new Intent(AssinaturaUsuarioActivity.this, ConclusaoActivity.class);
        startActivity(intent);
        finish();
    }

}