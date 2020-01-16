package com.example.navigationhaazrai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MarkAttendanceAdapter extends RecyclerView.Adapter<MarkAttendanceAdapter.MarkAttendanceViewHolder> {

    private ArrayList<MarkAttendanceItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onPresentClick(int position);
        void onAbsentClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public  static class MarkAttendanceViewHolder extends RecyclerView.ViewHolder {
        public Switch aSwitch, pSwitch;
        public TextView mTextView1;
        public MarkAttendanceViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            aSwitch = itemView.findViewById(R.id.absent);
            pSwitch = itemView.findViewById(R.id.present);
            mTextView1 = itemView.findViewById(R.id.textView1);

            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onAbsentClick(position);
                        }
                    }
                }
            });
            pSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onPresentClick(position);
                        }
                    }
                }
            });


        }
    }

    public MarkAttendanceAdapter(ArrayList<MarkAttendanceItem> exampleList){
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public MarkAttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mark_attendance_items, parent, false);
        MarkAttendanceViewHolder exampleViewHolder = new MarkAttendanceViewHolder(view,mListener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MarkAttendanceViewHolder holder, int position) {
        MarkAttendanceItem currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem.getmText1());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
