package com.udb.mlkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button gotolabel = (Button) findViewById(R.id.gotolabel);
        gotolabel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(200);

                Intent intent = new Intent(MainActivity.this, label.class);
                startActivity(intent);
            }

        });

        Button gotoface = (Button) findViewById(R.id.gotoface);
        gotoface.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(200);

                Intent intent = new Intent(MainActivity.this, facedet.class);
                startActivity(intent);


            }

        });

        Button gototext = (Button) findViewById(R.id.gototext);
        gototext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(200);

                Intent intent = new Intent(MainActivity.this, textdet.class);
                startActivity(intent);
            }

        });
    }
}
