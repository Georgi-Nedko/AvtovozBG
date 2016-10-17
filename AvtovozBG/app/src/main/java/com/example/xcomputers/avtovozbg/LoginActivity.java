package com.example.xcomputers.avtovozbg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.xcomputers.avtovozbg.model.WifiManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


    private static final int RC_SIGN_IN = 9001;

    private TextView appNameTV;
    protected GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton signInButton;
    private static String json;
    private static String displayName;
    private static String eMail;
    private String token;
    private AlertDialog alertDialog;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        wifiManager = WifiManager.getInstance(this);
        FirebaseMessaging.getInstance().subscribeToTopic("myTestTopic");
        token = FirebaseInstanceId.getInstance().getToken();

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Capture_it.ttf");
        appNameTV = (TextView) findViewById(R.id.appNameTV);
        appNameTV.setTypeface(custom_font);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(LocationServices.API)
                    .build();
        }

        signInButton = (SignInButton) findViewById(R.id.google_login_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiManager.isConnectingToInternet())
                    signIn();
                else
                    alertDialog = wifiManager.promptUserToTurnOnWifi();
            }
        });
    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.e("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            displayName = result.getSignInAccount().getDisplayName();
            eMail = result.getSignInAccount().getEmail();
            Log.e("displayName", displayName);
            Log.e("eMail", eMail);
            changeScreen(displayName, eMail);
            hideProgressDialog();
            finish();

        } else {
            //changeScreen(null,null, json);
            hideProgressDialog();

        }
    }

    @Override
    public void onStart() {
        alertDialog = wifiManager.promptUserToTurnOnWifi();
        mGoogleApiClient.connect();
       /* showProgressDialog();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.

            GoogleSignInResult result = opr.get();

            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });

        }*/
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("TAG", "onConnected called");

    }

    public void changeScreen(String displayName, String eMail) {
        Intent intent = new Intent(LoginActivity.this, CarsActivity.class);
        intent.putExtra("displayName", displayName);
        intent.putExtra("eMail", eMail);
        intent.putExtra("token", token);
        startActivity(intent);
        hideProgressDialog();
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
