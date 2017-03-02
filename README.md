# ParaCamera
[ ![Download](https://api.bintray.com/packages/janishar/mindorks/paracamera/images/download.svg) ](https://bintray.com/janishar/mindorks/paracamera/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ParaCamera-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4415)

##Simple android camera to capture and get bitmaps in three simple steps:

##1. Build Camera
```java
// Create global camera reference in an activity or fragment
Camera camera;

// Build the camera   
camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio; 
                .build(this);
```
##2. Capture Image
```java
// Call the camera takePicture method to open the existing camera             
        try {
            camera.takePicture();
        }catch (Exception e){
            e.printStackTrace();
        }
```
##3. Get bitmap and saved image path
```java
// Get the bitmap and image path onActivityResult of an activity or fragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Camera.REQUEST_TAKE_PHOTO){
            Bitmap bitmap = camera.getCameraBitmap();
            if(bitmap != null) {
                picFrame.setImageBitmap(bitmap);
            }else{
                Toast.makeText(this.getApplicationContext(),"Picture not taken!",Toast.LENGTH_SHORT).show();
            }
        }
    }   
```
```java
// The bitmap is saved in the app's folder
//  If the saved bitmap is not required use following code
    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }
```

# Gradle
```groovy
compile 'com.mindorks:paracamera:0.2.2'
```

### WRITE_EXTERNAL_STORAGE is required
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
