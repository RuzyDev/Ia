package br.com.arcom.signpad.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.com.arcom.signpad.dao.converters.DateConverter;
import br.com.arcom.signpad.entities.SigaToken;
import br.com.arcom.signpad.entities.Usuario;
import br.com.arcom.signpad.util.ConstantesUtils;

@Database(
        entities = {
                SigaToken.class,
                Usuario.class
        }
        , version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase dataBase;

    public synchronized static AppDataBase getInstance(Context context) {
        if (dataBase == null) {
            dataBase = Room.databaseBuilder(context.getApplicationContext()
            , AppDataBase.class, ConstantesUtils.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dataBase;
    }

    public abstract SigaTokenDAO sigaTokenDAO();

    public abstract UsuarioDAO usuarioDAO();

}