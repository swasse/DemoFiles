package be.ehb.demofiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //constante request //magic numbers are bad
    private final int REQUEST_PHOTOPAKKEN = 1;
    //ui widgets
    private EditText etInput;
    private ImageView ivProfile;
    //File naar foto
    private File photofile;
    //interaction
    private View.OnClickListener profileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takeAPhoto();
        }
    };

    private void takeAPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String imageFileName = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        photofile = new File(storageDir + "/" + imageFileName + ".jpg");

        Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), "be.ehb.demofiles.fileprovider", photofile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_PHOTOPAKKEN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //werkt enkel voor intents zonder uri
        if(requestCode == REQUEST_PHOTOPAKKEN && resultCode == RESULT_OK){
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            ivProfile.setImageBitmap(bitmap);

            Picasso.get().load(photofile).into(ivProfile);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = findViewById(R.id.et_input);
        ivProfile = findViewById(R.id.iv_profile);
        ivProfile.setOnClickListener(profileClickListener);
    }

    //werkt enkel voor buttons!
    //moet public void zijn, enkel view als parameter
    public void onReadClick(View v) {
        try {
            FileInputStream fileInputStream = getApplicationContext().openFileInput("mijnBestand.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            OnsObject toRead = (OnsObject) objectInputStream.readObject();
            etInput.setText(toRead.getOnzeString());
            objectInputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_LONG).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onWriteClick(View v) {
        try {
            //waar staat de file?
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput("mijnBestand.txt", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            //wat slaan we op
            OnsObject toWrite = new OnsObject(etInput.getText().toString());
            //effectief opslaan
            objectOutputStream.writeObject(toWrite);
            //verbinding naar file niet meer nodig, werk is gebeurd
            objectOutputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
