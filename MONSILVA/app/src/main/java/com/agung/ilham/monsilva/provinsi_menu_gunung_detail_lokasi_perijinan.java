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
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class provinsi_menu_gunung_detail_lokasi_perijinan extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private static  String   REGISTER_URL = null;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = provinsi_menu_gunung_detail_lokasi_perijinan.class.getSimpleName();
    private LocationRequest mLocationRequest;
    String item;
    double currentLatitude,currentLongitude;
    boolean gps_enabled = false;
    LocationManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinsi_menu_gunung_detail_lokasi_pos_perijinan);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        try
        {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }
        catch(Exception ex)
        {

        }


        checkKoneksi();



        Intent page = getIntent();
        item = page.getStringExtra("item");
        mMap.setMyLocationEnabled(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds 1000
                    .setFastestInterval(1000); // 1 second, in milliseconds
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the map.

            if (mMap == null)
            {
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                if (mMap != null)
                {
                    setUpMap();
                }
            }

    }

    private void setUpMap()
    {
        mMap.addMarker(new MarkerOptions().position(new LatLng(provinsi_menu_gunung_detail.latTujuan, provinsi_menu_gunung_detail.lngTujuan)).title("Pos Perijinan Pendakian Gunung"));
    }

    @Override
    public void onConnected(Bundle bundle)
    {


        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null)
        {

           LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else
        {
            handleNewLocation(location);
        }

    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution())
        {
            try
            {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    private void handleNewLocation(Location location)
    {
        Log.d(TAG, location.toString());

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        if (location != null)
        {

            getdatarute();

        }
         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }


    @Override
    public void onLocationChanged(Location location)
    {
        handleNewLocation(location);

    }

    private List<LatLng> decodePoly(String encoded)
    {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len)
        {
            int b, shift = 0, result = 0;
            do
            {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do
            {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        onBackPressed();
        return true;
    }

    private void getdatarute()
    {

        REGISTER_URL = "https://maps.googleapis.com/maps/api/directions/json?origin="+currentLatitude+","+currentLongitude+"&destination="+provinsi_menu_gunung_detail.latTujuan+","+provinsi_menu_gunung_detail.lngTujuan+"&key=AIzaSyCKZqJDJIbrVphot8k_UnPq8BWsqXuIkY0";
        Toast.makeText(provinsi_menu_gunung_detail_lokasi_perijinan.this,REGISTER_URL,Toast.LENGTH_LONG).show();
        Toast.makeText(provinsi_menu_gunung_detail_lokasi_perijinan.this,provinsi_menu_gunung_detail.latTujuan+","+provinsi_menu_gunung_detail.lngTujuan,Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try
                        {

                            final JSONObject json = new JSONObject(response);
                            JSONArray routeArray = json.getJSONArray("routes");
                            JSONObject routes = routeArray.getJSONObject(0);
                            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                            String encodedString = overviewPolylines.getString("points");
                            List<LatLng> list = decodePoly(encodedString);




                            PolylineOptions line = new PolylineOptions().width(3).color(Color.BLUE);
                            for (int i = 0; i < list.size(); i++)
                            {
                                line.add(list.get(i));
                            }
                            mMap.addPolyline(line);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(list.get(0).latitude, list.get(0).longitude), 14));

                        }
                        catch (JSONException e)
                        {

                        }

                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), "ini route"+ error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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

            alertDialogBuilder.setTitle("Internet anda tidak aktif, apakah anda ingin mengaktifkannya?")
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

        setUpMapIfNeeded();



    }

    public void chekgps()
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Gps anda tidak aktif, apakah anda ingin mengaktifkannya?")
                .setCancelable(false);

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);


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
        }
        return mobileDataEnabled;
    }
    public boolean toggleMobileDataConnection(boolean ON)
    {
        try
        {
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
        }
        catch (Exception e)
        {
        }
        return true;
    }

    public void onBackPressed()
    {
        Intent page = new Intent(provinsi_menu_gunung_detail_lokasi_perijinan.this, provinsi_menu_gunung_detail.class);
        page.putExtra("item",item);
        startActivity(page);
        provinsi_menu_gunung_detail_lokasi_perijinan.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent page = new Intent(provinsi_menu_gunung_detail_lokasi_perijinan.this, provinsi_menu_gunung_detail.class);
        page.putExtra("item",item);
        startActivity(page);
        provinsi_menu_gunung_detail_lokasi_perijinan.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }
}
