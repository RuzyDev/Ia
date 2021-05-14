package br.com.arcom.signpad.utilities;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import br.com.arcom.signpad.R;

public class CustomDialogCheck {
    public static void showDialog(Context context, String msg) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_check);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());
        TextView textMsg = dialog.findViewById(R.id.textView_msg_check);
        textMsg.setText(msg);
        dialog.show();
    }
}
