package com.udb.mlkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;

import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;


import java.util.List;

public class label extends AppCompatActivity {

    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CAMERA"};
    private Button captureimgbtn, detecttxtbtn;
    private ImageView imgview;
    private TextView textview;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }

        captureimgbtn = findViewById(R.id.capture_image);
        detecttxtbtn = findViewById(R.id.detect_text);
        imgview = findViewById(R.id.image_view);
        textview = findViewById(R.id.text_display);

            captureimgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textview.setText("");
                    textview.setTextSize(16);
                    dispatchTakePictureIntent();
                }
            });

            detecttxtbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    detectlandfromimage();
                    Vibrator vibb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibb.vibrate(250);

                }
            });


        }

        private void dispatchTakePictureIntent () {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imgview.setImageBitmap(imageBitmap);
            }
        }

        private void detectlandfromimage ()
        {
            FirebaseVisionLabelDetectorOptions options = new FirebaseVisionLabelDetectorOptions.Builder()
                    .setConfidenceThreshold(0.6f)
                    .build();
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
            FirebaseVisionLabelDetector detector = FirebaseVision.getInstance().getVisionLabelDetector(options);
            detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionLabel>>() {
                @Override
                public void onSuccess(List<FirebaseVisionLabel> firebaseVisionLabels) {
                    if (firebaseVisionLabels.isEmpty()) {
                        textview.setText("No Label Found");
                    } else {
                        for (FirebaseVisionLabel label : firebaseVisionLabels) {
                            int con = (int) (label.getConfidence() * 100);
                            textview.append(label.getLabel() + " ( Confidence: " + con + "% )" + "\n");
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(label.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }


