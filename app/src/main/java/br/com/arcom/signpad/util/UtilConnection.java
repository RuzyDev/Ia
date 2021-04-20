package br.com.arcom.signpad.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.arcom.signpad.R;

public class UtilConnection {

    public static String getCerificatePath(Context context) {
        final String certificadoPath;
        try {
            File file = new File(context.getCacheDir(), ConstantesUtils.CERTIFICATE);
            certificadoPath = ( file.exists() ) ? file.getAbsolutePath() : getRawUri(context, context.getResources().openRawResource(R.raw.certificate));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return certificadoPath;
    }

    public static String getRawUri(Context context, InputStream input) throws IOException {
        OutputStream output = null;
        File file = null;
        try {
            file = new File(context.getCacheDir(), ConstantesUtils.CERTIFICATE);
            if(file.exists())  return file.getAbsolutePath();

            output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read;

            while ((read = input.read(buffer)) != -1) output.write(buffer, 0, read);

            output.flush();
        } finally {
            if(output != null) output.close();
            if(input != null) input.close();

        }
        return file.getAbsolutePath();
    }

    public static String readFully(InputStream inputStream){
        if (inputStream == null) return "";

        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();

            final byte[] buffer = new byte[1024];
            int available = 0;

            while ((available = bufferedInputStream.read(buffer)) >= 0) {
                byteArrayOutputStream.write(buffer, 0, available);
            }
            bufferedInputStream.close();
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
