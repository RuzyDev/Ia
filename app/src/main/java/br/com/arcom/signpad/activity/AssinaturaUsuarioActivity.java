package br.com.arcom.signpad.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.kyanogen.signatureview.SignatureView;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.models.PdfTermoCompromisso;
import br.com.arcom.signpad.models.SigaResponse;
import br.com.arcom.signpad.services.LgpdVisitanteService;
import br.com.arcom.signpad.utilities.CustomDialogAviso;
import br.com.arcom.signpad.utilities.IntentParameters;
import br.com.arcom.signpad.utilities.UtilFile;
import br.com.arcom.signpad.utilities.UtilImage;
import br.com.arcom.signpad.utilities.UtilString;

public class AssinaturaUsuarioActivity extends AppCompatActivity {

    private static String pathUsuarioFoto;
    private static String pathUsuarioFotoTemp;
    private String mUsuarioNomeCom;
    private Long mUsuarioCpf;
    private String pathUsuarioAss;
    private String titlePdf;
    private String usuarioFotoName;
    private String pathPdf;

    private SignatureView mAssinaturaUsuario;
    private LinearLayout mAssConteudo;
    private LinearLayout mAssLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assinatura_usuario_activity);
        recuperarParametros();
    }

    public void recuperarParametros() {
        mAssinaturaUsuario = findViewById(R.id.assinaturaUsuario_SignatureView);
        mAssConteudo = findViewById(R.id.view_ass_conteudo);
        mAssLoading = findViewById(R.id.view_ass_progressBar);

        mUsuarioNomeCom = getIntent().getExtras().getString(IntentParameters.USUARIO_NOME_COMPLETO);
        mUsuarioCpf = getIntent().getExtras().getLong(IntentParameters.USUARIO_CPF);
        usuarioFotoName = getIntent().getExtras().getString(IntentParameters.USUARIO_FOTO_NAME);
        pathUsuarioFotoTemp = getIntent().getExtras().getString(IntentParameters.USUARIO_FOTO_PATH_TEMP);
    }

    public void toActivtyAnterior(View view) {
        onBackPressed();
    }

    public void toNextActivity(String pathPdf) {
        Intent intent = new Intent(AssinaturaUsuarioActivity.this, PdfActivity.class);
        intent.putExtra(IntentParameters.USUARIO_PDF_PATH, pathPdf);
        startActivity(intent);
        finish();
    }

    public boolean validarAssinatura() {
        if (mAssinaturaUsuario.isBitmapEmpty()) {
            Toast.makeText(this, "A assinatura não pode estar vazia!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void limparAssinatura(View view) {
        mAssinaturaUsuario.clearCanvas();
    }

    public String gerarPdf(Date dataAss) {
        // Salva imagem da assinatura

        String cpfVisitante = UtilString.formatarCpf(mUsuarioCpf.toString());
        String imagemName = mUsuarioNomeCom.trim() + "-" + cpfVisitante + "-ASSINATURAUSUARIO";
        Bitmap bitmap = mAssinaturaUsuario.getSignatureBitmap();
        pathUsuarioAss = UtilImage.saveImage(bitmap, imagemName, AssinaturaUsuarioActivity.this);

        // Rotacionar imagem temporaria
        Bitmap bitmapUsuarioFoto = BitmapFactory.decodeFile(pathUsuarioFotoTemp);
        File photoFileTemp = UtilFile.getFile(pathUsuarioFotoTemp);
        Uri photoUri = FileProvider.getUriForFile(AssinaturaUsuarioActivity.this, "br.com.arcom.signpad.fileprovider", photoFileTemp);
        bitmapUsuarioFoto = UtilImage.rotateBitmap(AssinaturaUsuarioActivity.this, photoUri, bitmapUsuarioFoto, pathUsuarioFotoTemp);

        // Salvar imagem temporaria
        bitmapUsuarioFoto = UtilImage.imageBitmapResize(bitmapUsuarioFoto, 480, 640);
        pathUsuarioFoto = UtilImage.saveImage(bitmapUsuarioFoto, usuarioFotoName, AssinaturaUsuarioActivity.this);

        // Criar Pdf
        titlePdf = mUsuarioNomeCom.trim() + "-" + cpfVisitante;
        return PdfTermoCompromisso.criarPdf(AssinaturaUsuarioActivity.this, mUsuarioNomeCom, cpfVisitante, pathUsuarioFoto, pathUsuarioAss, titlePdf, dataAss);
    }

    public void showLoading(Boolean value) {
        if (value) {
            mAssConteudo.setVisibility(View.GONE);
            mAssLoading.setVisibility(View.VISIBLE);
        } else {
            mAssConteudo.setVisibility(View.VISIBLE);
            mAssLoading.setVisibility(View.GONE);
        }
    }

    public void concluir(View view) {
        if (!validarAssinatura()) return;
        showLoading(true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> handler.post(() -> {
            ExecutorService threadpool = Executors.newCachedThreadPool();
            Future<SigaResponse> futureTask = threadpool.submit(this::salvarDados);
            threadpool.shutdown();

            try {
                SigaResponse result = futureTask.get();
                if (!result.getErro()) {
//                    CustomDialogAviso.showDialog(AssinaturaUsuarioActivity.this, result.getMsg());
//                    Log.d(Constantes.TAG_LOG_SIGNPAD, result.getMsg());
                    toNextActivity(pathPdf);
                } else {
                    CustomDialogAviso.showDialog(AssinaturaUsuarioActivity.this, result.getMsg());
                }
            } catch (ExecutionException | InterruptedException e) {
                CustomDialogAviso.showDialog(AssinaturaUsuarioActivity.this, e.getMessage());
            } finally {
                showLoading(false);
            }
        }));
        executor.shutdown();
    }

    private SigaResponse salvarDados() {
        Date dataAss = new Date();
        pathPdf = gerarPdf(dataAss);
        deletarDadosUsuario();
        return LgpdVisitanteService.salvarUsuario(AssinaturaUsuarioActivity.this, pathPdf, mUsuarioNomeCom, mUsuarioCpf, dataAss);
    }

    private void deletarDadosUsuario() {
        File file = new File(pathUsuarioFoto);
        file.delete();
        file = new File(pathUsuarioAss);
        file.delete();
        file = new File(pathUsuarioFotoTemp);
        file.delete();
    }

}