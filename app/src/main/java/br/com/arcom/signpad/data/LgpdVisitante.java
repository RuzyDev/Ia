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
    @ColumnInfo(name = "data_ass")
    private Date dataAss;

    @SerializedName("arquivoLgpdVisitante")
    @ColumnInfo(name = "path_pdf")
    private String pathPdf;

    public LgpdVisitante() {}

    public LgpdVisitante(Long cpf, String nome, Date dataAss, String pathPdf) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataAss = dataAss;
        this.pathPdf = pathPdf;
    }

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

    public Date getDataAss() {
        return dataAss;
    }

    public void setDataAss(Date dataAss) {
        this.dataAss = dataAss;
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
                ", dataAss=" + dataAss +
                ", pathPdf='" + pathPdf + '\'' +
                '}';
    }
}
