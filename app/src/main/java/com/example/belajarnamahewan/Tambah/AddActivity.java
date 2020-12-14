package com.example.belajarnamahewan.Tambah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.belajarnamahewan.R;

public class AddActivity extends AppCompatActivity {

    private Button btnHerbi, btnKarni, btnOmni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnHerbi = findViewById(R.id.btn_add_herbi);
        btnKarni = findViewById(R.id.btn_add_karni);
        btnOmni = findViewById(R.id.btn_add_omni);

        btnHerbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddActivity.this, AddHewanActivity.class);
                intent.putExtra("jenis_hewan","Herbivora");
                startActivity(intent);
            }
        });

        btnKarni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddActivity.this, AddHewanActivity.class);
                intent.putExtra("jenis_hewan","Karnivora");
                startActivity(intent);
            }
        });

        btnOmni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddActivity.this, AddHewanActivity.class);
                intent.putExtra("jenis_hewan","Omnivora");
                startActivity(intent);
            }
        });
    }
}
