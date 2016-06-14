





package com.agung.ilham.monsilva;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class peringatan_dini_maps_service extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Intent intent;
    ArrayList<LatLng> points = new ArrayList<LatLng>();
    Polyline line; //added
    LatLng  latLng;
    TextView WaktuMulaiLari,WaktuMulai,Jarak,StatusGps, Mulai;
    boolean gps_enabled;
    String status = "ANDROID MAIN";
    LocationManager lm;
    CountDownTimer timer;
    public static int CheckButtonPress = 0;
    int Usia,HasilKondisiFisik;
    String JenisKelamin;
    boolean FixCheck = false;
    String HasilJarak;
    String PembulatanDistance;
    boolean Prosess = false,checkNotifikasiCekKoneksi = false;
    Button buttonKembali,buttonSelanjutnya;
    int buttonWidth ,width,height;
    double heightButtonfinal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(status, "onCreate() event");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini_maps_service);


        Intent page = getIntent();
        JenisKelamin = page.getStringExtra("JenisKelamin");
        Usia = Integer.parseInt(page.getStringExtra("Usia"));

        setUpMapIfNeeded();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(false);

        buttonKembali = (Button) findViewById(R.id.buttonKembali);
        buttonSelanjutnya = (Button) findViewById(R.id.buttonProses);
        buttonSelanjutnya.setEnabled(false);
        StatusGps = (TextView) findViewById(R.id.StatusGps);
        Jarak = (TextView) findViewById(R.id.Jarak);
        WaktuMulaiLari = (TextView) findViewById(R.id.Time);
        Mulai = (TextView) findViewById(R.id.Mulai);
        WaktuMulai =  (TextView) findViewById(R.id.WaktuMulai);
        Mulai.setVisibility(View.INVISIBLE);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        buttonWidth = width/2;
        int buttonHeight = width/2;
        int heightScrollView = height/10;
        heightButtonfinal = heightScrollView*1;

        buttonKembali.setWidth(buttonWidth);
        buttonSelanjutnya.setWidth(buttonWidth);
        buttonKembali.setHeight((int)heightButtonfinal);
        buttonSelanjutnya.setHeight((int)heightButtonfinal);
        buttonSelanjutnya.setEnabled(false);


        WaktuMulaiLari.setWidth(buttonHeight);
        WaktuMulaiLari.setHeight((int) heightButtonfinal);
        Jarak.setWidth(buttonHeight);
        Jarak.setHeight((int) heightButtonfinal);

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
                Prosess = true;
            }
        });

        try
        {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!gps_enabled)
            {

                StatusGps.setText("GPS tidak diaktifkan. ketuk di sini untuk menuju 'Setelan lokasi Google'. Tekan ");

            }

        }
        catch(Exception ex)
        {

        }

        checkKoneksi();

        intent = new Intent(peringatan_dini_maps_service.this, peringatan_dini_maps_service_service.class);
        startService(intent);
        registerReceiver(receiverLatLing, filterLatLing);
        registerReceiver(receiver, filter);

        buttonKembali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int keyCode = 0;
                KeyEvent event = null;
                onKeyDown (keyCode, event);
            }
        });


        buttonSelanjutnya.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(peringatan_dini_maps_service.this, peringatan_dini_peralatan_tim.class);
                startActivity(i);
            }
        });


        Toast.makeText(getBaseContext(),"T-T",Toast.LENGTH_LONG).show();
    }

    IntentFilter filterLatLing = new IntentFilter("com.agung.ilham.monsilva");
    BroadcastReceiver receiverLatLing = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(status, "receiver GPSstatus() event");
            try
            {
                updateUI(intent);

            }
            catch (Exception e)
            {


                //Toast.makeText(getApplicationContext(), "Error GPS" + e.toString(), Toast.LENGTH_SHORT).show



            }

        }
    };

    IntentFilter filter = new IntentFilter("com.agung.ilham.monsilva");
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(status, "receiver GPSstatus() event");
            try
            {
                //Boolean GpsStatusDevice = getIntent().getExtras().getBoolean("gpsEnabled");
                String GpsStatusDevice =  intent.getExtras().getString("gpsEnabled");

                    if(GpsStatusDevice != null)
                    {
                        // Toast.makeText(getBaseContext(),GpsStatusDevice,Toast.LENGTH_LONG).show();
                        if(GpsStatusDevice.equalsIgnoreCase("Start"))
                        {
                            StatusGps.setOnClickListener(null);
                            StatusGps.setText("Menunggu sinyal GPS");
                        }
                        if(GpsStatusDevice.equalsIgnoreCase("Fix"))
                        {

                            if (FixCheck == false)
                            {
                                StatusGps.setText("Sinyal GPS Siap");
                                Mulai.setVisibility(View.VISIBLE);
                                FixCheck = true;

                            }
                        }
                        if(GpsStatusDevice.equalsIgnoreCase("Mulai Merekam"))
                        {
                            StatusGps.setText("Menghitung Jarak");

                        }
                        if(GpsStatusDevice.equalsIgnoreCase("Stop"))
                        {
                            StatusGps.setText("GPS tidak diaktifkan. ketuk di sini untuk menuju 'Setelan lokasi'. Tekan tombol kembali untuk mundur");

                            StatusGps.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            });

                        }
                    }

                ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected())
                {
                    if(GpsStatusDevice.equalsIgnoreCase("Stop") && Prosess == true)
                    {
                        NotifikasiGpsDisabled();
                    }
                    if(GpsStatusDevice.equalsIgnoreCase("Stop") && Prosess == false)
                    {
                        StatusGps.setText("GPS tidak diaktifkan. ketuk di sini untuk menuju 'Setelan lokasi'. Tekan tombol kembali untuk mundur");

                        StatusGps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        });

                    }
                }
                else
                {
                   if(Prosess == true)
                   {

                       NotifikasiInternetDisabled();

                   }

                }

            }
            catch (Exception e)
            {

                //Toast.makeText(getApplicationContext(), "Error GPS" + e.toString(), Toast.LENGTH_SHORT).show

            }

        }


    };
    private void updateUI(Intent intent)
    {
        Log.d(status, "updateUI() event");
        double Lat = 0;
        double Long = 0;
        try
        {

            Lat = Double.valueOf(intent.getStringExtra("Lat"));
            Long = Double.parseDouble(intent.getStringExtra("Long"));
            PembulatanDistance = intent.getStringExtra("PembulatanDistance");

            if (Lat != 0 && Long != 0)
            {
                latLng = new LatLng(Lat, Long);
                Toast.makeText(getBaseContext(),String.valueOf(latLng),Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Aku"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));

                /*points.add(latLng);
                redrawLine();
                double distance = SphericalUtil.computeLength(points);
                PembulatanDistance = String.format("%.2f", distance);*/
                Jarak.setText(PembulatanDistance+"  Meter");

            }


        }
        catch (Exception e)
        {

            // Toast.makeText(getApplicationContext(), "Error LatLing" + e.toString(), Toast.LENGTH_SHORT).show


        }
    }

    private void redrawLine()
    {
        Log.d(status, "redrawLine() event");
        // googleMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        // addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    public void HitungMulai()
    {
        Log.d(status, "HitungMulai() event");

        new CountDownTimer(6000, 1000){


            public void onTick(long millisUntilFinished)
            {
                WaktuMulai.setText(""+millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                WaktuMulai.setVisibility(View.INVISIBLE);
                CheckButtonPress = CheckButtonPress+1;
                // WaktuMulai.setText( "xx"+Integer.toString(CheckButtonPress));
                String checkButtonPress = Integer.toString(CheckButtonPress);
                Intent intent = new Intent("com.agung.ilham.monsilva");
                intent.putExtra("checkButtonPress",checkButtonPress);
                sendBroadcast(intent);

                HitungMundur();
            }
        }.start();



    }

    public void HitungMundur()
    {
        Log.d(status, "HitungMundur() event");
        // timer = new CountDownTimer(720000, 1000)
        timer = new CountDownTimer(50000, 1000)
        {


            public void onTick(long millisUntilFinished) {
                HasilJarak = "" + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                                        (millisUntilFinished)));
                WaktuMulaiLari.setText(HasilJarak);

            }

            public void onFinish() {
                Log.d(status, "onFinish() event");
                Intent intent = new Intent(peringatan_dini_maps_service.this,
                        peringatan_dini_maps_service_service.class);
                stopService(intent);
                WaktuMulaiLari.setText("Selesai");
                CheckButtonPress = 0;

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(5000);
                Prosess = false;
                        peringatan_dini_info_pribadi.JarakTempuhLari = PembulatanDistance;

                PembulatanDistance = PembulatanDistance.replace(",","");
                    Double HasilKondisiFisikDouble = ((Integer.parseInt(PembulatanDistance)-504.9)/44.73);
                    long  HasilKondisiFisikDibulatkan  =  Math.round(HasilKondisiFisikDouble);
                    HasilKondisiFisik =(int)HasilKondisiFisikDibulatkan;
                    cek();
                    //NotifikasiAkhir();
                    buttonSelanjutnya.setEnabled(true);
            }
        }.start();

    }
    @Override
    protected void onResume()
    {
        Log.d(status, "onResume() event");
        super.onResume();

    }

    private void setUpMapIfNeeded()
    {
        Log.d(status, "setUpMapIfNeeded() event");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null)
        {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
            {
                setUpMap();
            }
        }
    }
    private void setUpMap() {
        Log.d(status, "setUpMap() event");
        LatLng INDONESIA = new LatLng(6.1750,106.8283);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(INDONESIA));
    }
    @Override
    public void onMapReady(GoogleMap map)
    {
        Log.d(status, "onMapReady() event");
        LatLng INDONESIA = new LatLng(6.1750,106.8283);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(INDONESIA));
    }


    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {   Log.d(status, "onKeyDown() event");
        CheckButtonPress = 0;


        if(Prosess == true)
        {

            NotifikasiBatalKanProsesLari();

        }
        else
        {

            Intent intent = new Intent(peringatan_dini_maps_service.this,
                    peringatan_dini_maps_service_service.class);
            stopService(intent);
            points.clear();
            Intent i = new Intent(peringatan_dini_maps_service.this, peringatan_dini_info_pribadi.class);
            startActivity(i);
            peringatan_dini_maps_service.this.overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
        }

        return true;
    }
    private void  NotifikasiBatalKanProsesLari()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_peringatan_dini_maps_service_batalkan_proses_lari, null);
        Button Tidak  = (Button)view.findViewById(R.id.Tidak);
        Button Ya  = (Button)view.findViewById(R.id.Ya);


        alertDialogBuilder.setView(view);
        //alertDialogBuilder.setCancelable(false);
        Tidak.setWidth(buttonWidth-20);
        Ya.setWidth(buttonWidth-20);
        //Ya.setEnabled(false);


        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(width - 40,height/4);
        alert.show();

        Ya.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                timer.cancel();
                Intent intent = new Intent(peringatan_dini_maps_service.this,
                        peringatan_dini_maps_service_service.class);
                stopService(intent);
                points.clear();
                Intent i = new Intent(peringatan_dini_maps_service.this, peringatan_dini_info_pribadi.class);
                startActivity(i);
                peringatan_dini_maps_service.this.overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);

            }
        });


        Tidak.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {


            }
        });

    }
    private  void  NotifikasiInternetDisabled()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_peringatan_dini_service_internet_disabled, null);
        Button Ya  = (Button)view.findViewById(R.id.Ya);


        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        //Ya.setEnabled(false);


        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(width - 40,height/4);
        alert.show();

        Ya.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                timer.cancel();
                Intent intentService = new Intent(peringatan_dini_maps_service.this,
                        peringatan_dini_maps_service_service.class);
                stopService(intentService);
                points.clear();

                Intent thisIntent = getIntent();
                startActivity(thisIntent);
                finish();
            }
        });

    }


    private  void  NotifikasiGpsDisabled()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_peringatan_dini_service_gps_disabled, null);
        Button Ya  = (Button)view.findViewById(R.id.Ya);


        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        //Ya.setEnabled(false);


        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(width - 40,height/4);
        alert.show();

        Ya.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                timer.cancel();
                Intent intentService = new Intent(peringatan_dini_maps_service.this,
                        peringatan_dini_maps_service_service.class);
                stopService(intentService);
                points.clear();

                Intent thisIntent = getIntent();
                startActivity(thisIntent);
                finish();
            }
        });



    }


    private  void NotifikasiCekKoneksi()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_peringatan_dini_maps_service_notifikasi_cek_koneksi, null);
        Button Tidak  = (Button)view.findViewById(R.id.Tidak);
        Button Ya  = (Button)view.findViewById(R.id.Ya);


        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        Tidak.setWidth(buttonWidth-20);
        Ya.setWidth(buttonWidth-20);
        //Ya.setEnabled(false);



        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(width - 40,height/4);
        alert.show();

        checkNotifikasiCekKoneksi = true;

        Ya.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {


                // check current state first
                boolean state = isMobileDataEnable();
                // toggle the state
                if(state)toggleMobileDataConnection(false);
                else toggleMobileDataConnection(true);
                Toast.makeText(getApplicationContext(),"Koneksi Mobile Di Aktifkan", Toast.LENGTH_SHORT).show();

                if(!gps_enabled)
                {

                    StatusGps.setText("GPS tidak diaktifkan. ketuk di sini untuk menuju 'Setelan lokasi Google'. Tekan ");
                }
                alert.dismiss();
                checkNotifikasiCekKoneksi = false;

            }
        });


        Tidak.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                int keyCode = 0;
                KeyEvent event = null;
                onKeyDown (keyCode, event);

            }
        });

    }

    public  void checkKoneksi()
    {

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            if(!gps_enabled)
            {

                StatusGps.setText("GPS tidak diaktifkan. ketuk di sini untuk menuju 'Setelan lokasi Google'. Tekan ");

            }
        }
        else
        {
            if (checkNotifikasiCekKoneksi == false)
            {
                NotifikasiCekKoneksi();

            }

        }

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

    public void cek()
    {

        if(JenisKelamin.equalsIgnoreCase("Pria"))
        {
            Pria();
        }
        if(JenisKelamin.equalsIgnoreCase("Wanita"))
        {

            Wanita();

        }
    }


    public void Pria()
    {

        if(Usia >=20 && Usia <=24)
        {
            if(HasilKondisiFisik <32)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=32 && HasilKondisiFisik <=37)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";

            }
            if(HasilKondisiFisik >=38 && HasilKondisiFisik <=43)
            {


            }
            if(HasilKondisiFisik >=44 && HasilKondisiFisik <=50)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
                //Toast.makeText(peringatan_dini.this,"Haloooo", Toast.LENGTH_SHORT).show();
                //KondisiFisik.setText("TES");

            }
            if(HasilKondisiFisik >=51 && HasilKondisiFisik <=56)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";

            }
            if(HasilKondisiFisik >=57 && HasilKondisiFisik <=62)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";

            }
            if(HasilKondisiFisik >62)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=25 && Usia <=29 )
        {

            if(HasilKondisiFisik <31)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=31 && HasilKondisiFisik <=35)
            {

                peringatan_dini_info_pribadi. StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=36 && HasilKondisiFisik <=42)
            {


            }
            if(HasilKondisiFisik >=43 && HasilKondisiFisik <=48)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=49 && HasilKondisiFisik <=53)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";

            }
            if(HasilKondisiFisik >=54 && HasilKondisiFisik <=59)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >59)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=30 && Usia <=34 )
        {
            if(HasilKondisiFisik <29)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=29 && HasilKondisiFisik <=34)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";

            }
            if(HasilKondisiFisik >=35 && HasilKondisiFisik <=40)
            {


            }
            if(HasilKondisiFisik >=41 && HasilKondisiFisik <=45)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
            }
            if(HasilKondisiFisik >=46 && HasilKondisiFisik <=51)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";

            }
            if(HasilKondisiFisik >=52 && HasilKondisiFisik <=56)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >56)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }


        }
        if(Usia >=35 && Usia <=39 )
        {
            if(HasilKondisiFisik <28)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=28 && HasilKondisiFisik <=32)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=33 && HasilKondisiFisik <=38)
            {


            }
            if(HasilKondisiFisik >=39 && HasilKondisiFisik <=43)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
            }
            if(HasilKondisiFisik >=44 && HasilKondisiFisik <=48)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=49 && HasilKondisiFisik <=54)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";

            }
            if(HasilKondisiFisik >54)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=40 && Usia <=44 )
        {
            if(HasilKondisiFisik <26)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";

            }
            if(HasilKondisiFisik >=26 && HasilKondisiFisik <=31)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";

            }
            if(HasilKondisiFisik >=32 && HasilKondisiFisik <=35)
            {


            }
            if(HasilKondisiFisik >=36 && HasilKondisiFisik <=41)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
            }
            if(HasilKondisiFisik >=42 && HasilKondisiFisik <=46)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=47 && HasilKondisiFisik <=51)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >51)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";

            }

        }
        if(Usia >=45 && Usia <=49 )
        {

            if(HasilKondisiFisik <25)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";

            }
            if(HasilKondisiFisik >=25 && HasilKondisiFisik <=29)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=30 && HasilKondisiFisik <=34)
            {


            }
            if(HasilKondisiFisik >=35 && HasilKondisiFisik <=39)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=40 && HasilKondisiFisik <=43)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=44 && HasilKondisiFisik <=48)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >48)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }
        }
        if(Usia >=50 && Usia <=54 )
        {
            if(HasilKondisiFisik <24)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";

            }
            if(HasilKondisiFisik >=24 && HasilKondisiFisik <=27)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=28 && HasilKondisiFisik <=32)
            {


            }
            if(HasilKondisiFisik >=33 && HasilKondisiFisik <=36)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=37 && HasilKondisiFisik <=41)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=42 && HasilKondisiFisik <=46)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >46)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=55 && Usia <=59 )
        {
            if(HasilKondisiFisik <22)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=22 && HasilKondisiFisik <=26)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=27 && HasilKondisiFisik <=30)
            {


            }
            if(HasilKondisiFisik >=31 && HasilKondisiFisik <=34)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=35 && HasilKondisiFisik <=39)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";

            }
            if(HasilKondisiFisik >=40 && HasilKondisiFisik <=43)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";

            }
            if(HasilKondisiFisik >43)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=60)
        {
            if(HasilKondisiFisik <21)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";

            }
            if(HasilKondisiFisik >=21 && HasilKondisiFisik <=24)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";

            }
            if(HasilKondisiFisik >=25 && HasilKondisiFisik <=28)
            {


            }
            if(HasilKondisiFisik >=29 && HasilKondisiFisik <=32)
            {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=33 && HasilKondisiFisik <=36)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=37 && HasilKondisiFisik <=40)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >40)
            {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }







    }
    public void Wanita() {
        if (Usia >= 20 && Usia <= 24) {

            if (HasilKondisiFisik < 27) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 27 && HasilKondisiFisik <= 31) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 32 && HasilKondisiFisik <= 36) {


            }
            if (HasilKondisiFisik >= 37 && HasilKondisiFisik <= 41) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
            }
            if (HasilKondisiFisik >= 42 && HasilKondisiFisik <= 46) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 47 && HasilKondisiFisik <= 51) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 51) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }
        if (Usia >= 25 && Usia <= 29) {
            if (HasilKondisiFisik < 26) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 26 && HasilKondisiFisik <= 30) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 31 && HasilKondisiFisik <= 35) {


            }
            if (HasilKondisiFisik >= 36 && HasilKondisiFisik <= 40) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
            }
            if (HasilKondisiFisik >= 41 && HasilKondisiFisik <= 44) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 45 && HasilKondisiFisik <= 49) {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";

            }
            if (HasilKondisiFisik > 49) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }
        if (Usia >= 30 && Usia <= 34) {

            if (HasilKondisiFisik < 25) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 25 && HasilKondisiFisik <= 29) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 30 && HasilKondisiFisik <= 33) {


            }
            if (HasilKondisiFisik >= 34 && HasilKondisiFisik <= 37) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
            }
            if (HasilKondisiFisik >= 38 && HasilKondisiFisik <= 42) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 43 && HasilKondisiFisik <= 46) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 46) {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";

            }


        }
        if (Usia >= 35 && Usia <= 39) {

            if (HasilKondisiFisik < 24) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 24 && HasilKondisiFisik <= 27) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 28 && HasilKondisiFisik <= 31) {


            }
            if (HasilKondisiFisik >= 32 && HasilKondisiFisik <= 35) {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";

            }
            if (HasilKondisiFisik >= 36 && HasilKondisiFisik <= 40) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 41 && HasilKondisiFisik <= 44) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 44) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }

        }
        if (Usia >= 40 && Usia <= 44) {
            if (HasilKondisiFisik < 22) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 22 && HasilKondisiFisik <= 25) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 26 && HasilKondisiFisik <= 29) {


            }
            if (HasilKondisiFisik >= 30 && HasilKondisiFisik <= 33) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";

            }
            if (HasilKondisiFisik >= 34 && HasilKondisiFisik <= 37) {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";

            }
            if (HasilKondisiFisik >= 38 && HasilKondisiFisik <= 41) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 41) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }


        }
        if (Usia >= 45 && Usia <= 49) {
            if (HasilKondisiFisik < 21) {
                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";

            }
            if (HasilKondisiFisik >= 21 && HasilKondisiFisik <= 23) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 24 && HasilKondisiFisik <= 27) {


            }
            if (HasilKondisiFisik >= 28 && HasilKondisiFisik <= 31) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
            }
            if (HasilKondisiFisik >= 32 && HasilKondisiFisik <= 35) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 36 && HasilKondisiFisik <= 38) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 38) {

                peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
            }


            if (Usia >= 50 && Usia <= 54) {
                if (HasilKondisiFisik < 19) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
                }
                if (HasilKondisiFisik >= 19 && HasilKondisiFisik <= 22) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
                }
                if (HasilKondisiFisik >= 23 && HasilKondisiFisik <= 25) {


                }
                if (HasilKondisiFisik >= 26 && HasilKondisiFisik <= 29) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
                }
                if (HasilKondisiFisik >= 30 && HasilKondisiFisik <= 32) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
                }
                if (HasilKondisiFisik >= 33 && HasilKondisiFisik <= 36) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
                }
                if (HasilKondisiFisik > 36) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";
                }


            }
            if (Usia >= 55 && Usia <= 59) {
                if (HasilKondisiFisik < 18) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";
                }
                if (HasilKondisiFisik >= 18 && HasilKondisiFisik <= 20) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
                }
                if (HasilKondisiFisik >= 21 && HasilKondisiFisik <= 23) {


                }
                if (HasilKondisiFisik >= 24 && HasilKondisiFisik <= 27) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";
                }
                if (HasilKondisiFisik >= 28 && HasilKondisiFisik <= 30) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
                }
                if (HasilKondisiFisik >= 31 && HasilKondisiFisik <= 33) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
                }
                if (HasilKondisiFisik > 33) {
                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";

                }


            }
            if (Usia > 60) {

                if (HasilKondisiFisik < 16) {
                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Buruk";

                }
                if (HasilKondisiFisik >= 16 && HasilKondisiFisik <= 18) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Buruk";
                }
                if (HasilKondisiFisik >= 19 && HasilKondisiFisik <= 21) {


                }
                if (HasilKondisiFisik >= 22 && HasilKondisiFisik <= 24) {
                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Rata - Rata";

                }
                if (HasilKondisiFisik >= 25 && HasilKondisiFisik <= 27) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Baik";
                }
                if (HasilKondisiFisik >= 28 && HasilKondisiFisik <= 30) {

                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Sangat Baik";
                }
                if (HasilKondisiFisik > 30) {
                    peringatan_dini_info_pribadi.StatusKondisiFisik = "Luar Biasa";

                }


            }
        }
    }


}








