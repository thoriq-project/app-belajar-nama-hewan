package com.example.belajarnamahewan.Kuis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belajarnamahewan.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MulaiKuisActivity extends AppCompatActivity {

    private TextView textViewSoalPer,textViewSkor, textViewWaktu, textViewPertanyaan;

    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3;

    private Button buttonNext;


    private ColorStateList textColorDefaultRadio;
    private ArrayList<Question> questionList;
    //hitung soal
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    //ngirim highscore
    public static final String EXTRA_SCORE = "extraScore";

    //buat hitung mundur
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private long backPressedTime;

    //landscape setting
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulai_kuis);

        textViewPertanyaan = findViewById(R.id.txt_soal);
        textViewSoalPer = findViewById(R.id.txt_soal_ke);
        textViewSkor = findViewById(R.id.txt_skor);
        textViewWaktu = findViewById(R.id.txt_sisa_waktu);

        rbGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        buttonNext = findViewById(R.id.btn_submit);

        //warna font pilihan
        textColorDefaultRadio = rb1.getTextColors();

        //warna font waktu
        textColorDefaultCd = textViewWaktu.getTextColors();


        if (savedInstanceState == null){

            //konek sqlite dan landscape setting
            QuizDBHelper dbHelper = new QuizDBHelper(this);

            // menaruh soal dari database ke aplikasi dan tergantung pada querynya
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();

            // mengacak isi table saat di retrieve
            Collections.shuffle(questionList);

            showNextQuestion();

        } else {

            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered){
                startCountDown();
            } else {
                updateCountdownText();
                showSolution();
            }
    }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered){
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(MulaiKuisActivity.this, "Mohon pilih jawaban!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showNextQuestion();
                }
            }
        });

}

    private void showNextQuestion() {

        rb1.setTextColor(textColorDefaultRadio);
        rb2.setTextColor(textColorDefaultRadio);
        rb3.setTextColor(textColorDefaultRadio);

        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal){

            currentQuestion = questionList.get(questionCounter);

            textViewPertanyaan.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter ++;
            textViewSoalPer.setText(questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonNext.setText("Submit");

            //30 detiknya dimasukan kedalam timeLeftiInMillis
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }
        else {
            finishQuiz();
        }
    }

    //mulai hitungan mundurnya
    private void startCountDown() {

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();

            }

            @Override
            public void onFinish() {

                timeLeftInMillis = 0;
                updateCountdownText();
                checkAnswer();

            }

        }.start();

    }

    private void updateCountdownText() {

        int minutes = (int) (timeLeftInMillis / 1000) / 60 ;
        int seconds = (int) (timeLeftInMillis / 1000) % 60 ;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewWaktu.setText(timeFormatted);

        if (timeLeftInMillis < 5000) {
            textViewWaktu.setTextColor(Color.RED);
        }else {

            textViewWaktu.setTextColor(textColorDefaultCd);

        }
    }

    private void checkAnswer(){

        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answeredNumber = rbGroup.indexOfChild(rbSelected) + 1;

        if (answeredNumber == currentQuestion.getAnswerNumber()){

            score ++;
            textViewSkor.setText(score + "00");
        }

        showSolution();

    }


    //method untuk menampilkan jawaban yang benar setelah memilih jawaban
    //atau setelah waktu habis
    private void showSolution() {

        rb1.setTextColor(Color.YELLOW);
        rb2.setTextColor(Color.YELLOW);
        rb3.setTextColor(Color.YELLOW);


        switch (currentQuestion.getAnswerNumber()){

            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewPertanyaan.setText(currentQuestion.getOption1());
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewPertanyaan.setText(currentQuestion.getOption2());
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewPertanyaan.setText(currentQuestion.getOption3());
                break;

        }

        if (questionCounter < questionCountTotal) {
            buttonNext.setText("N e x t");
        }
        else {

            buttonNext.setText("F i n i s h");
        }

    }

    private void changeToIncorrectColor(RadioButton rbSelected) {

        rbSelected.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_kuis_salah));

    }

    private void finishQuiz() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    @Override
    public void onBackPressed() {


        Toast.makeText(this, "Tekan back lagi untuk keluar", Toast.LENGTH_SHORT).show();

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null){

            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,questionList);
    }

}
