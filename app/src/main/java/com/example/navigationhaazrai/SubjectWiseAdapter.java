package com.example.navigationhaazrai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectWiseAdapter extends RecyclerView.Adapter<SubjectWiseAdapter.SubjectWiseViewHolder> {

    private ArrayList<SubjectWiseItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public  static class SubjectWiseViewHolder extends RecyclerView.ViewHolder {
        public ImageView mDeleteImage;
        public TextView mTextView1, mTextView2, mTextView3;
        public SubjectWiseViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
            mDeleteImage = itemView.findViewById(R.id.iv_delete);

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public SubjectWiseAdapter(ArrayList<SubjectWiseItem> exampleList){
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public SubjectWiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_wise_item, parent, false);
        SubjectWiseViewHolder exampleViewHolder = new SubjectWiseViewHolder(view,mListener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectWiseViewHolder holder, int position) {
        SubjectWiseItem currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem.getmText1());
        holder.mTextView2.setText(currentItem.getmText2());
        holder.mTextView3.setText(currentItem.getmText3());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}

