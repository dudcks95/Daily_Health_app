package com.example.dailyhealth.ui.run;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dailyhealth.R;
import com.example.dailyhealth.db.Run;
import com.example.dailyhealth.viewmodel.RunViewModel;
import com.example.dailyhealth.util.TrackingUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RunFragmentAdapter extends RecyclerView.Adapter<RunFragmentAdapter.RunViewHolder> {

    ArrayList<Run> runArrayList;
    DiffUtil.ItemCallback<Run> diffCallback = new DiffUtil.ItemCallback<Run>() {

        @Override
        public boolean areItemsTheSame(@NonNull Run oldItem, @NonNull Run newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Run oldItem, @NonNull Run newItem) {
            return oldItem.hashCode() == newItem.hashCode();
        }

    };

    AsyncListDiffer<Run> differ = new AsyncListDiffer<Run>(this, diffCallback);
    public void submitList(List<Run> runList){
        differ.submitList(runList);
    }

    @NonNull
    @Override
    public RunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View runItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_run, parent, false);
        return new RunViewHolder(runItemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RunViewHolder holder, int position) {
        Run run = differ.getCurrentList().get(position);
        // holder에 계속 set
        Glide.with(holder.itemView)
                .load(run.getImg())
                .into(holder.ivRunImage);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(run.getTimestamp());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        holder.tvDate.setText(dateFormat.format(calendar.getTime()));

        holder.tvDistance.setText(run.getDistanceInMeters()/1000f+ "km");
        holder.tvTime.setText(
                TrackingUtility.getFormattedStopWatch(run.getTimeInMillis(),true)
        );

        //        holder.tvDate.setText(run.());
//        holder.tvTime.setText(run.getMsg());
//        holder.tvDistance.setText(run.getMsg());
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size() ;
    }


    class RunViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRunImage;
        TextView tvDate, tvTime, tvDistance;

        public RunViewHolder(@NonNull View itemView) {
            super(itemView);

            ivRunImage = itemView.findViewById(R.id.ivRunImage);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDistance = itemView.findViewById(R.id.tvDistance);

        }


    }

}
