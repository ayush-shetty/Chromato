package com.example.chromato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Laneselectpage extends AppCompatActivity {

    private ImageView imageView6;
    private TextView num;
    private TextView word;
    private Button done;
    private TextView sliderValue;
    private int nums=1,numspot=1;

    private DragRectView view;
    private Bitmap bitmap=null,b=null,bs=null,bsr=null;
    double area = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laneselectpage);
        int lane = getIntent().getExtras().getInt("lane");
        int spot = getIntent().getExtras().getInt("spot");

        ArrayList<ArrayList<Double>> array = new ArrayList<>();
// Add rows to the array
        for (int i = 0; i < lane; i++) {
            array.add(new ArrayList<>());
        }



        try {
            bitmap = BitmapFactory.decodeStream(Laneselectpage.this.openFileInput("myImage3"));//as bitmap
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        imageView6= findViewById(R.id.imageView6);
        imageView6.setImageBitmap(bitmap);

        done = findViewById(R.id.done);
        num = findViewById(R.id.nums);
        word=findViewById(R.id.textView3);
        view = findViewById(R.id.dragRect2);
       // sliderValue = findViewById(R.id.slider_value);
        view.requestFocus();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.threshold, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView100 = popupView.findViewById(R.id.image_view100);
        SeekBar slider = popupView.findViewById(R.id.slider);
        Button over= popupView.findViewById(R.id.over);
        View parentView = findViewById(R.id.laneselect);





        if (null != view) {
            view.setOnUpCallback(new DragRectView.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                            Toast.LENGTH_LONG).show();
                    int width= (view.width()*bitmap.getWidth())/view.getWidth();
                    int height= (view.height()*bitmap.getHeight())/view.getHeight();
                    int x=(view.x()*bitmap.getWidth())/view.getWidth();
                    int y=(view.y()*bitmap.getHeight())/view.getHeight();





                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (rect != null && bitmap!=null)
                            {
                                Intent intent = new Intent(Laneselectpage.this,Peakpage.class);



                                if(word.getText().equals("Select Lane No. "))
                                {

                                b= Bitmap.createBitmap(bitmap,x,y,width,height);
                                imageView6.setImageBitmap(b);
                                word.setText("Select Spot No. ");
                                num.setText(String.valueOf(numspot));



                                return;//exit
                                    }

                             if(word.getText().equals("Select Spot No. ")) {
                               if(numspot<=spot)
                                {
                                    Log.d("MYINT", "numspot: " + numspot);

                                    int widthb= (width*b.getWidth())/bitmap.getWidth();
                                    int heightb= (height*b.getHeight())/bitmap.getHeight();
                                    int xb= (x*b.getWidth())/bitmap.getWidth();
                                    int yb=(y*b.getHeight())/bitmap.getHeight();
                                   // yb=(int)(yb-15.0);
                                  //  if((heightb+40)<b.getHeight())
                                  //  heightb=(heightb+40);


                                    Log.d("MYINT", "width: " + widthb);
                                    Log.d("MYINT", "height: " + heightb);
                                    Log.d("MYINT", "bwidth: " + b.getWidth());
                                    Log.d("MYINT", "bheight: " + b.getHeight());

                                    bs= Bitmap.createBitmap(b,xb,yb,widthb,heightb);

                                    bsr = Bitmap.createBitmap(bs);


                                           parentView.setVisibility(View.GONE);

                                           slider.setMax(70);
                                           slider.setProgress(70);
                                       popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

                                    ++numspot;
                                    num.setText(String.valueOf(numspot));



                                    return;


                                }

                               else{
                               nums++;

                               if(nums>lane)
                               {   //intent.putExtra("matrix", matrix);
                                   intent.putExtra("array_key", array);
                                   Log.d("MYINT", "area: " + array);
                                   startActivity(intent);}


                               word.setText("Select Lane No. ");
                               num.setText(String.valueOf(nums));}
                               numspot=1;
                               imageView6.setImageBitmap(bitmap);
                               return;

                            }

                            }
                        }
                    });
                }
            });
        }

        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                Log.d("MYINT", "array index +1 " + nums);
                array.get(nums-1).add(area);
                if(numspot>spot)
                    done.performClick();
                parentView.setVisibility(View.VISIBLE);


            }
        });


        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
               // sliderValue.setText(String.valueOf(value));
                if(bs!=null)
                {
                    Log.d("MYINT", "spot select done b!=null " + bs);

                    Mat imageMat = new Mat();
                    Utils.bitmapToMat(bs, imageMat);
                    Log.d("MYINT", "imagemat done " + imageMat);
// Define a threshold value
                    Imgproc.cvtColor(imageMat,imageMat,Imgproc.COLOR_BGRA2GRAY);

                    double thresholdValue = slider.getProgress();
                    Mat binaryImage = new Mat();
// Apply thresholding to the image
                    if(imageMat!=null)
                    {Imgproc.threshold(imageMat, binaryImage, thresholdValue, 255, Imgproc.THRESH_BINARY);

                        Log.d("MYINT", "threshold done " + binaryImage);

//Find the contours of the image
                        ArrayList<MatOfPoint> contours = new ArrayList<>();
                        Log.d("MYINT", "submat done " + -1);
                        if(binaryImage!=null)
                        {    Log.d("MYINT", "imageROI not null " + binaryImage);

                            Imgproc.findContours(binaryImage, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
                            Mat img = new Mat();
                            if(bitmap!=null)
                            {Utils.bitmapToMat(bsr, img);

                                for(int j = 0; j < contours.size(); j++) {
                                    Imgproc.drawContours(img, contours, j, new Scalar(255, 0, 0,255), -1);
                                    Log.d("MYINT", "imageROI not null " +img);
                                }
                                Utils.matToBitmap(img, bs);
                                imageView100.setImageBitmap(bs);

                                //Calculate the area of the contours

                                for(int j = 0; j < contours.size(); j++) {
                                    area += Imgproc.contourArea(contours.get(j));
                                }
                                Log.d("MYINT", "area: " + area);}}}};


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {



            }
        });
    }
}