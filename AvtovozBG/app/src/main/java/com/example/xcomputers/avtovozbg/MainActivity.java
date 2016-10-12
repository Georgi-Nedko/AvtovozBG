package com.example.xcomputers.avtovozbg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private static final int RC_SIGN_IN = 9001;
    private TextView welcomeTV;
    protected GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton signInButton;
    private static String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("myTestTopic");
        String token = FirebaseInstanceId.getInstance().getToken();


        welcomeTV = (TextView) findViewById(R.id.welcomeTV);

        json = "{\"contacts\":{\"name\":\"Красимир Стоев\",\"phone\":\"0888552211\"},\"" +
                "posts\":[" +
                "{\"brand\":\"Opel\",\"model\":\"Astra\",\"hp\":90,\"year\":2000,\"km\":143000,\"color\":\"silver\",\"description\":\"В добро състояние, с 4 зимни гуми\",\"urls\":[\"https://upload.wikimedia.org/wikipedia/commons/0/04/Opel_Astra_G_front_20081128.jpg\",\"http://images.forum-auto.com/mesimages/417472/astra%20(3).jpg\",\"https://upload.wikimedia.org/wikipedia/commons/5/59/Vauxhall.astra.bristol.750pix.jpg\",\"http://www.tuning.bg/public/upload/gallery/extended/normal/Opel_Astra_G_24asti_1.png\",\"https://www.namcars.net/image/2006-Opel-Astra-1.9-GTC-25627-8698828_2.jpg\",\"http://www.picautos.com/images/opel-astra-gtc-2.0-turbo-cosmo-04.jpg\",\"http://www.motorstown.com/images/opel-astra-2.2-dti-06.jpg\"]}," +
                "{\"brand\":\"Ford\",\"model\":\"Fiesta\",\"hp\":60,\"year\":1996,\"km\":135000,\"color\":\"black\",\"description\":\"Удряна 4 пъти, не може да завива, най-добре за части\",\"urls\":[\"https://upload.wikimedia.org/wikipedia/commons/9/9d/Ford_Fiesta_MK3_rear_20070926.jpg\",\"http://www.powerful-cars.com/images/ford-d/1989-fiesta-4door-hatchback-11.jpg\",\"http://s1.cdn.autoevolution.com/images/gallery/FORDFiesta3Doors-2776_4.jpg\",\"http://www.fordfan.de/fordcars/carpic/75/image/1996fiesta.jpg\",\"http://images.forum-auto.com/mesimages/229428/Rouge.jpg\"]}," +
                "{\"brand\":\"Opel\",\"model\":\"Astra\",\"hp\":90,\"year\":2000,\"km\":143000,\"color\":\"silver\",\"description\":\"В добро състояние, с 4 зимни гуми\",\"urls\":[\"https://upload.wikimedia.org/wikipedia/commons/0/04/Opel_Astra_G_front_20081128.jpg\",\"http://images.forum-auto.com/mesimages/417472/astra%20(3).jpg\",\"https://upload.wikimedia.org/wikipedia/commons/5/59/Vauxhall.astra.bristol.750pix.jpg\",\"http://www.tuning.bg/public/upload/gallery/extended/normal/Opel_Astra_G_24asti_1.png\",\"https://www.namcars.net/image/2006-Opel-Astra-1.9-GTC-25627-8698828_2.jpg\",\"http://www.picautos.com/images/opel-astra-gtc-2.0-turbo-cosmo-04.jpg\",\"http://www.motorstown.com/images/opel-astra-2.2-dti-06.jpg\"]}," +
                "{\"brand\":\"Ford\",\"model\":\"Fiesta\",\"hp\":60,\"year\":1996,\"km\":135000,\"color\":\"black\",\"description\":\"Удряна 4 пъти, не може да завива, най-добре за части\",\"urls\":[\"https://upload.wikimedia.org/wikipedia/commons/9/9d/Ford_Fiesta_MK3_rear_20070926.jpg\",\"http://www.powerful-cars.com/images/ford-d/1989-fiesta-4door-hatchback-11.jpg\",\"http://s1.cdn.autoevolution.com/images/gallery/FORDFiesta3Doors-2776_4.jpg\",\"http://www.fordfan.de/fordcars/carpic/75/image/1996fiesta.jpg\",\"http://images.forum-auto.com/mesimages/229428/Rouge.jpg\"]}," +
                "{\"brand\":\"Opel\",\"model\":\"Astra\",\"hp\":90,\"year\":2000,\"km\":143000,\"color\":\"silver\",\"description\":\"В добро състояние, с 4 зимни гуми\",\"urls\":[\"https://upload.wikimedia.org/wikipedia/commons/0/04/Opel_Astra_G_front_20081128.jpg\",\"http://images.forum-auto.com/mesimages/417472/astra%20(3).jpg\",\"https://upload.wikimedia.org/wikipedia/commons/5/59/Vauxhall.astra.bristol.750pix.jpg\",\"http://www.tuning.bg/public/upload/gallery/extended/normal/Opel_Astra_G_24asti_1.png\",\"https://www.namcars.net/image/2006-Opel-Astra-1.9-GTC-25627-8698828_2.jpg\",\"http://www.picautos.com/images/opel-astra-gtc-2.0-turbo-cosmo-04.jpg\",\"http://www.motorstown.com/images/opel-astra-2.2-dti-06.jpg\"]}," +
                "{\"brand\":\"Ford\",\"model\":\"Fiesta\",\"hp\":60,\"year\":1996,\"km\":135000,\"color\":\"black\",\"description\":\"Удряна 4 пъти, не може да завива, най-добре за части\",\"urls\":[\"https://upload.wikimedia.org/wikipedia/commons/9/9d/Ford_Fiesta_MK3_rear_20070926.jpg\",\"http://www.powerful-cars.com/images/ford-d/1989-fiesta-4door-hatchback-11.jpg\",\"http://s1.cdn.autoevolution.com/images/gallery/FORDFiesta3Doors-2776_4.jpg\",\"http://www.fordfan.de/fordcars/carpic/75/image/1996fiesta.jpg\",\"http://images.forum-auto.com/mesimages/229428/Rouge.jpg\"]}," +
                "{\"brand\":\"Opel\",\"model\":\"Astra\",\"hp\":90,\"year\":2000,\"km\":143000,\"color\":\"silver\",\"description\":\"В добро състояние, с 4 зимни гуми\",\"urls\":[\"https://upload.wikimedia.org/wikipedia/commons/0/04/Opel_Astra_G_front_20081128.jpg\",\"http://images.forum-auto.com/mesimages/417472/astra%20(3).jpg\",\"https://upload.wikimedia.org/wikipedia/commons/5/59/Vauxhall.astra.bristol.750pix.jpg\",\"http://www.tuning.bg/public/upload/gallery/extended/normal/Opel_Astra_G_24asti_1.png\",\"https://www.namcars.net/image/2006-Opel-Astra-1.9-GTC-25627-8698828_2.jpg\",\"http://www.picautos.com/images/opel-astra-gtc-2.0-turbo-cosmo-04.jpg\",\"http://www.motorstown.com/images/opel-astra-2.2-dti-06.jpg\"]}," +
                "{\"brand\":\"Ford\",\"model\":\"Fiesta\",\"hp\":60,\"year\":1996,\"km\":135000,\"color\":\"black\",\"description\":\"Удряна 4 пъти, не може да завива, най-добре за части\",\"urls\":[\"https://upload.wikimedia.org/wikipedia/commons/9/9d/Ford_Fiesta_MK3_rear_20070926.jpg\",\"http://www.powerful-cars.com/images/ford-d/1989-fiesta-4door-hatchback-11.jpg\",\"http://s1.cdn.autoevolution.com/images/gallery/FORDFiesta3Doors-2776_4.jpg\",\"http://www.fordfan.de/fordcars/carpic/75/image/1996fiesta.jpg\",\"http://images.forum-auto.com/mesimages/229428/Rouge.jpg\"]}]}";


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
                signIn();
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
            String displayName = result.getSignInAccount().getDisplayName();
            String eMail = result.getSignInAccount().getEmail();
            Log.e("displayName", displayName);
            Log.e("eMail", eMail);
            String value = null;
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    value = getIntent().getExtras().getString(key);
                    Log.d("TAG", "Key: " + key + " Value: " + value);
                }
                value = getIntent().getExtras().getString("json");
            }
            changeScreen(displayName, eMail, json);
            hideProgressDialog();
            finish();

        } else {
            hideProgressDialog();
        }
    }


    @Override
    public void onStart() {

        mGoogleApiClient.connect();
        showProgressDialog();

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

        }
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
        //changeScreen(true);
    }
    public void changeScreen(String displayName, String eMail, String json){
        Intent intent = new Intent(MainActivity.this, CarsActivity.class);
        intent.putExtra("displayName", displayName);
        intent.putExtra("eMail", eMail);
        intent.putExtra("json", json);
        startActivity(intent);
        hideProgressDialog();
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
