package com.example.chromato;

import static java.lang.Math.sqrt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.yalantis.ucrop.UCrop;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Editpage extends AppCompatActivity {


    
  private  Bitmap bitmap=null;
  private Uri uri=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpage);


        uri = Uri.fromFile(getFileStreamPath("myImage")); //get previos image as uri

//UCrop
        UCrop uCrop = UCrop.of( uri, uri);
// Customize the options as needed
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
// Start the UCrop activity
        uCrop.start(Editpage.this);



        ImageView imageView2= (ImageView) findViewById(R.id.imageView2);



        imageView2.setImageURI(uri);

        Button bc= findViewById(R.id.bc);
        bc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            createImageFromBitmap(bitmap);
            openBCpage();

            }
        });


        Button invert = findViewById(R.id.invert);
        invert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap!=null){
                Mat image = new Mat();
                Utils.bitmapToMat(bitmap, image);
                Core.bitwise_not(image, image);
                Bitmap invertedBitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(image, invertedBitmap);

                if (invertedBitmap != null) {
                    imageView2.setImageBitmap(invertedBitmap);
                    createImageFromBitmap(invertedBitmap);

                    bitmap = invertedBitmap.copy(Bitmap.Config.ARGB_8888, true);
                }}


            }
        });



        Button Back= findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainpage();
            }
        });
    }


    @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

        //set cropped image
        ImageView imageView2= (ImageView) findViewById(R.id.imageView2);
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            // UCrop successfully cropped the image
            Uri croppedImageUri = UCrop.getOutput(data);
            bitmap = BitmapFactory.decodeFile(croppedImageUri.getPath());

            // Update the ImageView with the cropped image

            imageView2.setImageBitmap(bitmap);
        }


    }

    public void openMainpage(){
        Intent intent = new Intent(this,Mainpage.class);
        startActivity(intent);
    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage2";
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

    public void openBCpage(){
        Intent intent = new Intent(this,BCpage.class);

        startActivity(intent);
    }
}