package com.example.belajarnamahewan;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.belajarnamahewan.Kuis.KuisActivity;
import com.example.belajarnamahewan.Tambah.AddActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private CardView cardViewHerbi, cardViewKarni, cardViewOmni;

    private Dialog dialogPopup;

    private Button yes, no;

    private long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        dialogPopup = new Dialog(MainActivity.this);
        dialogPopup.setContentView(R.layout.exit_dialog);
        dialogPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        yes = dialogPopup.findViewById(R.id.btn_exit_yes);
        no = dialogPopup.findViewById(R.id.btn_exit_no);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view_home);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,  toolbar, R.string.drawer_open,
                R.string.drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_black_24dp, getTheme());
        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        cardViewHerbi =findViewById(R.id.card_main_herbi);
        cardViewKarni =findViewById(R.id.card_main_karni);
        cardViewOmni =findViewById(R.id.card_main_omni);

        cardViewHerbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HewanActivity.class);
                intent.putExtra("title", "Herbivora");
                startActivity(intent);
            }
        });

        cardViewKarni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HewanActivity.class);
                intent.putExtra("title", "Karnivora");
                startActivity(intent);
            }
        });

        cardViewOmni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HewanActivity.class);
                intent.putExtra("title", "Omnivora");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_about){

            Intent intent = new Intent(MainActivity.this, TentangActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.nav_kuis){

            Intent intent = new Intent(MainActivity.this, KuisActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_keluar){

            dialogPopup.show();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.this.finish();
                    System.exit(0);
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogPopup.dismiss();
                }
            });

        }

        return false;
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_LONG).show();

        if (backPressTime + 2000 > System.currentTimeMillis()){

            finish();
            System.exit(0);
        }

        backPressTime = System.currentTimeMillis();
    }
}
