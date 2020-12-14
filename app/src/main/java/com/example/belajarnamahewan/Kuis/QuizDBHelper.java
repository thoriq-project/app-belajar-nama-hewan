package com.example.belajarnamahewan.Kuis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.belajarnamahewan.Kuis.QuizContract.*;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class QuizDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTION_TABLE = " CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NUMBER + " INTEGER " +
                " ) ";

        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        fillQuestionTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);

    }


    private void addQuestion(Question question){

        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionTable.COLUMN_ANSWER_NUMBER, question.getAnswerNumber());

        db.insert(QuestionTable.TABLE_NAME, null, cv);
    }



    // retrieve semua pertanyaan dr database
    public ArrayList<Question> getAllQuestions() {

        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME + " ORDER BY RANDOM() " +
                " LIMIT 10 " , null);

        if (c.moveToFirst()){
            do{
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNumber(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUMBER)));
                questionList.add(question);

            }while (c.moveToNext());
        }

        c.close();
        return questionList;
    }



    // isi dari tabel pertanyaan
    private void fillQuestionTable() {

        // Level Easy ------------------------------------------------------------------------------------------------------
        Question q1 = new Question("Termasuk kedalam jenis apakah hewan ikan hiu ?",
                        "Herbivora","Karnivora","OmniVora",2);

        Question q2 = new Question("Bahasa Inggrisnya nama hewan Jerapah adalah ?",
                "Elephant",
                "Girrafe",
                "Crocodile",
                2);
        addQuestion(q2);

        Question q3 = new Question("Apa nama binatang Tikus dalama bahasa Inggris ? ",
                "Cat",
                "Rabbit",
                "Mouse",
                3);
        addQuestion(q3);

        Question q4 = new Question("Binatang yang memakan tumbuhan dan daging disebut juga binatang ?",
                "Omnivora",
                "Herbivora",
                "Omnibus Law",
                1);
        addQuestion(q4);

        Question q5 = new Question("Binatang yang hidupnya di 2 alam yaitu darat dan air disebut juga binatang ?",
                "Amfibi",
                "Herbivora",
                "Nocturnal",
                1);
        addQuestion(q5);

        Question q6 = new Question("Nama binatang harimau dalam bahasa Inggris disebut ?",
                "Lion",
                "Tiger",
                "Macan",
                2);
        addQuestion(q6);

        Question q7 = new Question("Musang adalah jenis binatang pemakan?",
                "Tumbuhan",
                "Tumbuhan dan Daging",
                "Daging",
                2);
        addQuestion(q7);

        Question q8 = new Question("Yang termasuk kedalam hewan pemakan tumbuhan dibawah ini adalah ?",
                "kambing, harimau, singa",
                "Kuda, Kambing, Sapi",
                "Sapi, Kambing, Ular",
                2);
        addQuestion(q8);

        Question q9 = new Question("Yang termasuk kedalam hewan pemakan daging dibawah ini adalah ?",
                "Harimau, Singa, Rusa",
                "Singa, Ular, Ikan Hiu",
                "Laba-Laba, Semut, Gajah",
                2);
        addQuestion(q9);

        Question q10 = new Question("Termasuk golongan apakah binatang Laba-laba?",
                "Omnivora",
                "Karnivora",
                "Herbivora",
                2);
        addQuestion(q10);

        // Level Medium ------------------------------------------------------------------------------------------------------------

        Question q11 = new Question("Dibawah ini manakah yang termasuk kedalam golongan binatang amfibi ?",
                "Cicak",
                "Kuda",
                "Kura-kura",
                3);
        addQuestion(q11);

        Question q12 = new Question("Dibawah ini manakah yang termasuk kedalam golongan binatang omnivora ?",
                "Lumba-lumba",
                "Kucing",
                "Burung Elang",
                1);
        addQuestion(q12);

        Question q13 = new Question("Disebut apakah binatang Kupu-Kupu dalam bahasa Inggris?",
                "Turtle",
                "Butterfly",
                "Dragonfly",
                2);
        addQuestion(q13);

        Question q14 = new Question("Horse adalah nama dalam bahasa inggris untuk binatang ? ",
                "Kuda",
                "Kumbang",
                "Kura - Kura",
                1);
        addQuestion(q14);

        Question q15 = new Question("Serangga Lalat dalam bahasa Inggris disebut juga ?",
                "Butterfly",
                "Fly",
                "Bird",
                2);
        addQuestion(q15);

        Question q16 = new Question("Hewan yang dengan ciri-ciri memiliki belalai panjang dalam bahasa inggris disebut?",
                "Horse",
                "Girrafe",
                "Elephant",
                3);
        addQuestion(q16);

        Question q17 = new Question("Hewan yang dijuluki sebagai Raja Hutan adalah?",
                "Lion",
                "Bird",
                "Eagle",
                1);
        addQuestion(q17);

        Question q18 = new Question("Eagle adalah nama dalam bahasa Inggris untuk binatang ?",
                "Burung Unta",
                "Burung Merpati",
                "Burung Elang",
                3);
        addQuestion(q18);

        Question q19 = new Question("Buaya adalah salah-satu binatang yang termasuk kedalam golongan ?",
                "Herbivora",
                "Amfibi",
                "Berbulu",
                2);
        addQuestion(q19);

        Question q20 = new Question("Ular dalam bahasa Inggris disebut juga ?",
                "Shark",
                "Snake",
                "Sheep",
                2);
        addQuestion(q20);



    }

}