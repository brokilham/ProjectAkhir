package com.agung.ilham.monsilva;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.SystemClock;

import java.util.Timer;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class peringatan_dini extends AppCompatActivity implements  NumberPicker.OnValueChangeListener,LocationListener {
    private LocationManager locationManager;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int JenisKelamin =-1,checkKelamin=0,checkUsia,checkJenisInputan,Usia,HasilKondisiFisik,Jarak,checkInputanJarak,checkJenisInputanJarak;
    static int selectedPositionJenisInputan = -1;
    static String StatusKondisiFisik;
  //  Location locationAsal=new Location("locationA");
    int check = 0;
    private double old_latitude = 0.0;
    private double old_longitude = 0.0;
    private double new_latitude = 0.0;
    private double new_longitude = 0.0;
    float[] result = null;
    public static float final_distance = 0.0f;
    private float distance = 0.0f;
    boolean gps_enabled = false;
    boolean network_enabled = false;
  //  private LocationListener locListener = new MyLocationListener();
    float accuracy;
    ProgressDialog pDialog;

    int widthScreen, heightScreen,buttonWidth,heightAlerDialog;
    CheckBox checkBoxPria,checkBoxWanita;
    Timer timer;
    int mins;
    Button SelanjutnyaButtonJenisKelamin,SelanjutnyaButtonJenisInputanJarak;
    String AngkaJarak;
    int  heightButtonfinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini);
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        widthScreen = displaymetrics.widthPixels;
        heightScreen = displaymetrics.heightPixels;
        buttonWidth = widthScreen/2;
        heightAlerDialog = heightScreen / 3;
        int heightScrollView = heightScreen/10;
        heightButtonfinal = heightScrollView*1;

        pilihJenisKelamin();
        timerValue = (TextView) findViewById(R.id.timerValue);





       locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
       result = new float[3];





       /*locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps_enabled && !network_enabled)
        {

            Toast.makeText(getBaseContext(),"nyalakan internet dan gps", Toast.LENGTH_SHORT).show();
        }
        if (gps_enabled)
        {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 1, this);
        }
        if (network_enabled)
        {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 1, this);
        }*/



    }


    private Runnable updateTimerThread = new Runnable() {

        public void run()
        {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
             mins = secs / 60;
            secs = secs % 60;
            //int milliseconds = (int) (updatedTime % 1000);
                               /* timerValue.setText("" + mins + ":"
                                        + String.format("%02d", secs) + ":"
                                        + String.format("%03d", milliseconds));*/

            timerValue.setText("" + mins + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
            if(mins == 12)
            {

                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(5000);
                // Vibrate for 500 milliseconds

            }
        }

    };

    private void pilihJenisKelamin(){


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_peringatan_dini_pilih_jenis_kelamin, null);

        alertDialogBuilder.setView(view).setCancelable(false);

        Button Kembali  = (Button)view.findViewById(R.id.Kembali);
        SelanjutnyaButtonJenisKelamin  = (Button)view.findViewById(R.id.Selanjutnya);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        toolbar.setTitle("Pilih Jenis Kelamin");
        RadioButton radioPria,radioWanita;

        radioPria = (RadioButton) view.findViewById(R.id.radioPria);
        radioWanita = (RadioButton) view.findViewById(R.id.radioWanita);

        radioPria.setHeight(heightButtonfinal);
        radioWanita.setHeight(heightButtonfinal);

        if (checkKelamin != 0)
        {
            RadioButton RadioPria = (RadioButton) view.findViewById(R.id.radioPria);
            RadioButton RadioWanita = (RadioButton) view.findViewById(R.id.radioWanita);

            if(JenisKelamin == 0)
            {
                RadioPria.setChecked(true);
                JenisKelamin = 0;
                SelanjutnyaButtonJenisKelamin.setEnabled(true);

            }
            else if(JenisKelamin == 1)
            {

                RadioWanita.setChecked(true);
                JenisKelamin = 1;
                SelanjutnyaButtonJenisKelamin.setEnabled(true);
            }


        }
        if (JenisKelamin == -1)
        {

            SelanjutnyaButtonJenisKelamin.setEnabled(false);
        }



        Kembali.setWidth(buttonWidth-20);
        SelanjutnyaButtonJenisKelamin.setWidth(buttonWidth-20);


        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(widthScreen - 40, heightScreen / 2);
        alert.show();





        SelanjutnyaButtonJenisKelamin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(peringatan_dini.this,Integer.toString(JenisKelamin), Toast.LENGTH_SHORT).show();
                pilihUmur();

            }
        });


        Kembali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();

            }
        });





      // final  ArrayList  mSelectedItems = new ArrayList();  // Where we track the selected ite

     /*   final String[] options = { "Pria", "Wanita"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

          if(checkKelamin==0)
          {
              alertDialogBuilder.setTitle("Pilih Jenis Kelamin")
                      .setCancelable(false)
                      .setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              JenisKelamin = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                              Toast.makeText(getBaseContext(), Integer.toString(JenisKelamin), Toast.LENGTH_SHORT).show();

                              if (!((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled())
                                  ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                              checkKelamin++;

                          }
                      });

          }
        else if(checkKelamin !=0)
          {

              alertDialogBuilder.setTitle("Pilih Jenis Kelamin")
                      .setCancelable(false)
                      .setSingleChoiceItems(options, JenisKelamin, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              JenisKelamin = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                                          Toast.makeText(getBaseContext(), Integer.toString(JenisKelamin), Toast.LENGTH_SHORT).show();

                              if (!((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled())
                                  ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                          }
                      });

          }


        alertDialogBuilder.setPositiveButton("Selanjutnya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pilihUmur();
                        Toast.makeText(getBaseContext(), Integer.toString(JenisKelamin), Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialogBuilder.setNegativeButton("Batalkan",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        onBackPressed();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        if(checkKelamin == 0)
        {
            alert.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }*/

    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioPria:
                if (checked)
                    JenisKelamin = 0;
                    SelanjutnyaButtonJenisKelamin.setEnabled(true);
                    checkKelamin++;
                    break;
            case R.id.radioWanita:
                if (checked)
                    JenisKelamin = 1;
                    SelanjutnyaButtonJenisKelamin.setEnabled(true);
                    checkKelamin++;
                    break;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void pilihUmur()
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.activity_peringatan_dini_dialogusia, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialogBuilder.setView(view).setCancelable(false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        toolbar.setTitle("Pilih Umur Anda");

        Button Kembali  = (Button)view.findViewById(R.id.Kembali);
        final Button Selanjutnya  = (Button)view.findViewById(R.id.Selanjutnya);


        final NumberPicker np = (NumberPicker)view.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 100
        np.setMinValue(22);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        if(checkUsia != 0 )
        {
            if(Usia != 0)
            {
                np.setValue(Usia);

            }

        }



        Selanjutnya.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getBaseContext(),String.valueOf(np.getValue()), Toast.LENGTH_SHORT).show();
                Usia = np.getValue();
                checkUsia++;
                pilihJenisInputan();


            }
        });


        Kembali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                pilihJenisKelamin();
                checkUsia++;
            }
        });

        // Add action buttons


        Kembali.setWidth(buttonWidth-20);
        Selanjutnya.setWidth(buttonWidth-20);


        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(widthScreen - 40,heightScreen/2);
        alert.show();


    }

    private void pilihJenisInputan(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_peringatan_dini_tipe_inputan_jarak, null);
        Button Kembali  = (Button)view.findViewById(R.id.Kembali);
        SelanjutnyaButtonJenisInputanJarak  = (Button)view.findViewById(R.id.Selanjutnya);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);

        alertDialogBuilder.setView(view).setCancelable(false);
        toolbar.setTitle("Pilih Cara yang anda pilih");

        RadioButton radioLari,radioHasilLari;

        radioLari = (RadioButton) view.findViewById(R.id.radioLari);
        radioHasilLari = (RadioButton) view.findViewById(R.id.radioHasilLari);

        radioLari.setHeight(heightButtonfinal);
        radioHasilLari.setHeight(heightButtonfinal);

        Kembali.setWidth(buttonWidth-20);
        SelanjutnyaButtonJenisInputanJarak.setWidth(buttonWidth-20);


        if (checkJenisInputanJarak != 0)
        {
            RadioButton RadioLari = (RadioButton) view.findViewById(R.id.radioLari);
            RadioButton RadioHasilLari = (RadioButton) view.findViewById(R.id.radioHasilLari);

            if(selectedPositionJenisInputan == 0)
            {
                RadioLari.setChecked(true);
                selectedPositionJenisInputan = 0;
                SelanjutnyaButtonJenisInputanJarak.setEnabled(true);

            }
            else if(selectedPositionJenisInputan == 1)
            {

                RadioHasilLari.setChecked(true);
                selectedPositionJenisInputan = 1;
                SelanjutnyaButtonJenisInputanJarak.setEnabled(true);
            }


        }
        if (selectedPositionJenisInputan == -1)
        {

            SelanjutnyaButtonJenisInputanJarak.setEnabled(false);
        }



        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(widthScreen - 40,heightScreen/2);
        alert.show();




        SelanjutnyaButtonJenisInputanJarak.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                if(selectedPositionJenisInputan == 0)
                {


                }
                else if(selectedPositionJenisInputan == 1)
                {
                    InputkanAngkaJarak();

                }


            }
        });


        Kembali.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                pilihUmur();

            }
        });




    }

    public void onRadioButtonClickedTipeInputanJarak(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId())
        {
            case R.id.radioLari:
                if (checked)
                    selectedPositionJenisInputan = 0;
                SelanjutnyaButtonJenisInputanJarak.setEnabled(true);
                checkJenisInputanJarak++;
                break;
            case R.id.radioHasilLari:
                if (checked)
                    selectedPositionJenisInputan = 1;
                SelanjutnyaButtonJenisInputanJarak.setEnabled(true);
                checkJenisInputanJarak++;
                break;
        }
    }

   public void  InputkanAngkaJarak()
   {

       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
       LayoutInflater inflater = this.getLayoutInflater();
       View view = inflater.inflate(R.layout.activity_peringatan_dini_input_angka_jarak, null);

       alertDialogBuilder.setView(view).setCancelable(false);
       final EditText InputAngkaJarak = (EditText)view.findViewById(R.id.InputAngkaJarak);
       Button Kembali  = (Button)view.findViewById(R.id.Kembali);
       final Button Selanjutnya  = (Button)view.findViewById(R.id.Selanjutnya);
       Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
       toolbar.setTitle("Jarak (Meter)");

       InputAngkaJarak.setHeight(heightButtonfinal);

       Kembali.setWidth(buttonWidth-20);
       Selanjutnya.setWidth(buttonWidth-20);
       if(checkInputanJarak !=0)
       {
           if(AngkaJarak != null)
           {
               InputAngkaJarak.setText(AngkaJarak);
               Selanjutnya.setEnabled(true);
           }

       }
       if(AngkaJarak == null)
       {
           Selanjutnya.setEnabled(false);

       }



       final AlertDialog alert = alertDialogBuilder.create();
       alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
       alert.getWindow().setLayout(widthScreen - 40,heightScreen/2);
       alert.show();

       InputAngkaJarak.addTextChangedListener(new TextWatcher() {
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               Selanjutnya.setEnabled(count > 0);
               checkInputanJarak++;
           }
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
           @Override
           public void afterTextChanged(Editable s) {
           }
       });


       Selanjutnya.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {

               AngkaJarak = String.valueOf(InputAngkaJarak.getText());
               Jarak = Integer.parseInt(AngkaJarak);
               Double HasilKondisiFisikDouble = ((Jarak -504.9)/44.73);
               long  HasilKondisiFisikDibulatkan  =  Math.round(HasilKondisiFisikDouble);
               HasilKondisiFisik =(int)HasilKondisiFisikDibulatkan;
               cek();
               Intent i = new Intent (peringatan_dini.this, peringatan_dini_peralatan_tim.class);

               startActivity(i);
               //Toast.makeText(peringatan_dini.this, Integer.toString(HasilKondisiFisik), Toast.LENGTH_SHORT).show();

           }
       });


       Kembali.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {

               pilihJenisInputan();

           }
       });
   }

    public void onValueChange(NumberPicker picker, int oldVal, int newVal)
    {

        Log.i("value is",""+newVal);

    }

  public void onLocationChanged(Location location) {


     /* TextView LatitudeLama,LongtitudeLama, LatitudeBaru,LongtitudeBaru,Accuracy;

      LatitudeLama = (TextView)findViewById(R.id.AsalLatitude);
      LongtitudeLama = (TextView)findViewById(R.id.AsalLongtitude);
      TextView Distance = (TextView)findViewById(R.id.Distance);
      LatitudeBaru = (TextView)findViewById(R.id.LatitudeBaru);
      LongtitudeBaru = (TextView)findViewById(R.id.LongtitudeBaru);
      Accuracy = (TextView)findViewById(R.id.Accuracy);



      if (location != null)
      {

          // This needs to stop getting the location data and save the battery power.
                accuracy = location.getAccuracy();
          Accuracy.setText("Accuracy :"+Float.toString(accuracy));

          if (mins == 12)
          {
              locationManager.removeUpdates(this);

          }
         // if(old_latitude == 0.0 && old_longitude == 0.0 && accuracy<10)
         if(old_latitude == 0.0 && old_longitude == 0.0)
          {

              pDialog.dismiss();
              old_latitude = location.getLatitude();
              old_longitude = location.getLongitude();
              startTime = SystemClock.uptimeMillis();
              customHandler.postDelayed(updateTimerThread, 0);

          }




          if(old_latitude != 0 && old_longitude !=0 )
          {

              new_latitude = location.getLatitude();
              new_longitude = location.getLongitude();

              LatitudeLama.setText("Latitude Lama :" +Double.toString(old_latitude));
              LongtitudeLama.setText("Longtitude Lama :" +Double.toString(old_longitude));
              LatitudeBaru.setText("Latitude Baru :" + Double.toString(new_latitude));
              LongtitudeBaru.setText("Longtitude Baru :" + Double.toString(new_longitude));


              Location.distanceBetween(old_latitude, old_longitude, new_latitude, new_longitude, result);

              distance = final_distance + result[0];//distance in meters between two locations

              final_distance = distance;
              String hasilAkhir = String.format("%.2f", final_distance);
              Distance.setText("Distance :" + hasilAkhir);
              //Distance.setText("Distance :" + hasilAkhir);

              old_latitude = new_latitude;
              old_longitude = new_longitude;


          }





      }*/



       /* if (mins != 1)
        {
            if(old_latitude == 0.0 && old_longitude == 0.0)
            {
                old_latitude = location.getLatitude();
                old_longitude = location.getLongitude();


            }

            TextView LatitudeLama,LongtitudeLama;

            LatitudeLama = (TextView)findViewById(R.id.AsalLatitude);
            LongtitudeLama = (TextView)findViewById(R.id.AsalLongtitude);

            LatitudeLama.setText("Latitude Lama :" + Double.toString(old_latitude));
            LongtitudeLama.setText("Longtitude Lama :" +Double.toString(old_longitude));


            new_latitude = location.getLatitude();
            new_longitude = location.getLongitude();

            TextView LatitudeBaru,LongtitudeBaru;
            TextView Distance = (TextView)findViewById(R.id.Distance);

            LatitudeBaru = (TextView)findViewById(R.id.LatitudeBaru);
            LongtitudeBaru = (TextView)findViewById(R.id.LongtitudeBaru);

            LatitudeBaru.setText("Latitude Baru :" + Double.toString(new_latitude));
            LongtitudeBaru.setText("Longtitude Baru :" + Double.toString(new_longitude));


            Location.distanceBetween(old_latitude, old_longitude, new_latitude, new_longitude, result);

            distance = final_distance + result[0];//distance in meters between two locations

            final_distance = distance;
            String hasilAkhir = String.format("%.2f", final_distance);
            Distance.setText("Distance :" + hasilAkhir);
            //Distance.setText("Distance :" + hasilAkhir);

            old_latitude = new_latitude;
            old_longitude = new_longitude;

        }*/

    }


    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }


    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    public void cek()
    {

        if(JenisKelamin == 0)
        {
            Pria();
        }
        if(JenisKelamin == 1)
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

                StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=32 && HasilKondisiFisik <=37)
            {
                StatusKondisiFisik = "Buruk";

            }
            if(HasilKondisiFisik >=38 && HasilKondisiFisik <=43)
            {


            }
            if(HasilKondisiFisik >=44 && HasilKondisiFisik <=50)
            {
                StatusKondisiFisik = "Rata - Rata";
                //Toast.makeText(peringatan_dini.this,"Haloooo", Toast.LENGTH_SHORT).show();
                //KondisiFisik.setText("TES");

            }
            if(HasilKondisiFisik >=51 && HasilKondisiFisik <=56)
            {
                StatusKondisiFisik = "Baik";

            }
            if(HasilKondisiFisik >=57 && HasilKondisiFisik <=62)
            {
                StatusKondisiFisik = "Sangat Baik";

            }
            if(HasilKondisiFisik >62)
            {

                StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=25 && Usia <=29 )
        {

            if(HasilKondisiFisik <31)
            {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=31 && HasilKondisiFisik <=35)
            {

                StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=36 && HasilKondisiFisik <=42)
            {


            }
            if(HasilKondisiFisik >=43 && HasilKondisiFisik <=48)
            {
                StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=49 && HasilKondisiFisik <=53)
            {
                StatusKondisiFisik = "Baik";

            }
            if(HasilKondisiFisik >=54 && HasilKondisiFisik <=59)
            {

                StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >59)
            {

                StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=30 && Usia <=34 )
        {
            if(HasilKondisiFisik <29)
            {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=29 && HasilKondisiFisik <=34)
            {
                StatusKondisiFisik = "Buruk";

            }
            if(HasilKondisiFisik >=35 && HasilKondisiFisik <=40)
            {


            }
            if(HasilKondisiFisik >=41 && HasilKondisiFisik <=45)
            {

                StatusKondisiFisik = "Rata - Rata";
            }
            if(HasilKondisiFisik >=46 && HasilKondisiFisik <=51)
            {

                StatusKondisiFisik = "Baik";

            }
            if(HasilKondisiFisik >=52 && HasilKondisiFisik <=56)
            {

                StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >56)
            {

                StatusKondisiFisik = "Luar Biasa";
            }


        }
        if(Usia >=35 && Usia <=39 )
        {
            if(HasilKondisiFisik <28)
            {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=28 && HasilKondisiFisik <=32)
            {

                StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=33 && HasilKondisiFisik <=38)
            {


            }
            if(HasilKondisiFisik >=39 && HasilKondisiFisik <=43)
            {

                StatusKondisiFisik = "Rata - Rata";
            }
            if(HasilKondisiFisik >=44 && HasilKondisiFisik <=48)
            {

                StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=49 && HasilKondisiFisik <=54)
            {
                StatusKondisiFisik = "Sangat Baik";

            }
            if(HasilKondisiFisik >54)
            {

                StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=40 && Usia <=44 )
        {
            if(HasilKondisiFisik <26)
            {
                StatusKondisiFisik = "Sangat Buruk";

            }
            if(HasilKondisiFisik >=26 && HasilKondisiFisik <=31)
            {
                StatusKondisiFisik = "Buruk";

            }
            if(HasilKondisiFisik >=32 && HasilKondisiFisik <=35)
            {


            }
            if(HasilKondisiFisik >=36 && HasilKondisiFisik <=41)
            {

                StatusKondisiFisik = "Rata - Rata";
            }
            if(HasilKondisiFisik >=42 && HasilKondisiFisik <=46)
            {

                StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=47 && HasilKondisiFisik <=51)
            {

                StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >51)
            {
                StatusKondisiFisik = "Luar Biasa";

            }

        }
        if(Usia >=45 && Usia <=49 )
        {

            if(HasilKondisiFisik <25)
            {
                StatusKondisiFisik = "Sangat Buruk";

            }
            if(HasilKondisiFisik >=25 && HasilKondisiFisik <=29)
            {

                StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=30 && HasilKondisiFisik <=34)
            {


            }
            if(HasilKondisiFisik >=35 && HasilKondisiFisik <=39)
            {
                StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=40 && HasilKondisiFisik <=43)
            {

                StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=44 && HasilKondisiFisik <=48)
            {

                StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >48)
            {

                StatusKondisiFisik = "Luar Biasa";
            }
        }
        if(Usia >=50 && Usia <=54 )
        {
            if(HasilKondisiFisik <24)
            {
                StatusKondisiFisik = "Sangat Buruk";

            }
            if(HasilKondisiFisik >=24 && HasilKondisiFisik <=27)
            {

                StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=28 && HasilKondisiFisik <=32)
            {


            }
            if(HasilKondisiFisik >=33 && HasilKondisiFisik <=36)
            {
                StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=37 && HasilKondisiFisik <=41)
            {

                StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=42 && HasilKondisiFisik <=46)
            {

                StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >46)
            {

                StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=55 && Usia <=59 )
        {
            if(HasilKondisiFisik <22)
            {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if(HasilKondisiFisik >=22 && HasilKondisiFisik <=26)
            {

                StatusKondisiFisik = "Buruk";
            }
            if(HasilKondisiFisik >=27 && HasilKondisiFisik <=30)
            {


            }
            if(HasilKondisiFisik >=31 && HasilKondisiFisik <=34)
            {
                StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=35 && HasilKondisiFisik <=39)
            {
                StatusKondisiFisik = "Baik";

            }
            if(HasilKondisiFisik >=40 && HasilKondisiFisik <=43)
            {
                StatusKondisiFisik = "Sangat Baik";

            }
            if(HasilKondisiFisik >43)
            {

                StatusKondisiFisik = "Luar Biasa";
            }

        }
        if(Usia >=60)
        {
            if(HasilKondisiFisik <21)
            {
                StatusKondisiFisik = "Sangat Buruk";

            }
            if(HasilKondisiFisik >=21 && HasilKondisiFisik <=24)
            {
                StatusKondisiFisik = "Buruk";

            }
            if(HasilKondisiFisik >=25 && HasilKondisiFisik <=28)
            {


            }
            if(HasilKondisiFisik >=29 && HasilKondisiFisik <=32)
            {
                StatusKondisiFisik = "Rata - Rata";

            }
            if(HasilKondisiFisik >=33 && HasilKondisiFisik <=36)
            {

                StatusKondisiFisik = "Baik";
            }
            if(HasilKondisiFisik >=37 && HasilKondisiFisik <=40)
            {

                StatusKondisiFisik = "Sangat Baik";
            }
            if(HasilKondisiFisik >40)
            {

                StatusKondisiFisik = "Luar Biasa";
            }

        }







    }
    public void Wanita() {
        if (Usia >= 20 && Usia <= 24) {

            if (HasilKondisiFisik < 27) {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 27 && HasilKondisiFisik <= 31) {

                StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 32 && HasilKondisiFisik <= 36) {


            }
            if (HasilKondisiFisik >= 37 && HasilKondisiFisik <= 41) {

                StatusKondisiFisik = "Rata - Rata";
            }
            if (HasilKondisiFisik >= 42 && HasilKondisiFisik <= 46) {

                StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 47 && HasilKondisiFisik <= 51) {

                StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 51) {

                StatusKondisiFisik = "Luar Biasa";
            }

        }
        if (Usia >= 25 && Usia <= 29) {
            if (HasilKondisiFisik < 26) {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 26 && HasilKondisiFisik <= 30) {

                StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 31 && HasilKondisiFisik <= 35) {


            }
            if (HasilKondisiFisik >= 36 && HasilKondisiFisik <= 40) {

                StatusKondisiFisik = "Rata - Rata";
            }
            if (HasilKondisiFisik >= 41 && HasilKondisiFisik <= 44) {

                StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 45 && HasilKondisiFisik <= 49) {
                StatusKondisiFisik = "Sangat Baik";

            }
            if (HasilKondisiFisik > 49) {

                StatusKondisiFisik = "Luar Biasa";
            }

        }
        if (Usia >= 30 && Usia <= 34) {

            if (HasilKondisiFisik < 25) {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 25 && HasilKondisiFisik <= 29) {

                StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 30 && HasilKondisiFisik <= 33) {


            }
            if (HasilKondisiFisik >= 34 && HasilKondisiFisik <= 37) {

                StatusKondisiFisik = "Rata - Rata";
            }
            if (HasilKondisiFisik >= 38 && HasilKondisiFisik <= 42) {

                StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 43 && HasilKondisiFisik <= 46) {

                StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 46) {
                StatusKondisiFisik = "Luar Biasa";

            }


        }
        if (Usia >= 35 && Usia <= 39) {

            if (HasilKondisiFisik < 24) {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 24 && HasilKondisiFisik <= 27) {

                StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 28 && HasilKondisiFisik <= 31) {


            }
            if (HasilKondisiFisik >= 32 && HasilKondisiFisik <= 35) {
                StatusKondisiFisik = "Rata - Rata";

            }
            if (HasilKondisiFisik >= 36 && HasilKondisiFisik <= 40) {

                StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 41 && HasilKondisiFisik <= 44) {

                StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 44) {

                StatusKondisiFisik = "Luar Biasa";
            }

        }
        if (Usia >= 40 && Usia <= 44) {
            if (HasilKondisiFisik < 22) {

                StatusKondisiFisik = "Sangat Buruk";
            }
            if (HasilKondisiFisik >= 22 && HasilKondisiFisik <= 25) {

                StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 26 && HasilKondisiFisik <= 29) {


            }
            if (HasilKondisiFisik >= 30 && HasilKondisiFisik <= 33) {

                StatusKondisiFisik = "Rata - Rata";

            }
            if (HasilKondisiFisik >= 34 && HasilKondisiFisik <= 37) {
                StatusKondisiFisik = "Baik";

            }
            if (HasilKondisiFisik >= 38 && HasilKondisiFisik <= 41) {

                StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 41) {

                StatusKondisiFisik = "Luar Biasa";
            }


        }
        if (Usia >= 45 && Usia <= 49) {
            if (HasilKondisiFisik < 21) {
                StatusKondisiFisik = "Sangat Buruk";

            }
            if (HasilKondisiFisik >= 21 && HasilKondisiFisik <= 23) {

                StatusKondisiFisik = "Buruk";
            }
            if (HasilKondisiFisik >= 24 && HasilKondisiFisik <= 27) {


            }
            if (HasilKondisiFisik >= 28 && HasilKondisiFisik <= 31) {

                StatusKondisiFisik = "Rata - Rata";
            }
            if (HasilKondisiFisik >= 32 && HasilKondisiFisik <= 35) {

                StatusKondisiFisik = "Baik";
            }
            if (HasilKondisiFisik >= 36 && HasilKondisiFisik <= 38) {

                StatusKondisiFisik = "Sangat Baik";
            }
            if (HasilKondisiFisik > 38) {

                StatusKondisiFisik = "Luar Biasa";
            }


            if (Usia >= 50 && Usia <= 54) {
                if (HasilKondisiFisik < 19) {

                    StatusKondisiFisik = "Sangat Buruk";
                }
                if (HasilKondisiFisik >= 19 && HasilKondisiFisik <= 22) {

                    StatusKondisiFisik = "Buruk";
                }
                if (HasilKondisiFisik >= 23 && HasilKondisiFisik <= 25) {


                }
                if (HasilKondisiFisik >= 26 && HasilKondisiFisik <= 29) {

                    StatusKondisiFisik = "Rata - Rata";
                }
                if (HasilKondisiFisik >= 30 && HasilKondisiFisik <= 32) {

                    StatusKondisiFisik = "Baik";
                }
                if (HasilKondisiFisik >= 33 && HasilKondisiFisik <= 36) {

                    StatusKondisiFisik = "Sangat Baik";
                }
                if (HasilKondisiFisik > 36) {

                    StatusKondisiFisik = "Luar Biasa";
                }


            }
            if (Usia >= 55 && Usia <= 59) {
                if (HasilKondisiFisik < 18) {

                    StatusKondisiFisik = "Sangat Buruk";
                }
                if (HasilKondisiFisik >= 18 && HasilKondisiFisik <= 20) {

                    StatusKondisiFisik = "Buruk";
                }
                if (HasilKondisiFisik >= 21 && HasilKondisiFisik <= 23) {


                }
                if (HasilKondisiFisik >= 24 && HasilKondisiFisik <= 27) {

                    StatusKondisiFisik = "Rata - Rata";
                }
                if (HasilKondisiFisik >= 28 && HasilKondisiFisik <= 30) {

                    StatusKondisiFisik = "Baik";
                }
                if (HasilKondisiFisik >= 31 && HasilKondisiFisik <= 33) {

                    StatusKondisiFisik = "Sangat Baik";
                }
                if (HasilKondisiFisik > 33) {
                    StatusKondisiFisik = "Luar Biasa";

                }


            }
            if (Usia > 60) {

                if (HasilKondisiFisik < 16) {
                    StatusKondisiFisik = "Sangat Buruk";

                }
                if (HasilKondisiFisik >= 16 && HasilKondisiFisik <= 18) {

                    StatusKondisiFisik = "Buruk";
                }
                if (HasilKondisiFisik >= 19 && HasilKondisiFisik <= 21) {


                }
                if (HasilKondisiFisik >= 22 && HasilKondisiFisik <= 24) {
                    StatusKondisiFisik = "Rata - Rata";

                }
                if (HasilKondisiFisik >= 25 && HasilKondisiFisik <= 27) {

                    StatusKondisiFisik = "Baik";
                }
                if (HasilKondisiFisik >= 28 && HasilKondisiFisik <= 30) {

                    StatusKondisiFisik = "Sangat Baik";
                }
                if (HasilKondisiFisik > 30) {
                    StatusKondisiFisik = "Luar Biasa";

                }


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_peringatan_dini, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed()
    {
        Intent i = new Intent(peringatan_dini.this, MainActivity.class);
        startActivity(i);
        peringatan_dini.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Intent i = new Intent(peringatan_dini.this, MainActivity.class);
        startActivity(i);
        peringatan_dini.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

      /*  if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
                      pDialog.dismiss();
                      timeSwapBuff += timeInMilliseconds;
                      customHandler.removeCallbacks(updateTimerThread);
                      locationManager.removeUpdates(this);
                    // finish();
        }*/
        return super.onKeyDown(keyCode, event);
    }
}
