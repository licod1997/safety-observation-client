package vn.edu.fpt.app_interface;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadImageAPI {
    @POST("uploadImage")
    @Multipart
    Call<String> uploadImage(@Part MultipartBody.Part img);
}
