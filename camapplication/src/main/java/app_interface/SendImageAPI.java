package app_interface;

import model.Base64Image;
import model.DetectionResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface SendImageAPI {
    @POST("/upload_file/")
    Call<List<DetectionResult>> uploadImage( @Body Base64Image image);

}
