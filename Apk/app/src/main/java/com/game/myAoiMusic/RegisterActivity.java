package com.game.myAoiMusic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegister;
    private EditText editTextRegisterPass;
    private EditText editTextRegisterRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        escoderTeclado();
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

    // EVENTOS DE CLICK
    public void backActivity(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_rigth);

    }
    public void clickTermsAndServices(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("     Terms and Services");
        builder.setIcon(R.drawable.logoappicon);
        builder.setMessage("Contrary to popular belief, Lorem Ipsum is not simply random text. " +
                "It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. " +
                "Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, " +
                "looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, " +
                "and going through the cites of the word in classical literature, discovered the undoubtable source. " +
                "Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" " +
                "(The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, " +
                "very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", " +
                "comes from a line in section 1.10.32.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
    // EVENTOS DE CLICK

    // ESCONDER TECLADO
    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    private void escoderTeclado(){
        editTextRegister = findViewById(R.id.editTextTextEmailRegister);
        editTextRegisterPass = findViewById(R.id.editTextTextPassRegister);
        editTextRegisterRepeat = findViewById(R.id.editTextTextPassRegister2);

        editTextRegister.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(RegisterActivity.this,editTextRegister);
                    hideSystemUI();
                }
            }
        });

        editTextRegisterPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(RegisterActivity.this,editTextRegisterPass);
                    hideSystemUI();
                }
            }
        });

        editTextRegisterRepeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(RegisterActivity.this,editTextRegisterRepeat);
                    hideSystemUI();
                }
            }
        });

        hideKeyboard(this,editTextRegister);


    }
    // ESCONDER TECLADO

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
        finishAffinity();
    }
}