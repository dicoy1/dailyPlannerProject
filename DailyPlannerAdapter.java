package com.daillyplanner.dailyplanner.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.daillyplanner.dailyplanner.AddNewTask;
import com.daillyplanner.dailyplanner.MainActivity;
import com.daillyplanner.dailyplanner.Model.DailyPlannerModel;
import com.daillyplanner.dailyplanner.R;
import com.daillyplanner.dailyplanner.Utils.DatabaseHandler;

import java.util.List;

public class DailyPlannerAdapter extends RecyclerView.Adapter<DailyPlannerAdapter.ViewHolder> {

    private List<DailyPlannerModel> dailyPlannerList ;
    private MainActivity activity;

    private DatabaseHandler db;


    public DailyPlannerAdapter(DatabaseHandler db,MainActivity activity){
        this.db = db;
        this.activity = activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_task,parent,false);
        return new ViewHolder(itemView);
    }


    public void onBindViewHolder(ViewHolder holder,int position){
        db.openDataBase();
        DailyPlannerModel item = dailyPlannerList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }
    public int getItemCount(){
        return dailyPlannerList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setTasks(List<DailyPlannerModel>dailyPlannerList){
        this.dailyPlannerList = dailyPlannerList ;
        notifyDataSetChanged();
    }
    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        DailyPlannerModel item = dailyPlannerList.get(position);
        db.deleteTask(item.getId());
        dailyPlannerList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        DailyPlannerModel item =  dailyPlannerList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.checkbox);
        }
    }
}
