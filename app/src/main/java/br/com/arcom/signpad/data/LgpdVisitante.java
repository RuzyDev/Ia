package br.com.arcom.signpad.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "lgpd_visitante")
public class LgpdVisitante implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @ColumnInfo(name = "cpf")
    @SerializedName("id")
    private Long cpf;

    @SerializedName("nomeVisitante")
    @ColumnInfo(name = "nome")
    private String nome;

    @SerializedName("dataAssinatura")
    @ColumnInfo(name = "data_preenchimento")
    private Date dataPreenchimento;

    @SerializedName("arquivoLgpdVisitante")
    @ColumnInfo(name = "path_pdf")
    private String pathPdf;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Date getDataPreenchimento() {
        return dataPreenchimento;
    }

    public void setDataPreenchimento(Date dataPreenchimento) {
        this.dataPreenchimento = dataPreenchimento;
    }

    public String getPathPdf() {
        return pathPdf;
    }

    public void setPathPdf(String pathPdf) {
        this.pathPdf = pathPdf;
    }

    @Override
    public String toString() {
        return "LgpdVisitante{" +
                "cpf=" + cpf +
                ", nome='" + nome + '\'' +
                ", dataPreenchimento=" + dataPreenchimento +
                ", pathPdf='" + pathPdf + '\'' +
                '}';
    }
}
