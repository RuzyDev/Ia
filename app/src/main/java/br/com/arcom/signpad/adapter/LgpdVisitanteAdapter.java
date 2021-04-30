package br.com.arcom.signpad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.arcom.signpad.R;
import br.com.arcom.signpad.data.LgpdVisitante;
import br.com.arcom.signpad.utilities.UtilDate;
import br.com.arcom.signpad.utilities.UtilString;

import static br.com.arcom.signpad.utilities.UtilDate.DATE_TIME_OP3;

public class LgpdVisitanteAdapter extends RecyclerView.Adapter<LgpdVisitanteAdapter.ViewHolder> {

    private final List<LgpdVisitante> mLgpdVisitantes;

    public LgpdVisitanteAdapter(List<LgpdVisitante> mLgpdVisitantes) {
        this.mLgpdVisitantes = mLgpdVisitantes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeTextView;
        public TextView cpfTextView;
        public TextView dataAssTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.lpgdVisitante_nome);
            cpfTextView = itemView.findViewById(R.id.lpgdVisitante_cpf);
            dataAssTextView = itemView.findViewById(R.id.lpgdVisitante_dataAss);
        }
    }

    @NonNull
    @Override
    public LgpdVisitanteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View lgpdVisitanteView = inflater.inflate(R.layout.item_lgpd_visitante, parent, false);
        return new ViewHolder(lgpdVisitanteView);
    }

    @Override
    public void onBindViewHolder(@NonNull LgpdVisitanteAdapter.ViewHolder holder, int position) {
        LgpdVisitante lgpdVisitante = mLgpdVisitantes.get(position);

        TextView nomeTextView = holder.nomeTextView;
        TextView cpfTextView = holder.cpfTextView;
        TextView dataAssTextView = holder.dataAssTextView;

        nomeTextView.setText(lgpdVisitante.getNome());
        cpfTextView.setText(UtilString.formatarCpf(String.valueOf(lgpdVisitante.getCpf())));
        dataAssTextView.setText(UtilDate.buscarDataAtual(lgpdVisitante.getDataAss(), DATE_TIME_OP3));
    }

    @Override
    public int getItemCount() {
        return mLgpdVisitantes.size();
    }
    
}
