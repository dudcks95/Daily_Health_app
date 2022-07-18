package com.example.dailyhealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dailyhealth.model.User;
import com.example.dailyhealth.service.UserService;
import com.example.dailyhealth.ui.user.UserClient;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_register extends AppCompatActivity {
    private String strName, strEmail, strGender;
    Button registerBtn;
    private TextView back;
    private EditText e_height, e_weight,e_name,e_email,e_gender;
    UserService userService = UserClient.getInstance().getUserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        registerBtn = findViewById(R.id.registerBtn);

        back = findViewById(R.id.back);

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
                Log.d("btn register>>>>", "click");
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
                        Log.d("registert>>>>", "call enqueue");
                        Intent intent1 = new Intent(MainActivity_register.this, MainActivity.class);
                        intent1.putExtra("name",strName);
                        intent1.putExtra("height",Integer.parseInt(e_height.getText().toString()));
                        intent1.putExtra("weight",Integer.parseInt(e_weight.getText().toString()));
                        Log.d(">>>>", "before startactivity");

                        startActivity(intent1);
                        Log.d("registert>>>>", "call enqueue");
                        Toast.makeText(MainActivity_register.this, "회원등록완료", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("fail>>>>", ""+ t.getLocalizedMessage() + "//" + t.getMessage());
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
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