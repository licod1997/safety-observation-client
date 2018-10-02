package vn.edu.fpt;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
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
    private Button btnChooseImage, btnSendFeedback;
    private ImageView imageView1,imageView2,imageView3,imageView4;
    private EditText edtFeedbackDescription;

    private List<File> listImageSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        imageView1 =findViewById(R.id.imageView1);
        imageView2 =findViewById(R.id.imageView2);
        imageView3 =findViewById(R.id.imageView3);
        imageView4 =findViewById(R.id.imageView4);

        edtFeedbackDescription = findViewById(R.id.edtFeedbackDescription);

        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);

        btnChooseImage.setOnClickListener(this);
        btnSendFeedback.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        EasyImage.configuration(getApplication()).setAllowMultiplePickInGallery(true);

        switch (view.getId()){
            case R.id.btnChooseImage:
                EasyImage.openGallery(SendFeedbackActivity.this, 0);
                break;
            case R.id.btnSendFeedback:
                sendFeedback();
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
                //Handle the images
                if(imagesFiles.size()!=4){
                    Toast.makeText(SendFeedbackActivity.this,"Chọn 4 hình",Toast.LENGTH_LONG).show();
                }else{

                    imageView1.setImageBitmap(BitmapFactory.decodeFile(imagesFiles.get(0).getAbsolutePath()));
                    imageView2.setImageBitmap(BitmapFactory.decodeFile(imagesFiles.get(1).getAbsolutePath()));
                    imageView3.setImageBitmap(BitmapFactory.decodeFile(imagesFiles.get(2).getAbsolutePath()));
                    imageView4.setImageBitmap(BitmapFactory.decodeFile(imagesFiles.get(3).getAbsolutePath()));

                    for (File fileImage :
                            imagesFiles) {
                        listImageSelected.add(fileImage);
                    }
                }

            }
        });
    }


    private void sendFeedback(){
        FeedbackPhotoDTO[] listFeedbackPhotoDTO = new FeedbackPhotoDTO[4];
        int i =0;
        for (File fileImage: listImageSelected) {
            uploadImage(fileImage);        //UploadFile -> maybe return Boolean

            FeedbackPhotoDTO feedbackPhotoDTO = new FeedbackPhotoDTO();
            feedbackPhotoDTO.setPhotoName(fileImage.getName());
            feedbackPhotoDTO.setPhotoDirectory("upload");
            listFeedbackPhotoDTO[i]= feedbackPhotoDTO;
            i++;
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
                Toast.makeText(SendFeedbackActivity.this,"Successfully",Toast.LENGTH_LONG).show();
                //clear
                imageView1.setImageResource(R.drawable.ic_launcher_background);
                imageView2.setImageResource(R.drawable.ic_launcher_background);
                imageView3.setImageResource(R.drawable.ic_launcher_background);
                imageView4.setImageResource(R.drawable.ic_launcher_background);
                edtFeedbackDescription.setText("");
                listImageSelected.clear();


            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(SendFeedbackActivity.this,"Failed",Toast.LENGTH_LONG).show();

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
}
