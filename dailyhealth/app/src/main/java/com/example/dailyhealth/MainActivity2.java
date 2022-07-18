package com.example.dailyhealth;

import static com.example.dailyhealth.util.Constants.PREFERENCE_FILE_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dailyhealth.model.User;
import com.example.dailyhealth.service.UserService;
import com.example.dailyhealth.ui.user.UserClient;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {
    private ISessionCallback mSessionCallback;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    UserService userService = UserClient.getInstance().getUserService();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getAppKeyHash();

        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        mSessionCallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                //로그인 요청
                UserManagement.getInstance().me(new MeV2ResponseCallback() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        //로그인 실패
                        Toast.makeText(MainActivity2.this, "로그인 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        // 세션 닫힘
                        Toast.makeText(MainActivity2.this, "세션이 닫혔어요.. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        Call<User> call = userService.list(result.getKakaoAccount().getEmail());
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if( response.body().getEmail().equals(result.getKakaoAccount().getEmail())) {
                                    Intent intent2 = new Intent(MainActivity2.this, MainActivity.class);
                                    editor.putString("name", result.getKakaoAccount().getProfile().getNickname());
                                    editor.putString("email",result.getKakaoAccount().getEmail());
                                    editor.apply();
                                    startActivity(intent2);
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.d(">>>>>>>>FFF",t.getMessage() +"//"+ t.getMessage());
                                //로그인 성공
                                Intent intent = new Intent(MainActivity2.this, MainActivity_register.class);
                                Log.d("testtest>>>>", "invoke: id333333=" + result.getKakaoAccount().getGender());
                                intent.putExtra("name", result.getKakaoAccount().getProfile().getNickname());
                                intent.putExtra("email", result.getKakaoAccount().getEmail());
                                intent.putExtra("gender", result.getKakaoAccount().getGender());

                                editor.putString("name", result.getKakaoAccount().getProfile().getNickname());
                                editor.putString("email",result.getKakaoAccount().getEmail());
                                editor.apply();
                                startActivity(intent);
                            }
                        });

//                        Call<List<User>> call = userService.list(result.getKakaoAccount().getEmail());
//                        call.enqueue(new Callback<List<User>>() {
//                            @Override
//                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                                if(response.body().get(user.getUserid()).getEmail()) {
//                                    Intent intent2 = new Intent(MainActivity2.this, MainActivity.class);
//                                    editor.putString("name", result.getKakaoAccount().getProfile().getNickname());
//                                    editor.putString("email",result.getKakaoAccount().getEmail());
//                                    editor.apply();
//                                    startActivity(intent2);
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<List<User>> call, Throwable t) {
//                                //로그인 성공
//                                Intent intent = new Intent(MainActivity2.this, MainActivity_register.class);
//                                Log.d("testtest>>>>", "invoke: id333333=" + result.getKakaoAccount().getGender());
//                                intent.putExtra("name", result.getKakaoAccount().getProfile().getNickname());
//                                intent.putExtra("email", result.getKakaoAccount().getEmail());
//                                intent.putExtra("gender", result.getKakaoAccount().getGender());
//
//                                editor.putString("name", result.getKakaoAccount().getProfile().getNickname());
//                                editor.putString("email",result.getKakaoAccount().getEmail());
//                                editor.apply();
//                                startActivity(intent);
//                            }
//                        });

                    }
                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Toast.makeText(MainActivity2.this, "onSessionOpenFailed", Toast.LENGTH_SHORT).show();
            }
        };
        Session.getCurrentSession().addCallback(mSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    // 카카오 로그인 시 필요한 해시키 얻는 메소드
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(),0));
                Log.d("Hash key",something);
            }
        }catch(Exception e){
            Log.e("name not found",e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data))
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mSessionCallback);
    }
}