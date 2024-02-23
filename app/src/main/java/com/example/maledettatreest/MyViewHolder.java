package com.example.maledettatreest;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView lineaName;
    private OnRecyclerViewClickListener mRecyclerViewClickListener;

    public MyViewHolder(@NonNull View itemView , OnRecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        lineaName = itemView.findViewById(R.id.linea);
        mRecyclerViewClickListener = recyclerViewClickListener;
        itemView.setOnClickListener(this);
    }

    public void updateContent(String lineaName){
        this.lineaName.setText(lineaName);
    }

    public void onClick(View view){
        mRecyclerViewClickListener.onRecyclerViewClick(view, getAdapterPosition());
        Log.d("ViewHolder", "OnClik on Element: " + lineaName.getText().toString());
    }
}
