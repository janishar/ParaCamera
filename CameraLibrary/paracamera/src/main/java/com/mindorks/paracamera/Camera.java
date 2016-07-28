package com.mindorks.paracamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by ali on 4/11/15.
 */
public class Camera {

    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final String IMAGE_JPG = "jpg";
    public static final String IMAGE_JPEG = "jpeg";
    public static final String IMAGE_PNG = "png";

    private static final String IMAGE_FORMAT_JPG = ".jpg";
    private static final String IMAGE_FORMAT_JPEG = ".jpeg";
    private static final String IMAGE_FORMAT_PNG = ".png";
    private static final int IMAGE_HEIGHT = 1000;
    private static final int IMAGE_COMPRESSION = 75;
    private static final String IMAGE_DEFAULT_DIR = "capture";
    private static final String IMAGE_DEFAULT_NAME = "img_";

    private Context context;
    private Activity activity;
    private String cameraBitmapPath = null;
    private Bitmap cameraBitmap = null;
    private String dirName;
    private String imageName;
    private String imageType;
    private int imageHeight;
    private int compression;

    public Camera(Activity activity) {
        this.activity = activity;
        context = activity.getApplicationContext();
        dirName = IMAGE_DEFAULT_DIR;
        imageName = IMAGE_DEFAULT_NAME + System.currentTimeMillis();
        imageHeight = IMAGE_HEIGHT;
        compression = IMAGE_COMPRESSION;
        imageType = IMAGE_FORMAT_JPG;
    }

    public CameraBuilder builder(){
        return new CameraBuilder();
    }

    public void takePicture() throws NullPointerException{
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = Utils.createImageFile(context, dirName, imageName, imageType);
            if (photoFile != null) {
                cameraBitmapPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }else{
                throw new NullPointerException("Bitmap received from camera is null");
            }
        }else{
            throw new NullPointerException("Unable to open camera");
        }
    }

    public String getCameraBitmapPath() {
        Bitmap bitmap = getCameraBitmap();
        bitmap.recycle();
        return cameraBitmapPath;
    }

    public Bitmap getCameraBitmap() {
        try {
            if (cameraBitmap != null) {
                cameraBitmap.recycle();
            }
            cameraBitmap = Utils.decodeFile(new File(cameraBitmapPath), imageHeight);
            if (cameraBitmap != null) {
                Utils.saveBitmap(cameraBitmap, cameraBitmapPath, imageType, compression);
            }
            return cameraBitmap;
        }catch (Exception e){
            return null;
        }
    }

    public String resizeAndGetCameraBitmapPath(int imageHeight) {
        Bitmap bitmap = resizeAndGetCameraBitmap(imageHeight);
        bitmap.recycle();
        return cameraBitmapPath;
    }

    public Bitmap resizeAndGetCameraBitmap(int imageHeight) {
        try {
            if (cameraBitmap != null) {
                cameraBitmap.recycle();
            }
            cameraBitmap = Utils.decodeFile(new File(cameraBitmapPath), imageHeight);
            if (cameraBitmap != null) {
                Utils.saveBitmap(cameraBitmap, cameraBitmapPath, imageType, compression);
            }
            return cameraBitmap;
        }catch (Exception e){
            return null;
        }
    }

    public void deleteImage(){
        if(cameraBitmapPath != null){
            File image = new File(cameraBitmapPath);
            if(image.exists()){
                image.delete();
            }
        }
    }

    public class CameraBuilder {
        public CameraBuilder setDirectory(String dirName){
            Camera.this.dirName = dirName;
            return this;
        }

        public CameraBuilder setName(String imageName){
            Camera.this.imageName = imageName;
            return this;
        }

        public CameraBuilder setImageFormat(String imageFormat){
            if(imageFormat == null){
                Camera.this.imageType = IMAGE_FORMAT_JPG;
            }
            if(imageFormat.equals("png") || imageFormat.equals("PNG") || imageFormat.equals(".png")){
                Camera.this.imageType = IMAGE_FORMAT_PNG;
            }
            else if(imageFormat.equals("jpg") || imageFormat.equals("JPG") || imageFormat.equals(".jpg")){
                Camera.this.imageType = IMAGE_FORMAT_JPG;
            }
            else if(imageFormat.equals("jpeg") || imageFormat.equals("JPEG") || imageFormat.equals(".jpeg")){
                Camera.this.imageType = IMAGE_FORMAT_JPEG;
            }else{
                Camera.this.imageType = IMAGE_FORMAT_JPG;
            }
            return this;
        }


        public CameraBuilder setImageHeight(int imageHeight){
            Camera.this.imageHeight = imageHeight;
            return this;
        }

        public CameraBuilder setCompression(int compression){
            if(compression > 100){
                compression = 100;
            }else if(compression < 0){
                compression = 0;
            }
            Camera.this.compression = compression;
            return this;
        }
    }
}

