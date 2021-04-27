package br.com.arcom.signpad.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UsuarioDAO {

    @Query("SELECT * FROM usuario")
    List<Usuario> getAll();

    @Query("SELECT * FROM usuario WHERE cpf = :cpf")
    Usuario getById(Long cpf);

    @Query("SELECT * FROM usuario WHERE cpf IN (:userCpfs)")
    List<Usuario> loadAllByIds(Long[] userCpfs);

    @Insert
    void insertAll(Usuario... usuarios);

    @Insert(onConflict = REPLACE)
    void insert(Usuario usuario);

    @Delete
    void delete(Usuario usuario);

    @Delete
    void deleteAll(List<Usuario> usuarios);

    @Query("UPDATE usuario SET nome = :nome, data_preenchimento = :dataPreenchimento, path_pdf = :pdfPath WHERE cpf = :cpf")
    void update(Long cpf, String nome, LocalDateTime dataPreenchimento, String pdfPath);
}
