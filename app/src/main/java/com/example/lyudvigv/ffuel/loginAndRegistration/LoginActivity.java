package com.example.lyudvigv.ffuel.loginAndRegistration;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.main_tab.MainTabActivity;

/**
 * Created by LyudvigV on 9/5/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private UserInfo _userInfo;

    private Activity _thisActivity;

    private String _baseUrl;
    private String _loginUrl;

    private TextView _tvEnter;
    private TextView _tvRegister;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        _thisActivity=this;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        _baseUrl = getResources().getString(R.string.service_url);
        _loginUrl = _baseUrl + "api/json/account/signin";



        _tvEnter = (TextView) findViewById(R.id.tvEnter);
        _tvEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(_thisActivity, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(_thisActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                    if (ActivityCompat.shouldShowRequestPermissionRationale(_thisActivity,Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                        ActivityCompat.requestPermissions(_thisActivity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);//MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    }
                    else {
                        // No explanation needed; request the permission


                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                login(_loginUrl);
                            } catch (Exception e) {}
                        }
                    });
                    thread.start();
                }



            }
        });



        _tvRegister = (TextView) findViewById(R.id.tvRegister);

        _tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
        //getSupportActionBar().hide();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                login(_loginUrl);
                            } catch (Exception e) {}
                        }
                    });
                    thread.start();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);



                    AlertDialog.Builder dialog = new AlertDialog.Builder(_thisActivity);
                    dialog.setMessage("Уважаемый ползователь, прилажению нужно узнать ваше местонахождение, поэтому вы должны нажмать ALLOW кнопку");
                    dialog.show();
                    //ActivityCompat.requestPermissions(_thisActivity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void login(String url) {

        Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
        startActivity(intent);
        /*Map<String,String> headers = new HashMap<>();
        headers.put("Content-type","application/json");

        JSONObject bodyParams = new JSONObject();
        try {
            bodyParams.accumulate("Login", "d@d.d");
            bodyParams.accumulate("Password", "dd");
            String response = RequestManager.post(url,headers,bodyParams);

            if(response.length()!=0)
            {
                _userInfo = new Gson().fromJson(response, UserInfo.class);
                ((MyApplication)this.getApplication()).setUserInfo(_userInfo);

                Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
                startActivity(intent);
            }else {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"Invalid Username or password", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e) {
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Invalid Service Url", Toast.LENGTH_LONG).show();
                }
            });
        }*/
    }
    public boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }

    }
}
