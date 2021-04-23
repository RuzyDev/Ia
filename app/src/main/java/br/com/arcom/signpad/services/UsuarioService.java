package br.com.arcom.signpad.services;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;

import br.com.arcom.signpad.dao.AppDataBase;
import br.com.arcom.signpad.entities.SigaToken;
import br.com.arcom.signpad.entities.Usuario;
import br.com.arcom.signpad.model.Response;
import br.com.arcom.signpad.retrofit.repositories.SigaRepository;
import br.com.arcom.signpad.util.ConstantesUtils;
import br.com.arcom.signpad.util.UtilDate;

public class UsuarioService {

    // Room
    private static AppDataBase appDataBase;
    private static String sigaToken;

    public static Response salvarUsuario(Context context, String pathPdf, String mUsuarioNomeCom, String mUsuarioCpf, Date dataPreechimento) {
        Response response = getSigaToken(context);
        if (!response.getErro()) {
            response = UsuarioService.salvarDados(context, sigaToken, pathPdf, mUsuarioNomeCom, mUsuarioCpf, dataPreechimento);
//            if (responseRequest.get().getStatusCode() == 201)
            return new Response(false, response.getMsg());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(mUsuarioNomeCom);
        usuario.setCpf(mUsuarioCpf);
        usuario.setDataPreenchimento(dataPreechimento);
        appDataBase.usuarioDAO().insert(usuario);
        return new Response(false, response.getMsg());
    }

    public static Response getSigaToken(Context context) {
        appDataBase = AppDataBase.getInstance(context);
        SigaToken possivelToken = appDataBase.sigaTokenDAO().getById(1);
        if (possivelToken != null) {
            Date datePlusHours = UtilDate.addHoursToJavaUtilDate(possivelToken.getTokenDate(), 7);
            Date dataAtual = new Date();
            if (datePlusHours.before(dataAtual) || dataAtual.equals(datePlusHours)) return buscarTokenRepository(possivelToken);

            sigaToken = possivelToken.getToken();
            return new Response(false, sigaToken);
        } else {
            return buscarTokenRepository(null);
        }
    }

    private static Response buscarTokenRepository(SigaToken sigaToken) {
        SigaRepository sigaRepository = SigaRepository.getInstance();
        Response response = sigaRepository.buscarToken();
        if (!response.getErro()) {
            if (sigaToken != null) {
                Log.d(ConstantesUtils.TAG_LOG_SIGNPAD, "token diferente de null");
                appDataBase.sigaTokenDAO().update(sigaToken.getId(), response.getMsg(), new Date());
            } else {
                Log.d(ConstantesUtils.TAG_LOG_SIGNPAD, "token iqual a null");
                sigaToken = new SigaToken();
                sigaToken.setId(1);
                sigaToken.setToken(response.getMsg());
                sigaToken.setTokenDate(new Date());
                appDataBase.sigaTokenDAO().insert(sigaToken);
            }

            return new Response(false, response.getMsg());
        } else {
            return new Response(true, response.getMsg());
        }
    }

    private static Response salvarDados(Context context, String sigaToken, String pathPdf, String mUsuarioNomeCom, String mUsuarioCpf, Date dataPreechimento) {
        return null;
    }

    public static List<Usuario> buscarUsuarios(Context context) {
        appDataBase = AppDataBase.getInstance(context);
        List<Usuario> usuarios = appDataBase.usuarioDAO().getAll();
        appDataBase.close();
        return usuarios;
    }

    public static void excluirUsuarios(Context context, List<Usuario> usuarios) {
        appDataBase = AppDataBase.getInstance(context);
        appDataBase.usuarioDAO().deleteAll(usuarios);
        appDataBase.close();
    }

    public static void atualizarUsuarioLocal(Context context, Usuario usuario) {
        appDataBase = AppDataBase.getInstance(context);
        appDataBase.usuarioDAO().update(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getDataPreenchimento());
        appDataBase.close();
    }

}
