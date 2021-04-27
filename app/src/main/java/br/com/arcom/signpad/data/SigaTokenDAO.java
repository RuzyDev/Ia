package br.com.arcom.signpad.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SigaTokenDAO {

    @Query("SELECT * FROM siga_token")
    List<SigaToken> getAll();

    @Query("SELECT * FROM siga_token WHERE id = :sID")
    SigaToken getById(int sID);

    @Query("SELECT * FROM siga_token WHERE id IN (:userIds)")
    List<SigaToken> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(SigaToken... sigaTokens);

    @Insert(onConflict = REPLACE)
    void insert(SigaToken sigaToken);

    @Delete
    void delete(SigaToken sigaToken);

    @Delete
    void deleteAll(List<SigaToken> sigaTokens);

    @Query("UPDATE siga_token SET token = :sToken, token_date = :sTokenDate WHERE id = :sID")
    void update(int sID, String sToken, Date sTokenDate);

}
