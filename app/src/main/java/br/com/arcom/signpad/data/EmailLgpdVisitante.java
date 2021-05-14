package br.com.arcom.signpad.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "email_lgpd_visitante")
public class EmailLgpdVisitante implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @ColumnInfo(name = "cpf")
    private Long cpf;

    @ColumnInfo(name = "email")
    private String email;

    public EmailLgpdVisitante() {}

    public EmailLgpdVisitante(Long cpf, String email) {
        this.cpf = cpf;
        this.email = email;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmailLgpdVisitante{" +
                "cpf=" + cpf +
                ", email='" + email + '\'' +
                '}';
    }
}
