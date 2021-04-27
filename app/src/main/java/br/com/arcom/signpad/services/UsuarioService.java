package br.com.arcom.signpad.services;

import android.content.Context;

import java.io.File;
import java.util.Date;
import java.util.List;

import br.com.arcom.signpad.api.SigaRepository;
import br.com.arcom.signpad.data.AppDataBase;
import br.com.arcom.signpad.data.SigaToken;
import br.com.arcom.signpad.data.Usuario;
import br.com.arcom.signpad.models.SigaResponse;
import br.com.arcom.signpad.utilities.UtilDate;

public class UsuarioService {

    private static AppDataBase appDataBase;

    public static SigaResponse salvarUsuario(Context context, String pathPdf, String mUsuarioNomeCom, Long mUsuarioCpf, Date dataPreechimento) {
        SigaResponse sigaResponse = getSigaToken(context);

        if (!sigaResponse.getErro()) {
            List<Usuario> usuarioList = appDataBase.usuarioDAO().getAll();
            if (usuarioList.size() > 0) {
                for (Usuario usuario : usuarioList) {
                    SigaResponse response = salvarDados(sigaResponse.getMsg(), usuario.getPathPdf(), usuario.getNome(), usuario.getCpf(), usuario.getDataPreenchimento());
                    if (!response.getErro()) {
                        File file = new File(usuario.getPathPdf());
                        file.delete();
                        appDataBase.usuarioDAO().delete(usuario);
                    } else {
                        salvarDadosLocalmente(mUsuarioNomeCom, mUsuarioCpf, dataPreechimento, pathPdf);
                        return new SigaResponse(false, sigaResponse.getMsg());
                    }
                }
            }

            SigaResponse response = salvarDados(sigaResponse.getMsg(), pathPdf, mUsuarioNomeCom, mUsuarioCpf, dataPreechimento);
            if (sigaResponse.getErro()) {
                salvarDadosLocalmente(mUsuarioNomeCom, mUsuarioCpf, dataPreechimento, pathPdf);
                return new SigaResponse(false, sigaResponse.getMsg());
            } else {
                File file = new File(pathPdf);
                file.delete();
            }
            return response;
        }

        salvarDadosLocalmente(mUsuarioNomeCom, mUsuarioCpf, dataPreechimento, pathPdf);
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

    private static void salvarDadosLocalmente(String nome, Long cpf, Date dataPreechimento, String pathPdf) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setDataPreenchimento(dataPreechimento);
        usuario.setPathPdf(pathPdf);
        appDataBase.usuarioDAO().insert(usuario);
    }

}
