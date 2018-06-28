package com.kewal.acute;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.Manifest;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;

import java.io.File;
import java.io.IOException;

public class AddDetails extends AppCompatActivity {
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
            actionBar.setTitle("Rate Employee");
        }

        Button button_submit = findViewById(R.id.button_submit);
        final EditText editText_comments = findViewById(R.id.editText_comments);

        ImageView imageView = findViewById(R.id.imageView_dp);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddDetails.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_image, null);
                ImageButton imageButtonCamera = mView.findViewById(R.id.imageButtonCamera);
                ImageButton imageButtonGallery = mView.findViewById(R.id.imageButtonGallery);
                final TextView textViewCancel = mView.findViewById(R.id.textViewCancel);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                imageButtonCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(ProfileActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                        if (ActivityCompat.checkSelfPermission(AddDetails.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AddDetails.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            dialog.dismiss();
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });
                imageButtonGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(ProfileActivity.this, "Gallery", Toast.LENGTH_SHORT).show();
                        try {
                            if (ActivityCompat.checkSelfPermission(AddDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                            } else {
                                // Create intent to Open Image applications like Gallery, Google Photos
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                galleryIntent.setType("image/*");
                                // Start the Intent
                                dialog.dismiss();
                                startActivityForResult(galleryIntent, GALLERY_PERMISSION_CODE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                textViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_comments = findViewById(R.id.editText_comments);
                emp_comments = editText_comments.getText().toString();
                Toast.makeText(AddDetails.this, "Employee details submitted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddDetails.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
                    ImageView imgView = findViewById(R.id.imageView_dp);
                    // Set the Image in ImageView after decoding the String
                    imgView.setImageBitmap(dp);
                }
                break;
            case GALLERY_PERMISSION_CODE:
                    try {
                        // When an Image is picked
                        if (resultCode == RESULT_OK
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


                            Bitmap loadedBitmap = BitmapFactory.decodeFile(imgDecodableString);

                            ExifInterface exif = null;
                            try {
                                File pictureFile = new File(imgDecodableString);
                                exif = new ExifInterface(pictureFile.getAbsolutePath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            int orientation = ExifInterface.ORIENTATION_NORMAL;

                            if (exif != null)
                                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                                    break;
                            }

                            ImageView imgView = findViewById(R.id.imageView_dp);
                            // Set the Image in ImageView after decoding the String
                            imgView.setImageBitmap(loadedBitmap);

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

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
