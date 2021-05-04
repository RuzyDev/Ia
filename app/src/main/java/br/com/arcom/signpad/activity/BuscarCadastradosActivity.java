package br.com.arcom.signpad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.math.NumberUtils;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.adapter.LgpdVisitanteAdapter;
import br.com.arcom.signpad.data.LgpdVisitante;
import br.com.arcom.signpad.models.BuscarLgpdVisitanteResponse;
import br.com.arcom.signpad.models.SigaResponse;
import br.com.arcom.signpad.services.LgpdVisitanteService;
import br.com.arcom.signpad.utilities.Constantes;
import br.com.arcom.signpad.utilities.CustomDialogAviso;
import kotlin.Triple;
import kotlin.reflect.KVariance;

public class BuscarCadastradosActivity extends AppCompatActivity {

    private RecyclerView rvLgpdVisitantes;
    private TextInputLayout tiSearch;

    private LinearLayout mConteudo;
    private LinearLayout mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_cadastrados_activity);
        recuperarParametros();
    }

    public void recuperarParametros() {
        rvLgpdVisitantes = findViewById(R.id.recycler_lgpdVisitantes);
        tiSearch = findViewById(R.id.text_input_search);

        mConteudo = findViewById(R.id.view_getCadast_conteudo);
        mLoading = findViewById(R.id.view_getCadast_progressBar);
    }

    public void showLoading(Boolean value) {
        if (value) {
            mConteudo.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
        } else {
            mConteudo.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
        }
    }

    public boolean validarTextSearch() {
        String textSearch = tiSearch.getEditText().getText().toString().trim();
        if (textSearch.isEmpty()) {
            tiSearch.setError("O campo de pesquisa não pode estar vazio!");
            return false;
        } else {
            tiSearch.setError(null);
            return true;
        }
    }

    public void buscarCadastrados(View view) {
        if (!validarTextSearch()) return;
        showLoading(true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> handler.post(() -> {
            ExecutorService threadpool = Executors.newCachedThreadPool();
            Future<Triplet<BuscarLgpdVisitanteResponse, String, String>> futureTask = threadpool.submit(this::buscarVisitantesCadastrados);
            threadpool.shutdown();

            try {
                Triplet<BuscarLgpdVisitanteResponse, String, String> result = futureTask.get();
                vereficarResponse(result.getValue0(), result.getValue1(), result.getValue2());
            } catch (ExecutionException | InterruptedException e) {
                CustomDialogAviso.showDialog(BuscarCadastradosActivity.this, e.getMessage());
            } finally {
                showLoading(false);
            }
        }));
        executor.shutdown();
    }

    public Triplet<BuscarLgpdVisitanteResponse, String, String> buscarVisitantesCadastrados() {
        String textSearch = tiSearch.getEditText().getText().toString().trim();
        String modoPesquisa;
        if (NumberUtils.isCreatable(textSearch)) {
            modoPesquisa = "cpf";
            return new Triplet<>(LgpdVisitanteService.buscarLpgdVisitante(BuscarCadastradosActivity.this, modoPesquisa, null, Long.valueOf(textSearch)),
                    modoPesquisa,
                    textSearch);
        } else {
            modoPesquisa = "nome";
            return new Triplet<>(LgpdVisitanteService.buscarLpgdVisitante(BuscarCadastradosActivity.this, modoPesquisa, textSearch, null),
                    modoPesquisa,
                    textSearch);
        }
    }

    public void vereficarResponse(BuscarLgpdVisitanteResponse response, String modoPesquisa, String textSearch) {
        if (!response.getErro()) {
            List<LgpdVisitante> lgpdVisitanteList = new ArrayList<>();
            if (modoPesquisa.equals("nome")) {
                if (response.getLgpdVisitanteList() != null) {
                    lgpdVisitanteList.addAll(response.getLgpdVisitanteList());
                } else {
                    CustomDialogAviso.showDialog(BuscarCadastradosActivity.this, "Nenhum cadastro encontrado com o nome '" + textSearch + "'!!");
                }
            } else {
                lgpdVisitanteList.add(response.getLgpdVisitante());
            }
            LgpdVisitanteAdapter adapter = new LgpdVisitanteAdapter(lgpdVisitanteList);
            rvLgpdVisitantes.setAdapter(adapter);
        } else {
            if (modoPesquisa.equals("cpf")) {
                CustomDialogAviso.showDialog(BuscarCadastradosActivity.this, "Nenhum cadastro encontrado com o CPF '" + textSearch + "'!!");
            } else {
                CustomDialogAviso.showDialog(BuscarCadastradosActivity.this, response.getMsg());
            }
            LgpdVisitanteAdapter adapter = new LgpdVisitanteAdapter(Collections.emptyList());
            rvLgpdVisitantes.setAdapter(adapter);
        }
        rvLgpdVisitantes.setLayoutManager(new LinearLayoutManager(this));
    }

}