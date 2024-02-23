package com.example.maledettatreest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfiloActivity extends AppCompatActivity {
    ImageView imageView;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
        //Comunication Controller
        CommunicationController cc = new CommunicationController(this);
        //ImageView
        imageView = findViewById(R.id.profile_image2);


        int SELECT_IMAGE_CODE = 1;
        // TextInput
       TextInputEditText TextInputUName = findViewById(R.id.textInputEditUName);
        //setto il nome dal database al textInput
            //creo il database
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user").build();

       new Thread(()->{
           //setto il nome preso dal DB
           String nomeInDB = db.userDao().restituisciUname();
           Looper.prepare();
           TextInputUName.post(()->{
               TextInputUName.setText(nomeInDB);
           });

           //setto l'immagine presa dal DB
          String immagine_B64 = db.userDao().restituisciUPicture(db.userDao().restituisciSid());
           byte[] decodedString = Base64.decode( immagine_B64, Base64.DEFAULT);
           Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
           //setto l'immagine. SONO DENTRO UN THREAD, non dovrei poterlo fare
           ImageView imageview = findViewById(R.id.profile_image2);
           imageview.post(()->{
               imageview.setImageBitmap(decodedByte);
           });





       }).start();



        //bottone per salvare nuovo nome
        findViewById(R.id.btnSetName).setOnClickListener(v -> {
            Log.d("PROFILO", String.valueOf(TextInputUName.getText()));
            if (String.valueOf(TextInputUName.getText()).length() < 20) {
                new Thread(() -> {
                    db.userDao().inserisciUname(String.valueOf(TextInputUName.getText()), db.userDao().restituisciSid());
                    Log.d("PROFILO", "nome aggiornato nel DB: " + db.userDao().restituisciUname());
                    //snackbar
                    Snackbar updatedUname = Snackbar.make(findViewById(R.id.activity_profilo), "Nome Aggiornato: " + db.userDao().restituisciUname(), Snackbar.LENGTH_SHORT);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) updatedUname.getView().getLayoutParams();
                    params.gravity = Gravity.TOP;
                    updatedUname.setBackgroundTint(getColor(R.color.dark_green));
                    updatedUname.show();
                    // eseguo setProfile
                    Response.Listener<JSONObject> responseListenersSetProfileName = response -> {
                        Log.d("PROFILO", "setprofileName eseguita");
                    };
                    cc.setProfileName(db.userDao().restituisciSid(), String.valueOf(TextInputUName.getText()), responseListenersSetProfileName);

                }).start();
            }else{
                Snackbar updatedUname = Snackbar.make(findViewById(R.id.activity_profilo), "Nome Troppo Lungo [MAX 20 Char]", Snackbar.LENGTH_SHORT);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) updatedUname.getView().getLayoutParams();
                params.gravity = Gravity.TOP;
                updatedUname.setBackgroundTint(getColor(R.color.red_alert));
                updatedUname.show();
            }
        });
//________________________________________________________________________________________________
        //immagine per cambiare immagine profilo
         findViewById(R.id.profile_image2).setOnClickListener(v ->{
             Intent intent = new Intent();
             intent.setType("image/*");
             intent.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(Intent.createChooser(intent, "Title"), SELECT_IMAGE_CODE);

         });

//________________________________________________________________________________________________





       /* TextInputUName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.d("PROFILO", s.toString());

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        */
        //menu sotto, con navigazione
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.activity_profilo);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.activity_profilo:
                        return true;
                    case R.id.activity_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }

        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CommunicationController cc = new CommunicationController(this);
        if (requestCode == 1) {
            Uri uri = data.getData();
            InputStream imageStream = null;
            try {
                //trasformo uri in Bitmap
                imageStream = getContentResolver().openInputStream(uri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(selectedImage, 150, 100, true);

                //Log.d("PROFILO", String.valueOf(encoded.length()));
               // imageView.setImageBitmap(cropToSquare(selectedImage));

               if (cropToSquare(bMapScaled).getByteCount()< 100000) {
                   //Snackbar Grandezza Giusta
                   Snackbar updatedUImage = Snackbar.make(findViewById(R.id.activity_profilo), "Immagine valida [ < 100KB]", Snackbar.LENGTH_SHORT);
                   FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) updatedUImage.getView().getLayoutParams();
                   params.gravity = Gravity.TOP;
                   updatedUImage.setBackgroundTint(getColor(R.color.dark_green));
                   updatedUImage.show();
                   //croppo l'immagine
                   Bitmap bMapScaledCropped = cropToSquare(bMapScaled);
                   //trasformo bitmap in B64
                   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                   bMapScaledCropped.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                   byte[] byteArray = byteArrayOutputStream .toByteArray();
                   String uImageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                   // salvo la B64 nel DB – (1) Apro il DB
                   final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                           AppDatabase.class, "user").build();
                   //creo la request per gestire le setProfile dell'immagine e salvo la B64 nel DB – (2) richiamo la query e salvo
                   new Thread(() -> {
                       db.userDao().inserisciUPicture(uImageEncoded,db.userDao().restituisciSid());
                       Response.Listener<JSONObject> responseListenerSetProfileImage = response -> {
                            Log.d("PROFILO", "setprofileImage eseguita");
                       };
                       cc.setProfileImage(db.userDao().restituisciSid(), uImageEncoded, responseListenerSetProfileImage);
                   }).start();
                   // setto l'immagine nell'ImageView
                   imageView.setImageBitmap(bMapScaledCropped);


               } else {
                   //snackbar
                   Snackbar updatedUImage = Snackbar.make(findViewById(R.id.activity_profilo), "Immagine non valida [ > 100KB]", Snackbar.LENGTH_SHORT);
                   FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) updatedUImage.getView().getLayoutParams();
                   params.gravity = Gravity.TOP;
                   updatedUImage.setBackgroundTint(getColor(R.color.red_alert));
                   updatedUImage.show();
               }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }


    }
    //ImageCropper
    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }


   }