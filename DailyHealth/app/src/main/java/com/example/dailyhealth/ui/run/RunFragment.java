package com.example.dailyhealth.ui.run;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dailyhealth.R;
import com.example.dailyhealth.viewmodel.RunViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RunFragment extends Fragment{
	RunViewModel viewModel;
	FloatingActionButton fab;
	RecyclerView rvRuns;
	RunFragmentAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View fragmentRun = inflater.inflate(R.layout.fragment_run, container, false);
		viewModel = new ViewModelProvider(this).get(RunViewModel.class);

		fab = fragmentRun.findViewById(R.id.fab);

		rvRuns = fragmentRun.findViewById(R.id.rvRuns);
		adapter = new RunFragmentAdapter();
		rvRuns.setAdapter(adapter);
		rvRuns.setLayoutManager(new LinearLayoutManager(getContext()));

		viewModel.runList.observe(getViewLifecycleOwner(), runList->{
			adapter.submitList(runList);
		});


		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// NavController는 프래그먼트에서는 findNavController 를 통해서 가져올 수 있다
				Navigation.findNavController(fragmentRun).navigate(R.id.trackingFragment);

			}
		});

		return fragmentRun;
		
	}

}
