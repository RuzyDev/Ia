package br.com.arcom.signpad.services;

import android.content.Context;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.arcom.signpad.api.SigaService;
import br.com.arcom.signpad.dao.AppDataBase;
import br.com.arcom.signpad.entities.SigaToken;
import br.com.arcom.signpad.entities.Usuario;
import br.com.arcom.signpad.model.Response;
import br.com.arcom.signpad.model.ResponseRequest;

public class UsuarioService {

    // Room
    private static AppDataBase appDataBase;
    private static String sigaToken;

    public static Response salvarUsuario(Context context, String pathPdf, String mUsuarioNomeCom, String mUsuarioCpf, Date dataPreechimento) {
//        Boolean tokenStatus = getToken(context);
//
//        if (tokenStatus) {
//            Optional<ResponseRequest> responseRequest = SigaService.salvarDados(context, sigaToken, pathPdf, mUsuarioNomeCom, mUsuarioCpf, dataPreechimento);
//            if (responseRequest.get().getStatusCode() == 201) return new Response(false, "Dados salvos com sucesso!");
//        }
//
//        Usuario usuario = new Usuario();
//        usuario.setNome(mUsuarioNomeCom);
//        usuario.setCpf(mUsuarioCpf);
//        usuario.setDataPreenchimento(dataPreechimento);
//        appDataBase.usuarioDAO().insert(usuario);
        return new Response(false, "Servidor fora do ar, dados salvos localmente!");
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

    private static Boolean getToken(Context context) {
        appDataBase = AppDataBase.getInstance(context);
        SigaToken possivelToken = appDataBase.sigaTokenDAO().getById(1);

        if (possivelToken != null) {
            sigaToken = possivelToken.getToken();
            return true;
        } else {
            ResponseRequest responseRequest = SigaService.buscarToken(context);
            if (responseRequest.getStatusCode() == 200) {
                appDataBase.sigaTokenDAO().update(possivelToken.getId(), responseRequest.getDados(), new Date());
                sigaToken = responseRequest.getDados();
                return true;
            } else {
                return false;
            }
        }
    }

}
