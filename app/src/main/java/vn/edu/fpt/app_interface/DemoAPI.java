package vn.edu.fpt.app_interface;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import vn.edu.fpt.model.DemoDTO;

public interface DemoAPI {
    @POST( "login" )
    @Headers( "Content-Type: application/json" )
    Call<Boolean> login( @Body DemoDTO demoDTO );
}
