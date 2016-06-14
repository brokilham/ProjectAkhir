package com.agung.ilham.monsilva;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;


public class peringatan_dini_peralatan_individu extends AppCompatActivity
{

    Button buttonKembali,buttonSelanjutnya;
    ScrollView Parent;
    static String HasilPembulatanPeralatan;
    double heightButtonfinal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini_peralatan_individu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Peringatan Dini");

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        buttonKembali = (Button) findViewById(R.id.buttonKembali);
        buttonSelanjutnya = (Button) findViewById(R.id.buttonSelanjutnya);
        Parent = (ScrollView) findViewById(R.id.parent);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        int buttonWidth = width/2;
        int heightScrollView = height/10;
        double heightScrollViewfinal = heightScrollView*7.5;
        heightButtonfinal = heightScrollView*1;

        SizeCheckBox();
        buttonKembali.setWidth(buttonWidth);
        buttonSelanjutnya.setWidth(buttonWidth);
        buttonKembali.setHeight((int)heightButtonfinal);
        buttonSelanjutnya.setHeight((int)heightButtonfinal);



        Parent.setLayoutParams(new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, (int) heightScrollViewfinal));



        buttonSelanjutnya.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

                double a, b = 0.0;
                String datastring;
                for(int i=0; i<peringatan_dini_peralatan_tim.SelectedItems.size(); i++)
                {

                    datastring = (String) peringatan_dini_peralatan_tim.SelectedItems.get(0);
                    a = Double.parseDouble(datastring);
                    b = a + b;
                }


                  Double hasil = Double.valueOf((b/27)*100);
                  HasilPembulatanPeralatan = String.format("%.2f", hasil);
                  Toast.makeText(getApplicationContext(), HasilPembulatanPeralatan , Toast.LENGTH_SHORT).show();

                Intent i = new Intent (peringatan_dini_peralatan_individu.this, peringatan_dini_gunung.class);

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

        switch(view.getId())
        {
            case R.id.Gaiter:
                if (checked)
                {

                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");


                }
                else
                {
                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Headlamp:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Jaket_Gunung:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;

            case R.id.Jam_Tangan:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Jas_Hujan:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Kaos_Kaki:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Masker:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Pakaian:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Peralatan_Makan:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Peralatan_Mandi:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Sandal_Gunung:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Sarung_Tangan:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Sepatu_Hiking:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;
            case R.id.Sleeping_Bag:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;

            case R.id.Tas_Gunung:
                if (checked)
                {
                    peringatan_dini_peralatan_tim.SelectedItems.add("1.0");
                }
                else
                {

                    peringatan_dini_peralatan_tim.SelectedItems.remove("1.0");
                }

                break;


        }
    }
    public void SizeCheckBox()
    {   CheckBox Gaiter,Headlamp,Jaket_Gunung,Jam_Tangan,Jas_Hujan,Kaos_Kaki,Masker,Pakaian,Peralatan_Makan,
            Peralatan_Mandi,Sandal_Gunung,Sarung_Tangan,Sepatu_Hiking,Sleeping_Bag,Tas_Gunung;

        Gaiter          = (CheckBox) findViewById(R.id.Gaiter);
        Headlamp        = (CheckBox) findViewById(R.id.Headlamp);
        Jaket_Gunung    = (CheckBox) findViewById(R.id.Jaket_Gunung);
        Jam_Tangan      = (CheckBox) findViewById(R.id.Jam_Tangan);
        Jas_Hujan       = (CheckBox) findViewById(R.id.Jas_Hujan);
        Kaos_Kaki       = (CheckBox) findViewById(R.id.Kaos_Kaki);
        Masker          = (CheckBox) findViewById(R.id.Masker);
        Pakaian         = (CheckBox) findViewById(R.id.Pakaian);
        Peralatan_Makan = (CheckBox) findViewById(R.id.Peralatan_Makan);
        Peralatan_Mandi = (CheckBox) findViewById(R.id.Peralatan_Mandi);
        Sarung_Tangan   = (CheckBox) findViewById(R.id.Sarung_Tangan);
        Sepatu_Hiking   = (CheckBox) findViewById(R.id.Sepatu_Hiking);
        Sleeping_Bag    = (CheckBox) findViewById(R.id.Sleeping_Bag);
        Tas_Gunung      = (CheckBox) findViewById(R.id.Tas_Gunung);


        Gaiter.setHeight((int)heightButtonfinal);
        Headlamp.setHeight((int)heightButtonfinal);
        Jaket_Gunung.setHeight((int)heightButtonfinal);
        Jam_Tangan.setHeight((int)heightButtonfinal);
        Jas_Hujan.setHeight((int)heightButtonfinal);
        Kaos_Kaki.setHeight((int)heightButtonfinal);
        Masker.setHeight((int)heightButtonfinal);
        Pakaian.setHeight((int)heightButtonfinal);
        Peralatan_Makan.setHeight((int)heightButtonfinal);
        Peralatan_Mandi.setHeight((int)heightButtonfinal);
        Sarung_Tangan.setHeight((int)heightButtonfinal);
        Sepatu_Hiking.setHeight((int)heightButtonfinal);
        Sleeping_Bag.setHeight((int)heightButtonfinal);
        Tas_Gunung.setHeight((int)heightButtonfinal);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_peringatan_dini_peralatan_individu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent page = new Intent(peringatan_dini_peralatan_individu.this, MainActivity.class);
            startActivity(page);
            peringatan_dini_peralatan_individu.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Intent page = new Intent(peringatan_dini_peralatan_individu.this, peringatan_dini_peralatan_tim.class);
        startActivity(page);
        peringatan_dini_peralatan_individu.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent page = new Intent(peringatan_dini_peralatan_individu.this, peringatan_dini_peralatan_tim.class);
        startActivity(page);
        peringatan_dini_peralatan_individu.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }

}
