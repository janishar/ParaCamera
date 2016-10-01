# ParaCamera
[ ![Download](https://api.bintray.com/packages/janishar/mindorks/paracamera/images/download.svg) ](https://bintray.com/janishar/mindorks/paracamera/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ParaCamera-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4415)

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

# Recent Libraries: 
#[`PlaceHolderView`](https://github.com/janishar/PlaceHolderView)
#### `PlaceHolderView` helps create views without any adapter in a very modular form. It uses the power of RecyclerView and enhances it to another level. For the first time with the list view comes card stack view.

#[`JPost`](https://https://github.com/janishar/JPost)
#### JPost is a pubsub library based on massages over a channel. It's very efficient and much powerful than other pubsub libraries. It prevents memory leak and increases code control. Also, provide a mechanism to run code asynchronously.

#### Why should you use `JPost` library
1. In contrast to the existing pub-sub libraries, it hold the subscribers with weakreference. Thus it doesn't create memory leaks.
2. Single message can be sent to selected subscribes. This avoids the problem of event getting received at undesirable places. Thus minimising the chances of abnormal application behaviour.
3. The subscriber addition can be controlled by using private channels. It minimises the chances of adding subscribes by mistake to receive undesirable messages.
4. It is a tiny library < 55kb . Thus not effecting the application overall size.
5. It facilicates synchronous as well as asynchronous message delivery and processing.
6. It provides a mechanism to run code asynchronously.

# Permissions
### No permission is required for api 19 (KitKat) and above

### For api 18 and below WRITE_EXTERNAL_STORAGE is required
```java
//  For api 18 and less WRITE_EXTERNAL_STORAGE is required
<uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="18" />
```
