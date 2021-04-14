package br.com.arcom.signpad.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

import br.com.arcom.signpad.R;

public class SignPadActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 1240;

    private String[] appPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signpad_activity);
        vereficarPermissoes();
        recuperarParametros();
    }

    public void recuperarParametros() {
        gerarTextoTermoConsentimento();
    }

    public void gerarTextoTermoConsentimento() {
        WebView view = new WebView(this);
        view.setVerticalScrollBarEnabled(false);
        ((ConstraintLayout) findViewById(R.id.constraint_text_signpad_activity)).addView(view);
        view.loadData(getString(R.string.texto_info_termo), "text/html; charset=utf-8", "utf-8");
    }

    public void toNextActivity(View view) {
        Intent intent = new Intent(SignPadActivity.this, DadosUsuarioActivity.class);
        startActivity(intent);
    }

    public void vereficarPermissoes(){
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions){
            if (PermissionChecker.checkSelfPermission(this,perm) != PermissionChecker.PERMISSION_GRANTED){
                listPermissionsNeeded.add(perm);
            }
        }

        if (!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSIONS_REQUEST_CODE
            );
        }
    }

}