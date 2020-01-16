package com.example.navigationhaazrai;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAttendanceAdapter extends RecyclerView.Adapter<ViewAttendanceAdapter.ViewAttendanceHolder> {

    private ArrayList<ViewAttendanceItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public  static class ViewAttendanceHolder extends RecyclerView.ViewHolder{
        public TextView subject, total, present, absent, percent;
        public ViewAttendanceHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject);
            total = itemView.findViewById(R.id.total);
            present = itemView.findViewById(R.id.present);
            absent = itemView.findViewById(R.id.absent);
            percent = itemView.findViewById(R.id.percent);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ViewAttendanceAdapter(ArrayList<ViewAttendanceItem> exampleList){
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ViewAttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_attendance_items, parent, false);
        ViewAttendanceHolder exampleViewHolder = new ViewAttendanceHolder(view,mListener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAttendanceHolder holder, int position) {
        ViewAttendanceItem currentItem = mExampleList.get(position);
        holder.subject.setText(currentItem.getmSubject());
        holder.total.setText(currentItem.getmTotal());
        holder.present.setText(currentItem.getmPresent());
        holder.absent.setText(currentItem.getmAbsent());
        holder.percent.setText(currentItem.getmPercent());
        if(position == 0)
        {
            holder.subject.setTextColor(Color.rgb(126,74,150));
            holder.subject.setTextSize(13);
            holder.total.setTextColor(Color.rgb(126,74,150));
            holder.total.setTextSize(13);
            holder.present.setTextColor(Color.rgb(126,74,150));
            holder.present.setTextSize(13);
            holder.absent.setTextColor(Color.rgb(126,74,150));
            holder.absent.setTextSize(13);
            holder.percent.setTextColor(Color.rgb(126,74,150));
            holder.percent.setTextSize(13);
        }

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}
