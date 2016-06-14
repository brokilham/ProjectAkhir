package com.agung.ilham.monsilva;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class peringatan_dini_info_pribadi extends AppCompatActivity{

    Button buttonKembali,buttonSelanjutnya, SelanjutnyaButtonJenisInputanJarak;;
    boolean checkJenisKelamin = false, checkUsia = false;
    int buttonWidth ,width,height,selectedPositionJenisInputan =-1, Usia,HasilKondisiFisik;
    String JenisKelamin;
    public static String StatusKondisiFisik;
    static String JarakTempuhLari;
    int countJumlahInputan;


    double heightButtonfinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini_info_pribadi);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Peringatan Dini");

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        buttonKembali = (Button) findViewById(R.id.buttonKembali);
        buttonSelanjutnya = (Button) findViewById(R.id.buttonProses);
        buttonSelanjutnya.setEnabled(false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        buttonWidth = width/2;
        int heightScrollView = height/10;
        heightButtonfinal = heightScrollView*1;



        buttonKembali.setWidth(buttonWidth);
        buttonSelanjutnya.setWidth(buttonWidth);
        buttonKembali.setHeight((int)heightButtonfinal);
        buttonSelanjutnya.setHeight((int)heightButtonfinal);

        buttonKembali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(peringatan_dini_info_pribadi.this, MainActivity.class);
                startActivity(i);
                 peringatan_dini_info_pribadi.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        buttonSelanjutnya.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pilihJenisInputan();
                  }
        });


        spinnerUsia();
        spinnerJenisKelamin();
     }

