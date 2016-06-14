






package com.agung.ilham.monsilva;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by ILHAM on 12/24/2015.
 */
public class peringatan_dini_maps_service_service  extends Service  {
    private static final String TAG = "Android";



    private static final long DURATION_TO_FIX_LOST_MS = 10000;

    private static final long MINIMUM_UPDATE_TIME = 10000;
    private static final float MINIMUM_UPDATE_DISTANCE = 10;

    private LocationManager locationManager;
    private MyGpsListener gpsListener;



    private boolean gpsEnabled;
    private boolean gpsFix;
    private double latitude;
    private double longitude;
    private int satellitesTotal;
    private int satellitesUsed;
    private float accuracy;
    int NotifiyCheck = 0 ;
    boolean FixCheck =  false;
    Bundle bundle = new Bundle();
    public static ArrayList<LatLng> coordinates = new ArrayList<LatLng>();


    public static double Lat,Long;
    int GpsStatusFix = 0;
    NotificationManager mNotificationManager;
    int NOTIFIKASI = 1;
    boolean NotificationCheck;
    int CheckButtonPress;
    String valueCheckButton;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        registerReceiver(receiver, filter);


    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");

        if (locationManager != null)
        {
            // remove the delegates to stop the GPS
            locationManager.removeGpsStatusListener(gpsListener);
            locationManager.removeUpdates(gpsListener);
            coordinates.clear();
            CheckButtonPress = 0;
            locationManager = null;
        }

        if(NotificationCheck == true)
        {
            mNotificationManager.cancel(NOTIFIKASI);
            coordinates.clear();
            CheckButtonPress = 0;
            NotificationCheck =false;
        }

