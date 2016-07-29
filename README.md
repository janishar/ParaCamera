# ParaCamera (Under Development)
##Simple android camera to capture and get bitmaps in three simple steps:

##1. Build Camera
```java


// Create global camera reference in an activity
Camera camera;

// Instantiate the camera
camera = new Camera(this);

// Build the camera
camera.builder()
          .setDirectory("pics")
          .setName("ali_" + System.currentTimeMillis())
          .setImageFormat(Camera.IMAGE_JPEG)
          .setCompression(75)
          .setImageHeight(1000);
              

```
##2. Capture Image
```java

// Call the camera takePicture method to open the existing camera             
camera.takePicture();

```
##3. Get bitmap and saved image path
```java

// Get the bitmap and image path onActivityResult of an activity
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
```java
compile 'com.mindorks:paracamera:0.0.2'
```

# Permissions
### No permission is required for api 19 (KitKat) and above

### For api 18 and below WRITE_EXTERNAL_STORAGE is required
```java
//  For api 18 and less WRITE_EXTERNAL_STORAGE is required
<uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="18" />
```
