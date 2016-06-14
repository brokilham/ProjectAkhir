package com.agung.ilham.monsilva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class provinsi_menu_gunung_detail_informasi_gunung extends AppCompatActivity {
    private static final String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Gunung_Detail_Informasi.php";
    public static final String KEY_MOUNTAIN_NAME = "mountain_name";
    String item,pictureSource;
    TextView MountainProvince,MountainCity,MountainStatus,MountainHigh,JudulGunung;
    ImageView  gambargunung;
    Button ButtonCobaLagi;
    ScrollView scrollView;
    CircleProgressBar progressBar;
    String TAG = "MyTag";
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String ImageTAG = "MyTag";
    ImageRequest imageRequest;
    RequestQueue ImagerequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinsi_menu_gunung_detail_informasi_gunung);

        Intent page = getIntent();
        item = page.getStringExtra("item");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas
        actionBar.setTitle("Detail Gunung");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        scrollView = (ScrollView) findViewById(R.id.parent);
        JudulGunung=(TextView) findViewById(R.id.judulgunung);
        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        MountainProvince = (TextView) findViewById(R.id.MountainProvince);
        MountainCity = (TextView) findViewById(R.id.MountainCity);
        MountainStatus = (TextView) findViewById(R.id.MountainStatus);
        MountainHigh = (TextView) findViewById(R.id.MountainHigh);
        gambargunung = (ImageView) findViewById(R.id.gambargunung);
        ButtonCobaLagi = (Button) findViewById(R.id.CobaLagi);

        JudulGunung.setText(item);
        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        ButtonCobaLagi.setVisibility(View.INVISIBLE);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int widthPotong = width/5;
        int imageheight = widthPotong*4;
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,imageheight);
        gambargunung.setLayoutParams(parms);
        JudulGunung.setHeight(widthPotong);


        ButtonCobaLagi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getdatagunung();
                progressBar.setVisibility(View.VISIBLE);
                ButtonCobaLagi.setVisibility(View.INVISIBLE);


            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            progressBar.setVisibility(View.VISIBLE);
            getdatagunung();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            ToastTidakAdaKoneksi();
            progressBar.setVisibility(View.INVISIBLE);
            ButtonCobaLagi.setVisibility(View.VISIBLE);
        }


    }
    private void getdatagunung()
    {

            stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        JSONObject jsonResponse,jsonnamaProvinsi,jsonnamaKota,jsonnamaGambar,jsonstatusGunung,
                                jsonketinggianGunung,jsonpenjelasanGunung;
                        String namaProvinsi,namaKota,namaGambar,kategoriGunung,ketinggianGunung,penjelasanGunung;
                        try
                        {
                                jsonResponse = new JSONObject(response);
                                Log.d("RESPONSE----", jsonResponse.toString());

                                JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

                                jsonnamaProvinsi     = jsonMainNode.getJSONObject(0);
                                jsonnamaKota         = jsonMainNode.getJSONObject(1);
                                jsonnamaGambar       = jsonMainNode.getJSONObject(2);
                                jsonstatusGunung     = jsonMainNode.getJSONObject(3);
                                jsonketinggianGunung = jsonMainNode.getJSONObject(4);
                                jsonpenjelasanGunung = jsonMainNode.getJSONObject(5);

                                namaProvinsi     = jsonnamaProvinsi.optString("Mountain_Province");
                                namaKota         = jsonnamaKota.optString("Mountain_City");
                                namaGambar       = jsonnamaGambar.optString("Mountain_Picture");
                                kategoriGunung   = jsonstatusGunung.optString("Mountain_Status");
                                ketinggianGunung = jsonketinggianGunung.optString("Mountain_High");
                                penjelasanGunung = jsonpenjelasanGunung.optString("Mountain_Explanation");

                                MountainProvince.setText(namaProvinsi);
                                MountainCity.setText(namaKota);
                                pictureSource =  namaGambar.replaceAll(" ","%20");
                                MountainStatus.setText(kategoriGunung);
                                MountainHigh.setText(ketinggianGunung+" MDPL");

                                DocumentView MountainExplanation = (DocumentView) findViewById(R.id.MountainExplanation);
                                Spannable spanMountainExplanation = new SpannableString(penjelasanGunung);
                                MountainExplanation.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
                                MountainExplanation.setText(spanMountainExplanation);


                            progressBar.setVisibility(View.INVISIBLE);
                                scrollView.setVisibility(View.VISIBLE);
                                ambilgambar();

                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
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
                        ButtonCobaLagi.setVisibility(View.VISIBLE);

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
    private void ambilgambar()
    {

        String url = "http://tugas-akhir.hol.es/Uploads/"+pictureSource;
        imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>()
                {
                    @Override
                    public void onResponse(Bitmap bitmap)
                    {
                        gambargunung.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener()
                {
                    public void onErrorResponse(VolleyError error)
                    {
                        gambargunung.setImageResource(R.drawable.product_image_not_available);
                    }
                });
        imageRequest.setTag(ImageTAG);
        ImagerequestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        imageRequest.setRetryPolicy(policy);
        ImagerequestQueue.add(imageRequest);

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
        getMenuInflater().inflate(R.menu.menu_provinsi_gunung_detail, menu);
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
        Intent page = new Intent(provinsi_menu_gunung_detail_informasi_gunung.this, provinsi_menu_gunung_detail.class);
        page.putExtra("item",item);
        startActivity(page);
        provinsi_menu_gunung_detail_informasi_gunung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent page = new Intent(provinsi_menu_gunung_detail_informasi_gunung.this, provinsi_menu_gunung_detail.class);
        page.putExtra("item",item);
        startActivity(page);
        provinsi_menu_gunung_detail_informasi_gunung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }
    protected void onStop ()
    {
        super.onStop();
        if (requestQueue != null)
        {
            requestQueue.cancelAll(TAG);
        }
        if (ImagerequestQueue != null)
        {
            ImagerequestQueue.cancelAll(TAG);
        }

    }
}
