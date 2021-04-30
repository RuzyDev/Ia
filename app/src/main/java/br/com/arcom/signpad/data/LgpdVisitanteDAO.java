package br.com.arcom.signpad.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface LgpdVisitanteDAO {

    @Query("SELECT * FROM lgpd_visitante")
    List<LgpdVisitante> getAll();

    @Query("SELECT * FROM lgpd_visitante WHERE cpf = :cpf")
    LgpdVisitante getById(Long cpf);

    @Query("SELECT * FROM lgpd_visitante WHERE cpf IN (:cpfs)")
    List<LgpdVisitante> loadAllByIds(Long[] cpfs);

    @Insert
    void insertAll(LgpdVisitante... lgpdVisitantes);

    @Insert(onConflict = REPLACE)
    void insert(LgpdVisitante lgpdVisitante);

    @Delete
    void delete(LgpdVisitante lgpdVisitante);

    @Delete
    void deleteAll(List<LgpdVisitante> lgpdVisitantes);

    @Query("UPDATE lgpd_visitante SET nome = :nome, data_ass = :dataAss, path_pdf = :pdfPath WHERE cpf = :cpf")
    void update(Long cpf, String nome, LocalDateTime dataAss, String pdfPath);
}
