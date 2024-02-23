package com.example.maledettatreest;

import static java.security.AccessController.getContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Response;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class MyViewHolderPost extends RecyclerView.ViewHolder {
    private TextView delay, status, comment, author, date;
    private Button follow;
    private ImageView profileImage;
    CommunicationController cc = new CommunicationController(itemView.getContext());

    public MyViewHolderPost(@NonNull View itemView) {
        super(itemView);

        delay = itemView.findViewById(R.id.tv_ritardo);
        status = itemView.findViewById(R.id.tv_stato);
        comment = itemView.findViewById(R.id.tv_commento);
        author = itemView.findViewById(R.id.tv_nome_utente);
        date = itemView.findViewById(R.id.tv_data);
        follow = itemView.findViewById((R.id.btn_segui));
        profileImage = itemView.findViewById(R.id.profile_image);
    }



    public void updateContent(int delay, int status, String comment, String author, String date, boolean follow, int authorId, int pversion){

        final AppDatabase db = Room.databaseBuilder(itemView.getContext(),
                AppDatabase.class, "user").build();
        //parso i commenti
         if(delay == -1) { this.delay.setVisibility(View.INVISIBLE); itemView.findViewById(R.id.textView4).setVisibility(View.INVISIBLE);} else { this.delay.setText(String.valueOf(delay));};
         if(status == -1) { this.status.setVisibility(View.INVISIBLE); itemView.findViewById(R.id.textView5).setVisibility(View.INVISIBLE);} else { this.status.setText(String.valueOf(status));};
         if(comment.equals("-1")) { this.comment.setVisibility(View.INVISIBLE);itemView.findViewById(R.id.textView3).setVisibility(View.INVISIBLE);} else { this.comment.setText(comment);};
        this.author.setText(author);
        this.date.setText(date);
       // gestisco i follow/unfollow
        if (follow){
            this.follow.setText("Non Seguire");
        } else {
            this.follow.setText("Segui");
        }
        // gestire il press sul bottone "Segui"
        this.follow.setOnClickListener(v -> {
            // Se il testo sul bottone è SEGUI ( quindi devo seguire)
            if ( this.follow.getText() == "Segui"){
                //thread per gestire risposta e fare richiesta di rete follow
                new Thread(() -> {
                    Response.Listener responselistenerFollow = response -> {
                        Log.d("F/U", "Utente Seguito: "+ authorId);
                    };
                    String sid = db.userDao().restituisciSid();
                    cc.follow(sid, String.valueOf(authorId), responselistenerFollow);
                    Log.d("F/U", "Utente che voglio seguire: "+ authorId);
                }).start();
                //Snackbar per conferma follow
                Snackbar updatedUImage = Snackbar.make(itemView, "Ora segui: " + " [ " + author + " ]", Snackbar.LENGTH_SHORT);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) updatedUImage.getView().getLayoutParams();
                params.gravity = Gravity.TOP;
                updatedUImage.show();
                // cambio il testo del pulsante Follow/Unfollow
                this.follow.setText("Non Seguire");
            } else {
                //thread per gestire risposta e fare richiesta di rete unfollow
                new Thread(() -> {
                    Response.Listener responselistenerunFollow = response -> {
                        Log.d("F/U", "Utente Non Seguito: "+ authorId);
                    };
                    String sid = db.userDao().restituisciSid();
                    cc.unFollow(sid, String.valueOf(authorId), responselistenerunFollow);
                    Log.d("F/U", "Utente che non voglio seguire: "+ authorId);
                }).start();
                //Snackbar per conferma unfollow
                Snackbar updatedUImage = Snackbar.make(itemView, "Ora NON segui: " + " [ " + author + " ]", Snackbar.LENGTH_SHORT);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) updatedUImage.getView().getLayoutParams();
                params.gravity = Gravity.TOP;
                updatedUImage.show();
                // cambio il testo del pulsante Follow/Unfollow
                this.follow.setText("Segui");
            }
        });
                // AGGIORNO UPICTURE COMMENTI

        new Thread(() ->{
            if(db.userDao().restituisciUid(String.valueOf(authorId)) == 0) {
                Response.Listener<JSONObject> responselistenerGetUserPictureFirst = response -> {
                    new Thread(() -> {
                        try {
                            String picture = response.getString("picture");
                            User user = new User(author, String.valueOf(authorId), String.valueOf(pversion), picture);
                            UserEntity userDaInserire = new UserEntity(user, "");
                            db.userDao().insertUser(userDaInserire);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }).start();
                };

                cc.getUserPicture(db.userDao().restituisciSid(), String.valueOf(authorId), responselistenerGetUserPictureFirst);
            } else {

                if (pversion == Integer.parseInt(db.userDao().restituisciPversion(String.valueOf(authorId)))) {
                    String B64_Upicture = db.userDao().restituisciUPictureByUid(String.valueOf(authorId));
                    profileImage.post(() -> {
                         Log.d("CercaUtente", "user: "+ author + " con foto: "+ B64_Upicture);
                        byte[] decodedString = Base64.decode(B64_Upicture, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profileImage.setImageBitmap(decodedByte);
                    });


                } else {
                    Log.d("COMMENTI", "pversion è diversa");
                    Response.Listener<JSONObject> responselistenerGetUserPicture = response -> {
                        try {
                            db.userDao().inserisciUPicturePversionByUid(String.valueOf(authorId), response.getString("picture"), response.getString("pversion"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        profileImage.post(() -> {
                            byte[] decodedString = new byte[0];
                            try {
                                decodedString = Base64.decode(response.getString("picture"), Base64.DEFAULT);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            profileImage.setImageBitmap(decodedByte);
                        });
                    };
                    cc.getUserPicture(db.userDao().restituisciSid(), String.valueOf(authorId), responselistenerGetUserPicture);

                }
            }
        }).start();

    }

}
