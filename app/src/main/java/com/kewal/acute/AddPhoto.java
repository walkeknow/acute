package com.kewal.acute;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.DrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.Manifest;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddPhoto extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_PERMISSION_CODE = 1;
    String imgDecodableString;
    private static String emp_comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Add Photo");
        }

        Button button_photo = findViewById(R.id.button_camera);
        Button button_gallery = findViewById(R.id.button_gallery);
        Button button_submit = findViewById(R.id.button_submit);

        imageView = findViewById(R.id.imageView_dp);

        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AddPhoto.this,Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(AddPhoto.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddPhoto.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddPhoto.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                    } else {
                        // Create intent to Open Image applications like Gallery, Google Photos
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // Start the Intent
                        startActivityForResult(galleryIntent, GALLERY_PERMISSION_CODE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_comments = findViewById(R.id.editText_comments);
                emp_comments = editText_comments.getText().toString();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    Bitmap dp = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(dp);
                }
                break;
            case GALLERY_PERMISSION_CODE:
                    try {
                        // When an Image is picked
                        if (requestCode == GALLERY_PERMISSION_CODE && resultCode == RESULT_OK
                                && null != data) {
                            // Get the Image from data

                            Uri selectedImage = data.getData();
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };

                            // Get the cursor
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imgDecodableString = cursor.getString(columnIndex);
                            cursor.close();
                            ImageView imgView = (ImageView) findViewById(R.id.imageView_dp);
                            // Set the Image in ImageView after decoding the String
                            imgView.setImageBitmap(BitmapFactory
                                    .decodeFile(imgDecodableString));

                        } else {
                            Toast.makeText(this, "You haven't picked Image",
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                                .show();
                    }
                break;
            default:
                break;
        }
    }
}
