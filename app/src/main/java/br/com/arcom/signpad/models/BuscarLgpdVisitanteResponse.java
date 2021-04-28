package br.com.arcom.signpad.models;

import java.util.List;

import br.com.arcom.signpad.data.LgpdVisitante;

public class BuscarLgpdVisitanteResponse {

    private final Boolean erro;
    private final String msg;
    private List<LgpdVisitante> lgpdVisitanteList;

    public BuscarLgpdVisitanteResponse(Boolean erro, String msg, List<LgpdVisitante> lgpdVisitanteList) {
        this.erro = erro;
        this.msg = msg;
        this.lgpdVisitanteList = lgpdVisitanteList;
    }

    public Boolean getErro() {
        return erro;
    }

    public String getMsg() {
        return msg;
    }

    public List<LgpdVisitante> getLgpdVisitanteList() {
        return lgpdVisitanteList;
    }

    public void setLgpdVisitanteList(List<LgpdVisitante> lgpdVisitanteList) {
        this.lgpdVisitanteList = lgpdVisitanteList;
    }
}
