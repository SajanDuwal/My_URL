package com.sajiman.mychat.responseController;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.sajiman.mychat.utility.AppText;
import com.sajiman.mychat.utility.FileHandling;

import java.io.File;
import java.io.IOException;

public class Save extends AppCompatActivity {


    private Uri galleryImageUri;
    private Uri cameraUri;
    private AlertDialog.Builder dialog;

    private int CHECK_WRITE_PERMISSION;

    public void set() {

        initPermissions();
        buildDialogImage();
        if (CHECK_WRITE_PERMISSION != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            dialog.show();
        }
    }


    private void initPermissions() {
        CHECK_WRITE_PERMISSION = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppText.WRITE_STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppText.WRITE_STORAGE_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                } else {
                    dialog.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void buildDialogImage() {
        dialog = new AlertDialog.Builder(this);
        dialog.setItems(new String[]{"Take a photo", "Choose from gallery", "Remove Photo"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraUri = Uri.fromFile(FileHandling.makeImageFile(Save.this));
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                        startActivityForResult(cameraIntent, AppText.CAMERA_REQUEST_CODE);
                        break;

                    case 1:
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, AppText.GALLERY_REQUEST_CODE);
                        break;

                    case 2:
                        cameraUri = null;
                        galleryImageUri = null;
//                        mIvUserImage.setImageResource(R.drawable.user_image);
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AppText.GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        cameraUri = null;
                        galleryImageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), galleryImageUri);
//                            mIvUserImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case AppText.CAMERA_REQUEST_CODE:
                if (cameraUri != null) {
                    galleryImageUri = null;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cameraUri);
//                        mIvUserImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private File getImageFile() {
        File image = null;
        if (galleryImageUri != null && cameraUri == null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), galleryImageUri);
                File imageFile = FileHandling.makeImageFile(Save.this);
                image = FileHandling.writeImageToFile(imageFile, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (cameraUri != null && galleryImageUri == null) {
            image = FileHandling.makeImageFile(Save.this).getAbsoluteFile();
        }
        return image;
    }
}
