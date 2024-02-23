package com.example.maledettatreest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private LayoutInflater mInflater;
    private OnRecyclerViewClickListener mRecyclerViewClickListener;

    public MyAdapter(Context context ,OnRecyclerViewClickListener recyclerViewClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mRecyclerViewClickListener = recyclerViewClickListener;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_row, parent, false);
        return new MyViewHolder(view , mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String linea = Model.getInstance().tratte.get(position).tname;
        Log.d("Dimensione model", linea);
        holder.updateContent(linea);
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSize();
    }
}
