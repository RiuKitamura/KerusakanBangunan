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
    final int CAMERA_REQUEST_CODE1 = 999;
    final int CAMERA_REQUEST_CODE2 = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        getSupportActionBar().setTitle("Formulir");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nama_bg = findViewById(R.id.nama_bangunan);
        lantai = findViewById(R.id.jml_lantai);
        tahun = findViewById(R.id.thn_dibuat);
        alamat_bg = findViewById(R.id.latitude);
        lati = findViewById(R.id.nama_bangunan);
        longi = findViewById(R.id.longitude);
        poto = findViewById(R.id.add_photo_btn);
        nama = findViewById(R.id.nama_person);
        alamat = findViewById(R.id.alamat_person);
        no_hp = findViewById(R.id.nomor_person);
        next = findViewById(R.id.next_btn);

        poto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    captureFromCamera();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    pickFromGallery();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,CAMERA_REQUEST_CODE2);
    }

    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                if (resultCode == Activity.RESULT_OK)
                    switch (requestCode){
                        case CAMERA_REQUEST_CODE1:
                            poto.setImageURI(Uri.parse(cameraFilePath));
                            break;
                        case CAMERA_REQUEST_CODE2:
                            //data.getData return the content URI for the selected Image
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();
                            //Get the column index of MediaStore.Images.Media.DATA
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            //Gets the String value in the column
                            String imgDecodableString = cursor.getString(columnIndex);
                            cursor.close();
                            // Set the Image in ImageView after decoding the String
                            poto.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                            break;

                    }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
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
