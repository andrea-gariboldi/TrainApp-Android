package com.example.maledettatreest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BachecaActivity extends AppCompatActivity {
    public String QualeLinea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacheca);
        //creo il model
        Model m = Model.getInstance();

        //apro il DB
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user").build();



        //lista post
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapterPost adapter = new MyAdapterPost(this);

        TextView tvNomeLinea = findViewById(R.id.tv_nomeLinea);



        CommunicationController cc = new CommunicationController(this);

        //bottone per tornare alla home
        findViewById(R.id.btnTornaHome).setOnClickListener(v -> {
            m.posts= new JSONArray();
            Intent intent = new Intent(this, MainActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
        });

        //bottone per gestire la mappa
        findViewById(R.id.btnInfo).setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
        });

        //bottone per invertire tratta
        findViewById(R.id.btnInverti).setOnClickListener(v -> {
            new Thread(()->{
            Response.Listener<JSONObject> responseListenerPostsRefresh = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("PostTratta", response.toString());
                    try {
                        m.parseResponsePost(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerView.setAdapter(adapter);
                }
            };

                String sid = db.userDao().restituisciSid();
                cc.getPosts( sid, m.nomeTrattaSelezionata.s2.terminus_did, responseListenerPostsRefresh);
                m.nomeTrattaSelezionata = new Tratta(m.nomeTrattaSelezionata.s2, m.nomeTrattaSelezionata.s1);
            }).start();

            tvNomeLinea.setText(m.nomeTrattaSelezionata.tname);
        });

        if (m.getSizePosts() == 0) {
            //ricevo la linea cliccata (cosi posso fare la richiesta di rete)
            Intent myIntent = getIntent();
            int lineaClicked = Integer.parseInt(myIntent.getStringExtra("lineaClicked"));
            m.nomeTrattaSelezionata = m.tratte.get(lineaClicked);
            tvNomeLinea.setText(m.nomeTrattaSelezionata.tname);



            Response.Listener<JSONObject> responseListenerPosts = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("PostTratta", response.toString());
                    try {
                        m.parseResponsePost(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerView.setAdapter(adapter);
                }
            };
            //richiesta di rete
            new Thread(()-> {
                String sid = db.userDao().restituisciSid();
                cc.getPosts(sid, m.tratte.get(lineaClicked).s1.terminus_did, responseListenerPosts);
            }).start();


        } else {
            recyclerView.setAdapter(adapter);
           tvNomeLinea.setText(m.nomeTrattaSelezionata.tname);
        }

    }
}