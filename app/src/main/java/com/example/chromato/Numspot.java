package com.example.chromato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Numspot extends AppCompatActivity {
    private EditText nolane;
    private EditText nospot;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numspot);

        Uri uri = Uri.fromFile(getFileStreamPath("myImage3"));
        ImageView imageView4= findViewById(R.id.imageView);
        imageView4.setImageURI(uri);

        nolane=findViewById(R.id.nolane);
        nospot=findViewById(R.id.nospot);
        confirm=findViewById(R.id.confirm2);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lanes = Integer.parseInt(nolane.getText().toString());
                int spots = Integer.parseInt(nospot.getText().toString());


                Intent intent = new android.content.Intent(Numspot.this,Laneselectpage.class);
                int nums=1;
                intent.putExtra("lane", lanes);
                intent.putExtra("spot", spots);
                intent.putExtra("nums", nums);//for lane select no.

                startActivity(intent);
            }
        });


    }
}