package com.example.maledettatreest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class InfoLineaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_linea);

        //bottone per tornare indietro
        findViewById(R.id.btnTornaHome2).setOnClickListener(v ->{
            Intent intent = new Intent(this, BachecaActivity.class);
            overridePendingTransition(0,0);
            startActivity(intent);
        });
    }

}