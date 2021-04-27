package br.com.arcom.signpad.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "siga_token")
public class SigaToken implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "token")
    private String token;

    @ColumnInfo(name = "token_date")
    private Date tokenDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(Date tokenDate) {
        this.tokenDate = tokenDate;
    }

    @Override
    public String toString() {
        return "SigaToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", tokenDate=" + tokenDate +
                '}';
    }
}
