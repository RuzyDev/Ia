package br.com.arcom.signpad.services;

import android.content.Context;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.arcom.signpad.api.SigaRepository;
import br.com.arcom.signpad.data.AppDataBase;
import br.com.arcom.signpad.data.EmailLgpdVisitante;
import br.com.arcom.signpad.data.LgpdVisitante;
import br.com.arcom.signpad.data.SigaToken;
import br.com.arcom.signpad.models.BuscarLgpdVisitanteResponse;
import br.com.arcom.signpad.models.SigaResponse;
import br.com.arcom.signpad.utilities.UtilDate;

public class LgpdVisitanteService {

    private static AppDataBase appDataBase;

    public static SigaResponse salvarUsuario(Context context, String pathPdf, String mUsuarioNomeCom, Long mUsuarioCpf, Date dataAss) {
        SigaResponse sigaResponse = getSigaToken(context);

        if (!sigaResponse.getErro()) {
            List<LgpdVisitante> lgpdVisitantes = appDataBase.lgpdVisitanteDAO().getAll();
            if (lgpdVisitantes.size() > 0) {
                for (LgpdVisitante lgpdVisitante : lgpdVisitantes) {
                    SigaResponse response = salvarDados(sigaResponse.getMsg(), lgpdVisitante.getPathPdf(), lgpdVisitante.getNome(), lgpdVisitante.getCpf(), lgpdVisitante.getDataAss());
                    if (!response.getErro()) {
                        appDataBase.lgpdVisitanteDAO().delete(lgpdVisitante);
                    } else {
                        salvarDadosLocalmente(mUsuarioNomeCom, mUsuarioCpf, dataAss, pathPdf);
                        return new SigaResponse(false, response.getMsg());
                    }
                }
            }

            List<EmailLgpdVisitante> emailLgpdVisitantes = appDataBase.emailLgpdVisitanteDAO().getAll();
            if (emailLgpdVisitantes.size() > 0) {
                for (EmailLgpdVisitante emailLgpdVisitante : emailLgpdVisitantes) {
                    SigaResponse response = enviarPdfPorEmail(context, emailLgpdVisitante.getEmail(), emailLgpdVisitante.getCpf());
                    if (!response.getErro()) {
                        appDataBase.emailLgpdVisitanteDAO().delete(emailLgpdVisitante);
                    } else {
                        return new SigaResponse(false, response.getMsg());
                    }
                }
            }

            SigaResponse response = salvarDados(sigaResponse.getMsg(), pathPdf, mUsuarioNomeCom, mUsuarioCpf, dataAss);
            if (response.getErro()) salvarDadosLocalmente(mUsuarioNomeCom, mUsuarioCpf, dataAss, pathPdf);
            return new SigaResponse(false, response.getMsg());
        }

        salvarDadosLocalmente(mUsuarioNomeCom, mUsuarioCpf, dataAss, pathPdf);
        return new SigaResponse(false, sigaResponse.getMsg());
    }

    public static SigaResponse getSigaToken(Context context) {
        appDataBase = AppDataBase.getInstance(context);
        SigaToken possivelToken = appDataBase.sigaTokenDAO().getById(1);
        if (possivelToken != null) {
            Date datePlusHours = UtilDate.addHoursToJavaUtilDate(possivelToken.getTokenDate(), 7);
            Date dataAtual = new Date();
            if (datePlusHours.before(dataAtual) || dataAtual.equals(datePlusHours))
                return buscarTokenRepository(possivelToken);
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

    private static SigaResponse salvarDados(String sigaToken, String pathPdf, String mUsuarioNomeCom, Long mUsuarioCpf, Date dataAss) {
        SigaRepository sigaRepository = SigaRepository.getInstance();
        SigaResponse sigaResponse = sigaRepository.salvarDadosVisitante(sigaToken, pathPdf, mUsuarioNomeCom, mUsuarioCpf, dataAss);
        return (!sigaResponse.getErro()) ? new SigaResponse(false, sigaResponse.getMsg()) : new SigaResponse(true, sigaResponse.getMsg());
    }

    private static void salvarDadosLocalmente(String nome, Long cpf, Date dataAss, String pathPdf) {
        LgpdVisitante lgpdVisitante = appDataBase.lgpdVisitanteDAO().getById(cpf);
        if (lgpdVisitante != null) {
            if (!pathPdf.equals(lgpdVisitante.getPathPdf())) new File(lgpdVisitante.getPathPdf()).delete();
        } else {
            lgpdVisitante = new LgpdVisitante();
            lgpdVisitante.setCpf(cpf);
        }
        lgpdVisitante.setNome(nome);
        lgpdVisitante.setDataAss(dataAss);
        lgpdVisitante.setPathPdf(pathPdf);
        appDataBase.lgpdVisitanteDAO().insert(lgpdVisitante);
    }

    public static BuscarLgpdVisitanteResponse buscarLpgdVisitante(Context context, String modoPesquisa, String nome, Long cpf) {
        SigaResponse sigaResponse = getSigaToken(context);
        if (!sigaResponse.getErro()) return SigaRepository.getInstance().buscarLpgdVisitante(sigaResponse.getMsg(), modoPesquisa, nome, cpf);
        return new BuscarLgpdVisitanteResponse(true, sigaResponse.getMsg(), Collections.emptyList());
    }

    public static SigaResponse enviarPdfPorEmail(Context context, String email, Long cpf) {
        SigaResponse sigaResponse = getSigaToken(context);
        if (!sigaResponse.getErro()) {
            SigaRepository sigaRepository = SigaRepository.getInstance();
            SigaResponse response = sigaRepository.enviarPdfPorEmail(sigaResponse.getMsg(), email, cpf);
            return (!response.getErro()) ? new SigaResponse(false, response.getMsg()) : new SigaResponse(true, response.getMsg());
        }

        salvarEmailLgpdVisitanteLocalmente(cpf, email);
        return new SigaResponse(true, sigaResponse.getMsg());
    }

    private static void salvarEmailLgpdVisitanteLocalmente(Long cpf, String email) {
        EmailLgpdVisitante emailLgpdVisitante = appDataBase.emailLgpdVisitanteDAO().getById(cpf);
        if (emailLgpdVisitante == null) emailLgpdVisitante = new EmailLgpdVisitante();
        emailLgpdVisitante.setCpf(cpf);
        emailLgpdVisitante.setEmail(email);
        appDataBase.emailLgpdVisitanteDAO().insert(emailLgpdVisitante);
    }

}
