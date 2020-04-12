package com.johnolafenwa.pytorchandroid;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int cameraRequestCode = 001;

    Classifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get Permission in the first time
        SharedPreferences sharedPreferences = getSharedPreferences("New_Here",0);
        Boolean first_run = sharedPreferences.getBoolean("is_firstrun",true);
        if (first_run){
            sharedPreferences.edit().putBoolean("is_firstrun",false).commit();
            if (Build.VERSION.SDK_INT >= 23) {
                int REQUEST_CODE_PERMISSION_STORAGE = 100;
                String[] permissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                for (String str : permissions) {
                    if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                        this.requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE);
                        return;
                    }
                }
            }
        }

        classifier = new Classifier(Utils.assetFilePath(this,"squeezenet-noisy.pt"));

        Button capture = findViewById(R.id.capture);

        capture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Intent cameraIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent,cameraRequestCode);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permission,
                                           int[] grantResults) {
        if(grantResults[0] == 0){
            Toast.makeText(this, "Thank you, please restarting the app.", Toast.LENGTH_SHORT).show();
            exitapp();
        }
        else {
            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
            exitapp();
        }

    }
    public void exitapp(){
        new Handler().postDelayed(new Runnable(){
            public void run() {
                //execute the task
                System.exit(0);
            }
        }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == cameraRequestCode && resultCode == RESULT_OK && null != data){

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Intent resultView = new Intent(this,Result.class);

            resultView.putExtra("imagedata",picturePath);

            Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);

            String pred = classifier.predict(imageBitmap);
            resultView.putExtra("pred",pred);

            startActivity(resultView);

        }

    }
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
