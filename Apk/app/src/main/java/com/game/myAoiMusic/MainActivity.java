package com.game.myAoiMusic;


import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    // VARIAVEIS DE VIEWS
    private ListView listMusic;
    private EditText editText;
    private ImageButton configButton;
    private ImageView  person, receive;
    // VARIAVEIS DE VIEWS

    // VARIAIS APLICATION
    String[] items;
    private String songName;
    public static MediaPlayer music;
    public static final int PICK_IMAGE = 1234;
    // VARIAIS APLICATION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialized();
        runtimePermission();
        escoderTeclado();

        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            recive = findViewById(R.id.reciveUrlImage);
            URL url = null;
            try {
                url = new URL(String.valueOf(account.getPhotoUrl()));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                recive.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideSystemUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    // FULLSCREEN
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUI();
        }
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    // FULLSCREEN

    // LISTA
    public void initialized(){
        listMusic = findViewById(R.id.listOfMusic);
        editText = findViewById(R.id.editTextTextPersonName6);
        configButton = findViewById(R.id.btnConfig);
        receive = findViewById(R.id.reciveUrlImage);
    }
    public void runtimePermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this,"PERMISSION REQUIRED",Toast.LENGTH_SHORT)
                                .show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }
    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();


        assert files != null;
        for (File singleFile: files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));
                Collections.sort(arrayList);
            }else{
                if (singleFile.getName().endsWith(".mp3") ||singleFile.getName().endsWith(".wav") ||
                        singleFile.getName().endsWith(".flac")){
                    arrayList.add(singleFile);
                    Collections.sort(arrayList);
                }
            }

        }

        return arrayList;
    }
    public void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];

        for (int i=0;i<mySongs.size();i++){

            items[i] = mySongs.get(i).getName().replace(".mp3","")
                    .replace(".wav","");
        }

        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,R.layout.layout_items,R.id.person_name,items);
        listMusic.setAdapter(myAdapter);


        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (myAdapter != null) {
                    myAdapter.getFilter().filter(arg0.toString());
                }
                if (arg0.toString().equals("")) {
                    myAdapter.notifyDataSetChanged();
                }
            }

        });

        listMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                songName = listMusic.getItemAtPosition(i).toString();

                startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                        .putExtra("songs",mySongs).putExtra("songNameMusic",songName).putExtra("position",i));

            }
        });
    }
    // LISTA

    // ESCONDER TECLADO
    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    private void escoderTeclado(){
        editText = findViewById(R.id.editTextTextPersonName6);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(MainActivity.this,editText);
                    hideSystemUI();
                }
            }
        });

        hideKeyboard(this,editText);
    }
    // ESCONDER TECLADO

    // EVENTOS DE CLICK
    public void onClickImage(View view){
        //Intent intent = new Intent(MainActivity.this,UserActivity.class);
        //startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_rigth);
        //Toast.makeText(MainActivity.this,"FUNCIONANDO", Toast.LENGTH_SHORT).show();
    }
    public void onClickConfig(View view){
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_rigth,R.anim.slide_out_left);
    }
    // EVENTOS DE CLICK

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_CANCELED){
            if(requestCode == PICK_IMAGE){
                Uri selectedImage = data.getData();
                assert selectedImage != null;
                receive.setImageURI(Uri.parse(String.valueOf(selectedImage)));
                Toast.makeText(getApplicationContext(), selectedImage.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideSystemUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        music.stop();
        music.release();
        finishAffinity();
    }
}