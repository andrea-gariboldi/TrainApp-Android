package com.example.maledettatreest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Model {
    private static Model theInstance = null;
    private JSONObject lines;
    public ArrayList<Tratta> tratte = new ArrayList<>();
    public JSONArray posts;
    Tratta nomeTrattaSelezionata;
    public ArrayList<StazioneMappa> stazioniMappa = new ArrayList<>();
    String sid;

    public static synchronized Model getInstance() {
        if (theInstance == null) {
            theInstance = new Model();
        }
        return theInstance;
    }

    private Model() {
        posts = new JSONArray();
    }

    public void saveLines(JSONObject response){
        lines = response;
    }

    public JSONObject getLines() {
        return lines;
    }

    public void popolaLineeStringa(JSONObject response) throws JSONException {
        JSONArray js = response.getJSONArray("lines");
        for (int i = 0; i < js.length(); i++) {
           String nome1 = js.getJSONObject(i).getJSONObject("terminus1").getString("sname");
           String nome2 = js.getJSONObject(i).getJSONObject("terminus2").getString("sname");
           int did1 = js.getJSONObject(i).getJSONObject("terminus1").getInt("did");
            int did2 = js.getJSONObject(i).getJSONObject("terminus2").getInt("did");

            Stazione stazione1 = new Stazione(nome1, did1);
            Stazione stazione2 = new Stazione(nome2, did2);

            Tratta tratta1 = new Tratta(stazione1, stazione2);
            Tratta tratta2 = new Tratta(stazione2, stazione1);

            tratte.add(tratta1);
            tratte.add(tratta2);


        }
    }

    public int getSize() {
        return tratte.size();
    }



    public void parseResponsePost(JSONObject response) throws JSONException {
        JSONArray allPosts = response.getJSONArray("posts");
        posts = new JSONArray();
        for (int i = 0; i < allPosts.length(); i++) {
            if ( !allPosts.getJSONObject(i).has("status") && !allPosts.getJSONObject(i).has("delay") && !allPosts.getJSONObject(i).has("comment")){
                continue;
            } else {
                posts.put(allPosts.getJSONObject(i));
            }
        }





    }

    public int getSizePosts() {
        return posts.length();
    }
}
