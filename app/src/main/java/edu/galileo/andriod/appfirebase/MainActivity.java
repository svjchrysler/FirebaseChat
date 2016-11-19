package edu.galileo.andriod.appfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    @BindView(R.id.btnSend)
    Button btnSend;

    @BindView(R.id.txtMessage)
    EditText txtMessage;

    @BindView(R.id.txtView)
    TextView txtView;

    @BindView(R.id.imageView)
    ImageView img;



    DatabaseReference myRef;
    StorageReference storageRef;
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        configFirebaseAuth();
        authenticate();
        configFirebase();
        getMessage();
        img.setImageResource(R.drawable.naruto);

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        findViewById(R.id.sign_in_button).setOnClickListener(this);

    }


    private void configFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        myRef =  database.getReference();
        storageRef = storage.getReferenceFromUrl("gs://react-firebase-9ad62.appspot.com");
    }

    private void configFirebaseAuth() {
       //mAuth = FirebaseAuth.getInstance();
        //Log.e("Estado", mAuth.getCurrentUser().getEmail());
        //Toast.makeText(this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
    }

    private void authenticate() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .requestProfile()
                .build();
    }

    private void signIn() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.e("Error login", result.toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        this.setTitle(account.getDisplayName());
        Picasso.with(this).load(account.getPhotoUrl()).into(img);


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

                img.setDrawingCacheEnabled(true);
                img.buildDrawingCache();
                Bitmap bitmap = img.getDrawingCache();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                String timeStamp = "image"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())+".png";
                StorageReference mountainImagesRef = storageRef.child("images/"+timeStamp);
                /*InputStream stream = null;
                try {
                    stream = new FileInputStream(new File(Util.uriImage.getPath()));
                } catch (FileNotFoundException e) {
                    Log.e("Error Path", e.getMessage());
                }*/

                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("image/png")
                        .build();



                UploadTask uploadTask = mountainImagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                    }
                });
                return null;
            }
        };

        task.execute();


        Chat chat = new Chat(this.getTitle().toString(), txtMessage.getText().toString());
        myRef.push().setValue(chat);

    }

    private void getMessage() {
        ChildEventListener childEnventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Chat chaT = dataSnapshot.getValue(Chat.class);
                txtView.setText(txtView.getText().toString() + chaT.getUsername() + ": " + chaT.getMessage() + " \n");
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.Createaccount:
                Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
