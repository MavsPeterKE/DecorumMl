package com.example.android.DecorumMl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.DecorumMl.adapters.MainAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {

    FloatingActionButton cameraButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    ImageView imageView;
    private static final String TAG = MainActivity.class.getSimpleName();
    int result;
    Button detectButton;
    TextView textTextView;
    TextView confidenceTextView;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    ArrayList<FirebaseVisionLabel> firebaseVisionLabels = new ArrayList<>() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        cameraButton = findViewById(R.id.camera_fab);
        imageView = findViewById(R.id.captured_image);
        detectButton = findViewById(R.id.detect_button);
        textTextView = findViewById(R.id.text);
        confidenceTextView = findViewById(R.id.confidence);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();

            }
        });


        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "Value of firebaseImageLabels " + firebaseVisionLabels.size());
                runImageRecognition();

                if (firebaseVisionLabels.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Can't Identify Image, Try different one", Toast.LENGTH_SHORT).show();
                }

            }
        });


        recyclerView = findViewById(R.id.rec_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


    }

    public void getSelectedItem(FirebaseVisionLabel visionLabel,int pos){
        //showSuccessDialogue(visionLabel);
    }

    private void showSuccessDialogue(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Successfully Detected");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "View Recommended Items",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(MainActivity.this,RecommendationActivity.class));
                    }
                });
        alertDialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void runImageRecognition() {

       // FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(imageBitmap, result);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

      /**  FirebaseVisionLabelDetectorOptions options =
                new FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(0.8f)
                        .build(); */

         FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
         .getVisionLabelDetector();

        Task<List<FirebaseVisionLabel>> resultImage =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionLabel> labels) {
                                        // Task completed successfully
                                        // ...
                                        detectButton.setEnabled(false);
                                        getDetectedImages(labels.get(0).getLabel(),labels.get(1).getLabel());
                                        for (FirebaseVisionLabel label: labels) {
                                            String text = label.getLabel();
                                            String entityId = label.getEntityId();
                                            float confidence = label.getConfidence();

                                            firebaseVisionLabels.add(label);

                                         //   textTextView.setText(text);
                                           // confidenceTextView.setText(String.valueOf(confidence));

                                            mainAdapter = new MainAdapter(MainActivity.this, firebaseVisionLabels);
                                            recyclerView.setAdapter(mainAdapter);

                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        detectButton.setEnabled(true);
                                        Toast.makeText(getApplicationContext(), "Something Went wrong! Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                });


    }

    private String getDetectedImages(String item1, String item2){
        String result = "No Category Found for Product";
        String resul2 = "No Category Found for Product";
        HashMap<String,List<String>> categories = new HashMap<>();
        List<String> kitchen = new ArrayList<>();
        kitchen.add("cup");
        kitchen.add("Spoon");
        kitchen.add("juice");
        kitchen.add("Sufuria");
        categories.put("kitchen ",kitchen);
        Log.e( "getDetectedImages: ", item1 + " "+item2 );

        List<String> bedroom = new ArrayList<>();
        bedroom.add("stand");
        bedroom.add("bed");
        bedroom.add("matress");
        bedroom.add("net");
        categories.put("Bedroom ",bedroom);

        List<String> sitting_room = new ArrayList<>();
        sitting_room.add("flower pot");
        sitting_room.add("table");
        sitting_room.add("mobile phone");
        sitting_room.add("tv");
        sitting_room.add("tableware");
        sitting_room.add("radio");
        sitting_room.add("couch");
        sitting_room.add("chair");
        sitting_room.add("room");
        sitting_room.add("curtain");
        categories.put("Sitting Room",sitting_room);

        //Loop through to check
        for (Map.Entry<String, List<String>> entry: categories.entrySet()){
            for (String catItem:entry.getValue()){
                if (item1.equalsIgnoreCase(catItem)){
                    result =  entry.getKey() + " Item";
                }if (item2.equalsIgnoreCase(catItem)){
                    resul2 = entry.getKey() + " Item";
                }
            }
        }
        String finalMsg1 = result.contains("No Category")?"":result;
        String finalMsg2 = resul2.contains("No Category")?"":resul2;
        String concat = finalMsg2.equals("")?" ":" And ";
        String msg = finalMsg1.contains(finalMsg2)? finalMsg1 :finalMsg1 + concat +finalMsg2;
        showSuccessDialogue(msg);
        return msg ;

    }



    @Override
    protected void onResume() {
        super.onResume();
        detectButton.setEnabled(true);
        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runImageRecognition();
            }
        });
        firebaseVisionLabels.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Context ctx=this; // or you can replace **'this'** with your **ActivityName.this**
        Intent i = ctx.getPackageManager().getLaunchIntentForPackage("com.rapid.Decorum");
        ctx.startActivity(i);
    }
}
