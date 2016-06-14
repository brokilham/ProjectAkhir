package com.agung.ilham.monsilva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class survival_tips_detail extends AppCompatActivity {
    String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Tips_Survival_Detail.php";
    String KEY_ID_SURVIVAL_NAME = "survival_id_name";
    String TAG = "MyTag";
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String ImageTag = "MyTag";
    ImageRequest imageRequest;
    RequestQueue ImageRequestQueue;
    String IdSurvivalName,SurvivalName;
    String picture;
    String pictureSource;
    TextView judul_survival, keterangan;
    Button ButtonCobaLagi;
    ImageView mImageView;
    CircleProgressBar progressBar;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival_tips_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Intent page = getIntent();
        IdSurvivalName = page.getStringExtra("IdSurvivalName");
        SurvivalName = page.getStringExtra("SurvivalName");


        scrollView = (ScrollView) findViewById(R.id.parent);
        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        ButtonCobaLagi = (Button) findViewById(R.id.CobaLagi);
        mImageView = (ImageView) findViewById(R.id.imageView);
        judul_survival = (TextView) findViewById(R.id.judul_survival);
        keterangan = (TextView) findViewById(R.id.keterangan);

        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        ButtonCobaLagi.setVisibility(View.GONE);
        judul_survival.setTypeface(null, Typeface.BOLD);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int widthPotong = width/5;
        int imageheight = widthPotong*4;
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,imageheight);
        mImageView.setLayoutParams(parms);
        judul_survival.setHeight(widthPotong);


        ButtonCobaLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getdatasurvival();
                ButtonCobaLagi.setVisibility(View.INVISIBLE);

            }
        });


        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            progressBar.setVisibility(View.VISIBLE);
            getdatasurvival();
            judul_survival.setText(SurvivalName);

        }
        else
        {
            ToastTidakAdaKoneksi();
            ButtonCobaLagi.setVisibility(View.VISIBLE);

        }

    }

    private void getdatasurvival(){

       stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonResponse;
                        try {
                            jsonResponse = new JSONObject(response);
                            Log.d("RESPONSE----", jsonResponse.toString());

                            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                            JSONObject jsonChildNode2 = jsonMainNode.getJSONObject(1);
                            picture = jsonChildNode.optString("survival_picture");
                            String explanation = jsonChildNode2.optString("survival_explanation");

                            //keterangan.setText(Html.fromHtml(explanation));
                            WebView webview = (WebView) findViewById(R.id.webview);
                            webview.loadData(explanation, "text/html; charset=UTF-8", null);
                           // keterangan.setText(explanation);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }


                        progressBar.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.VISIBLE);
                        ambilgambar();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastGagal();
                        progressBar.setVisibility(View.INVISIBLE);
                        ButtonCobaLagi.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_ID_SURVIVAL_NAME,IdSurvivalName);
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
        if(picture != null)
        {

            pictureSource = picture.replaceAll(" ", "%20");


        }


       String url = "http://tugas-akhir.hol.es/Uploads/"+pictureSource;


// Retrieves an image specified by the URL, displays it in the UI.
      imageRequest = new ImageRequest(url,
               new Response.Listener<Bitmap>() {
                   @Override
                   public void onResponse(Bitmap bitmap) {

                       mImageView.setImageBitmap(bitmap);
                   }
               }, 0, 0, null,
               new Response.ErrorListener() {
                   public void onErrorResponse(VolleyError error) {
                       mImageView.setImageResource(R.drawable.product_image_not_available);
                   }
               });

       imageRequest.setTag(ImageTag);
       ImageRequestQueue = Volley.newRequestQueue(this);
       int socketTimeout = 30000;//30 seconds - change to what you want
       RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
       imageRequest.setRetryPolicy(policy);
       ImageRequestQueue.add(imageRequest);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survival_tips_detail, menu);
        return true;
    }
    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null)
        {
            requestQueue.cancelAll(TAG);
        }
        if (ImageRequestQueue != null)
        {
            ImageRequestQueue.cancelAll(TAG);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;
    }

    public void onBackPressed()
    {
        Intent i = new Intent(survival_tips_detail.this, survival_tips.class);
        startActivity(i);
        survival_tips_detail.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent i = new Intent(survival_tips_detail.this, survival_tips.class);
        startActivity(i);
        survival_tips_detail.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }
}
