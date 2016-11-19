package com.mindorks.paracamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by janisharali on 28/07/16.
 */
public class Utils {

    /**
     *
     * @param context
     * @param dirName
     * @param fileName
     * @param fileType
     * @return
     */
    static File createImageFile(
            Context context,
            String dirName,
            String fileName,
            String fileType) {
        try {
            File file = createDir(context, dirName);
            File image = new File(file.getAbsoluteFile() + File.separator + fileName + fileType);
            image.createNewFile();
            return image;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param context
     * @param dirName
     * @return
     */
    private static File createDir(
            Context context,
            String dirName){
        File file = new File(context.getExternalFilesDir(null) + File.separator + dirName);
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }

    /**
     *
     * @param file
     * @param requiredHeight
     * @return
     */
    static Bitmap decodeFile(File file, int requiredHeight) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, o);

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= requiredHeight &&
                    o.outHeight / scale / 2 >= requiredHeight) {
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param bitmap
     * @param filePath
     * @param imageType
     * @param compression
     */
    static void saveBitmap(Bitmap bitmap, String filePath, String imageType, int compression) {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            // PNG is a loss less format, the compression factor (100) is ignored
            switch (imageType) {
                case "png":
                case "PNG":
                case ".png":
                    bitmap.compress(Bitmap.CompressFormat.PNG, compression, out);
                    break;
                case "jpg":
                case "JPG":
                case ".jpg":
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compression, out);
                    break;
                case "jpeg":
                case "JPEG":
                case ".jpeg":
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compression, out);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
