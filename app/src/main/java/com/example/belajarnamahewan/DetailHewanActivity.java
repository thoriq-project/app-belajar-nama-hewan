package com.example.belajarnamahewan;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belajarnamahewan.Model.Hewan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DetailHewanActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewNama, textViewBing, textViewDesk;
    private FloatingActionButton floatingActionButton;

    private String id = "";
    private String jenis = "";

    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hewan);

        imageView = findViewById(R.id.img_detail_hewan);

        textViewNama = findViewById(R.id.txt_detail_nama);
        textViewBing = findViewById(R.id.txt_detail_bing);
        textViewDesk = findViewById(R.id.txt_detail_desk);

        floatingActionButton = findViewById(R.id.fab_detail_hewan);

        id = getIntent().getStringExtra("id");
        jenis = getIntent().getStringExtra("jenis");


        progressDialog = new ProgressDialog(this);

        retriveAllData(id);

    }

    private void retriveAllData(String id) {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(jenis);
        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){

                    final Hewan hewan = dataSnapshot.getValue(Hewan.class);

                    Picasso.get().load(hewan.getImage()).into(imageView);

                    textViewNama.setText(hewan.getNama());
                    textViewBing.setText(hewan.getBing());
                    textViewDesk.setText(hewan.getDesk());

                    progressDialog.setMessage("Sedang memuat suara...");
                    progressDialog.setTitle("Mohon tunggu");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(hewan.getSuara());
                        mediaPlayer.prepareAsync();

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {

                                progressDialog.dismiss();
                                Toast.makeText(DetailHewanActivity.this, "Suara siap diputar", Toast.LENGTH_SHORT).show();

                            }
                        });

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                floatingActionButton.setImageResource(R.drawable.playbtn);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!mediaPlayer.isPlaying()){

                                mediaPlayer.start();
                                floatingActionButton.setImageResource(R.drawable.pause24dp);

                            } else {

                                mediaPlayer.pause();
                                floatingActionButton.setImageResource(R.drawable.playbtn);
                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mediaPlayer.stop();
    }
}
