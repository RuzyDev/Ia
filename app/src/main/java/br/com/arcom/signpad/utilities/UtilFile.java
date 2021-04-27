package br.com.arcom.signpad.utilities;

import android.util.Log;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class UtilFile {

    public static File getFile(String path) {
        File image = new File(path);
        return (image.exists()) ? image : null;
    }

    public static int countKBytes(String path) {
        File foto = new File(path);
        if (!foto.exists())  return 0;
        int kbytes = new BigDecimal(foto.length()).divide(new BigDecimal("1000"), RoundingMode.HALF_UP).intValue();
        foto = null;
        return kbytes;
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.delete()) Log.d(Constantes.TAG_LOG_SIGNPAD, "Arquivo deletado!");
    }

}
