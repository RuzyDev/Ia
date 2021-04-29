package br.com.arcom.signpad.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.activity.BuscarCadastradosActivity;

public class CustomDialogSenha {
    public static void showDialog(Context context, String senha) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_senha);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> {
            TextInputLayout tiSenha = dialog.findViewById(R.id.text_input_senha);
            if (tiSenha.getEditText().getText().toString().equals(senha)) {
                Intent intent = new Intent(context, BuscarCadastradosActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            } else {
                tiSenha.setError("Senha inválida!!");
            }
        });
        Button btnCancelar = dialog.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
