package com.agung.ilham.monsilva;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PeringatanDini2 extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = provinsi_menu_gunung_detail_lokasi_perijinan.class.getSimpleName();
    private LocationRequest mLocationRequest;
    String item;
    double currentLatitude,currentLongitude;
    boolean gps_enabled = false;
    LocationManager lm;


    TextView LokasiBaru,Accuracy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Intent page = getIntent();
        item = page.getStringExtra("item");

        LokasiBaru = (TextView)findViewById(R.id.LokasiBaru);
        Accuracy  = (TextView)findViewById(R.id.Accuracy);

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        try
        {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }
        catch(Exception ex)
        {



        }

        checkKoneksi();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {


        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null)
        {

           LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
           Toast.makeText(this, "Lokasi ngga ketemu",Toast.LENGTH_SHORT).show();
        }
        else
        {
            handleNewLocation(location);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    private void handleNewLocation(Location location)
    {
        Log.d(TAG, location.toString());

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        float accuracy = location.getAccuracy();
        Accuracy.setText(Float.toString(accuracy));
        LokasiBaru.setText( Double.toString(currentLatitude) + " WORKS " + Double.toString(currentLongitude) + "");


    }


    @Override
    public void onLocationChanged(Location location)
    {
        handleNewLocation(location);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;
    }

  public  void checkKoneksi()
    {

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        final Location location=new Location("locationA");
        if (networkInfo != null && networkInfo.isConnected())
        {
            if(!gps_enabled)
            {
                chekgps();
            }
        }
        else
        {


            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setMessage("Internet anda tidak aktif, apakah anda ingin mengaktifkannya?")
                    .setCancelable(false);


            alertDialogBuilder.setPositiveButton("Ya",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {

                            // check current state first
                            boolean state = isMobileDataEnable();
                            // toggle the state
                            if(state)toggleMobileDataConnection(false);
                            else toggleMobileDataConnection(true);
                            Toast.makeText(getApplicationContext(),"Koneksi Mobile Di Aktifkan", Toast.LENGTH_SHORT).show();

                            if(!gps_enabled)
                            {
                                chekgps();
                            }
                            else
                            {
                                handleNewLocation(location);
                            }

                        }
                    });
            alertDialogBuilder.setNegativeButton("Tidak",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {

                            onBackPressed();

                        }
                    });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        }

    }



    public void chekgps()
    {



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Gps anda tidak aktif, apakah anda ingin mengaktifkannya?")
                .setCancelable(false);


        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);


                    }
                });
        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                        onBackPressed();

                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }
    public boolean isMobileDataEnable()
    {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        try
        {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        }
        catch (Exception e)
        {
            // Some problem accessible private API and do whatever error handling you want here
        }
        return mobileDataEnabled;
    }
    public boolean toggleMobileDataConnection(boolean ON)
    {
        try {
            //create instance of connectivity manager and get system connectivity service
            final ConnectivityManager conman = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            //create instance of class and get name of connectivity manager system service class
            final Class conmanClass  = Class.forName(conman.getClass().getName());
            //create instance of field and get mService Declared field
            final Field iConnectivityManagerField= conmanClass.getDeclaredField("mService");
            //Attempt to set the value of the accessible flag to true
            iConnectivityManagerField.setAccessible(true);
            //create instance of object and get the value of field conman
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            //create instance of class and get the name of iConnectivityManager field
            final Class iConnectivityManagerClass=  Class.forName(iConnectivityManager.getClass().getName());
            //create instance of method and get declared method and type
            final Method setMobileDataEnabledMethod= iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE);
            //Attempt to set the value of the accessible flag to true
            setMobileDataEnabledMethod.setAccessible(true);
            //dynamically invoke the iConnectivityManager object according to your need (true/false)
            setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
        } catch (Exception e){
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

        }
        return super.onKeyDown(keyCode, event);
    }
}
