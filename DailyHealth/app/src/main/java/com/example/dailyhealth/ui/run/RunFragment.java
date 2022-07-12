package com.example.dailyhealth.ui.run;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dailyhealth.R;
import com.example.dailyhealth.ui.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RunFragment extends Fragment{
	MainViewModel viewModel;
	FloatingActionButton fab;
	Fragment TrackingFragment = new TrackingFragment2();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

//		viewModel = new ViewModelProvider(this).get(MainViewModel.class);
		View fragmentRun = inflater.inflate(R.layout.fragment_run, container, false);

		fab = fragmentRun.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// NavController는 프래그먼트에서는 findNavController 를 통해서 가져올 수 있다
				Navigation.findNavController(fragmentRun).navigate(R.id.trackingFragment2);

			}
		});
		return fragmentRun;
		
	}

}
