package com.example.chromato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Peakpage extends AppCompatActivity {

private Button reset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peakpage);
        reset=findViewById(R.id.reset);
        ArrayList<ArrayList<Double>> array = (ArrayList<ArrayList<Double>>) getIntent().getSerializableExtra("array_key");


        TableLayout table = new TableLayout(this);
        table.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        TextView cell = null;
        for (int i = 0; i < array.size(); i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < array.get(i).size(); j++) {
                cell = new TextView(this);
                cell.setText(String.valueOf(array.get(i).get(j)));
                cell.setGravity(Gravity.CENTER);
                cell.setPadding(10, 30, 10, 10);
                cell.setTextSize(15);
                cell.setTextColor(Color.BLACK);
                cell.setTypeface(Typeface.DEFAULT_BOLD);
                cell.setBackgroundColor(Color.WHITE);

                row.addView(cell);
                row.setGravity(Gravity.CENTER);
            }
            table.addView(row);
            Log.d("MYINT", "row: " + row);
        }

        table.setGravity(Gravity.CENTER);
        ((ViewGroup) findViewById(R.id.peakpage)).addView(table);



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Peakpage.this,Mainpage.class);
                startActivity(intent);
            }
        });

    }
}