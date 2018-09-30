package vn.edu.fpt.app_interface;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import vn.edu.fpt.model.FeedbackDTO;

public interface SendFeedbackAPI {
    @POST( "sendFeedback" )
    @Headers( "Content-Type: application/json" )
    Call<String> sendFeedback(@Body FeedbackDTO feedbackDTO );


}



