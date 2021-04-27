package br.com.arcom.signpad.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.com.arcom.signpad.utilities.Constantes;

@Database(
        entities = {
                SigaToken.class,
                Usuario.class
        }
        , version = 1)
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase dataBase;

    public synchronized static AppDataBase getInstance(Context context) {
        if (dataBase == null || !dataBase.isOpen()) {
            dataBase = Room.databaseBuilder(context.getApplicationContext()
                    , AppDataBase.class, Constantes.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dataBase;
    }

    public abstract SigaTokenDAO sigaTokenDAO();

    public abstract UsuarioDAO usuarioDAO();

}