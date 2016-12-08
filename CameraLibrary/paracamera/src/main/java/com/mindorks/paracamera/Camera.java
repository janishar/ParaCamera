package com.mindorks.paracamera;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by ali on 4/11/15.
 */
public class Camera {

    /**
     * public variables to be used in the builder
     */
    public static int REQUEST_TAKE_PHOTO = 1234;
    public static final String IMAGE_JPG = "jpg";
    public static final String IMAGE_JPEG = "jpeg";
    public static final String IMAGE_PNG = "png";

    /**
     * default values used by camera
     */
    private static final String IMAGE_FORMAT_JPG = ".jpg";
    private static final String IMAGE_FORMAT_JPEG = ".jpeg";
    private static final String IMAGE_FORMAT_PNG = ".png";
    private static final int IMAGE_HEIGHT = 1000;
    private static final int IMAGE_COMPRESSION = 75;
    private static final String IMAGE_DEFAULT_DIR = "capture";
    private static final String IMAGE_DEFAULT_NAME = "img_";

    private enum MODE {ACTIVITY, FRAGMENT}

    /**
     * Private variables
     */
    private Context context;
    private Activity activity;
    private Fragment fragment;
    private String cameraBitmapPath = null;
    private Bitmap cameraBitmap = null;
    private String dirName;
    private String imageName;
    private String imageType;
    private int imageHeight;
    private int compression;
    private boolean isCorrectOrientationRequired;
    private MODE mode;

    /**
     * @param activity to return the camera results
     */
    public Camera(Activity activity) {
        this.activity = activity;
        context = activity.getApplicationContext();
        mode = MODE.ACTIVITY;
        init();
    }

    /**
     * @param fragment to return the camera results
     */
    public Camera(Fragment fragment) {
        this.fragment = fragment;
        context = fragment.getActivity().getApplicationContext();
        mode = MODE.FRAGMENT;
        init();
    }

    private void init() {
        dirName = IMAGE_DEFAULT_DIR;
        imageName = IMAGE_DEFAULT_NAME + System.currentTimeMillis();
        imageHeight = IMAGE_HEIGHT;
        compression = IMAGE_COMPRESSION;
        imageType = IMAGE_FORMAT_JPG;
    }

    /**
     * @return create camera builder
     */
    public CameraBuilder builder() {
        return new CameraBuilder();
    }

    /**
     * Initiate the existing camera apps
     *
     * @throws NullPointerException
     */
    public void takePicture() throws NullPointerException, IllegalAccessException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (mode) {
            case ACTIVITY:
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    File photoFile = Utils.createImageFile(context, dirName, imageName, imageType);
                    if (photoFile != null) {
                        cameraBitmapPath = photoFile.getAbsolutePath();
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);
                        } else {
                            uri = Uri.fromFile(photoFile);
                        }
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    } else {
                        throw new NullPointerException("Image file could not be created");
                    }
                } else {
                    throw new IllegalAccessException("Unable to open camera");
                }
                break;
            case FRAGMENT:
                if (takePictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
                    File photoFile = Utils.createImageFile(context, dirName, imageName, imageType);
                    if (photoFile != null) {
                        cameraBitmapPath = photoFile.getAbsolutePath();
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);
                        } else {
                            uri = Uri.fromFile(photoFile);
                        }
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        fragment.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    } else {
                        throw new NullPointerException("Image file could not be created");
                    }
                } else {
                    throw new IllegalAccessException("Unable to open camera");
                }
                break;
        }
    }

    /**
     * @return the saved bitmap path but scaling bitmap as per builder
     */
    public String getCameraBitmapPath() {
        Bitmap bitmap = getCameraBitmap();
        bitmap.recycle();
        return cameraBitmapPath;
    }

    /**
     * @return The scaled bitmap as per builder
     */
    public Bitmap getCameraBitmap() {
        return resizeAndGetCameraBitmap(imageHeight);
    }

    /**
     * @param imageHeight
     * @return Bitmap path with approx desired height
     */
    public String resizeAndGetCameraBitmapPath(int imageHeight) {
        Bitmap bitmap = resizeAndGetCameraBitmap(imageHeight);
        bitmap.recycle();
        return cameraBitmapPath;
    }

    /**
     * @param imageHeight
     * @return Bitmap with approx desired height
     */
    public Bitmap resizeAndGetCameraBitmap(int imageHeight) {
        try {
            if (cameraBitmap != null) {
                cameraBitmap.recycle();
            }
            cameraBitmap = Utils.decodeFile(new File(cameraBitmapPath), imageHeight);
            if (cameraBitmap != null) {
                if (isCorrectOrientationRequired) {
                    cameraBitmap = Utils.rotateBitmap(cameraBitmap, Utils.getImageRotation(cameraBitmapPath));
                }
                Utils.saveBitmap(cameraBitmap, cameraBitmapPath, imageType, compression);
            }
            return cameraBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deletes the saved camera image
     */
    public void deleteImage() {
        if (cameraBitmapPath != null) {
            File image = new File(cameraBitmapPath);
            if (image.exists()) {
                image.delete();
            }
        }
    }

    /**
     * Camera builder declaration
     */
    public class CameraBuilder {
        public CameraBuilder setDirectory(String dirName) {
            Camera.this.dirName = dirName;
            return this;
        }

        public CameraBuilder setTakePhotoRequestCode(int requestCode) {
            Camera.REQUEST_TAKE_PHOTO = requestCode;
            return this;
        }

        public CameraBuilder setName(String imageName) {
            Camera.this.imageName = imageName;
            return this;
        }

        public CameraBuilder resetToCorrectOrientation(boolean reset) {
            Camera.this.isCorrectOrientationRequired = reset;
            return this;
        }

        public CameraBuilder setImageFormat(String imageFormat) {
            if (imageFormat == null) {
                Camera.this.imageType = IMAGE_FORMAT_JPG;
            }
            if (imageFormat.equals("png") || imageFormat.equals("PNG") || imageFormat.equals(".png")) {
                Camera.this.imageType = IMAGE_FORMAT_PNG;
            } else if (imageFormat.equals("jpg") || imageFormat.equals("JPG") || imageFormat.equals(".jpg")) {
                Camera.this.imageType = IMAGE_FORMAT_JPG;
            } else if (imageFormat.equals("jpeg") || imageFormat.equals("JPEG") || imageFormat.equals(".jpeg")) {
                Camera.this.imageType = IMAGE_FORMAT_JPEG;
            } else {
                Camera.this.imageType = IMAGE_FORMAT_JPG;
            }
            return this;
        }

        public CameraBuilder setImageHeight(int imageHeight) {
            Camera.this.imageHeight = imageHeight;
            return this;
        }

        public CameraBuilder setCompression(int compression) {
            if (compression > 100) {
                compression = 100;
            } else if (compression < 0) {
                compression = 0;
            }
            Camera.this.compression = compression;
            return this;
        }
    }
}

