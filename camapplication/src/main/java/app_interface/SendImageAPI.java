package app_interface;

import module.Image;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SendImageAPI {
    @POST("uploadImage")
    @Headers( "Content-Type: application/json" )
    Call<String> uploadImage(@Body String  image);

}
