package vn.edu.fpt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.Constants;
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

public class SendFeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private final int TAKE_IMAGE = 1;
    private ImageButton imgButtonChoosePhoto, imgButtonCamera;
    private LinearLayout lnLayoutImageView;

    private EditText edtFeedbackDescription;
    private ImagesAdapter imagesAdapter;
    protected RecyclerView recyclerView;
    private ImageView imageLorem;


    ProgressDialog pd;


    private List<File> listImageSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);


        imgButtonCamera = findViewById(R.id.imgButtonCamera);
        imgButtonChoosePhoto = findViewById(R.id.imgButtonChooseImage);
        lnLayoutImageView = findViewById(R.id.lnlayoutImageView);
        edtFeedbackDescription = findViewById(R.id.edtFeedbackDescription);
        recyclerView = findViewById(R.id.recycler_view);
        imageLorem = findViewById(R.id.image_lorem);


        imagesAdapter = new ImagesAdapter(SendFeedbackActivity.this, listImageSelected);
        recyclerView.setLayoutManager(new LinearLayoutManager(SendFeedbackActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imagesAdapter);

        imgButtonChoosePhoto.setOnClickListener(this);
        imgButtonCamera.setOnClickListener(this);

        toolbar();


    }

    @Override
    public void onClick(View view) {
        EasyImage.configuration(getApplication()).setAllowMultiplePickInGallery(true);

        switch (view.getId()) {
            case R.id.imgButtonChooseImage:
                listImageSelected.clear();


                EasyImage.openGallery(SendFeedbackActivity.this, 0);
                imageLorem.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;

            case R.id.imgButtonCamera:
                Intent intent = new Intent(SendFeedbackActivity.this, CameraFeedbackActivity.class);

                startActivityForResult(intent, TAKE_IMAGE);

                imageLorem.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
        }
    }


    //handle selected list image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == TAKE_IMAGE && resultCode == RESULT_OK) {


            Bundle extras=data.getExtras();

            String img_path=extras.getString("img_path");

            File img = new File(img_path);

            listImageSelected.add(img);
            imagesAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(listImageSelected.size() - 1);
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(SendFeedbackActivity.this, "Đã có lỗi xảy ra, xin thử lại!", Toast.LENGTH_LONG).show();
                System.out.println("=======ERROR while picking photo=======");
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imagesFiles);
            }
        });
    }


    private void sendFeedback() {
        if (listImageSelected.size() < 4) {
            Toast.makeText(SendFeedbackActivity.this, "Chọn ít nhất 4 hình về vật thể!", Toast.LENGTH_LONG).show();
            return;
        } else if (edtFeedbackDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(SendFeedbackActivity.this, "Mô tả chi tiết không được để trống!", Toast.LENGTH_LONG).show();
            return;
        }
        pd = new ProgressDialog(SendFeedbackActivity.this); // API <26
        pd.setMessage("Sending Feedback");
        pd.show();
        FeedbackPhotoDTO[] listFeedbackPhotoDTO = new FeedbackPhotoDTO[listImageSelected.size()];

        for (int i = 0; i < listImageSelected.size(); i++) {
            FeedbackPhotoDTO feedbackPhotoDTO = new FeedbackPhotoDTO();
            feedbackPhotoDTO.setPhotoName(listImageSelected.get(i).getName());
            listFeedbackPhotoDTO[i] = feedbackPhotoDTO;
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
                switch (response.body()) {
                    case "empty_list_image":
                        pd.dismiss();
                        Toast.makeText(SendFeedbackActivity.this, "Đã có lỗi xảy ra. Danh sách hình RỖNG", Toast.LENGTH_LONG).show();
                        break;
                    case "exist_image":
                        pd.dismiss();
                        Toast.makeText(SendFeedbackActivity.this, "Đã có lỗi xảy ra. Hình đã có trên hệ thống", Toast.LENGTH_LONG).show();
                        break;
                    case "send_feedback_successfully":
                        //upload file
                        for (File img : listImageSelected) {
                            uploadImage(img);
                        }
                        pd.dismiss();
                        Toast.makeText(SendFeedbackActivity.this, "Gửi phản hồi thành công", Toast.LENGTH_LONG).show();
                        //Clear
                        lnLayoutImageView.setVisibility(View.GONE);
                        edtFeedbackDescription.setText("");
                        listImageSelected.clear();
                        onPhotosReturned(listImageSelected);
                        break;
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pd.dismiss();

                Toast.makeText(SendFeedbackActivity.this, "Đã có lỗi xảy ra.", Toast.LENGTH_LONG).show();

                Log.e("main", "on error is called and the error is  ----> " + t.getMessage());

            }
        });


    }

    private void uploadImage(final File fileImage) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage);

        MultipartBody.Part body = MultipartBody.Part.createFormData("img", fileImage.getName(), requestBody);

        UploadImageAPI serviceUploadImgAPI = RetrofitInstance.getRetrofitInstance().create(UploadImageAPI.class);

        Call<String> callUpload = serviceUploadImgAPI.uploadImage(body);

        callUpload.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                System.out.println("Upload file is successfully");
                System.out.println(response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Upload file is failed");
                Log.e("ERROR: ", t.getMessage());
            }
        });

    }


    private void onPhotosReturned(List<File> returnedPhotos) {
        listImageSelected.addAll(returnedPhotos);
        imagesAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(listImageSelected.size() - 1);
    }

    /*- Toolbar ------------------------------------------------------------------------- */
    public void toolbar() {
        /* Toolbar */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.feedback));

        // Back icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Show
        actionBar.show();

    }


    // Load the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*- On Options Item Selected --------------------------------------------------------- */
    // One of the toolbar icons was clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //
        if (id == R.id.navigationSendFeedback) {
            sendFeedback();
        } else {
            // Back icon clicked
            Intent i = new Intent(SendFeedbackActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
