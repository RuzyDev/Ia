package br.com.arcom.signpad.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class UtilPhoto {

    public static Bitmap rotateBitmap(Context context, Uri photoUri, Bitmap bitmap, String pathToUserPhoto) {
        int orientation = getCameraPhotoOrientation(context, photoUri, pathToUserPhoto);
        if (orientation <= 0) return bitmap;
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return bitmap;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static File createPhotoFile(String photoName, Context context) {
        File storageDir = new File(ContextCompat.getExternalFilesDirs(context, null)[0] + "/Pictures");
        if (!storageDir.exists()) storageDir.mkdirs();
        File image = null;

        try {
            image = File.createTempFile(photoName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
