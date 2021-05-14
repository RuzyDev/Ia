package br.com.arcom.signpad.api;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.arcom.signpad.data.LgpdVisitante;
import br.com.arcom.signpad.models.BuscarLgpdVisitanteResponse;
import br.com.arcom.signpad.models.LgpdVisitanteRequest;
import br.com.arcom.signpad.models.SigaResponse;
import br.com.arcom.signpad.utilities.UtilDate;
import br.com.arcom.signpad.utilities.UtilMobile;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class SigaRepository {

    private static SigaRepository sigaRepository;

    public static SigaRepository getInstance() {
        if (sigaRepository == null) sigaRepository = new SigaRepository();
        return sigaRepository;
    }

    private final SigaApi sigaApi;

    public SigaRepository(){
        sigaApi = new SigaService().getApi();
    }

    public SigaResponse buscarToken() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<SigaResponse> futureTask = executor.submit(() -> {
            try {
                Response<String> response = sigaApi.buscarToken(new LgpdVisitanteRequest(43L, UtilMobile.getPassword(), true)).execute();
                return new SigaResponse(false, response.body());
            } catch (IOException e) {
                return new SigaResponse(true, e.getMessage());
            }
        });
        executor.shutdown();
        try {
            return futureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            return new SigaResponse(true, e.getMessage());
        }
    }

    public SigaResponse salvarDadosVisitante(String sigaToken, String pathPdf, String mUsuarioNomeCom, Long mUsuarioCpf, Date dataAss) {
        File file = new File(pathPdf);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("arquivo",
                file.getName(), RequestBody.create(file, MediaType.parse("application/pdf")));

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<SigaResponse> futureTask = executor.submit(() -> {
            try {
                Response<Void> response = sigaApi.salvarDadosLgpdVisitante(
                        ("Bearer " + sigaToken),
                        mUsuarioNomeCom,
                        mUsuarioCpf,
                        UtilDate.buscarDataAtual(dataAss, UtilDate.DATE_TIME_OP2),
                        filePart
                ).execute();
                return (response.isSuccessful())
                        ? new SigaResponse(false, "Dados salvos com sucesso!")
                        : new SigaResponse(true, response.errorBody().string());
            } catch (IOException e) {
                return new SigaResponse(true, e.getMessage());
            }
        });
        executor.shutdown();

        try {
            return futureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            return new SigaResponse(true, e.getMessage());
        }
    }

    public BuscarLgpdVisitanteResponse buscarLpgdVisitante(String sigaToken, String modoPesquisa, String nome, Long cpf) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<BuscarLgpdVisitanteResponse> futureTask = executor.submit(() -> {
            if (modoPesquisa.equals("nome")) {
                Response<List<LgpdVisitante>> response = sigaApi.buscarLpgdVisitante(("Bearer " + sigaToken), modoPesquisa, nome).execute();
                return (response.isSuccessful())
                        ? new BuscarLgpdVisitanteResponse(false, "", response.body())
                        : new BuscarLgpdVisitanteResponse(true, response.errorBody().string(), Collections.emptyList());
            } else {
                Response<LgpdVisitante> response = sigaApi.buscarLpgdVisitante(("Bearer " + sigaToken), modoPesquisa, cpf).execute();
                return (response.isSuccessful())
                        ? new BuscarLgpdVisitanteResponse(false, "", response.body())
                        : new BuscarLgpdVisitanteResponse(true, response.errorBody().string(), Collections.emptyList());
            }
        });
        executor.shutdown();

        try {
            return futureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            return new BuscarLgpdVisitanteResponse(true, e.getMessage(), Collections.emptyList());
        }
    }

    public SigaResponse enviarPdfPorEmail(String sigaToken, String email, Long cpf) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<SigaResponse> futureTask = executor.submit(() -> {
            try {
                Response<Void> response = sigaApi.enviarPdfPorEmail(("Bearer " + sigaToken), email, cpf).execute();
                return (response.isSuccessful())
                        ? new SigaResponse(false, "Termo de consentimento enviado com sucesso!")
                        : new SigaResponse(true, response.errorBody().string());
            } catch (IOException e) {
                return new SigaResponse(true, e.getMessage());
            }
        });
        executor.shutdown();

        try {
            return futureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            return new SigaResponse(true, e.getMessage());
        }
    }

}
