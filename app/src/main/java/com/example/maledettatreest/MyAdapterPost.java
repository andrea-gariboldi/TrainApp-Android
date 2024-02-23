package com.example.maledettatreest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

public class MyAdapterPost extends RecyclerView.Adapter<MyViewHolderPost>{
    private LayoutInflater mInflater;

    public MyAdapterPost(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.single_post, parent, false);
            return new MyViewHolderPost(view);
    }

    public String FiltraJSONST ( int position, String keyword){
       JSONArray posts = Model.getInstance().posts;
       String valore_contenuto = "-1";
       try {
           if (posts.getJSONObject(position).has(keyword)) {
               valore_contenuto= posts.getJSONObject(position).getString(keyword);
           } else {
           }
       }catch (JSONException e) {
               e.printStackTrace();
           }
       return valore_contenuto;
    }

    public Integer FiltraJSONINT ( int position, String keyword){
        JSONArray posts = Model.getInstance().posts;
        int valore_contenuto = -1 ;
        try {
            if (posts.getJSONObject(position).has(keyword)) {
                valore_contenuto= posts.getJSONObject(position).getInt(keyword);
            } else {
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return valore_contenuto;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderPost holder, int position) {
        JSONArray posts = Model.getInstance().posts;
       /* try {
            holder.updateContent(posts.getJSONObject(position).getInt("delay"),
                    posts.getJSONObject(position).getInt("status"),
                    posts.getJSONObject(position).getString("comment"),
                    posts.getJSONObject(position).getString("authorName"),
                    posts.getJSONObject(position).getString("datetime").substring(0,16),
                    posts.getJSONObject(position).getBoolean("followingAuthor"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        try {
            holder.updateContent(FiltraJSONINT(position, "delay"),
                    FiltraJSONINT(position, "status"),
                    FiltraJSONST(position, "comment"),
                    posts.getJSONObject(position).getString("authorName"),
                    posts.getJSONObject(position).getString("datetime").substring(0,16),
                    posts.getJSONObject(position).getBoolean("followingAuthor"),
                    posts.getJSONObject(position).getInt("author"),
                    posts.getJSONObject(position).getInt("pversion")
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSizePosts();
    }
}
