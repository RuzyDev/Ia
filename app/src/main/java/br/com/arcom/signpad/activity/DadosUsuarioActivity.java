package br.com.arcom.signpad.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.util.IntegerParameterUtils;
import br.com.arcom.signpad.util.IntentParameterUtils;
import br.com.arcom.signpad.util.MaskEditUtil;

public class DadosUsuarioActivity extends AppCompatActivity {

    private ImageView mUsuarioImagem;
    private EditText mUsuarioNomeCom;
    private EditText mUsuarioCpf;
    private Bitmap bitmapUsuarioFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dados_usuario_activity);
        recuperarParametros();
        adicionarMascara();
    }

    public void recuperarParametros() {
        mUsuarioImagem = findViewById(R.id.usuario_imagem);
        mUsuarioNomeCom = findViewById(R.id.editText_usuario_nomeCom);
        mUsuarioCpf = findViewById(R.id.editText_usuario_cpf);
    }

    private void adicionarMascara() {
        mUsuarioCpf.addTextChangedListener(MaskEditUtil.insert(MaskEditUtil.CPF_MASK, mUsuarioCpf));
    }

    public void toActivtyAnterior(View view) {
        onBackPressed();
    }

    public void tirarFoto(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, IntegerParameterUtils.CAM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntegerParameterUtils.CAM_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmapUsuarioFoto = (Bitmap) data.getExtras().get("data");
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmapUsuarioFoto);

            roundedBitmapDrawable.setCornerRadius(10.0f);
            roundedBitmapDrawable.setAntiAlias(true);
            mUsuarioImagem.setImageDrawable(roundedBitmapDrawable);
            mUsuarioImagem.setTag("usuarioFoto");
        }
    }

    public void toNextActivity(View view) {
        String imagemName = mUsuarioNomeCom.getText().toString().trim()+"-"+mUsuarioCpf.getText().toString()+"-FOTOUSUARIO";
        Intent intent = new Intent(DadosUsuarioActivity.this, AssinaturaUsuarioActivity.class);
        intent.putExtra(IntentParameterUtils.USUARIO_NOME_COMPLETO, mUsuarioNomeCom.getText().toString());
        intent.putExtra(IntentParameterUtils.USUARIO_CPF, mUsuarioCpf.getText().toString().trim());
        intent.putExtra(IntentParameterUtils.USUARIO_FOTO_NAME, imagemName);
        intent.putExtra(IntentParameterUtils.USUARIO_FOTO_BITMAP, bitmapUsuarioFoto);
        startActivity(intent);
        finish();
    }

}