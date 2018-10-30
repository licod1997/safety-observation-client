package vn.edu.fpt;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera ;
    SurfaceHolder holder;

    public ShowCamera(Context context, Camera camera)  {
        super(context);
        this.camera = camera;
        this.holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {



        Camera.Parameters params = camera.getParameters();
        //forcus mode
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);


        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size mSize = sizes.get(0);
        for(int i=0;i<sizes.size();i++)
        {
            if(sizes.get(i).width > mSize.width)
                mSize = sizes.get(i);
        }
    //Change the orientation of the camera
        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            params.set("orientation","portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        }else{
            params.set("orientation","landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);
        }

        params.setPictureSize(mSize.width,mSize.height);
        params.setPictureFormat(ImageFormat.JPEG);
        camera.setParameters(params);


        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;

    }
}
