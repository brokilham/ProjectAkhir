package com.agung.ilham.monsilva;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ViewFlipper;


public class MainActivity extends AppCompatActivity {
    public static int checkSurvivalTipsList = 0,checkPeringatanDiniGunungLIst = 0, checkSurvivalTipsDetail = 0, checkEquipmentPersonalList = 0,checkEquipmentPersonalTeam = 0,checkMountainProvinceList = 0, checkInformasiGunungDetail;
    int mFlipping = 0 ; // Initially flipping is off
    Button mButton ; // Reference to button available in the layout to start and stop the flipper
    ImageView splashImageView;
    boolean splashloading = false;
    String status = "Android: ";
    int width,height;
    int buttonWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        Log.d(status, "onCreate() event");

        /*if (!isTaskRoot())
        {
            finish();
            return;
        }*/

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

      /*  splashImageView = new ImageView(this);
        splashImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        splashImageView.setImageResource(R.drawable.splash_screen);
        setContentView(splashImageView);
        splashloading = true;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                splashloading = false;
                // set your layout file in below
                setContentView(R.layout.activity_main);
            }
        }, 3000);*/
        
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        buttonWidth = width/2;
        //int buttonHeight = height/4;
        int buttonHeight = height/3;


       /* ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);
        android.view.ViewGroup.LayoutParams paramsflipper =  flipper.getLayoutParams();
        paramsflipper.height = height/2;
        flipper.setLayoutParams(paramsflipper);
        flipper.startFlipping();*/



        ImageView buttonGunung,buttonPeralatan,buttonSurvivalTips,buttonPeringatanDini;
        int buttonWidth = width/2;

        ImageView GambarLogo = (ImageView) findViewById(R.id.Logo);
        ViewGroup.LayoutParams GambarLogoparams = GambarLogo.getLayoutParams();
        GambarLogoparams.height = (buttonWidth/2)+16;
        GambarLogoparams.width  = (buttonWidth/2)+16;
        GambarLogo.setLayoutParams(GambarLogoparams);

        buttonGunung = (ImageView) findViewById(R.id.buttonGunung);
        android.view.ViewGroup.LayoutParams buttonGunungTipsparams =  buttonGunung.getLayoutParams();
        buttonGunungTipsparams.height = buttonHeight;
        buttonGunungTipsparams.width = buttonWidth;
        buttonGunung.setLayoutParams(buttonGunungTipsparams);

       /* buttonGunung.setWidth(buttonWidth);
        buttonGunung.setHeight(buttonHeight);*/

        buttonPeralatan = (ImageView) findViewById(R.id.buttonPeralatan);
        android.view.ViewGroup.LayoutParams buttonPeralatanparams =  buttonPeralatan.getLayoutParams();
        buttonPeralatanparams.height = buttonHeight;
        buttonPeralatanparams.width = buttonWidth;
        buttonPeralatan.setLayoutParams(buttonPeralatanparams);

        /* buttonPeralatan.setWidth(buttonWidth);
        buttonPeralatan.setHeight(buttonHeight);*/

        buttonSurvivalTips = (ImageView) findViewById(R.id.buttonSurvivalTips);
        android.view.ViewGroup.LayoutParams buttonSurvivalTipsparams =  buttonSurvivalTips.getLayoutParams();
        buttonSurvivalTipsparams.height = buttonHeight;
        buttonSurvivalTipsparams.width = buttonWidth;
        buttonSurvivalTips.setLayoutParams(buttonSurvivalTipsparams);

        /*buttonSurvivalTips.setWidth(buttonWidth);
        buttonSurvivalTips.setHeight(buttonHeight);*/

        buttonPeringatanDini = (ImageView) findViewById(R.id.buttonPeringatanDini);
        android.view.ViewGroup.LayoutParams buttonPeringatanDiniparams =  buttonPeringatanDini.getLayoutParams();
        buttonPeringatanDiniparams.height = buttonHeight;
        buttonPeringatanDiniparams.width = buttonWidth;
        buttonPeringatanDini.setLayoutParams(buttonPeringatanDiniparams);


       /* buttonPeringatanDini.setWidth(buttonWidth);
        buttonPeringatanDini.setHeight(buttonHeight);*/



        buttonGunung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, provinsi_gunung.class);
                startActivity(i);
            }
        });

        buttonPeralatan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, Peralatan.class);
                startActivity(i);
            }
        });

        buttonSurvivalTips.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, survival_tips.class);
                startActivity(i);
            }
        });

        buttonPeringatanDini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               // peringatan_dini_peralatan_tim.SelectedItems.clear();
               // Intent i = new Intent (MainActivity.this, MainActivity2.class);
                 Intent i = new Intent (MainActivity.this, peringatan_dini_info_pribadi.class);

                startActivity(i);
            }
        });






    }


    private  void NotifikasiKeluar()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_main_notifikasi_keluar, null);
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



        Ya.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {


                moveTaskToBack(true);
                finish();


            }
        });


        Tidak.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                alert.dismiss();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(status, "onCreateOptionsMenu() event");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(status, "onOptionsItemSelected() event");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   /* public void buttongunung(View v)
    {

        // Toast.makeText(this, "data loaded", 1000).show();
        Button button=(Button)findViewById(R.id.buttongunung);
        button.setBackgroundColor(Color.GRAY);
        Intent i = new Intent (MainActivity.this, provinsi_gunung.class);
        startActivity(i);



    }*/

}
