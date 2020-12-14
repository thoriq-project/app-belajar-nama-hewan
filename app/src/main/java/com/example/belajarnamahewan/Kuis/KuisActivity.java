package com.example.belajarnamahewan.Kuis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.belajarnamahewan.R;

public class KuisActivity extends AppCompatActivity {

    private TextView textViewSkorTertinggi;
    private Button buttonMulai;

    private static final int REQUEST_CODE_KUIS = 1;


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighScore";

    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuis);

        textViewSkorTertinggi = findViewById(R.id.txt_skor_tertinggi);
        buttonMulai = findViewById(R.id.btn_mulai_kuis);

        buttonMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KuisActivity.this, MulaiKuisActivity.class);
                startActivityForResult(intent, REQUEST_CODE_KUIS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_KUIS) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(MulaiKuisActivity.EXTRA_SCORE, 0);
                if (score   > highscore){

                    updateHighScore(score);
                }
            }
        }
    }

    private void loadHighScore(){

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE,0);
        textViewSkorTertinggi.setText(highscore + "00");
    }

    private void updateHighScore(int highscoreNew) {

        highscore = highscoreNew;
        textViewSkorTertinggi.setText(highscore + "00");

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();

    }
}
