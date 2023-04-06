package com.example.chromato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Mainpage extends AppCompatActivity {


   private Button Upload,Camera;

    private Bitmap bitmap;
    private Mat mat;
    int UPLOAD_CODE=100,CAMERA_CODE=101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        if(OpenCVLoader.initDebug()) Log.d("LOADED","SUCCESS");
        else Log.d("LOADED","err");

        getPermission();

       Camera=findViewById(R.id.Camera);
       Upload=findViewById(R.id.Upload);




       Upload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
             intent.setType("image/*");
             startActivityForResult(intent,UPLOAD_CODE);
           }
       });


       Camera.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(intent,CAMERA_CODE);
           }
       });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==UPLOAD_CODE && data!=null)
        {
            try {
                bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());

                mat=new Mat();
                Utils.bitmapToMat(bitmap,mat);

                Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);

                Utils.matToBitmap(mat,bitmap);

                createImageFromBitmap(bitmap);
                openEditpage();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode==CAMERA_CODE && data!=null){
            bitmap=(Bitmap) data.getExtras().get("data");


            mat=new Mat();
            Utils.bitmapToMat(bitmap,mat);

            Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);

            Utils.matToBitmap(mat,bitmap);


            createImageFromBitmap(bitmap);
            openEditpage();

        }
    }

    void getPermission(){
        if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},102);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==102 && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getPermission();
            }
        }
    }

    public void openEditpage(){
        Intent intent = new Intent(this,Editpage.class);

        startActivity(intent);
    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
}