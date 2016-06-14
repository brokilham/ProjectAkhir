package com.agung.ilham.monsilva;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class peralatan_kalkulator_peralatan extends AppCompatActivity{


     int countJumlahOrang,HasilJumlahInput,countLamaPendakian;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peralatan_kalkulator_peralatan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas
        actionBar.setTitle("Kalkulator Peralatan");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        final EditText JumlahPendaki,LamaPendakian;
        final Button Hitung;

        Hitung = (Button) findViewById(R.id.Hitung);
        Hitung.setEnabled(false);

        JumlahPendaki = (EditText)findViewById(R.id.JumlahOrang);
        LamaPendakian = (EditText)findViewById(R.id.LamaPendakian);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int  height = displaymetrics.heightPixels;
        int heightScrollView = height/10;
        int heightButtonfinal = heightScrollView*1;

        JumlahPendaki.setHeight(heightButtonfinal);
        LamaPendakian.setHeight(heightButtonfinal);


        JumlahPendaki.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString().equals(""))
                {
                    countJumlahOrang = 0;
                }
                else
                {
                    countJumlahOrang = Integer.parseInt(String.valueOf(s));


                }


                    if(countJumlahOrang !=0 && countLamaPendakian!=0 )
                    {
                        Hitung.setEnabled(true);
                    }
                    else
                    {
                        Hitung.setEnabled(false);
                    }





                // Hitung.setEnabled(count > 0);

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        LamaPendakian.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(s.toString().equals(""))
                {
                    countLamaPendakian = 0;
                }
                else
                {
                    countLamaPendakian = Integer.parseInt(String.valueOf(s));


                }

                    if(countJumlahOrang !=0 && countLamaPendakian!=0 )
                    {
                        Hitung.setEnabled(true);
                    }
                    else
                    {
                        Hitung.setEnabled(false);
                    }


                 }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


        Hitung.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent i = new Intent (peralatan_kalkulator_peralatan.this, peralatan_kalkulator_peralatan_hasil_perhitungan.class);
                i.putExtra("JumlahPendaki",Integer.toString(countJumlahOrang));
                i.putExtra("LamaPendakian",Integer.toString(countLamaPendakian));
                startActivity(i);

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_peralatan_kalkulator_peralatan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return super.onOptionsItemSelected(item);

    }

    public void onBackPressed()
    {
        Intent page = new Intent(peralatan_kalkulator_peralatan.this, Peralatan.class);
        startActivity(page);
        peralatan_kalkulator_peralatan.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent page = new Intent(peralatan_kalkulator_peralatan.this, Peralatan.class);
        startActivity(page);
        peralatan_kalkulator_peralatan.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }
}
