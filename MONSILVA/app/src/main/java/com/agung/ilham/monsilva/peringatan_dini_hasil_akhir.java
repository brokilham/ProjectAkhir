package com.agung.ilham.monsilva;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class peringatan_dini_hasil_akhir extends AppCompatActivity {

    String KEY_NAME_PROVINCE ="NameProvince";
    String KEY_NAME_CITY = "NameCity";
    String KEY_NAME_MOUNTAIN_SELECT = "NamaGunungSelect";

    String IdGunung,NameProvince,NameCity,NamaGunungSelect;
    Button buttonKembali,CobaLagi;
    CircleProgressBar progressBar;

    RequestQueue requestQueue;
    StringRequest stringRequest;
    String TAG = "MyTag";

    TextView NamaGunungInfo,KotaGunungInfo,RekomendasiPendakian,KeteranganCuacaHariIni,SuhuMaxCuacaHariIni,
    SuhuMinCuacaHariIni,KeteranganCuacaBesok,SuhuMaxBesok,SuhuMinBesok,StatusGunung,KondisiFisik,JarakLari,KelengkapanPeralatan;

   RelativeLayout LayoutInfoGunung,LayoutRekomendasi,LayoutCuacaHariIni,LayoutCuacaBesok,LayoutStatusGunung,
    LayoutKondisiFisik, LayoutKelengkapanPeralatan;

    int Temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini_hasil_akhir);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Peringatan Dini");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        progressBar   = (CircleProgressBar) findViewById(R.id.progressBar);
        buttonKembali = (Button) findViewById(R.id.buttonKembali);
        CobaLagi      = (Button)findViewById(R.id.CobaLagi);

        NamaGunungInfo         = (TextView)findViewById(R.id.NamaGunungInfo);
        KotaGunungInfo         = (TextView)findViewById(R.id.KotaGunungInfo);
        RekomendasiPendakian   = (TextView)findViewById(R.id.RekomendasiPendakian);
        KeteranganCuacaHariIni = (TextView)findViewById(R.id.KeteranganCuacaHariIni);
        SuhuMaxCuacaHariIni    = (TextView)findViewById(R.id.SuhuMaxCuacaHariIni);
        SuhuMinCuacaHariIni    = (TextView)findViewById(R.id.SuhuMinCuacaHariIni);
        KeteranganCuacaBesok   = (TextView)findViewById(R.id.KeteranganCuacaBesok);
        SuhuMaxBesok           = (TextView)findViewById(R.id.SuhuMaxBesok);
        SuhuMinBesok           = (TextView)findViewById(R.id.SuhuMinBesok);
        StatusGunung           = (TextView)findViewById(R.id.StatusGunung);
        KondisiFisik           = (TextView)findViewById(R.id.KondisiFisik);
        JarakLari              = (TextView)findViewById(R.id.JarakLari);
        KelengkapanPeralatan   = (TextView)findViewById(R.id.KelengkapanPeralatan);

        LayoutInfoGunung = (RelativeLayout) findViewById(R.id.LayoutInfoGunung);
        LayoutRekomendasi = (RelativeLayout) findViewById(R.id.LayoutRekomendasi);
        LayoutCuacaHariIni = (RelativeLayout) findViewById(R.id.LayoutCuacaHariIni);
        LayoutCuacaBesok = (RelativeLayout) findViewById(R.id.LayoutCuacaHariIni);
        LayoutStatusGunung = (RelativeLayout) findViewById(R.id.LayoutStatusGunung);
        LayoutKondisiFisik = (RelativeLayout) findViewById(R.id.LayoutKondisiFisik);
        LayoutKelengkapanPeralatan = (RelativeLayout) findViewById(R.id.LayoutKelengkapanPeralatan);


     /*   LayoutInfoGunung.setVisibility(View.INVISIBLE);
        LayoutRekomendasi.setVisibility(View.INVISIBLE);
        LayoutCuacaHariIni.setVisibility(View.INVISIBLE);
        LayoutCuacaBesok.setVisibility(View.INVISIBLE);
        LayoutStatusGunung.setVisibility(View.INVISIBLE);
        LayoutKondisiFisik.setVisibility(View.INVISIBLE);
        LayoutKelengkapanPeralatan.setVisibility(View.INVISIBLE);*/

        CobaLagi.setVisibility(View.INVISIBLE);
        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int heightScrollView = height/10;
        double heightButtonfinal = heightScrollView*1;

        buttonKembali.setHeight((int)heightButtonfinal);

        Intent intent = getIntent();

        IdGunung = intent.getStringExtra("IdGunung");
        NameProvince = intent.getStringExtra("NameProvince");
        NameCity = intent.getStringExtra("NameCity");
        NamaGunungSelect = intent.getStringExtra("NamaGunungSelect");




        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {

            progressBar.setVisibility(View.VISIBLE);
            getdatacuaca();

        }
        else
        {
            CobaLagi.setVisibility(View.VISIBLE);

        }



        buttonKembali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });


        CobaLagi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CobaLagi.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                getdatacuaca();


            }
        });


    }


    private void getdatacuaca()
    {
       String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Ambil_cuaca_1.php";
        stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        JSONObject jsonResponse;

                         try {
                            jsonResponse = new JSONObject(response);
                            Log.d("RESPONSE----", jsonResponse.toString());

                            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

                            JSONObject jsonChildNode,jsonChildNode1,jsonChildNode2,jsonChildNode3,
                                    jsonChildNode4,jsonChildNode5,jsonChildNode6;

                            String StatusGunungString, CuacaHariIniString,SuhuMinHariIniString,SuhuMaxHariIniString,CuacaBesokString,
                                    SuhuMinBesokString,SuhuMaxBesokString;

                            jsonChildNode = jsonMainNode.getJSONObject(0);
                            jsonChildNode1 = jsonMainNode.getJSONObject(1);
                            jsonChildNode2 = jsonMainNode.getJSONObject(2);
                            jsonChildNode3 = jsonMainNode.getJSONObject(3);
                            jsonChildNode4 = jsonMainNode.getJSONObject(4);
                            jsonChildNode5 = jsonMainNode.getJSONObject(5);
                            jsonChildNode6 = jsonMainNode.getJSONObject(6);

                           /*  jsonChildNode1 = jsonMainNode.getJSONObject(0);
                             jsonChildNode2 = jsonMainNode.getJSONObject(1);
                             jsonChildNode3 = jsonMainNode.getJSONObject(2);
                             jsonChildNode4 = jsonMainNode.getJSONObject(3);
                             jsonChildNode5 = jsonMainNode.getJSONObject(4);
                             jsonChildNode6 = jsonMainNode.getJSONObject(5);*/


                            StatusGunungString   = jsonChildNode.optString("StatusGunung");
                            CuacaHariIniString   = jsonChildNode1.optString("CuacaHariIni");
                            SuhuMinHariIniString = jsonChildNode2.optString("SuhuMinHariIni");
                            SuhuMaxHariIniString = jsonChildNode3.optString("SuhuMaxHariIni");
                            CuacaBesokString     = jsonChildNode4.optString("CuacaBesok");
                            SuhuMinBesokString   = jsonChildNode5.optString("SuhuMinBesok");
                            SuhuMaxBesokString   = jsonChildNode6.optString("SuhuMaxBesok");

                        try
                        {
                            NamaGunungInfo.setText(NamaGunungSelect);
                            KotaGunungInfo.setText(NameCity);
                            //RekomendasiPendakian
                            KeteranganCuacaHariIni.setText(CuacaHariIniString);
                            SuhuMaxCuacaHariIni.setText(SuhuMaxHariIniString+"°C");
                            SuhuMinCuacaHariIni.setText(SuhuMinHariIniString+"°C");
                            KeteranganCuacaBesok.setText(CuacaBesokString);
                            SuhuMaxBesok.setText(SuhuMaxBesokString+"°C");
                            SuhuMinBesok.setText(SuhuMinBesokString+"°C");
                            StatusGunung.setText(StatusGunungString); // errornya di sini
                            KondisiFisik.setText(peringatan_dini_info_pribadi.StatusKondisiFisik); //error
                            JarakLari.setText(peringatan_dini_info_pribadi.JarakTempuhLari +" Meter"); //error
                            KelengkapanPeralatan.setText(peringatan_dini_peralatan_individu.HasilPembulatanPeralatan); //error

                            LayoutInfoGunung.setVisibility(View.VISIBLE);
                            LayoutRekomendasi.setVisibility(View.VISIBLE);
                            LayoutCuacaHariIni.setVisibility(View.VISIBLE);
                            LayoutCuacaBesok.setVisibility(View.VISIBLE);
                            LayoutStatusGunung.setVisibility(View.VISIBLE);
                            LayoutKondisiFisik.setVisibility(View.VISIBLE);
                            LayoutKelengkapanPeralatan.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.INVISIBLE);
                            //Temperature = Integer.parseInt(SuhuMaxHariIniString);
                           // Toast.makeText(getBaseContext(),String.valueOf(Temperature),Toast.LENGTH_SHORT).show();
                            Temperature = 14;
                            FuzzySuhuHariIni();
                        }
                        catch (Exception e)
                        {
                           // textView.setText("penampilan"+e.toString());

                        }

                            progressBar.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                           Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(peringatan_dini_hasil_akhir.this,error.toString(),Toast.LENGTH_LONG).show();
                        CobaLagi.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_NAME_PROVINCE,NameProvince);
                params.put(KEY_NAME_CITY,NameCity);
                params.put(KEY_NAME_MOUNTAIN_SELECT,NamaGunungSelect);
                return params;
            }

        };

        stringRequest.setTag(TAG);
        requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
    private  void FuzzySuhuHariIni()
    {
        double A1,A2;
        String StatusTemperature;
        int BatasAtasDingin = 3,BatasAtasSejuk = 15 ,BatasAtasNormal = 27,BatasAtasHangat = 39,BatasAtasPanas =51;

        int BatasBawahDingin = -12 ,BatasBawahSejuk = 0 ,BatasBawahNormal = 12,BatasBawahHangat = 24,BatasBawahPanas =36;
        int SelisihDingin = 0,SelisihSejuk = 0,SelisihNormal = 0,SelisihHangat = 0,SelisihPanas = 0;
        int TitikTengahDingin,TitikTengahSejuk,TitikTengahNormal,TitikTengahHangat,TitikTengahPanas;
        if (Temperature >=BatasBawahDingin && Temperature<=BatasAtasDingin)
        {
            StatusTemperature = "Dingin";
            SelisihDingin = Temperature - BatasAtasDingin;
            Toast.makeText(getBaseContext(),"SelisihDingin : "+String.valueOf(SelisihDingin),Toast.LENGTH_LONG).show();

        }
        if (Temperature >=BatasBawahSejuk && Temperature<=BatasAtasSejuk)
        {

            StatusTemperature = "Sejuk";
            SelisihSejuk = Temperature - BatasAtasSejuk;
            Toast.makeText(getBaseContext(),"SelisihSejuk : "+String.valueOf(SelisihSejuk),Toast.LENGTH_LONG).show();

        }
        if (Temperature >=BatasBawahNormal && Temperature<=BatasAtasNormal)
        {
            StatusTemperature = "Normal";
            SelisihNormal = Temperature - BatasAtasNormal;
            Toast.makeText(getBaseContext(),"SelisihNormal : "+String.valueOf(SelisihNormal),Toast.LENGTH_LONG).show();

        }
        if (Temperature >=BatasBawahHangat && Temperature<=BatasAtasHangat)
        {

            StatusTemperature = "Hangat";
            SelisihHangat = Temperature - BatasAtasHangat;
            Toast.makeText(getBaseContext(),"SelisihHangat : "+String.valueOf(SelisihHangat),Toast.LENGTH_LONG).show();
        }
        if (Temperature >=BatasBawahPanas && Temperature<=BatasAtasPanas)
        {

            StatusTemperature = "Panas";
            SelisihPanas = Temperature - BatasAtasPanas;
            Toast.makeText(getBaseContext(),"SelisihPanas : "+String.valueOf(SelisihPanas),Toast.LENGTH_LONG).show();

        }

        if(SelisihDingin!=0 && SelisihSejuk!=0)
        {

            Toast.makeText(getBaseContext(),"tes",Toast.LENGTH_LONG).show();
          /*  if (SelisihDingin <= SelisihSejuk)
            {*/
                //Selisih Dingin A1
                TitikTengahDingin = BatasAtasDingin/2;
                TitikTengahSejuk  = BatasAtasSejuk/2;
                A1= (BatasAtasDingin-Temperature)/(BatasAtasDingin-TitikTengahDingin);
                A2= (Temperature-BatasBawahSejuk)/(TitikTengahSejuk-BatasBawahSejuk);
            /*}*/
            Toast.makeText(getBaseContext(),"A1 : "+String.valueOf(A1) +" A2 : "+ String.valueOf(A2),Toast.LENGTH_LONG).show();

        }
        else if(SelisihSejuk !=0 && SelisihNormal !=0)
        {
            Toast.makeText(getBaseContext(),"tes",Toast.LENGTH_LONG).show();

            TitikTengahSejuk = BatasAtasSejuk/2;
            TitikTengahNormal  = BatasAtasNormal/2;
            A1= (BatasAtasSejuk-Temperature)/(BatasAtasSejuk-TitikTengahSejuk);
            A2= (Temperature-BatasBawahNormal)/(TitikTengahNormal-BatasBawahNormal);
            Toast.makeText(getBaseContext(),"A1 : "+String.valueOf(A1) +" A2 : "+ String.valueOf(A2),Toast.LENGTH_LONG).show();

        }
        else if (SelisihNormal !=0 && SelisihHangat !=0)
        {
            Toast.makeText(getBaseContext(),"tes",Toast.LENGTH_LONG).show();

            TitikTengahNormal = BatasAtasNormal/2;
            TitikTengahHangat  = BatasAtasHangat/2;
            A1= (BatasAtasNormal-Temperature)/(BatasAtasNormal-TitikTengahNormal);
            A2= (Temperature-BatasBawahHangat)/(TitikTengahHangat-BatasBawahHangat);
            Toast.makeText(getBaseContext(),"A1 : "+String.valueOf(A1) +" A2 : "+ String.valueOf(A2),Toast.LENGTH_LONG).show();

        }
        else if (SelisihHangat !=0 && SelisihPanas !=0)
        {
            Toast.makeText(getBaseContext(),"tes",Toast.LENGTH_LONG).show();

            TitikTengahHangat = BatasAtasHangat/2;
            TitikTengahPanas  = BatasAtasPanas/2;
            A1= (BatasAtasHangat-Temperature)/(BatasAtasHangat-TitikTengahHangat);
            A2= (Temperature-BatasBawahPanas)/(TitikTengahPanas-BatasBawahPanas);
            Toast.makeText(getBaseContext(),"A1 : "+String.valueOf(A1) +" A2 : "+ String.valueOf(A2),Toast.LENGTH_LONG).show();

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_peringatan_dini_hasil_akhir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {

            Intent page = new Intent(peringatan_dini_hasil_akhir.this, MainActivity.class);
            startActivity(page);
            peringatan_dini_hasil_akhir.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {

        Intent page = new Intent(peringatan_dini_hasil_akhir.this, peringatan_dini_gunung.class);
        startActivity(page);
        peringatan_dini_hasil_akhir.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent page = new Intent(peringatan_dini_hasil_akhir.this, peringatan_dini_gunung.class);
        startActivity(page);
        peringatan_dini_hasil_akhir.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null)
        {
            requestQueue.cancelAll(TAG);
        }

    }

}

