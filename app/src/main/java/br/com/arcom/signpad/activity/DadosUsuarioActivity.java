package br.com.arcom.signpad.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.utilities.IntegerParameter;
import br.com.arcom.signpad.utilities.IntentParameter;
import br.com.arcom.signpad.utilities.UtilFile;
import br.com.arcom.signpad.utilities.UtilImage;
import br.com.arcom.signpad.utilities.UtilValidate;

public class DadosUsuarioActivity extends AppCompatActivity {

    private static String pathToUserPhotoTemp;
    private TextInputLayout textInputNome;
    private TextInputLayout textInputCpf;
    private TextView textFotoMsgErro;
    private ImageView mUsuarioImagem;
    private Bitmap bitmapUsuarioFoto;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dados_usuario_activity);
        recuperarParametros();
    }

    public void recuperarParametros() {
        mUsuarioImagem = findViewById(R.id.usuario_imagem);
        textInputNome = findViewById(R.id.text_input_nameUser);
        textInputCpf = findViewById(R.id.text_input_cpfUser);
        textFotoMsgErro = findViewById(R.id.text_foto_msg_erro);
    }

    public boolean validarNome() {
        String nomeUsuario = textInputNome.getEditText().getText().toString().trim();
        if (nomeUsuario.isEmpty()) {
            textInputNome.setError("O nome não pode estar vazio!");
            return false;
        } else if (nomeUsuario.length() < 2) {
            textInputNome.setError("Por favor, preencha o NOME corretamente!");
            return false;
        } else {
            textInputNome.setError(null);
            return true;
        }
    }

    public boolean validarCpf() {
        String cpfUsuario = textInputCpf.getEditText().getText().toString().trim();
        if (cpfUsuario.isEmpty()) {
            textInputCpf.setError("O cpf não pode estar vazio!");
            return false;
        } else if (cpfUsuario.length() < 11) {
            textInputCpf.setError("Por favor, preencha o CPF corretamente!");
            return false;
        } else if (!UtilValidate.isCPF(cpfUsuario)) {
            textInputCpf.setError("CPF inválido!");
            return false;
        } else {
            textInputCpf.setError(null);
            return true;
        }
    }

    public boolean validarFoto() {
        String fotoTag = mUsuarioImagem.getTag().toString().trim();
        if (fotoTag.equals("tirarFoto") || UtilFile.countKBytes(pathToUserPhotoTemp) == 0) {
            textFotoMsgErro.setVisibility(View.VISIBLE);
            return false;
        } else {
            textFotoMsgErro.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    public void toActivtyAnterior(View view) {
        UtilFile.deleteFile(pathToUserPhotoTemp);
        onBackPressed();
    }

    public void toNextActivity(View view) {
        if (!validarNome() | !validarCpf() | !validarFoto()) return;
        String nomeUsuario = textInputNome.getEditText().getText().toString().trim();
        Long cpfUsuario = Long.valueOf(textInputCpf.getEditText().getText().toString().trim());
        String imagemName = nomeUsuario+"-"+cpfUsuario+"-FOTOUSUARIO";

        Intent intent = new Intent(DadosUsuarioActivity.this, AssinaturaUsuarioActivity.class);
        intent.putExtra(IntentParameter.USUARIO_NOME_COMPLETO, nomeUsuario);
        intent.putExtra(IntentParameter.USUARIO_CPF, cpfUsuario);
        intent.putExtra(IntentParameter.USUARIO_FOTO_NAME, imagemName);
        intent.putExtra(IntentParameter.USUARIO_FOTO_PATH_TEMP, pathToUserPhotoTemp);
        startActivity(intent);
    }

    public void tirarFoto(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = UtilImage.createPhotoFile("photoTemp", DadosUsuarioActivity.this, "/Pictures");
            if (photoFile != null) {
                pathToUserPhotoTemp = photoFile.getAbsolutePath();
                photoUri = FileProvider.getUriForFile(DadosUsuarioActivity.this, "br.com.arcom.signpad.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, IntegerParameter.CAM_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntegerParameter.CAM_REQUEST && resultCode == Activity.RESULT_OK) {

            if (UtilFile.countKBytes(pathToUserPhotoTemp) == 0) {
                UtilFile.deleteFile(pathToUserPhotoTemp);
                defaultUsuarioImagem();
            }

            bitmapUsuarioFoto = BitmapFactory.decodeFile(pathToUserPhotoTemp);
            bitmapUsuarioFoto = UtilImage.rotateBitmap(DadosUsuarioActivity.this, photoUri, bitmapUsuarioFoto, pathToUserPhotoTemp);

            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmapUsuarioFoto);
            roundedBitmapDrawable.setCornerRadius(10.0f);
            roundedBitmapDrawable.setAntiAlias(true);

            mUsuarioImagem.setImageDrawable(roundedBitmapDrawable);
            mUsuarioImagem.setTag("usuarioFoto");
            textFotoMsgErro.setVisibility(View.INVISIBLE);
        }

        if (requestCode == IntegerParameter.CAM_REQUEST && resultCode == Activity.RESULT_CANCELED && data == null) {
            UtilFile.deleteFile(pathToUserPhotoTemp);
            defaultUsuarioImagem();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (pathToUserPhotoTemp != null) UtilFile.deleteFile(pathToUserPhotoTemp);
    }

    public void defaultUsuarioImagem() {
        mUsuarioImagem.setImageResource(R.drawable.ic_usuario_foto2);
        mUsuarioImagem.setTag("tirarFoto");
        textFotoMsgErro.setVisibility(View.VISIBLE);
    }

}