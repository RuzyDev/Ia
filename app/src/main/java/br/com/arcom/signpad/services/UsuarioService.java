package br.com.arcom.signpad.services;

import android.content.Context;
import android.util.Log;

import java.util.Date;

import br.com.arcom.signpad.api.SigaRepository;
import br.com.arcom.signpad.data.AppDataBase;
import br.com.arcom.signpad.data.SigaToken;
import br.com.arcom.signpad.data.Usuario;
import br.com.arcom.signpad.model.SigaResponse;
import br.com.arcom.signpad.utilities.Constantes;
import br.com.arcom.signpad.utilities.UtilDate;

public class UsuarioService {

    private static AppDataBase appDataBase;

    public static SigaResponse salvarUsuario(Context context, String pathPdf, String mUsuarioNomeCom, Long mUsuarioCpf, Date dataPreechimento) {
        SigaResponse sigaResponse = getSigaToken(context);

        if (!sigaResponse.getErro()) {
            return salvarDados(sigaResponse.getMsg(), pathPdf, mUsuarioNomeCom, mUsuarioCpf, dataPreechimento);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(mUsuarioNomeCom);
        usuario.setCpf(mUsuarioCpf);
        usuario.setDataPreenchimento(dataPreechimento);
        appDataBase.usuarioDAO().insert(usuario);
        return new SigaResponse(false, sigaResponse.getMsg());
    }

    public static SigaResponse getSigaToken(Context context) {
        appDataBase = AppDataBase.getInstance(context);
        SigaToken possivelToken = appDataBase.sigaTokenDAO().getById(1);
        if (possivelToken != null) {
            Date datePlusHours = UtilDate.addHoursToJavaUtilDate(possivelToken.getTokenDate(), 7);
            Date dataAtual = new Date();
            if (datePlusHours.before(dataAtual) || dataAtual.equals(datePlusHours)) return buscarTokenRepository(possivelToken);
            return new SigaResponse(false, possivelToken.getToken());
        } else {
            return buscarTokenRepository(null);
        }
    }

    private static SigaResponse buscarTokenRepository(SigaToken sigaToken) {
        SigaRepository sigaRepository = SigaRepository.getInstance();
        SigaResponse sigaResponse = sigaRepository.buscarToken();
        if (!sigaResponse.getErro()) {
            if (sigaToken != null) {
                appDataBase.sigaTokenDAO().update(sigaToken.getId(), sigaResponse.getMsg(), new Date());
            } else {
                sigaToken = new SigaToken();
                sigaToken.setId(1);
                sigaToken.setToken(sigaResponse.getMsg());
                sigaToken.setTokenDate(new Date());
                appDataBase.sigaTokenDAO().insert(sigaToken);
            }
            return new SigaResponse(false, sigaResponse.getMsg());
        } else {
            return new SigaResponse(true, sigaResponse.getMsg());
        }
    }

    private static SigaResponse salvarDados(String sigaToken, String pathPdf, String mUsuarioNomeCom, Long mUsuarioCpf, Date dataPreechimento) {
        SigaRepository sigaRepository = SigaRepository.getInstance();
        SigaResponse sigaResponse = sigaRepository.salvarDados(sigaToken, pathPdf, mUsuarioNomeCom, mUsuarioCpf, dataPreechimento);
        return (!sigaResponse.getErro()) ? new SigaResponse(false, sigaResponse.getMsg()) : new SigaResponse(true, sigaResponse.getMsg());
    }

}
