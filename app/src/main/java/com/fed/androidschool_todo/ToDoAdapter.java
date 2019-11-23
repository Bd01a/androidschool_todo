package com.fed.androidschool_todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDoSchema.ToDoElement> mActivities;
    private OnDataChanged mOnDataChanged;

    ToDoAdapter(List<ToDoSchema.ToDoElement> activities, OnDataChanged onDataChanged) {
        mActivities = new ArrayList<>(activities);
        mOnDataChanged = onDataChanged;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ToDoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.bind(mActivities.get(position));
    }

    @Override
    public int getItemCount() {
        return mActivities.size();
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;
        CheckBox mCheckBox;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_view);
            mCheckBox = itemView.findViewById(R.id.checkbox_flag);
        }

        void bind(final ToDoSchema.ToDoElement element){
            mTextView.setText(element.getActivity());
            mCheckBox.setChecked(element.isFlag());
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    element.setFlag(!element.isFlag());
                    mOnDataChanged.onUpdate(element);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnDataChanged.onDelete(element, v);
                    return true;
                }
            });
        }
    }

    interface OnDataChanged{
         void onUpdate(ToDoSchema.ToDoElement element);
         void onDelete(ToDoSchema.ToDoElement element, View v);
    }


}
