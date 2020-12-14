package com.example.belajarnamahewan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belajarnamahewan.Holder.HewanHolder;
import com.example.belajarnamahewan.Model.Hewan;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HewanActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private DatabaseReference myRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private String title = "";

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hewan);

        toolbar = findViewById(R.id.toolbar_hewan);
        setSupportActionBar(toolbar);
        title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);

        recyclerView = findViewById(R.id.rv_hewan);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        myRef = FirebaseDatabase.getInstance().getReference().child(title);

        progressBar = findViewById(R.id.pb_hewan);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Hewan> options = new FirebaseRecyclerOptions.Builder<Hewan>()
                .setQuery(myRef,Hewan.class)
                .build();

        FirebaseRecyclerAdapter<Hewan, HewanHolder> adapter = new FirebaseRecyclerAdapter<Hewan, HewanHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HewanHolder hewanHolder, int i, @NonNull final Hewan hewan) {

                if (progressBar != null){

                    progressBar.setVisibility(View.GONE);
                }

                Picasso.get().load(hewan.getImage()).into(hewanHolder.imageViewHewan);
                hewanHolder.textViewHewan.setText(hewan.getNama());

                hewanHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(HewanActivity.this, DetailHewanActivity.class);
                        intent.putExtra("id",hewan.getId());
                        intent.putExtra("jenis",hewan.getJenis());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public HewanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_hewan, parent, false);
                HewanHolder holder = new HewanHolder(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
