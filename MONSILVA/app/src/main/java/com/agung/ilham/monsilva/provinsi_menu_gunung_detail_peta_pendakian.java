package com.agung.ilham.monsilva;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class provinsi_menu_gunung_detail_peta_pendakian extends AppCompatActivity
{
    String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Gunung_Maps.php";
    String KEY_MAPS_MOUNTAIN = "mountain_name";
    String item,pictureSource,PetaPendakianGunung;
    ImageView gambarpetapendakian;
    CircleProgressBar progressBar;
    Button CobaLagi;
    ImageRequest request;
    RequestQueue requestQueue;
    String TAG ="MyTag";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinsi_menu_gunung_detail_peta_pendakian);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Intent page = getIntent();
        item = page.getStringExtra("item");

        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        CobaLagi = (Button) findViewById(R.id.CobaLagi);
        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        CobaLagi.setVisibility(View.INVISIBLE);
        gambarpetapendakian  = (ImageView) findViewById(R.id.gambarpetapendakian);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            progressBar.setVisibility(View.VISIBLE);
            getdatasurvival();

        }
        else
        {

            progressBar.setVisibility(View.INVISIBLE);
            ToastTidakAdaKoneksi();
            CobaLagi.setVisibility(View.VISIBLE);
        }

        CobaLagi.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CobaLagi.setVisibility(View.INVISIBLE);
                getdatasurvival();
            }
        });

    }

    private void getdatasurvival()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        JSONObject jsonResponse;

                        try{
                            jsonResponse = new JSONObject(response);
                            Log.d("RESPONSE----", jsonResponse.toString());

                            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

                            JSONObject jsonEquipmentCategory;
                            jsonEquipmentCategory = jsonMainNode.getJSONObject(0);


                            PetaPendakianGunung   =  jsonEquipmentCategory.optString("Mountain_Maps");
                            pictureSource         =  PetaPendakianGunung.replaceAll(" ","%20");

                            ambilgambar();

                        }
                        catch(JSONException e)
                        {
                            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        ToastGagal();
                        CobaLagi.setVisibility(View.VISIBLE);

                    }
                })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_MAPS_MOUNTAIN,item);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }
    private void ambilgambar()
    {

        String url = "http://tugas-akhir.hol.es/Uploads/"+pictureSource;
        request = new ImageRequest(url,
                new Response.Listener<Bitmap>()
                {
                    @Override
                    public void onResponse(Bitmap bitmap)
                    {
                        gambarpetapendakian.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener()
                {
                    public void onErrorResponse(VolleyError error)
                    {
                        gambarpetapendakian.setImageResource(R.drawable.product_image_not_available);
                    }
                });
        request.setTag(TAG);
        requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request );


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
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_provinsi_menu_gunung_detail_peta_pendakian, menu);
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
        Intent page = new Intent(provinsi_menu_gunung_detail_peta_pendakian.this, provinsi_menu_gunung_detail.class);
        page.putExtra("item",item);
        startActivity(page);
        provinsi_menu_gunung_detail_peta_pendakian.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent page = new Intent(provinsi_menu_gunung_detail_peta_pendakian.this, provinsi_menu_gunung_detail.class);
        page.putExtra("item",item);
        startActivity(page);
        provinsi_menu_gunung_detail_peta_pendakian.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
