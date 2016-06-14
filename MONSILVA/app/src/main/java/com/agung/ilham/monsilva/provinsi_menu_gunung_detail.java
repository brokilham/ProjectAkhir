package com.agung.ilham.monsilva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class provinsi_menu_gunung_detail extends AppCompatActivity {

    String item;
    public static  Double latTujuan = null;
    public static  Double lngTujuan = null;
    CircleProgressBar progressBar;
    Button informasigunung,lokasiperijinan,petapendakian,CobaLagi;
    static int CheckDetailGunungPage, CheckLokasiPerijinan, CheckPetaPendakian;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String TAG = "MyTag";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinsi_menu_gunung_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
         CobaLagi = (Button) findViewById(R.id.CobaLagi);
        informasigunung = (Button) findViewById(R.id.InformasiGunung);
        lokasiperijinan = (Button) findViewById(R.id.LokasiPerijinan);
        petapendakian   = (Button) findViewById(R.id.PetaPendakian);

        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        CobaLagi.setVisibility(View.INVISIBLE);

        Intent page = getIntent();
        item = page.getStringExtra("item");
        informasigunung.setVisibility(View.INVISIBLE);
        lokasiperijinan.setVisibility(View.INVISIBLE);
        petapendakian.setVisibility(View.INVISIBLE);

        informasigunung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (CheckDetailGunungPage == 0)
                {
                    CheckDetailGunungPage = CheckDetailGunungPage+1;

                }
                Intent page = new Intent (provinsi_menu_gunung_detail.this, provinsi_menu_gunung_detail_informasi_gunung.class);
                page.putExtra("item",item);
                startActivity(page);
            }
        });

        lokasiperijinan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CheckLokasiPerijinan == 0)
                {
                    CheckLokasiPerijinan = CheckLokasiPerijinan+1;

                }
                Intent page = new Intent (provinsi_menu_gunung_detail.this, provinsi_menu_gunung_detail_lokasi_perijinan.class);
                page.putExtra("item",item);
                startActivity(page);

            }
        });

        petapendakian.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (CheckPetaPendakian ==0)
                {
                    CheckPetaPendakian = CheckPetaPendakian+1;

                }
                Intent page = new Intent (provinsi_menu_gunung_detail.this, provinsi_menu_gunung_detail_peta_pendakian.class);
                page.putExtra("item",item);
                startActivity(page);
            }
        });

        CobaLagi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                CobaLagi.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                getdatagunung();

            }
        });


           ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (CheckDetailGunungPage == 1 || CheckLokasiPerijinan == 1 || CheckPetaPendakian ==1)
            {
                progressBar.setVisibility(View.VISIBLE);
                readcache();
            }
            else
            {

                progressBar.setVisibility(View.VISIBLE);
                getdatagunung();

            }

        }
        else
        {

            ToastTidakAdaKoneksi();
            CobaLagi.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_provinsi_menu_gunung_detail, menu);
        return true;
    }
   private void getdatagunung()
   {

        String REGISTER_URL_MOUNTAIN = "http://tugas-akhir.hol.es/aplication/Gunung_Pos_Perijinan.php";
        final String KEY_MOUNTAIN_NAME = "mountain_name";

        stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_MOUNTAIN,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        File file;
                        FileOutputStream outputStream;

                        try
                        {
                            file = new File(getCacheDir(), "MyCache"+item);

                            outputStream = new FileOutputStream(file);
                            outputStream.write(response.getBytes());
                            outputStream.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        readcache ();


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        ToastGagal();
                       progressBar.setVisibility(View.INVISIBLE);
                        CobaLagi.setVisibility(View.VISIBLE);

                    }
                })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_MOUNTAIN_NAME,item);
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

    private void readcache()
    {

        String line;
        BufferedReader input = null;
        File file = null;
        StringBuilder textfile = new StringBuilder();
        try
        {

            file = new File(getCacheDir(), "MyCache"+item);
            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));


            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null)
            {
                textfile.append(line);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }



        JSONObject jsonResponse,JsonLongitudePos,JsonLatitudePos;

        try
        {
            jsonResponse = new JSONObject(textfile.toString());
            Log.d("RESPONSE----", jsonResponse.toString());

            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

            JsonLatitudePos     = jsonMainNode.getJSONObject(0);
            JsonLongitudePos    = jsonMainNode.getJSONObject(1);

            latTujuan  = Double.parseDouble(JsonLatitudePos.optString("Mountain_Latitude"));
            lngTujuan  = Double.parseDouble(JsonLongitudePos.optString("Mountain_Longitude"));

            progressBar.setVisibility(View.INVISIBLE);
            informasigunung.setVisibility(View.VISIBLE);
            lokasiperijinan.setVisibility(View.VISIBLE);
            petapendakian.setVisibility(View.VISIBLE);



        }
        catch (JSONException e)
        {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }




    }

    private void ToastGagal()
    {
        Context context=getApplicationContext();
        LayoutInflater inflater=getLayoutInflater();
        View view =inflater.inflate(R.layout.toast_gagal, null);
        Toast toast=new Toast(context);
        TextView TextToast = (TextView) view.findViewById(R.id.TextToast);
        TextToast.setText("Koneksi Internet Anda Bermasalah !!!");
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }
    private void ToastTidakAdaKoneksi()
    {

        Context context=getApplicationContext();
        LayoutInflater inflater=getLayoutInflater();
        View view =inflater.inflate(R.layout.toast_gagal, null);
        Toast toast=new Toast(context);
        TextView TextToast = (TextView) view.findViewById(R.id.TextToast);
        TextToast.setText("Tidak Ada Koneksi Internet");
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

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
        CheckDetailGunungPage = 0;
        CheckLokasiPerijinan  = 0;
        CheckPetaPendakian    = 0;
        Intent i = new Intent(provinsi_menu_gunung_detail.this, provinsi_gunung.class);
        startActivity(i);
        provinsi_menu_gunung_detail.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        CheckDetailGunungPage = 0;
        CheckLokasiPerijinan  = 0;
        CheckPetaPendakian    = 0;
        Intent i = new Intent(provinsi_menu_gunung_detail.this, provinsi_gunung.class);
        startActivity(i);
        provinsi_menu_gunung_detail.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }

    protected void onStop ()
    {
        super.onStop();
        if (requestQueue != null)
        {
            requestQueue.cancelAll(TAG);
        }

    }
}
