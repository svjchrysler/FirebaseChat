package edu.galileo.andriod.appfirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.coordinatorFather)
    CoordinatorLayout coordinatorLayout;

    private GoogleSignInOptions gso;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        configInit();
    }

    private void configInit() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserProfile", 0);
        String username = sharedPreferences.getString("username", null);
        String photo = sharedPreferences.getString("photo", null);
        String token = sharedPreferences.getString("token", null);
        if (username != null && photo != null && token != null) {
            Util.TOKEN_USER = token;
            startActivity(new Intent(MessageActivity.this, MainActivity.class));
            finish();
        } else {
            configFirebaseAuth();
        }
    }

    private void configFirebaseAuth() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                //.requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestProfile()
                .build();
    }

    @OnClick(R.id.btnLogin)
    protected void login() {
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
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Error al iniciar Session", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        configSharePreference(account);
        Toast.makeText(this, account.getEmail(), Toast.LENGTH_SHORT).show();
    }

    private void configSharePreference(GoogleSignInAccount account) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserProfile", 0);
        String username = sharedPreferences.getString("username", null);
        String photo = sharedPreferences.getString("photo", null);
        String token = sharedPreferences.getString("token", null);
        if (username == null || photo == null || token == null) {
            sharedPreferences.edit().putString("username", account.getDisplayName()).commit();
            sharedPreferences.edit().putString("photo", account.getPhotoUrl().toString()).commit();
            sharedPreferences.edit().putString("token", account.getId()).commit();
        }
        redirectActivity();
    }

    private void redirectActivity() {
        startActivity(new Intent(MessageActivity.this, MainActivity.class));
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
