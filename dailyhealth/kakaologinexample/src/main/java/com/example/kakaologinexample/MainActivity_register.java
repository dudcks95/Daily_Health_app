package com.example.kakaologinexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kakaologinexample.model.User;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_register extends AppCompatActivity {
    private String strName, strEmail, strGender;
    private TextView back;
    private EditText e_height, e_weight,e_name,e_email,e_gender;
    User user;
    UserService userService = UserClient.getInstance().getUserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        Button registerBtn = findViewById(R.id.registerBtn);

        e_name = findViewById(R.id.e_name);
        e_email = findViewById(R.id.e_email);
        e_gender = findViewById(R.id.e_gender);

        e_height = findViewById(R.id.e_height);
        e_weight = findViewById(R.id.e_weight);

        Intent intent = getIntent();
        strName = intent.getStringExtra("name");
        strEmail = intent.getStringExtra("email");
        strGender = intent.getExtras().get("gender").toString();

        e_name.setText(strName);
        e_email.setText(strEmail);
        if(strGender.equals("MALE")){
            e_gender.setText("남자");
        }else if(strGender.equals("FEMALE")){
            e_gender.setText("여자");
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                User user = new User(
                        e_name.getText().toString(),
                        e_email.getText().toString(),
                        Integer.parseInt(e_height.getText().toString()),
                        Integer.parseInt(e_weight.getText().toString()),
                        e_gender.getText().toString());
                Call<User> call = userService.save(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Intent intent1 = new Intent(MainActivity_register.this, MessengerActivity.class);
                        startActivity(intent1);

                        Toast.makeText(MainActivity_register.this, "회원등록완료", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        finish();
                    }
                });
            }
        });


    }
}