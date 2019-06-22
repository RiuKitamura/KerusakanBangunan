package com.example.acer.kerusakanbangunan;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class AmbilGambarRetakan extends AppCompatActivity {

    int kode,struktur;
    ImageButton gallery,camera;
    String pathToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambil_gambar_retakan);

        gallery = findViewById(R.id.galery_btn);
        camera = findViewById(R.id.camera_btn);

        Bundle b = getIntent().getExtras();
        kode = b.getInt("id");
        struktur = b.getInt("stuk");
        Toast.makeText(this, ""+kode+" "+struktur, Toast.LENGTH_SHORT).show();

        if(struktur==1){
            getSupportActionBar().setTitle("Ambil Gambar Kolom");
        }
        else if(struktur==2){
            getSupportActionBar().setTitle("Ambil Gambar Balok");
        }
        else if(struktur==3){
            getSupportActionBar().setTitle("Ambil Gambar Dinding");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null){
                    File photoFile = null;
                    photoFile = createPhotoFile();
                    if(photoFile != null){
                        pathToFile = photoFile.getAbsolutePath();
                        Uri photoUri = FileProvider.getUriForFile(AmbilGambarRetakan.this, "com.thecodecity.cameraandroid.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                startActivityForResult(intent, 0);
            }
        });
    }

    private  File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try{
            image = File.createTempFile(name,".jpg", storageDir);
        }
        catch (IOException e){
            Log.d("mylog","Excep : "+e.toString());
        }
        return image;
    }

    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            CropImage.activity(Uri.fromFile(new File(pathToFile)))
                    .setAspectRatio(1,1)
                    .start(this);
        }
        else if(requestCode == 0 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri(); //Mengubah data image kedalam Uri
                Bitmap  mBitmap = null;
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    int a = mBitmap.getWidth();
                    int b = mBitmap.getHeight();
                    System.out.println("width = "+a+", height = "+b);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                moveToUpdate(kode, 1);
                onBackPressed();

                //Menampilkan Gambar pada ImageView
//                Picasso.get().load(imageUri).into(poto);
//                isi_gambar=true;

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //Menangani Jika terjadi kesalahan
                String error = result.getError().toString();
                Log.d("Exception", error);
                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    void moveToUpdate(int id, int level){
        try{
            MainActivity.mSQLiteHelper.updateDataKerusakan(level,id);
            //updateList();
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(getIntent());
//            overridePendingTransition(0, 0);
        }
        catch (Exception e){
            Log.e("error", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
