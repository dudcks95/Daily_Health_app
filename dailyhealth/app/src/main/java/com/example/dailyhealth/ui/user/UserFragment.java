package com.example.dailyhealth.ui.user;


import static com.example.dailyhealth.util.Constants.PREFERENCE_FILE_KEY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dailyhealth.MainActivity;
import com.example.dailyhealth.MainActivity_register;
import com.example.dailyhealth.R;
import com.example.dailyhealth.model.User;
import com.example.dailyhealth.service.UserService;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment {
    Button btnLogout, btnUpdate;
    TextView kName, kHeight, kWeight;
    UserService userService = UserClient.getInstance().getUserService();
    Call<User> userList;
    User user1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user, container, false);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);
        String email = sharedPref.getString("email",null);

          /*  String name = sharedPref.getString("name",null);
        int Weight = sharedPref.getInt("weight",1);
        int Height = sharedPref.getInt("height",1);*/


        kName = view.findViewById(R.id.kName);
        kHeight = view.findViewById(R.id.kHeight);
        kWeight = view.findViewById(R.id.kWeight);

        Call<User> call = userService.list(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                kName.setText(response.body().getUsername());
                kHeight.setText(Integer.toString(response.body().getHeight())+"cm");
                kWeight.setText(Integer.toString(response.body().getWeight())+"kg");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                View dialogView = view.inflate(view.getContext(), R.layout.item_user, null);

                EditText eName = dialogView.findViewById(R.id.eName);
                EditText eHeight = dialogView.findViewById(R.id.eHeight);
                EditText eWeight = dialogView.findViewById(R.id.eWeight);
                EditText eGender = dialogView.findViewById(R.id.eGender);



                AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                dlg.setTitle("내 정보 수정");
                dlg.setView(dialogView);
                dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        User userdto = new User(eName.getText().toString(),
                                Integer.parseInt(eHeight.getText().toString()),
                                Integer.parseInt(eWeight.getText().toString()),
                                eGender.getText().toString());


                        Call<User> call = userService.update(email, userdto);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                eName.setText("송현진");
                                eHeight.setText(user1.getHeight());
                                eWeight.setText(user1.getWeight());
                                eGender.setText(user1.getGender());
                                notifyAll();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    }
                });
                dlg.setNegativeButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = userService.delete(email);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                                    @Override
                                    public void onCompleteLogout() {
//                                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.mainActivity2);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
                dlg.show();
            }
        });



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
//                        getActivity().finish();
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.mainActivity2);

                    }
                });
            }
        });
        return view;
    }


}