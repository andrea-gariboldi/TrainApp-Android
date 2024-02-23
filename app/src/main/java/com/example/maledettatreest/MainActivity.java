package com.example.maledettatreest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewClickListener{
   // final String PREFS_NAME = "MyPrefsfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cerchio che gira, segnala il caricamento
        ProgressBar pB = findViewById(R.id.progress_loader);

        //ricevo dalla rete
        CommunicationController cc = new CommunicationController(this);

        //creo il database
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user").build();


        //creo il model
        Model m = Model.getInstance();

        //controllo se è la prima volta che apro l'app –> per ora non
        // serve ( l'ultima graffa dell'else è commentata in fondo)
       /*
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            settings.edit().putBoolean("my_first_time", false).apply();
            Log.d("PrimaVolta", "onCreate: ");
        }else{
            */
            new Thread(() -> {
                Log.d("DimensioneDB", String.valueOf(db.userDao().getCount()));
                if(db.userDao().getCount() == 0) {
                    Response.Listener<JSONObject> responseListenerRegister = response -> {
                        try {
                            String sid = response.getString("sid");
                            Response.Listener<JSONObject> responseListenerProfile = response2 -> {
                                try {
                                    User user = new User(response2.getString("name"), response2.getString("uid"), response2.getString("pversion"), response2.getString("picture"));
                                    Log.d("CostruttoreUser", String.valueOf(user));
                                    UserEntity userEnt = new UserEntity(user, sid);
                                    //questo potrebbe causare problemi, nel caso toglierlo
                                    new Thread(() -> {
                                        db.userDao().insertUser(userEnt);
                                    }).start();
                                    Log.d("CostruttoreUser", String.valueOf(db.userDao().getCount()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            };
                            cc.getProfile(sid, responseListenerProfile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    };
                    cc.register(responseListenerRegister);
                }else{
                    Log.d("DATABASE", "databadse già pieno" +   db.userDao().getCount());

                }
            }).start();
        /*
        }
        */







        //menu sotto, con navigazione
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.activity_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.activity_profilo:
                        startActivity(new Intent(getApplicationContext(), ProfiloActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.activity_home:
                        return true;
                }
                return false;
            }
        });

        //lista linee
        RecyclerView recyclerView = findViewById(R.id.recyclerViewLinee);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this, this);




        //decido cosa fare con la risposta di getLines
        if(m.getSize() == 0){
            Response.Listener<JSONObject> responseListenerLines = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        new Thread(() -> {
                            m.saveLines(response);
                        }).start();
                    try {
                        m.popolaLineeStringa(response);
                        Log.d("popolaLineeStringa", Model.getInstance().tratte.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerView.post(() -> {
                        //il cerchio del caricamento non è più visibile
                            pB.setVisibility(View.GONE);
                        //modifico la view in modo da visualizzare le linee ricevute dalla rete
                        recyclerView.setAdapter(adapter);
                    });
                }
            };
                //richiesta di rete
                cc.getLines("YpbgbyAdECHkJfLR", responseListenerLines);
        }else{
            recyclerView.setAdapter(adapter);
            //il cerchio del caricamento non è più visibile
            pB.setVisibility(View.GONE);
        }
        Log.d("Dimensione", String.valueOf(Model.getInstance().getSize()));
    }
    @Override
    public void onRecyclerViewClick(View v, int position) {
        Log.d("MainActivity", "Tap evento on item: " + position);
        Intent intent = new Intent(this, BachecaActivity.class);
        intent.putExtra("lineaClicked",String.valueOf(position));
        overridePendingTransition(0,0);
        startActivity(intent);
    }
}