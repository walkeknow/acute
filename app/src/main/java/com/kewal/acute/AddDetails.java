package com.kewal.acute;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDetails extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_PERMISSION_CODE = 2;
    private static String emp_comments;
    DatabaseReference databaseReferenceEmployees;
    private StorageReference mStorageRef;
    Uri selectedImage;
    String id;
    String mCurrentPhotoPath;
    private final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    //private static AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Rate Employee");
        }

        Button button_submit = findViewById(R.id.button_submit);

        imageView = findViewById(R.id.imageView_dp);

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
                        dialog.dismiss();
                        // Toast.makeText(ProfileActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                        if (ActivityCompat.checkSelfPermission(AddDetails.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AddDetails.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        } else {
                            dispatchTakePictureIntent();
                        }
                    }
                });
                imageButtonGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            if (ActivityCompat.checkSelfPermission(AddDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                            } else {
                                // Create intent to Open Image applications like Gallery, Google Photos
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                galleryIntent.setType("image/*");
                                // Start the Intent
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

                //databaseReferenceEmployees = FirebaseDatabase.getInstance().getReference();
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStorageRef = FirebaseStorage.getInstance().getReference();
                databaseReferenceEmployees = FirebaseDatabase.getInstance().getReference();

                id = databaseReferenceEmployees.push().getKey();

                Intent intentEmp = getIntent();

                Employee employee = (Employee) intentEmp.getSerializableExtra("EmpObj");

                EditText editText_comments = findViewById(R.id.editText_comments);
                emp_comments = editText_comments.getText().toString();

                RatingBar ratingBar = findViewById(R.id.ratingBar);
                String stars = String.valueOf(ratingBar.getRating());
                employee.setEmpId(id);
                employee.setRating(stars);
                employee.setComment(emp_comments);

                if(selectedImage != null) {
                    uploadFile(employee);
                } else {
                    databaseReferenceEmployees.child("Employees").child(employee.getEmpName()).setValue(employee);
                    Toast.makeText(AddDetails.this, "Employee details submitted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddDetails.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    private void uploadDetails(Employee employee) {

        if(id != null) {
            databaseReferenceEmployees.child("Employees").child(employee.getEmpName()).setValue(employee);

            Toast.makeText(AddDetails.this, "employee details submitted, Thank You!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddDetails.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else {
            Toast.makeText(AddDetails.this, "There was some problem with submitting the details, please try again later", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadFile(final Employee employee) {

        StorageReference empRef = mStorageRef.child("employees").child("employee" + id +".jpg");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading data to Acute...");
        progressDialog.show();

        if (selectedImage != null) {
            UploadTask uploadTask = empRef.putFile(selectedImage);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadDetails(employee);
                    progressDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(AddDetails.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            })
           .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage((int)progress + "%    completed");
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                // Start the Intent
                startActivityForResult(galleryIntent, GALLERY_PERMISSION_CODE);
            } else {
                Toast.makeText(this, "gallery permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    if(resultCode == Activity.RESULT_OK) {
                        try{
                            /*Bitmap dp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            //Bitmap dp = (Bitmap) data.getExtras().get("data");
                            Bitmap dpnew = getRotatedImage(dp);
                            ImageView imgView = findViewById(R.id.imageView_dp);
                            // Set the Image in ImageView after decoding the String
                            imgView.setImageBitmap(dpnew);*/
                            setPic();
                        } catch (Exception e){
                            Toast.makeText(AddDetails.this, "Image could not be displayed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case GALLERY_PERMISSION_CODE:
                    try {
                        // When an Image is picked
                        if (resultCode == RESULT_OK
                                && null != data) {
                            // Get the Image from data

                            selectedImage = data.getData();
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };

                            // Get the cursor
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String imgDecodableString = cursor.getString(columnIndex);
                            cursor.close();


                            Bitmap loadedBitmap = BitmapFactory.decodeFile(imgDecodableString);

                            loadedBitmap = getRotatedImage(loadedBitmap, imgDecodableString);

                            // Set the Image in ImageView after decoding the String
                            imageView.setImageBitmap(loadedBitmap);

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

    private Bitmap getRotatedImage (Bitmap loadedBitmap, String imgDecodableString) {
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

        return loadedBitmap;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(AddDetails.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                selectedImage = FileProvider.getUriForFile(AddDetails.this,
                        "com.kewal.acute.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);

            }
        }
    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {

        //ImageView mImageView = findViewById(R.id.imageView_dp);

        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        bitmap = getRotatedImage(bitmap, mCurrentPhotoPath);

        imageView.setImageBitmap(bitmap);

        galleryAddPic();
    }
}
