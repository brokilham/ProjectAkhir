package com.agung.ilham.monsilva;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class survival_tips extends AppCompatActivity {

    List<Map<String,String>> survivalList = new ArrayList<Map<String,String>>();
    ListView listView;
    ArrayList SelectedIdArray = new ArrayList();
    CircleProgressBar progressBar;
    Button CobaLagi;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String TAG = "MyTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival_tips);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        CobaLagi = (Button)findViewById(R.id.CobaLagi);
        CobaLagi.setVisibility(View.INVISIBLE);

        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if(MainActivity.checkSurvivalTipsList == 0)
            {
                progressBar.setVisibility(View.VISIBLE);
                getListSurvival();
                MainActivity.checkSurvivalTipsList = MainActivity.checkSurvivalTipsList+1;

            }
            else if(MainActivity.checkSurvivalTipsList !=0)
            {
                progressBar.setVisibility(View.VISIBLE);
                readcache();

            }

        }
        else
        {

            if(MainActivity.checkSurvivalTipsList !=0)
            {
                ToastTidakAdaKoneksi();
                progressBar.setVisibility(View.VISIBLE);
                readcache ();
            }
            else
            {   ToastTidakAdaKoneksi();
                CobaLagi.setVisibility(View.VISIBLE);
            }
        }

       CobaLagi.setOnClickListener(new View.OnClickListener()
       {
            @Override
            public void onClick(View v)
            {
                CobaLagi.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                if(MainActivity.checkSurvivalTipsList == 0)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    getListSurvival();

                }
                else if(MainActivity.checkSurvivalTipsList !=0)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    readcache();

                }
            }
       });
    }

    private void getListSurvival()
    {

        progressBar.setVisibility(View.VISIBLE);

        String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Judul_Tips_Survival.php";
         stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        File file;
                        FileOutputStream outputStream;

                        try {

                            file = new File(getCacheDir(),"MyCacheSurvivalList");

                            outputStream = new FileOutputStream(file);
                            outputStream.write(response.getBytes());
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MainActivity.checkSurvivalTipsList = MainActivity.checkSurvivalTipsList+1;
                        readcache ();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.INVISIBLE);
                        ToastGagal();
                        CobaLagi.setVisibility(View.VISIBLE);

                    }
                }){

        };

        stringRequest.setTag(TAG);
        requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    private HashMap<String, String>createSurvival(String name, String name1)
    {
        HashMap<String, String> survivalName = new HashMap<String, String>();
        survivalName.put(name, name1);
        return survivalName;
    }
    private void readcache ()
    {
        String line;
        BufferedReader input = null;
        File file = null;
        StringBuilder textfile = new StringBuilder();
        try
        {

            file = new File(getCacheDir(), "MyCacheSurvivalList");
            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                textfile.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

         JSONObject jsonResponse;
        try
        {
            jsonResponse = new JSONObject(textfile.toString());
            Log.d("RESPONSE----", jsonResponse.toString());

            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String idname = jsonChildNode.optString("survival_id");
                String name = jsonChildNode.optString("survival_name");

                SelectedIdArray.add(idname);

                survivalList.add(createSurvival("result", name));


                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }

        listView = (ListView) findViewById(R.id.listView);
        SimpleAdapter simpleAdapter = new SimpleAdapter(survival_tips.this, survivalList, android.R.layout.simple_list_item_1, new String[] {"result"}, new int[]
                {android.R.id.text1});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                String  SurvivalName = ((TextView)view).getText().toString();
                String  IdSurvivalName = (String) SelectedIdArray.get(position);

                Intent page = new Intent (survival_tips.this, survival_tips_detail.class);
                String item = ((TextView)view).getText().toString();
                page.putExtra("IdSurvivalName",IdSurvivalName);
                page.putExtra("SurvivalName",SurvivalName);
                startActivity(page);
            }
        });

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

        getMenuInflater().inflate(R.menu.menu_survival_tips, menu);
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

        Intent i = new Intent(survival_tips.this, MainActivity.class);
        startActivity(i);
        survival_tips.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {

        Intent i = new Intent(survival_tips.this, MainActivity.class);
        startActivity(i);
        survival_tips.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
