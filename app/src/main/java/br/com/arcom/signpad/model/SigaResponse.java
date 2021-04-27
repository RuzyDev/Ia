package br.com.arcom.signpad.model;

public class SigaResponse {

    private final Boolean erro;
    private final String msg;

    public SigaResponse(final Boolean erro, final String msg) {
        this.erro = erro;
        this.msg = msg;
    }

    public Boolean getErro() {
        return erro;
    }

    public String getMsg() {
        return msg;
    }
}
