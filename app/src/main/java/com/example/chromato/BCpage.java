package com.example.chromato;

import static java.lang.Math.sqrt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Rect;
import android.widget.Toast;


public class BCpage extends AppCompatActivity {
    private Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcpage);

        try {
          bitmap = BitmapFactory.decodeStream(BCpage.this.openFileInput("myImage2"));//as bitmap
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Mat image = new Mat();
        Utils.bitmapToMat(bitmap, image);
        ImageView imageView4= findViewById(R.id.imageView6);
        imageView4.setImageBitmap(bitmap);


        final DragRectView view = (DragRectView) findViewById(R.id.dragRect);

        if (null != view) {
            view.setOnUpCallback(new DragRectView.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                            Toast.LENGTH_LONG).show();

                    int width= (view.width()*bitmap.getWidth())/view.getWidth();
                    int height= (view.height()*bitmap.getHeight())/view.getHeight();
// Define the structuring element
                    double r = sqrt(width^ 2 + height^ 2);  // radius of the structuring element

                    Mat structuring_element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2 * r + 1, 2 * r + 1));

// Perform morphological opening
                    Mat opened = new Mat();
                    Imgproc.morphologyEx(image, opened, Imgproc.MORPH_OPEN, structuring_element);

// Subtract the opened image from the original image to get the corrected image
                    Mat corrected = new Mat();
                    Core.subtract(image, opened, corrected);

// Convert the corrected image back to a Bitmap for display
                    Bitmap correctedBitmap = Bitmap.createBitmap(corrected.cols(), corrected.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(corrected, correctedBitmap);
                    if (correctedBitmap != null)
                    { imageView4.setImageBitmap(correctedBitmap);
                    createImageFromBitmap(correctedBitmap);}

                    bitmap = correctedBitmap.copy(Bitmap.Config.ARGB_8888, true);

                }
            });
        }

        Button Back=findViewById(R.id.Back2);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new android.content.Intent(BCpage.this,Editpage.class);

                startActivity(intent);

            }
        });

    Button Confirm=findViewById(R.id.confirm);
        Confirm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            createImageFromBitmap(bitmap);
           Intent intent = new android.content.Intent(BCpage.this,Numspot.class);

            startActivity(intent);

        }
    });
}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage3";
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


