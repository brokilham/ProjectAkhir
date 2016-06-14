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
import android.support.v7.app.ActionBarActivity;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class peralatan_tim_detail extends AppCompatActivity {

    String KEY_EQUIPMENT_ID_NAME = "equipment_id";
    String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Peralatan_Detail.php";
    String   PeralatanName,PeralatanId,EquipmentCategory,EquipmentPicture,EquipmentFungcion,EquipmentTips,pictureSource;
    TextView kategoriperalatan,JudulPeralatanTim;
    ImageView gambarperalatan;
    Button ButtonCobaLagi;
    CircleProgressBar progressBar;
    ScrollView scrollView;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String TAG = "MyTag";
    ImageRequest Imagerequest;
    String ImageTAG = "MyTag";
    RequestQueue imageRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peralatan_tim_detail);
        Intent intent = getIntent();

        PeralatanName = intent.getStringExtra("PeralatanName");
        PeralatanId = intent.getStringExtra("PeralatanId");
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas
        actionBar.setTitle("Peralatan Tim");





        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        kategoriperalatan = (TextView)  findViewById(R.id.kategoriperalatan);
        gambarperalatan   = (ImageView) findViewById(R.id.gambarperalatan);
        JudulPeralatanTim = (TextView)findViewById(R.id.judulperalatantim);
        ButtonCobaLagi = (Button) findViewById(R.id.CobaLagi);

        scrollView = (ScrollView) findViewById(R.id.parent);
        scrollView.setVisibility(View.INVISIBLE);
        ButtonCobaLagi.setVisibility(View.INVISIBLE);
        JudulPeralatanTim.setText(PeralatanName);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int widthPotong = width/5;
        int imageheight = widthPotong*4;
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,imageheight);
        gambarperalatan.setLayoutParams(parms);
        JudulPeralatanTim.setHeight(widthPotong);


        ButtonCobaLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                ButtonCobaLagi.setVisibility(View.INVISIBLE);
                getPeralatanTimDetail();
            }
        });


        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            progressBar.setVisibility(View.VISIBLE);
            getPeralatanTimDetail();

        }
        else
        {

            ToastTidakAdaKoneksi();
            ButtonCobaLagi.setVisibility(View.VISIBLE);
            // display error
        }
    }

    private void getPeralatanTimDetail(){


       stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(peralatan_tim_detail.this,response,Toast.LENGTH_LONG).show();
                        //  mTxtDisplay.setText(response.toString());

                        JSONObject jsonResponse;

                        try{
                            jsonResponse = new JSONObject(response);
                            Log.d("RESPONSE----", jsonResponse.toString());

                            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");


                                JSONObject jsonEquipmentCategory,jsonEquipmentPicture,jsonEquipmentFungcion,jsonEquipmentTips;
                                jsonEquipmentCategory = jsonMainNode.getJSONObject(0);
                                jsonEquipmentPicture  = jsonMainNode.getJSONObject(1);
                                jsonEquipmentFungcion = jsonMainNode.getJSONObject(2);
                                jsonEquipmentTips     = jsonMainNode.getJSONObject(3);

                                EquipmentCategory     =  jsonEquipmentCategory.optString("Equipment_Category");
                                EquipmentPicture      =  jsonEquipmentPicture.optString("Equipment_Picture");
                                EquipmentFungcion     =  jsonEquipmentFungcion .optString("Equipment_Fungcion");
                                EquipmentTips         =  jsonEquipmentTips.optString("Equipment_Tips");


                               kategoriperalatan.setText(EquipmentCategory);
                               pictureSource =  EquipmentPicture.replaceAll(" ","%20");

                               DocumentView fungsiperalatan = (DocumentView) findViewById(R.id.fungsiperalatan);
                               Spannable spanfungsiperalatan = new SpannableString(EquipmentFungcion);
                               fungsiperalatan.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
                               fungsiperalatan.setText(spanfungsiperalatan);

                               DocumentView dc = (DocumentView) findViewById(R.id.documentView);
                               Spannable span = new SpannableString(EquipmentTips);
                               dc.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
                               dc.setText(span);

                              // tipsperalatan.setText("Tips : "+EquipmentTips);
                                progressBar.setVisibility(View.INVISIBLE);
                               //pDialog.dismiss();
                               scrollView.setVisibility(View.VISIBLE);
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
                       // pDialog.dismiss();
                        progressBar.setVisibility(View.INVISIBLE);
                        ToastGagal();
                        ButtonCobaLagi.setVisibility(View.VISIBLE);

                        //Toast.makeText(peralatan_tim_detail.this,error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_EQUIPMENT_ID_NAME,PeralatanId);
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

// Retrieves an image specified by the URL, displays it in the UI.
        Imagerequest = new ImageRequest(url,
                new Response.Listener<Bitmap>()
                {
                    @Override
                    public void onResponse(Bitmap bitmap)
                    {
                        gambarperalatan.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener()
                {
                    public void onErrorResponse(VolleyError error)
                    {
                        gambarperalatan.setImageResource(R.drawable.product_image_not_available);
                    }
                });
        Imagerequest.setTag(ImageTAG);
        imageRequestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        Imagerequest.setRetryPolicy(policy);
        imageRequestQueue.add(Imagerequest);
    }

    private void ToastGagal()
    {
        Context context= getApplicationContext();
        LayoutInflater inflater= getLayoutInflater();
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
        Context context= getApplicationContext();
        LayoutInflater inflater = getLayoutInflater();
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
        getMenuInflater().inflate(R.menu.menu_peralatan_tim_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;
    }

    public void onBackPressed()
    {
        Intent i = new Intent(peralatan_tim_detail.this, Peralatan.class);
        startActivity(i);
        peralatan_tim_detail.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent i = new Intent(peralatan_tim_detail.this, Peralatan.class);
        startActivity(i);
        peralatan_tim_detail.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        return  true;
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null)
        {
            requestQueue.cancelAll(TAG);
        }
        if (imageRequestQueue != null)
        {
            imageRequestQueue.cancelAll(TAG);
        }

    }

    }

