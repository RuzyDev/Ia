package br.com.arcom.signpad.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.adapter.LgpdVisitanteAdapter;
import br.com.arcom.signpad.data.LgpdVisitante;
import br.com.arcom.signpad.models.BuscarLgpdVisitanteResponse;
import br.com.arcom.signpad.services.LgpdVisitanteService;
import br.com.arcom.signpad.utilities.Constantes;
import br.com.arcom.signpad.utilities.CustomDialogAviso;

public class BuscarCadastradosActivity extends AppCompatActivity {

    private RecyclerView rvLgpdVisitantes;
    private TextInputLayout tiSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_cadastrados_activity);
        recuperarParametros();
    }

    public void recuperarParametros() {
        rvLgpdVisitantes = findViewById(R.id.recycler_lgpdVisitantes);
        tiSearch = findViewById(R.id.text_input_search);
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

    public void buscarVisitantesCadastrados(View view) {
        if (validarTextSearch()) {
            String textSearch = tiSearch.getEditText().getText().toString().trim();
            String modoPesquisa;
            if (NumberUtils.isCreatable(textSearch)) {
                modoPesquisa = "cpf";
                BuscarLgpdVisitanteResponse response = LgpdVisitanteService.buscarLpgdVisitante(BuscarCadastradosActivity.this, modoPesquisa, null, Long.valueOf(textSearch));
                vereficarResponse(response, modoPesquisa, textSearch);
            } else {
                modoPesquisa = "nome";
                BuscarLgpdVisitanteResponse response = LgpdVisitanteService.buscarLpgdVisitante(BuscarCadastradosActivity.this, modoPesquisa, textSearch, null);
                vereficarResponse(response, modoPesquisa, textSearch);
            }
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
            CustomDialogAviso.showDialog(BuscarCadastradosActivity.this, response.getMsg());
            LgpdVisitanteAdapter adapter = new LgpdVisitanteAdapter(Collections.emptyList());
            rvLgpdVisitantes.setAdapter(adapter);
        }
        rvLgpdVisitantes.setLayoutManager(new LinearLayoutManager(this));
    }

}