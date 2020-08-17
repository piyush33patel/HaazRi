package com.example.navigationhaazrai;

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
        public TextView subject, total, present, absent, percent, message;
        public ViewAttendanceHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject);
            total = itemView.findViewById(R.id.total);
            present = itemView.findViewById(R.id.present);
            absent = itemView.findViewById(R.id.absent);
            percent = itemView.findViewById(R.id.percent);
            message = itemView.findViewById(R.id.message);
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
        holder.message.setText(currentItem.getmMessgae());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}

