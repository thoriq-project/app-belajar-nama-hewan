package com.example.belajarnamahewan.Tambah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.belajarnamahewan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddHewanActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private StorageReference audioStorageRef;
    private StorageReference imageStorageRef;

    private ImageView imageViewHewan;
    private EditText etNamaHewan, etBing, etDesk;
    private TextView tvJenisHewan,tvNamaFileAudio;
    private Button btnPilih;

    private String jenisHewan = "";
    private String currentDate, currentTime, randomKey;
    private String downloadImgUrl, downloadAudioUrl;
    private String namaHewan, bInggris, deskripsi;
    private String audioName;

    private Uri imageUri, songUri;

    private static final int galleryPick = 1;
    private static final int storagePick = 2;

    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hewan);

        toolbar = findViewById(R.id.toolbar_add_hewan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewHewan = findViewById(R.id.img_add_hewan);
        etNamaHewan = findViewById(R.id.et_namahewan);
        etBing = findViewById(R.id.et_binggris);
        etDesk = findViewById(R.id.et_deskripsi);
        tvJenisHewan = findViewById(R.id.txt_jenis_hewan);
        tvNamaFileAudio = findViewById(R.id.txt_file_suara);
        btnPilih = findViewById(R.id.btn_pilih_suara);

        progressDialog = new ProgressDialog(this);

        jenisHewan = getIntent().getStringExtra("jenis_hewan");
        tvJenisHewan.setText(jenisHewan);

        myRef = FirebaseDatabase.getInstance().getReference().child(jenisHewan);
        imageStorageRef = FirebaseStorage.getInstance().getReference().child("Images");
        audioStorageRef = FirebaseStorage.getInstance().getReference().child("Audios");

        imageViewHewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaGallery();
            }
        });

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaStorage();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_add){

            Toast.makeText(this, "Bismillah!", Toast.LENGTH_SHORT).show();
            checkValidasi();
        }

        return super.onOptionsItemSelected(item);
    }


    private void bukaGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    private void bukaStorage() {

        Intent storageIntent = new Intent();
        storageIntent.setAction(Intent.ACTION_GET_CONTENT);
        storageIntent.setType("audio/*");
        startActivityForResult(storageIntent,storagePick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewHewan.setImageURI(imageUri);

        }

        if (requestCode == storagePick && resultCode == RESULT_OK && data != null) {

            songUri = data.getData();

            Cursor cursor = getApplicationContext().getContentResolver()
                    .query(songUri, null, null, null, null);

            //mengambil nama file audio
            int indexedname = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            audioName = cursor.getString(indexedname);
            tvNamaFileAudio.setText(audioName);
            cursor.close();

        }
    }


    private void checkValidasi() {

        namaHewan = etNamaHewan.getText().toString();
        bInggris = etBing.getText().toString();
        deskripsi = etDesk.getText().toString();

        if (imageUri == null){

            Toast.makeText(this, "Maaf, sambar belum dipilih", Toast.LENGTH_SHORT).show();

        } else if (songUri == null){

            Toast.makeText(this, "Maaf, suara belum dipilih", Toast.LENGTH_SHORT).show();

        } else if (namaHewan.isEmpty()){

            Toast.makeText(this, "Nama harus diisi", Toast.LENGTH_SHORT).show();

        } else if (bInggris.isEmpty()){

            Toast.makeText(this, "Nama hewan dalam bahasa inggris harus diisi", Toast.LENGTH_SHORT).show();

        } else if (deskripsi.isEmpty()){

            Toast.makeText(this, "Isi kolom deskripsi terlebih dahulu", Toast.LENGTH_SHORT).show();

        } else

            simpanSemuaData();



    }

    private void simpanSemuaData() {

        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
        currentDate = date.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        currentTime = time.format(calendar.getTime());

        randomKey = currentDate + currentTime;

        final StorageReference imagePath = imageStorageRef.child(imageUri.getLastPathSegment() + randomKey + ".jpg");
        final UploadTask uploadImage = imagePath.putFile(imageUri);

        uploadImage.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(AddHewanActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddHewanActivity.this, "Gambar berhasil diupload", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadImage.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){

                            throw task.getException();

                        }

                        downloadImgUrl = imagePath.getDownloadUrl().toString();

                        return imagePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){

                            downloadImgUrl = task.getResult().toString();

                            Toast.makeText(AddHewanActivity.this, "Url gambar berhasil didownload", Toast.LENGTH_SHORT).show();

                            uploadSuara();

                        }

                    }
                });
            }
        });

    }

    private void uploadSuara() {

        final StorageReference audioPath = audioStorageRef.child(songUri.getLastPathSegment() + randomKey + ".mp3");
        final UploadTask uploadTaskAudio = audioPath.putFile(songUri);

        uploadTaskAudio.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddHewanActivity.this, "Suara gagal diupload"+e.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddHewanActivity.this, "Suara berhasil diupload", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTaskAudio.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){

                            throw task.getException();

                        }

                        downloadAudioUrl = audioPath.getDownloadUrl().toString();

                        return audioPath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){

                            downloadAudioUrl = task.getResult().toString();

                            Toast.makeText(AddHewanActivity.this, "Url suara berhasil download", Toast.LENGTH_SHORT).show();

                            simpanDataKeDatabase();

                        }
                    }
                });
            }
        });

        uploadTaskAudio.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                int currentProgress = (int)progress;
                progressDialog.setMessage("Upload suara : " +currentProgress+ " %");

            }
        });
    }

    private void simpanDataKeDatabase(){

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", randomKey);
        hashMap.put("nama", namaHewan);
        hashMap.put("bing", bInggris);
        hashMap.put("desk", deskripsi);
        hashMap.put("image", downloadImgUrl);
        hashMap.put("suara", downloadAudioUrl);
        hashMap.put("jenis", jenisHewan);

        myRef.child(randomKey).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Toast.makeText(AddHewanActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            clearField();
                            progressDialog.dismiss();

                        } else {

                            String message = task.getException().toString();
                            Toast.makeText(AddHewanActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    private void clearField(){

        imageViewHewan.setImageURI(null);
        tvNamaFileAudio.setText(null);
        etNamaHewan.setText(null);
        etDesk.setText(null);
        etBing.setText(null);

    }

}
