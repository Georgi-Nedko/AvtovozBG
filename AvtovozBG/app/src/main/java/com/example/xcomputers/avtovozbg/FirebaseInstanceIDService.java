package com.example.xcomputers.avtovozbg;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.xcomputers.avtovozbg.CarsActivity.registerToken;

/**
 * Created by xComputers on 10.10.2016 г..
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(REG_TOKEN, token);

    }


}
