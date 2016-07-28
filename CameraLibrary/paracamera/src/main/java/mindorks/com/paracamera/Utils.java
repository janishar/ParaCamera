package mindorks.com.paracamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by janisharali on 28/07/16.
 */
public class Utils {

    public static File createImageFile(
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

    public static File createDir(
            Context context,
            String dirName){
        File file = new File(context.getExternalFilesDir(null) + File.separator + dirName);
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static Bitmap decodeFile(File file, int requiredHeight) {
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
            return null;
        }
    }

    public static void saveBitmap(Bitmap bitmap,String filePath, String imageType, int compression){

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            // PNG is a loss less format, the compression factor (100) is ignored
            if(imageType.equals("png") || imageType.equals("PNG") || imageType.equals(".png")){
                bitmap.compress(Bitmap.CompressFormat.PNG, compression, out);
            }
            else if(imageType.equals("jpg") || imageType.equals("JPG") || imageType.equals(".jpg")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, compression, out);
            }
            else if(imageType.equals("jpeg") || imageType.equals("JPEG") || imageType.equals(".jpeg")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, compression, out);
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
