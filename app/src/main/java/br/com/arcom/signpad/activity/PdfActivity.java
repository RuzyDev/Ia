package br.com.arcom.signpad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.models.SigaResponse;
import br.com.arcom.signpad.services.LgpdVisitanteService;
import br.com.arcom.signpad.utilities.Constantes;
import br.com.arcom.signpad.utilities.CustomDialogAviso;
import br.com.arcom.signpad.utilities.CustomDialogSendEmail;
import br.com.arcom.signpad.utilities.IntentParameters;

public class PdfActivity extends AppCompatActivity {

    private PDFView mPdfView;
    private String pdfPath;
    private Long cpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_activity);
        recuperarParametros();
        mostrarPdf();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), SignPadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void recuperarParametros() {
        mPdfView = findViewById(R.id.pdfView);
        pdfPath = getIntent().getExtras().getString(IntentParameters.USUARIO_PDF_PATH);
        cpf = getIntent().getExtras().getLong(IntentParameters.USUARIO_CPF);
    }

    public void toNextActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SignPadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void mostrarPdf() {
        File pdf = new File(pdfPath);
        if (pdf.exists()) {
            mPdfView.fromFile(pdf).load();
        } else {
            CustomDialogAviso.showDialog(PdfActivity.this, "Arquivo PDF não existe");
        }
    }

    public void enviarEmail(View view) {
        CustomDialogSendEmail.showDialog(PdfActivity.this, cpf);
    }

}