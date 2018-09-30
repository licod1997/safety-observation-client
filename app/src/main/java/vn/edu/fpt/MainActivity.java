package vn.edu.fpt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.app_interface.DemoAPI;
import vn.edu.fpt.model.DemoDTO;
import vn.edu.fpt.network.RetrofitInstance;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtResponse;
    private Button btnLogin,btnFeedback;
    private EditText edtUsername, edtPassword;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        txtResponse = (TextView) findViewById( R.id.txtResponse );
        edtUsername = (EditText) findViewById( R.id.edtUsername );
        edtPassword = (EditText) findViewById( R.id.edtPassword );
        btnLogin = (Button) findViewById( R.id.btnLogin );
        btnFeedback = (Button)findViewById(R.id.btnFeedback);


        btnLogin.setOnClickListener(this);
        btnFeedback.setOnClickListener(this);
    }

    private void login() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        DemoDTO demoDTO = new DemoDTO();
        demoDTO.setUsername( username );
        demoDTO.setPassword( password );

        DemoAPI service = RetrofitInstance.getRetrofitInstance().create( DemoAPI.class );
        Call<Boolean> call = service.login( demoDTO );

//        Log.wtf( "URL Called", call.request().url() + "" );

        call.enqueue( new Callback<Boolean>() {
            @Override
            public void onResponse( Call<Boolean> call, Response<Boolean> response ) {
                txtResponse.setText( "Response: " + response.body() );
            }

            @Override
            public void onFailure( Call<Boolean> call, Throwable t ) {
                Toast.makeText( MainActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnFeedback:
                Intent intent = new Intent(this, SendFeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }
}
