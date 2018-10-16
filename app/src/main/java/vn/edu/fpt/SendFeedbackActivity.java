package vn.edu.fpt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.Serializable;
import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.app_interface.DemoAPI;
import vn.edu.fpt.app_interface.SendFeedbackAPI;
import vn.edu.fpt.app_interface.UploadImageAPI;
import vn.edu.fpt.model.FeedbackDTO;
import vn.edu.fpt.model.FeedbackPhotoDTO;
import vn.edu.fpt.network.RetrofitInstance;

import static android.widget.Toast.LENGTH_SHORT;

public class SendFeedbackActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String PHOTOS_KEY = "feedback_photos_list";


    private Button btnSendFeedback,btnChooseImage ,btnCamera;
    private ImageButton imgButtonChoosePhoto, imgButtonCamera;
    private LinearLayout lnLayoutImageView;
//    private ImageButton ;

    private EditText edtFeedbackDescription;
    private ImagesAdapter imagesAdapter;
    protected RecyclerView recyclerView;


    ProgressDialog pd ;


    private List<File> listImageSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
//FCM
        FirebaseMessaging.getInstance().subscribeToTopic("foo-bar");




        imgButtonCamera = findViewById(R.id.imgButtonCamera);
        imgButtonChoosePhoto = findViewById(R.id.imgButtonChooseImage);
        lnLayoutImageView = findViewById(R.id.lnlayoutImageView) ;
        lnLayoutImageView.setVisibility(View.GONE);
        edtFeedbackDescription = findViewById(R.id.edtFeedbackDescription);
//        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);
        recyclerView = findViewById(R.id.recycler_view);
//        btnCamera =  findViewById(R.id.btnCamera);


        if (savedInstanceState != null) {
            listImageSelected = (ArrayList<File>) savedInstanceState.getSerializable(PHOTOS_KEY);
        }

        imagesAdapter = new ImagesAdapter(SendFeedbackActivity.this, listImageSelected);
        recyclerView.setLayoutManager(new LinearLayoutManager(SendFeedbackActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imagesAdapter);

       imgButtonChoosePhoto.setOnClickListener(this);
       imgButtonCamera.setOnClickListener(this);
        btnSendFeedback.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        EasyImage.configuration(getApplication()).setAllowMultiplePickInGallery(true);

        switch (view.getId()){
            case R.id.imgButtonChooseImage:
                EasyImage.openGallery(SendFeedbackActivity.this, 0);
                lnLayoutImageView.setVisibility(View.VISIBLE);

                break;
            case R.id.btnSendFeedback:
                sendFeedback();

                break;
            case R.id.imgButtonCamera:
                EasyImage.openCamera(SendFeedbackActivity.this,0);
                lnLayoutImageView.setVisibility(View.VISIBLE);
                break;
        }
    }


//handle selected list image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(SendFeedbackActivity.this,"Something went wrong, select again plesase",Toast.LENGTH_LONG).show();
                System.out.println("=======ERROR while picking photo=======");
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imagesFiles);


            }
        });
    }


    private void sendFeedback(){
        if(listImageSelected.size()<4){
            Toast.makeText(SendFeedbackActivity.this,"Chọn ít nhất 4 hình",Toast.LENGTH_LONG).show();
            return;
        }
        pd = new ProgressDialog(SendFeedbackActivity.this); // API <26
        pd.setMessage("Sending Feedback");
        pd.show();
        FeedbackPhotoDTO[] listFeedbackPhotoDTO = new FeedbackPhotoDTO[listImageSelected.size()];

        for (int i= 0 ; i <listImageSelected.size(); i++) {
            FeedbackPhotoDTO feedbackPhotoDTO = new FeedbackPhotoDTO();
            feedbackPhotoDTO.setPhotoName(listImageSelected.get(i).getName());
            listFeedbackPhotoDTO[i]= feedbackPhotoDTO;
        }


        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setFeedbackDescription(edtFeedbackDescription.getText().toString().trim());
        feedbackDTO.setFeedbackPhotoList(listFeedbackPhotoDTO);
        feedbackDTO.setTime(Calendar.getInstance().getTimeInMillis());

        SendFeedbackAPI serviceFeedbackAPI = RetrofitInstance.getRetrofitInstance().create(SendFeedbackAPI.class);
        Call<String> call = serviceFeedbackAPI.sendFeedback(feedbackDTO);

////        Log.wtf("URL Called", .request().url() + "" );



        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                //upload file
                for (File img: listImageSelected) {
                    uploadImage(img);
                }
                pd.dismiss();

                Toast.makeText(SendFeedbackActivity.this,"Send feedback successfully",Toast.LENGTH_LONG).show();
                //Clear
                lnLayoutImageView.setVisibility(View.GONE);
                edtFeedbackDescription.setText("");
                listImageSelected.clear();
                onPhotosReturned(listImageSelected);


            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pd.dismiss();

                Toast.makeText(SendFeedbackActivity.this,"Send Failed",Toast.LENGTH_LONG).show();

                Log.e("main", "on error is called and the error is  ----> " + t.getMessage());
            }
        });


    }

    private void uploadImage(final File fileImage){

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage);

        MultipartBody.Part body = MultipartBody.Part.createFormData("img", fileImage.getName(), requestBody);
        UploadImageAPI serviceUploadImgAPI = RetrofitInstance.getRetrofitInstance().create(UploadImageAPI.class);

        Call<String> callUpload = serviceUploadImgAPI.uploadImage(body);

        callUpload.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Upload file is successfully");
                System.out.println(fileImage.getName()+"");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Upload file is fialed");
                Log.e("ERROR: ", t.getMessage());
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PHOTOS_KEY, (Serializable) listImageSelected);
    }

    private void onPhotosReturned(List<File> returnedPhotos) {
        listImageSelected.addAll(returnedPhotos);
        imagesAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(listImageSelected.size() - 1);
    }
}
