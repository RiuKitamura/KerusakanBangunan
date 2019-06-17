package com.example.acer.kerusakanbangunan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FormActivity extends AppCompatActivity {

    EditText nama_bg, lantai, tahun, alamat_bg, lati, longi, nama, alamat, no_hp;
    ImageView poto;
    Button next;

    private String cameraFilePath;
    final int CAMERA_REQUEST_CODE1 = 1;
    final int CAMERA_REQUEST_CODE2 = 0;

    boolean isi_gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        getSupportActionBar().setTitle("Formulir");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nama_bg = (EditText) findViewById(R.id.nama_bangunan);
        lantai = (EditText) findViewById(R.id.jml_lantai);
        tahun = (EditText) findViewById(R.id.thn_dibuat);
        alamat_bg = (EditText) findViewById(R.id.alamat_bangunan);
        lati = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        poto = findViewById(R.id.add_photo_btn);
        nama = (EditText) findViewById(R.id.nama_person);
        alamat = (EditText) findViewById(R.id.alamat_person);
        no_hp = (EditText) findViewById(R.id.nomor_person);
        next = findViewById(R.id.next_btn);

        isi_gambar = false;
        poto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("masuk");
                SelectImage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama_bg.getText().length()!=0 && lantai.getText().length()!=0 && tahun.getText().length()!=0
                        && alamat_bg.getText().length()!=0 && lati.getText().length()!=0 && longi.getText().length()!=0
                        && isi_gambar!=false && nama.getText().length()!=0 && alamat.getText().length()!=0
                        && no_hp.getText().length()!=0 ){
                    try{
                        System.out.println("nama b "+nama_bg.getText());
                        System.out.println("lantai:" +lantai.getText());
                        System.out.println("tahun "+tahun.getText());
                        System.out.println("alamat b "+alamat_bg.getText());
                        System.out.println("lati "+lati.getText());
                        System.out.println("longi "+longi.getText());
                        System.out.println("nama "+nama.getText());
                        System.out.println("alamat "+alamat.getText());
                        System.out.println("nomor "+no_hp.getText());
                        MainActivity.mSQLiteHelper.insertData(
                                nama_bg.getText().toString().trim(),
                                lantai.getText().toString().trim(),
                                tahun.getText().toString().trim(),
                                alamat_bg.getText().toString().trim(),
                                lati.getText().toString().trim(),
                                longi.getText().toString().trim(),
                                imageViewToByte(poto),
                                nama.getText().toString().trim(),
                                alamat.getText().toString().trim(),
                                no_hp.getText().toString().trim()
                        );
                        Toast.makeText(FormActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(FormActivity.this, "Harus diisi semua", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE1);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE2);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==CAMERA_REQUEST_CODE1){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                poto.setImageBitmap(bmp);
                isi_gambar=true;

            }else if(requestCode==CAMERA_REQUEST_CODE2){
                Bitmap  mBitmap = null;
                Uri selectedImageUri = data.getData();
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //poto.setImageURI(selectedImageUri);
                poto.setImageBitmap(mBitmap);
                isi_gambar=true;
            }

        }
    }


    private static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed(){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(FormActivity.this);

    }

    public class ViewDialog {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            Button mDialogNo = dialog.findViewById(R.id.frmNo);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"No" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            Button mDialogOk = dialog.findViewById(R.id.frmOk);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Ok" ,Toast.LENGTH_SHORT).show();
                    FormActivity.super.onBackPressed();
                }
            });

            dialog.show();
        }
    }
}