        super.onDestroy();

    }

    private void initializeLocationManager()
    {
        Log.e(TAG, "initializeLocationManager");

        // ask Android for the GPS service
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // make a delegate to receive callbacks
        gpsListener = new MyGpsListener();
        // ask for updates on the GPS status
        locationManager.addGpsStatusListener(gpsListener);
        // ask for updates on the GPS location
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MINIMUM_UPDATE_TIME, MINIMUM_UPDATE_DISTANCE, gpsListener);

    }

    @Override
    public IBinder onBind(Intent intent)
    {   /*valueCheckButton = intent.getExtras().getString("test");
        CheckButtonPress = Integer.parseInt(valueCheckButton);
        */
        return null;
    }




    private String getGrade() {

        if (!gpsEnabled) {
            return "Disabled";
        }
        if (!gpsFix) {
            return "Waiting for Fix";
        }
        if (accuracy <= 10) {
            return "Good";
        }
        if (accuracy <= 30) {
            return "Fair";
        }
        if (accuracy <= 100) {
            return "Bad";
        }
        return "Unusable";
    }

    protected class MyGpsListener implements GpsStatus.Listener, LocationListener {

        // the last location time is needed to determine if a fix has been lost
        private long locationTime = 0;
        private List<Float> rollingAverageData = new LinkedList<Float>();

        @Override
        public void onGpsStatusChanged(int changeType) {
            if (locationManager != null) {

                Location gpslocation;
                Intent intent = new Intent("com.agung.ilham.monsilva");
                // status changed so ask what the change was
                GpsStatus status = locationManager.getGpsStatus(null);
                switch(changeType) {
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        //  gpsEnabled = true;
                        // gpsFix = true;
                        Log.w(TAG, "GPS_EVENT_FIRST_FIX");
                        intent.putExtra("gpsEnabled","Fix");
                        sendBroadcast(intent);
                        if (FixCheck == false)
                        {

                            gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Lat     = gpslocation.getLatitude();
                            Long    = gpslocation.getLongitude();
                            sendResult();
                            GpsStatusFix++;
                            FixCheck = true;
                        }

                        break;

                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        // gpsEnabled = true;
                        // if it has been more then 10 seconds since the last update, consider the fix lost
                        //gpsFix = System.currentTimeMillis() - locationTime < DURATION_TO_FIX_LOST_MS;
                        Log.w(TAG, "GPS_EVENT_SATELLITE_STATUS");
                         // Toast.makeText(getBaseContext(),Integer.toString(GpsStatusFix)+"&"+Integer.toString(CheckButtonPress),
                           //       Toast.LENGTH_SHORT).show();
                        if (GpsStatusFix == 1 && CheckButtonPress == 1)
                        {
                            if (NotifiyCheck ==0)
                            {
                                Notify();
                                NotifiyCheck++;
                            }

                            intent.putExtra("gpsEnabled","Mulai Merekam");
                            sendBroadcast(intent);

                            gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Lat     = gpslocation.getLatitude();
                            Long    = gpslocation.getLongitude();
                            sendResult();


                        }
                        break;

                    case GpsStatus.GPS_EVENT_STARTED: // GPS turned on
                        Log.w(TAG, "GPS_EVENT_STARTED");
                        //gpsEnabled = true;
                        //gpsFix = false;
                        intent.putExtra("gpsEnabled","Start");
                        sendBroadcast(intent);
                        break;
                    case GpsStatus.GPS_EVENT_STOPPED: // GPS turned off
                        // gpsEnabled = false;
                        //gpsFix = false;
                        //GpsStatusFix = 0;
                        Log.w(TAG, "GPS_EVENT_STOPPED");
                        intent.putExtra("gpsEnabled","Stop");
                        sendBroadcast(intent);
                        break;
                    default:
                        Log.w(TAG, "unknown GpsStatus event type. "+changeType);
                        return;
                }

                // number of satellites, not useful, but cool
                int newSatTotal = 0;
                int newSatUsed = 0;
                for(GpsSatellite sat : status.getSatellites()) {
                    newSatTotal++;
                    if (sat.usedInFix()) {
                        newSatUsed++;
                    }
                }
                satellitesTotal = newSatTotal;
                satellitesUsed = newSatUsed;


            }
        }


        public void sendResult()
        {
            Log.e(TAG, "sendResult");
            LatLng latLng = new LatLng(Lat, Long);
            coordinates.add(latLng);
            double distance = SphericalUtil.computeLength(coordinates);
            String PembulatanDistance = String.format("%.2f", distance);
            Intent intent = new Intent("com.agung.ilham.monsilva");
            intent.putExtra("PembulatanDistance",PembulatanDistance);
            intent.putExtra("Lat",String.valueOf(Lat));
            intent.putExtra("Long", String.valueOf(Long));
            sendBroadcast(intent);

        }

        @Override
        public void onLocationChanged(Location location)
        {

            locationTime = location.getTime();
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (location.hasAccuracy())
            {
                // rolling average of accuracy so "Signal Quality" is not erratic
                updateRollingAverage(location.getAccuracy());
            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
			/* dont need this info */
        }

        @Override
        public void onProviderEnabled(String provider) {
			/* dont need this info */
        }

        @Override
        public void onProviderDisabled(String provider) {
			/* dont need this info */
        }

        private void updateRollingAverage(float value) {
            // does a simple rolling average
            rollingAverageData.add(value);
            if (rollingAverageData.size() > 10) {
                rollingAverageData.remove(0);
            }

            float average = 0.0f;
            for(Float number : rollingAverageData) {
                average += number;
            }
            average = average / rollingAverageData.size();

            accuracy = average;
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Notify()
    {
        NotificationCheck = true;
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("MONSILVA")
                        .setContentText("Menghitung jarak");

        mBuilder.setOngoing(true);

        // Creates an explicit intent for an Activity in your app
        //Intent resultIntent = new Intent(this,peringatan_dini_maps_service.class);
        Intent resultIntent = new Intent(this,peringatan_dini_lari.class);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.from(this);
        // Adds the back stack for the Intent (but not the Intent itself)
       // stackBuilder.addParentStack(peringatan_dini_maps_service.class);
        stackBuilder.addParentStack(peringatan_dini_lari.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFIKASI, mBuilder.build());


    }

    IntentFilter filter = new IntentFilter("com.agung.ilham.monsilva");
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try
            {
                valueCheckButton = intent.getExtras().getString("checkButtonPress");
                CheckButtonPress = Integer.parseInt(valueCheckButton);




            }
            catch (Exception e)
            {


                //Toast.makeText(getApplicationContext(), "Error GPS" + e.toString(), 


            }

        }
    };
}





