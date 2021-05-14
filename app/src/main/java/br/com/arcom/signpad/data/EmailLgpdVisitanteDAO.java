package br.com.arcom.signpad.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EmailLgpdVisitanteDAO {

    @Query("SELECT * FROM email_lgpd_visitante")
    List<EmailLgpdVisitante> getAll();

    @Query("SELECT * FROM email_lgpd_visitante WHERE cpf = :cpf")
    EmailLgpdVisitante getById(Long cpf);

    @Query("SELECT * FROM email_lgpd_visitante WHERE cpf IN (:cpfs)")
    List<EmailLgpdVisitante> loadAllByIds(Long[] cpfs);

    @Insert
    void insertAll(EmailLgpdVisitante... emailLgpdVisitantes);

    @Insert(onConflict = REPLACE)
    void insert(EmailLgpdVisitante emailLgpdVisitante);

    @Delete
    void delete(EmailLgpdVisitante emailLgpdVisitante);

    @Delete
    void deleteAll(List<EmailLgpdVisitante> emailLgpdVisitantes);

    @Query("UPDATE email_lgpd_visitante SET email = :email WHERE cpf = :cpf")
    void update(Long cpf, String email);
}
