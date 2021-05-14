package br.com.arcom.signpad.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.itextpdf.text.pdf.parser.Line;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.activity.AssinaturaUsuarioActivity;
import br.com.arcom.signpad.activity.BuscarCadastradosActivity;
import br.com.arcom.signpad.activity.PdfActivity;
import br.com.arcom.signpad.data.AppDataBase;
import br.com.arcom.signpad.models.SigaResponse;
import br.com.arcom.signpad.services.LgpdVisitanteService;

public class CustomDialogSendEmail {

    private static Dialog dialog;
    private static LinearLayout mConteudo;
    private static LinearLayout mLoading;
    private static TextInputLayout textInputEmail;
    private static Button btnOk;
    private static Button btnCancelar;

    public static void showDialog(Context context, Long cpf) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_send_email);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        recuperarParametros();
        showLoading(false);

        btnOk.setOnClickListener(v ->  {
            String email = textInputEmail.getEditText().getText().toString().trim();
            sendEmail(context, email, cpf);
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public static void sendEmail(Context context, String email, Long cpf) {
        if (!validarEmail(email)) return;
        showLoading(true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> handler.post(() -> {
            ExecutorService threadpool = Executors.newCachedThreadPool();
            Future<SigaResponse> futureTask = threadpool.submit(() -> LgpdVisitanteService.enviarPdfPorEmail(context, email, cpf));
            threadpool.shutdown();

            try {
                SigaResponse result = futureTask.get();

                if (result.getErro()) {
                    showLoading(false);
                    CustomDialogAviso.showDialog(context, "Servidor fora do ar, o termo será enviado quando voltar!!");
                } else {
                    showLoading(false);

                    CustomDialogCheck.showDialog(context, "Enviado com sucesso!!");
                }
                dialog.dismiss();
            } catch (ExecutionException | InterruptedException e) {
                CustomDialogAviso.showDialog(context, e.getMessage());
            } finally {
                showLoading(false);
            }
        }));
        executor.shutdown();
    }

    public static void recuperarParametros() {
        mConteudo = dialog.findViewById(R.id.view_conteduo);
        mLoading = dialog.findViewById(R.id.view_progressBar);
        btnOk = dialog.findViewById(R.id.btn_enviar);
        textInputEmail = dialog.findViewById(R.id.text_input_email);
        btnCancelar = dialog.findViewById(R.id.btn_cancelar);
    }

    public static boolean validarEmail(String email) {
        if (email.isEmpty()) {
            textInputEmail.setError("E-mail obrigátorio!!");
            return false;
        }
        return true;
    }

    public static void showLoading(Boolean value) {
        if (value) {
            mConteudo.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
        } else {
            mConteudo.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
        }
    }

}

