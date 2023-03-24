package br.com.arcom.signpad.api;

import com.google.gson.annotations.SerializedName;

public class DadosLgpdVisitante {
    @SerializedName("nome")
    private String nome;
    @SerializedName("cpf")
    private Long cpf;
    @SerializedName("dataAss")
    private String dataAss;
    @SerializedName("filePart")
    private String filePart;

    public DadosLgpdVisitante(String nome, Long cpf, String dataAss, String filePart) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataAss = dataAss;
        this.filePart = filePart;
    }
}