public void spinnerUsia()
{
    // Get reference of widgets from XML layout
    final List<String> spinnerArray =  new ArrayList<String>();
    for(int i=21; i<=100; i++)
    {
        if (i>21)
        {

            String item = Integer.toString(i);
            spinnerArray.add(item);

        }
        else
        {
            spinnerArray.add("Pilih Usia Anda...");

        }

    }

    final Spinner spinner = (Spinner) findViewById(R.id.spinner);


   // Initializing an ArrayAdapter
    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
            this,R.layout.activity_peringatan_dini_info_pribadi_spinner_item,spinnerArray){
        @Override
        public boolean isEnabled(int position){
            if(position == 0)
            {
                // Disable the first item from Spinner
                // First item will be use for hint
                return false;
            }
            else
            {
                return true;
            }
        }
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            tv.setHeight((int) heightButtonfinal);
            if(position == 0){
                // Set the hint text color gray
                tv.setTextColor(Color.GRAY);
            }
            else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }
    };
    spinnerArrayAdapter.setDropDownViewResource(R.layout.activity_peringatan_dini_info_pribadi_spinner_item);
    spinner.setAdapter(spinnerArrayAdapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItemText = (String) parent.getItemAtPosition(position);
            // If user change the default selection
            // First item is disable and it is used for hint
            if(position > 0){
                // Notify the selected item text

               Usia = Integer.parseInt(selectedItemText);
                checkUsia = true;
                if (checkUsia == true && checkJenisKelamin == true)
                {
                    buttonSelanjutnya.setEnabled(true);

                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
}

    public void spinnerJenisKelamin()
    {

        final List<String> spinnerArray =  new ArrayList<String>();
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerJenisKelamin);

        spinnerArray.add("Pilih Jenis Kelamin Anda...");
        spinnerArray.add("Pria");
        spinnerArray.add("Wanita");

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.activity_peringatan_dini_info_pribadi_spinner_item,spinnerArray){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setHeight((int) heightButtonfinal);
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.activity_peringatan_dini_info_pribadi_spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text

                    JenisKelamin =  selectedItemText;
                    checkJenisKelamin = true;
                    if (checkUsia == true && checkJenisKelamin == true)
                    {
                        buttonSelanjutnya.setEnabled(true);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void pilihJenisInputan(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_peringatan_dini_tipe_inputan_jarak, null);
        Button Kembali  = (Button)view.findViewById(R.id.Kembali);
        SelanjutnyaButtonJenisInputanJarak  = (Button)view.findViewById(R.id.Selanjutnya);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);

        alertDialogBuilder.setView(view);
        toolbar.setTitle("Pilih Cara yang anda pilih");

        RadioButton radioLari,radioHasilLari;

        radioLari = (RadioButton) view.findViewById(R.id.radioLari);
        radioHasilLari = (RadioButton) view.findViewById(R.id.radioHasilLari);

        radioLari.setHeight((int) heightButtonfinal);
        radioHasilLari.setHeight((int) heightButtonfinal);

        Kembali.setWidth(buttonWidth-20);
        SelanjutnyaButtonJenisInputanJarak.setWidth(buttonWidth-20);
        SelanjutnyaButtonJenisInputanJarak.setEnabled(false);



        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(width - 40,height/2);
        alert.show();




        SelanjutnyaButtonJenisInputanJarak.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                if(selectedPositionJenisInputan == 0)
                {
                    String UsiaString = Integer.toString(Usia);
                    Intent i = new Intent (peringatan_dini_info_pribadi.this, peringatan_dini_lari.class);
                    i.putExtra("JenisKelamin", JenisKelamin);
                    i.putExtra("Usia", UsiaString);
                    startActivity(i);

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

                alert.dismiss();

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
                    SelanjutnyaButtonJenisInputanJarak.setEnabled(true);
                    selectedPositionJenisInputan = 0;
                break;
            case R.id.radioHasilLari:
                if (checked)
                    SelanjutnyaButtonJenisInputanJarak.setEnabled(true);
                    selectedPositionJenisInputan = 1;
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
        final Button SelanjutnyaInputkanAngkaJarak = (Button)view.findViewById(R.id.Selanjutnya);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        toolbar.setTitle("Jarak (Meter)");

        InputAngkaJarak.setHeight((int) heightButtonfinal);

        Kembali.setWidth(buttonWidth-20);
        SelanjutnyaInputkanAngkaJarak.setWidth(buttonWidth-20);

        SelanjutnyaInputkanAngkaJarak.setEnabled(false);


        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(width - 40,height/2);
        alert.show();

        InputAngkaJarak.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // SelanjutnyaInputkanAngkaJarak.setEnabled(count > 0);
                if(s.toString().equals(""))
                {
                    countJumlahInputan = 0;
                }
                else
                {
                    countJumlahInputan = Integer.parseInt(String.valueOf(s));


                }


                if(countJumlahInputan !=0 )
                {
                    SelanjutnyaInputkanAngkaJarak.setEnabled(true);
                }
                else
                {
                    SelanjutnyaInputkanAngkaJarak.setEnabled(false);
                }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


        SelanjutnyaInputkanAngkaJarak.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String AngkaJarak = String.valueOf(InputAngkaJarak.getText());
                JarakTempuhLari = AngkaJarak;
                int Jarak = Integer.parseInt(AngkaJarak);
                Double HasilKondisiFisikDouble = ((Jarak -504.9)/44.73);
                long  HasilKondisiFisikDibulatkan  =  Math.round(HasilKondisiFisikDouble);
                HasilKondisiFisik =(int)HasilKondisiFisikDibulatkan;
                cek();
                Toast.makeText(getBaseContext(),StatusKondisiFisik,Toast.LENGTH_LONG).show();

                Intent i = new Intent (peringatan_dini_info_pribadi.this, peringatan_dini_peralatan_tim.class);
                startActivity(i);

            }
        });


        Kembali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                alert.dismiss();

            }
        });
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

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent i = new Intent(peringatan_dini_info_pribadi.this, MainActivity.class);
        startActivity(i);
        peringatan_dini_info_pribadi.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_peringatan_dini_info_pribadi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent i = new Intent(peringatan_dini_info_pribadi.this, MainActivity.class);
            startActivity(i);
            peringatan_dini_info_pribadi.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

        return super.onOptionsItemSelected(item);
    }
}
