package br.com.arcom.signpad.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import br.com.arcom.signpad.util.IntegerParameterUtils;
import br.com.arcom.signpad.util.IntentParameterUtils;
import br.com.arcom.signpad.util.MaskEditUtil;
import br.com.arcom.signpad.util.StringParameterUtils;
import br.com.arcom.signpad.util.UtilImage;
import br.com.arcom.signpad.util.UtilValidate;

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
//        adicionarMascara();
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
        if (fotoTag.equals("tirarFoto")) {
            textFotoMsgErro.setVisibility(View.VISIBLE);
            return false;
        } else {
            textFotoMsgErro.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private void adicionarMascara() {
        textInputCpf.getEditText().addTextChangedListener(MaskEditUtil.insert(MaskEditUtil.CPF_MASK, textInputCpf.getEditText()));
    }

    public void toActivtyAnterior(View view) {
        deleteImageTemp();
        onBackPressed();
    }

    public void toNextActivity(View view) {
        if (!validarNome() | !validarCpf() | !validarFoto()) return;
        String nomeUsuario = textInputNome.getEditText().getText().toString().trim();
        String cpfUsuario = textInputCpf.getEditText().getText().toString().trim();
        String imagemName = nomeUsuario+"-"+cpfUsuario+"-FOTOUSUARIO";

        Intent intent = new Intent(DadosUsuarioActivity.this, AssinaturaUsuarioActivity.class);
        intent.putExtra(IntentParameterUtils.USUARIO_NOME_COMPLETO, nomeUsuario);
        intent.putExtra(IntentParameterUtils.USUARIO_CPF, cpfUsuario);
        intent.putExtra(IntentParameterUtils.USUARIO_FOTO_NAME, imagemName);
        intent.putExtra(IntentParameterUtils.USUARIO_FOTO_PATH_TEMP, pathToUserPhotoTemp);
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
                startActivityForResult(cameraIntent, IntegerParameterUtils.CAM_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntegerParameterUtils.CAM_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmapUsuarioFoto = BitmapFactory.decodeFile(pathToUserPhotoTemp);
            bitmapUsuarioFoto = UtilImage.rotateBitmap(DadosUsuarioActivity.this, photoUri, bitmapUsuarioFoto, pathToUserPhotoTemp);

            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmapUsuarioFoto);
            roundedBitmapDrawable.setCornerRadius(10.0f);
            roundedBitmapDrawable.setAntiAlias(true);

            mUsuarioImagem.setImageDrawable(roundedBitmapDrawable);
            mUsuarioImagem.setTag("usuarioFoto");
            textFotoMsgErro.setVisibility(View.INVISIBLE);
        }

        if (requestCode == IntegerParameterUtils.CAM_REQUEST && resultCode == Activity.RESULT_CANCELED && data == null) {
            deleteImageTemp();
        }
    }

    public void deleteImageTemp() {
        File file = new File(pathToUserPhotoTemp);
        if (file.delete()) Log.d(StringParameterUtils.TAG_LOG_SIGNPAD, "Arquivo de 0 bytes deletado!");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteImageTemp();
    }
}