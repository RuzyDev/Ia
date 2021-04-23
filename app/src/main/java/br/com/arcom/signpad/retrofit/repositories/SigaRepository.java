package br.com.arcom.signpad.retrofit.repositories;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.arcom.signpad.model.UsuarioRequest;
import br.com.arcom.signpad.retrofit.api.SigaApi;
import br.com.arcom.signpad.retrofit.services.SigaService;
import br.com.arcom.signpad.util.UtilMobile;
import retrofit2.Response;

public class SigaRepository {

    private static SigaRepository sigaRepository;

    public static SigaRepository getInstance(){
        if (sigaRepository == null) sigaRepository = new SigaRepository();
        return sigaRepository;
    }

    private final SigaApi sigaApi;

    public SigaRepository(){
        sigaApi = new SigaService().getApi();
    }

    public br.com.arcom.signpad.model.Response buscarToken() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<br.com.arcom.signpad.model.Response> futureTask = executor.submit(() -> {
            try {
                Response<String> response = sigaApi.buscarToken(new UsuarioRequest(43L, UtilMobile.getPassword(), true)).execute();
                return new br.com.arcom.signpad.model.Response(false, response.body());
            } catch (IOException e) {
                return new br.com.arcom.signpad.model.Response(true, e.getMessage());
            }
        });
        executor.shutdown();

        try {
            return futureTask.get();
        } catch (ExecutionException | InterruptedException ignored) {
            return null;
        }
    }
}
