package br.com.arcom.signpad.model;

public class ResponseRequest {

    private Integer statusCode;
    private String dados;

    public ResponseRequest() {}

    public ResponseRequest(final Integer statusCode, final String dados) {
        this.statusCode = statusCode;
        this.dados = dados;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getDados() {
        return dados;
    }
}
