package com.agung.ilham.monsilva;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class peringatan_dini_peralatan_tim extends AppCompatActivity {

    Button buttonKembali,buttonSelanjutnya;
    public static ArrayList SelectedItems = new ArrayList();
    double heightButtonfinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini_peralatan_tim);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Peringatan Dini");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        buttonKembali = (Button) findViewById(R.id.buttonKembali);
        buttonSelanjutnya = (Button) findViewById(R.id.buttonSelanjutnya);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        int buttonWidth = width/2;
        int heightScrollView = height/10;
        heightButtonfinal = heightScrollView*1;



        buttonKembali.setWidth(buttonWidth);
        buttonSelanjutnya.setWidth(buttonWidth);
        buttonKembali.setHeight((int)heightButtonfinal);
        buttonSelanjutnya.setHeight((int)heightButtonfinal);
        SizeCheckBox();
        SelectedItems.clear();



        buttonSelanjutnya.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent i = new Intent(peringatan_dini_peralatan_tim.this, peringatan_dini_peralatan_individu.class);
                startActivity(i);


            }
        });

        buttonKembali.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                onBackPressed();
            }
        });



    }


    public void onCheckboxClicked(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        int CheckListPeralatanTim = 0;

        if(CheckListPeralatanTim == 0)
        {
            switch(view.getId())
            {
                case R.id.Alat_Jahit:
                    if (checked)
                    {

                        SelectedItems.add("1.0");


                    }
                    else
                    {
                       SelectedItems.remove("1.0");
                    }

                    break;

               case R.id.Kompor_Lapangan:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                       SelectedItems.remove("1.0");
                    }
                    break;

                    case R.id.P3K:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }
                    break;

                case R.id.Korek_Api:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                       SelectedItems.remove("1.0");
                    }
                    break;

                case R.id.Matras:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }

                    break;

                case R.id.Nesting:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }
                    break;

                case R.id.Pisau:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }
                    break;

                case R.id.Peralatan_Cuci:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }
                    break;

                case R.id.Penunjuk_Arah:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }
                    break;

                case R.id.Tenda:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }
                    break;

                case R.id.Trash_Bag:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }
                    break;

                case R.id.Tempat_Air:
                    if (checked)
                    {
                        SelectedItems.add("1.0");

                    }
                    else
                    {
                        SelectedItems.remove("1.0");
                    }
                    break;


            }

        }
    }
    public void SizeCheckBox()
    {   CheckBox Alat_Jahit,Kompor_Lapangan,P3K,Korek_Api,Matras,Nesting,Pisau,Peralatan_Cuci,Penunjuk_Arah,Tenda
            ,Trash_Bag,Tempat_Air;

        Alat_Jahit      = (CheckBox) findViewById(R.id.Alat_Jahit);
        Kompor_Lapangan = (CheckBox) findViewById(R.id.Kompor_Lapangan);
        P3K             = (CheckBox) findViewById(R.id.P3K);
        Korek_Api       = (CheckBox) findViewById(R.id.Korek_Api);
        Matras          = (CheckBox) findViewById(R.id.Matras);
        Nesting         = (CheckBox) findViewById(R.id.Nesting);
        Pisau           = (CheckBox) findViewById(R.id.Pisau);
        Peralatan_Cuci  = (CheckBox) findViewById(R.id.Peralatan_Cuci);
        Penunjuk_Arah   = (CheckBox) findViewById(R.id.Penunjuk_Arah);
        Tenda           = (CheckBox) findViewById(R.id.Tenda);
        Trash_Bag       = (CheckBox) findViewById(R.id.Trash_Bag);
        Tempat_Air      = (CheckBox) findViewById(R.id.Tempat_Air);


        Alat_Jahit.setHeight((int)heightButtonfinal);
        Kompor_Lapangan .setHeight((int)heightButtonfinal);
        P3K.setHeight((int)heightButtonfinal);
        Korek_Api.setHeight((int)heightButtonfinal);
        Matras.setHeight((int)heightButtonfinal);
        Nesting.setHeight((int)heightButtonfinal);
        Pisau.setHeight((int)heightButtonfinal);
        Peralatan_Cuci.setHeight((int)heightButtonfinal);
        Penunjuk_Arah.setHeight((int)heightButtonfinal);
        Tenda.setHeight((int)heightButtonfinal);
        Trash_Bag.setHeight((int)heightButtonfinal);
        Tempat_Air.setHeight((int)heightButtonfinal);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_peringatan_dini_peralatan_tim, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
      if (id == R.id.action_settings)
      {

          Intent page = new Intent(peringatan_dini_peralatan_tim.this, MainActivity.class);
          startActivity(page);
          peringatan_dini_peralatan_tim.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Intent page = new Intent(peringatan_dini_peralatan_tim.this, peringatan_dini_info_pribadi.class);
        startActivity(page);
        peringatan_dini_peralatan_tim.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent page = new Intent(peringatan_dini_peralatan_tim.this, peringatan_dini_info_pribadi.class);
        startActivity(page);
        peringatan_dini_peralatan_tim.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }
}
