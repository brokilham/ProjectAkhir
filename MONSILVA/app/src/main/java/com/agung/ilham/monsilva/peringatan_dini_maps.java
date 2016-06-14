package com.agung.ilham.monsilva;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class peringatan_dini_maps extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, GpsStatus.Listener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static final String TAG = peringatan_dini_maps.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    boolean gps_enabled = false;
    LocationManager lm;
    private GpsStatus mStatus;
    TextView StatusGps;
    int checkGpsFix = 0,Buttoncheck,checkAwalStart,checkTimer;
    private ArrayList<LatLng> points; //added
    Polyline line; //added
    TextView Mulai,WaktuMulai;
    NotificationManager mNotificationManager;
    int NOTIFIKASI = 1;
    String status = "ANDROID";
    private TextView Timer,Jarak;
    CountDownTimer timer;
    Intent LangLing;
   @Override
    protected void onCreate(Bundle savedInstanceState)
   {
        Log.d(status, "onCreate() event");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini_maps);

        StatusGps = (TextView) findViewById(R.id.StatusGps);
        Jarak = (TextView) findViewById(R.id.Jarak);
        Mulai = (TextView) findViewById(R.id.Mulai);
        WaktuMulai =  (TextView) findViewById(R.id.WaktuMulai);
        Mulai.setVisibility(View.INVISIBLE);
        points = new ArrayList<LatLng>(); //added
        Timer = (TextView) findViewById(R.id.Time);

        setUpMapIfNeeded();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        int buttonHeight = width/2;

        Jarak.setWidth(buttonHeight);
        Timer.setWidth(buttonHeight);

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.addGpsStatusListener(this);


        try
        {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }
        catch(Exception ex)
        {

        }
        if(!gps_enabled)
        {
            StatusGps.setText("GPS tidak diaktifkan. ketuk di sini untuk menuju 'Setelan lokasi Google'. Tekan tombol kembali untuk mundur");
        }



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)   // 10 seconds, in milliseconds 1000
                .setFastestInterval(1000) // 1 second, in milliseconds
                .setSmallestDisplacement(10); //10 meters
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(false);

                /*
        mMap.getUiSettings().setZoomControlsEnabled(true);*/


        StatusGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        Mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Mulai.setVisibility(View.INVISIBLE);
                HitungMulai();
            }
        });
    }


    @Override
    protected void onResume() {
        Log.d(status, "onResume() event");
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause()
    {    Log.d(status, "onPause() event");
        super.onPause();
        if (mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded()
    {    Log.d(status, "setUpMapIfNeeded() event");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void handleNewLocation(Location location)
    {    Log.d(status, "handleNewLocation() event");



        double currentLatitude,currentLongitude;
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));




    }

    private void redrawLine()
    {    Log.d(status, "redrawLine() event");

       // googleMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
       // addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    private void setUpMap()
    {   Log.d(status, "setUpMap() event");


          LatLng INDONESIA = new LatLng(6.1750,106.8283);
          mMap.moveCamera(CameraUpdateFactory.newLatLng(INDONESIA));

    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.d(status, location.toString());
        handleNewLocation(location);
    }

    @Override
    public void onConnected(Bundle bundle)
    { Log.d(status, "onConnected() event");
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
        Log.i(status, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {   Log.d(status, "onConnectionFailed() event");

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

    @Override
    public void onGpsStatusChanged(int event)
    {    Log.d(status, "onGpsStatusChanged() event");

        mStatus = lm.getGpsStatus(mStatus);
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                //Event sent when the GPS system has started.
                // Do Something with mStatus info
                StatusGps.setOnClickListener(null);
                StatusGps.setText("Menunggu sinyal Gps.");
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                //Event sent when the GPS system has stopped.
                // Do Something with mStatus info
                Toast.makeText(this,"Gps Dimatikan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(peringatan_dini_maps.this,MainActivity.class);
                startActivity(intent);
              break;

            case GpsStatus.GPS_EVENT_FIRST_FIX:
               //  Event sent when the GPS system has received its first fix since starting.
                // Do Something with mStatus info
                 StatusGps.setVisibility(View.INVISIBLE);
               /* Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                checkGpsFix++;
                handleNewLocation(location);*/
                Location gpslocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(gpslocation != null)
                {

                    //StatusGps.setText("GPS Info:"+gpslocation.getLatitude()+":"+gpslocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(new LatLng(gpslocation.getLatitude(), gpslocation.getLongitude())).title("Awal"));
                    LatLng latLng = new LatLng(gpslocation.getLatitude(), gpslocation.getLongitude());
                    points.add(latLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                    Mulai.setVisibility(View.VISIBLE);
                    checkGpsFix++;




                }
                lm.addGpsStatusListener(this);
                break;

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                //Event sent periodically to report GPS satellite status.
                // Do Something with mStatus info
                 Location gpslocationUpdate = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(gpslocationUpdate != null)
                {
                    if (checkGpsFix==1 && Buttoncheck == 1)
                    {

                        LatLng latLng = new LatLng(gpslocationUpdate.getLatitude(), gpslocationUpdate.getLongitude());
                        points.add(latLng); //added
                        redrawLine(); //added
                        double distance = SphericalUtil.computeLength(points);
                        String PembulatanDistance = String.format("%.2f", distance);
                        Jarak.setText(PembulatanDistance+"  Meter");

                        if(checkAwalStart == 0)
                        {   //customHandler.postDelayed(updateTimerThread, 0);
                            HitungMundur();
                            checkAwalStart++;


                        }
                    }
                }



                break;
        }

    }
 public void HitungMundur()
 {
     Log.d(status, "HitungMundur() event");

     //timer = new CountDownTimer(720000, 1000)
     timer =  new CountDownTimer(50000, 1000)
     {


         public void onTick(long millisUntilFinished)
         {
            // Timer.setText(": "+ millisUntilFinished / 1000);

             checkTimer++;
             Timer.setText(""+String.format("%d min, %d sec",
                     TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                     TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                     TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

         }

         public void onFinish()
         {  Log.d(status, "onFinish() event");

             checkTimer=0;
             Timer.setText("Selesai");
             mNotificationManager.cancel(NOTIFIKASI);
             Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
             Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
             r.play();
             Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
             v.vibrate(5000);

         }
     }.start();



 }


    public void HitungMulai()
    {
        Log.d(status, "HitungMulai() event");

        // new CountDownTimer(720000, 1000)

        new CountDownTimer(6000, 1000){


            public void onTick(long millisUntilFinished)
            {
                WaktuMulai.setText(""+millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                WaktuMulai.setVisibility(View.INVISIBLE);
                Buttoncheck++;
                Notify();
            }
        }.start();



    }
    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {    Log.d(status, "onKeyDown() event");



        //onDestroy();
            if (checkTimer != 0)
            {
                timer.cancel();

            }
            if (lm != null)
            {
                // Turning off
                NotifikasiBatalKanProsesLari();

            }
            if(lm==null)
            {
                Intent i = new Intent(peringatan_dini_maps.this, MainActivity.class);
                mNotificationManager.cancel(NOTIFIKASI);
                startActivity(i);
                peringatan_dini_maps.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }


        return  false;
    }
    private void  NotifikasiBatalKanProsesLari()
    {    Log.d(status,"NotifikasiBatalKanProsesLari()");
        // final  ArrayList  mSelectedItems = new ArrayList();  // Where we track the selected ite

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Apakah anda yakin membatalkan proses?");


        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mNotificationManager.cancel(NOTIFIKASI);
                        removeGpsListener();
                        Intent i = new Intent(peringatan_dini_maps.this, MainActivity.class);
                        startActivity(i);
                        peringatan_dini_maps.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                    }
                });
        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

public void removeGpsListener()
{
    Log.d(status,"removeGpsListener()");
    lm.removeGpsStatusListener(this);
    mMap.setMyLocationEnabled(false);

}
protected void onStop() {
    super.onStop();
    Log.d(status, "The onStop() event");

} public void Notify()
    {

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("MONSILVA")
                .setContentText("Menghitung Jarak lari...")
                .setOngoing(true);



        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFIKASI, mBuilder.build());

    }

    @Override
    protected void onRestart()
    {   Log.d(status, "onRestart() event");
        super.onRestart();

    }
}
