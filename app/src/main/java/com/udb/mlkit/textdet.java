package com.udb.mlkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class textdet extends AppCompatActivity {

    private Button captureimgbtn, detecttxtbtn;
    private ImageView imgview;
    private TextView textview;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textdet);

        captureimgbtn = findViewById(R.id.capture_image);
        detecttxtbtn = findViewById(R.id.detect_text);
        imgview = findViewById(R.id.image_view);
        textview = findViewById(R.id.text_display);

        captureimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview.setText("");
                dispatchTakePictureIntent();
            }
        });

        detecttxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibb.vibrate(250);
               detecttextfromimage();
            }
        });


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imgview.setImageBitmap(imageBitmap);
        }
    }

    private void detecttextfromimage()
    {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                displaytextfromimage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(textdet.this,"Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displaytextfromimage(FirebaseVisionText firebaseVisionText){

        List<FirebaseVisionText.Block> blocklist = firebaseVisionText.getBlocks();
        if (blocklist.size() == 0 )
        {
            Toast.makeText(this,"No Text Found in Image",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StringBuilder text= new StringBuilder();
            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
                text.append(block.getText() + "\n");
            }
            textview.setTextSize(17);
            textview.setText(text.toString());

        }
    }

}
