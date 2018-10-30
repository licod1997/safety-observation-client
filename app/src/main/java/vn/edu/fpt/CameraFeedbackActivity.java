package vn.edu.fpt;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.aprilapps.easyphotopicker.Constants;

public class CameraFeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton captureImage ;
    Camera camera ;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    ImageView tuorial_360 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_feedback);
        frameLayout = findViewById(R.id.frameLayout);
        //open camera
        camera = Camera.open();
        captureImage = findViewById(R.id.captureImage);
        showCamera = new ShowCamera(this,camera);
        tuorial_360=findViewById(R.id.tutorial_360);
        frameLayout.addView(showCamera);
        captureImage.setOnClickListener(this);
        toolbar();

    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_file = getOutputMediaFile();

            if(picture_file == null){
               return;
            }else {
                try {
                    FileOutputStream fos  = new FileOutputStream(picture_file);
                    fos.write(data);
                    fos.close();
                    Bundle bundle= new Bundle();
                    bundle.putString("img_path", getOutputMediaFile().getAbsolutePath().trim());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,returnIntent);

                    finish();
                    camera.startPreview();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch ( IOException e){
                    e.printStackTrace();
                }

            }

        }
    };



    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();

        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }else{
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");

            return mediaFile;
        }
    }


    public void captureImage(){
        if(camera != null){
            camera.takePicture(null,null,mPictureCallback);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.captureImage:
                captureImage();
                break;
        }
    }

    public void toolbar(){
        /* Toolbar */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Camera Feedback");
        // Back icon
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Show
        actionBar.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

            // Back icon clicked
            Intent i = new Intent(CameraFeedbackActivity.this, SendFeedbackActivity.class);
            startActivity(i);
            finish();

        return super.onOptionsItemSelected(item);
    }
}
