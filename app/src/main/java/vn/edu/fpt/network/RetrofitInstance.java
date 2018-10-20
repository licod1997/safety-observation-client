package vn.edu.fpt.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String COFFEE_HOUSE_URL = "http://10.240.10.126:8080";

    private static final String BASE_HOME_URL = "http://192.168.1.109:8080";

    private static final String BASE_SCHOOL_URL = "http://10.82.135.137:8080";



    /**
     * Create an instance of Retrofit object
     * */


    public static Retrofit getRetrofitInstance() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setLenient().create();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_HOME_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}