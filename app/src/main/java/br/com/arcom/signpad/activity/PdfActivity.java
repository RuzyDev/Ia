package br.com.arcom.signpad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.utilities.CustomDialogAviso;
import br.com.arcom.signpad.utilities.IntentParameters;

public class PdfActivity extends AppCompatActivity {

    private PDFView mPdfView;
    private String pdfPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_activity);
        recuperarParametros();

        File pdf = new File(pdfPath);
        if (pdf.exists()) {
            mPdfView.fromFile(pdf).load();
        } else {
            CustomDialogAviso.showDialog(PdfActivity.this, "pdf não existe");
        }
    }

    public void recuperarParametros() {
        mPdfView = findViewById(R.id.pdfView);
        pdfPath = getIntent().getExtras().getString(IntentParameters.USUARIO_PDF_PATH);
    }

    public void toNextActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SignPadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}