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

    @Query("SELECT * FROM usuario WHERE id = :sID")
    Usuario getById(int sID);

    @Query("SELECT * FROM usuario WHERE id IN (:userIds)")
    List<Usuario> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(Usuario... usuarios);

    @Insert(onConflict = REPLACE)
    void insert(Usuario usuario);

    @Delete
    void delete(Usuario usuario);

    @Delete
    void deleteAll(List<Usuario> usuarios);

    @Query("UPDATE usuario SET nome = :nome, cpf = :cpf, data_preenchimento = :dataPreenchimento WHERE id = :sID")
    void update(int sID, String nome, Long cpf, LocalDateTime dataPreenchimento);
}
