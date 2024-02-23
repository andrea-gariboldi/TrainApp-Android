package com.example.maledettatreest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class CommunicationController {
    private RequestQueue queue = null;
    public static final String BASE_URL = "https://ewserver.di.unimi.it/mobicomp/treest/";

    public CommunicationController(Context context){
        queue = Volley.newRequestQueue(context);
    }

        //private static final String GET_LINES_URL = BASE_URL +"getLines.php";
        public void basicRequest (String endpoint, JSONObject request, Response.Listener<JSONObject> responseListener){
            final String URL = BASE_URL + endpoint + ".php";
            Log.d("Richiesta", URL);
            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    request,
                    responseListener,
                    error ->{
                        Log.d("Richiesta", "Error: "+ error.toString());
                        error.printStackTrace();
                    }
            );
            queue.add(req);
        }

        public void getLines(String sid, Response.Listener<JSONObject> responseListener){
            JSONObject js = new JSONObject();
            try{
                js.put("sid", sid);
            }catch (JSONException e){
                e.printStackTrace();
            }
            basicRequest("getLines", js, responseListener);
        }

        public void getPosts(String sid, int did, Response.Listener<JSONObject> responseListener){
            JSONObject js = new JSONObject();
            try{
                js.put("sid", sid);
                js.put("did", did);
            }catch (JSONException e){
                e.printStackTrace();
            }
            basicRequest("getPosts", js, responseListener);
        }

    public void getStations(String sid, int did, Response.Listener<JSONObject> responseListener){
        JSONObject js = new JSONObject();
        try{
            js.put("sid", sid);
            js.put("did", did);
        }catch (JSONException e){
            e.printStackTrace();
        }
        basicRequest("getStations", js, responseListener);
    }

    public void register(Response.Listener<JSONObject> responseListener){
        JSONObject js = new JSONObject();
        basicRequest("register", js, responseListener);
    }

    public void getProfile(String sid, Response.Listener<JSONObject> responseListener){
        JSONObject js = new JSONObject();
        try{
            js.put("sid", sid);
        }catch (JSONException e){
            e.printStackTrace();
        }
        basicRequest("getProfile", js, responseListener);
    }


    public void follow(String sid, String uid, Response.Listener<JSONObject> responseListener){
        JSONObject js = new JSONObject();
        try{
            js.put("sid", sid);
            js.put("uid", uid);
        }catch (JSONException e){
            e.printStackTrace();
        }
        basicRequest("follow", js, responseListener);
    }

    public void unFollow(String sid, String uid, Response.Listener<JSONObject> responseListener){
        JSONObject js = new JSONObject();
        try{
            js.put("sid", sid);
            js.put("uid", uid);
        }catch (JSONException e){
            e.printStackTrace();
        }
        basicRequest("unfollow", js, responseListener);
    }

    public void setProfileImage(String sid, String picture, Response.Listener<JSONObject> responseListener){
        JSONObject js = new JSONObject();
        try{
            js.put("sid", sid);
            js.put("picture", picture);
        }catch (JSONException e){
            e.printStackTrace();
        }
        basicRequest("setProfile", js, responseListener);
    }

    public void setProfileName(String sid, String newName, Response.Listener<JSONObject> responseListener){
        JSONObject js = new JSONObject();
        try{
            js.put("sid", sid);
            js.put("name", newName);
        }catch (JSONException e){
            e.printStackTrace();
        }
        basicRequest("setProfile", js, responseListener);
    }

    public void getUserPicture(String sid, String uid, Response.Listener<JSONObject> responseListener){
        JSONObject js = new JSONObject();
        try{
            js.put("sid", sid);
            js.put("uid", uid);
        }catch (JSONException e){
            e.printStackTrace();
        }
        basicRequest("getUserPicture", js, responseListener);
    }

}
