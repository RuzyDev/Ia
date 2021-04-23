package br.com.arcom.signpad.model;

import com.google.gson.annotations.SerializedName;

public class UsuarioRequest {

    @SerializedName("idUsuario")
    private Long idUsuario;

    @SerializedName("senha")
    private String senha;

    @SerializedName("apenasToken")
    private Boolean apenasToken;

    public UsuarioRequest(Long idUsuario, String senha, Boolean apenasToken) {
        this.idUsuario = idUsuario;
        this.senha = senha;
        this.apenasToken = apenasToken;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public Boolean getApenasToken() {
        return apenasToken;
    }
}
