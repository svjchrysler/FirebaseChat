package edu.galileo.andriod.appfirebase;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnSend)
    Button btnSend;

    int cont = 0;

    @BindView(R.id.txtMessage)
    EditText txtMessage;

    @BindView(R.id.txtView)
    TextView txtView;

    @BindView(R.id.imageView)
    ImageView img;

    Uri imagenGaleria;

    DatabaseReference myRef;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configFirebase();
        getMessage();
        img.setImageResource(R.drawable.naruto);
    }

    private void configFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        myRef =  database.getReference();
        storageRef = storage.getReferenceFromUrl("gs://react-firebase-9ad62.appspot.com");
    }

    @OnClick(R.id.imageView)
    public void galeria() {
        RxImagePicker.with(this).requestImage(Sources.GALLERY).subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                Util.uriImage = uri;
                img.setImageURI(uri);
            }
        });
    }

    @OnClick(R.id.btnSend)
    public void sendMessage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Subiendo");
        progressDialog.show();

         AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                /*InputStream stream = null;
                try {
                    //stream = new FileInputStream(new File("/storage/emulated/0/Download/newsletter-naruto3.png"));
                    stream = new FileInputStream(new File(Util.uriImage.getPath()));
                } catch (FileNotFoundException e) {
                    Log.e("error", e.getMessage());
                }*/

                img.setDrawingCacheEnabled(true);
                img.buildDrawingCache();
                Bitmap bitmap = img.getDrawingCache();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                StorageReference mountainImagesRef = storageRef.child("images/d.jpg");

                UploadTask uploadTask = mountainImagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("Error", exception.getMessage());
                        progressDialog.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("Correcto", taskSnapshot.getMetadata().toString());
                        progressDialog.dismiss();
                    }
                });
                return null;
            }
        };

        task.execute();
/*
        Chat chat = new Chat("erman", txtMessage.getText().toString());
        myRef.push().setValue(chat);*/


    }

    private void getMessage() {
        ChildEventListener childEnventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Chat chaT = dataSnapshot.getValue(Chat.class);
                txtView.setText(txtView.getText().toString() + chaT.getMessage() + " \n");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addChildEventListener(childEnventListener);
    }
}
