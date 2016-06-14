package com.agung.ilham.monsilva;

import android.app.ProgressDialog;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class peringatan_dini_gunung extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<Map<String,String>> gununglList = new ArrayList<Map<String,String>>();
    Button buttonKembali,buttonProses,CobaLagi;
    ListView ListGunung;
    String NamaGunungSelect;
    ArrayList SelectedIdGunungArray = new ArrayList();
    ArrayList SelectedProvinceArray = new ArrayList();
    ArrayList SelectedCityArray = new ArrayList();
    int positionChoiceList;
    CircleProgressBar progressBar;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String TAG = "MyTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peringatan_dini_gunung);
        ActionBar actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Peringatan Dini");

        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        buttonKembali = (Button) findViewById(R.id.buttonKembali);
        buttonProses = (Button) findViewById(R.id.buttonProses);
        CobaLagi = (Button) findViewById(R.id.CobaLagi);

        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        CobaLagi.setVisibility(View.INVISIBLE);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        int buttonWidth = width/2;
        int heightScrollView = height/10;
        double heightButtonfinal = heightScrollView*1;

        buttonKembali.setWidth(buttonWidth);
        buttonProses.setWidth(buttonWidth);
        buttonKembali.setHeight((int)heightButtonfinal);
        buttonProses.setHeight((int)heightButtonfinal);

        if (NamaGunungSelect == null)
        {
            buttonProses.setEnabled(false);

        }

        buttonKembali.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                onBackPressed();
            }
        });


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if(MainActivity.checkPeringatanDiniGunungLIst == 0)
            {
                progressBar.setVisibility(View.VISIBLE);
                getListGunung();


            }
            else if(MainActivity.checkPeringatanDiniGunungLIst !=0)
            {

                progressBar.setVisibility(View.VISIBLE);
                readcache ();



            }

        }
        else
        {


            if(MainActivity.checkPeringatanDiniGunungLIst !=0)
            {

                ToastTidakAdaKoneksi();
                progressBar.setVisibility(View.VISIBLE);
                readcache ();

            }
            else
            {
                ToastTidakAdaKoneksi();
                CobaLagi.setVisibility(View.VISIBLE);

            }


        }


        buttonProses.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                String IdGunung = (String) SelectedIdGunungArray.get(positionChoiceList);
                String NameProvince = (String) SelectedProvinceArray.get(positionChoiceList);
                String NameCity = (String) SelectedCityArray.get(positionChoiceList);

                Intent page = new Intent (peringatan_dini_gunung.this, peringatan_dini_hasil_akhir.class);
                page.putExtra("IdGunung",IdGunung);
                page.putExtra("NameProvince",NameProvince);
                page.putExtra("NameCity",NameCity);
                page.putExtra("NamaGunungSelect",NamaGunungSelect);
                startActivity(page);
            }
        });


        CobaLagi.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CobaLagi.setVisibility(View.INVISIBLE);
                if(MainActivity.checkPeringatanDiniGunungLIst == 0)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    getListGunung();


                }
                else if(MainActivity.checkPeringatanDiniGunungLIst !=0)
                {

                    progressBar.setVisibility(View.VISIBLE);
                    readcache ();

                }

            }
        });

    }

    private void getListGunung()
    {
        progressBar.setVisibility(View.VISIBLE);

        String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Judul_Gunung_Peringatan_Dini.php";
        stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                       File file;
                        FileOutputStream outputStream;

                        try
                        {

                            file = new File(getCacheDir(),"PeringatanDiniGunung");

                            outputStream = new FileOutputStream(file);
                            outputStream.write(response.getBytes());
                            outputStream.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        MainActivity.checkPeringatanDiniGunungLIst = MainActivity.checkPeringatanDiniGunungLIst+1;
                        readcache ();


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
                }){
           /* @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_SURVIVAL_NAME,item);
                return params;
            }*/

        };

        stringRequest.setTag(TAG);
        requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);



    }

    private void readcache ()
    {
        String line;
        BufferedReader input = null;
        File file = null;
        StringBuilder textfile = new StringBuilder();
        try
        {

            file = new File(getCacheDir(),"PeringatanDiniGunung");
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



        JSONObject jsonResponse;

        try
        {
            jsonResponse = new JSONObject(textfile.toString());
            Log.d("RESPONSE----", jsonResponse.toString());

            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");
            for (int i = 0; i < jsonMainNode.length(); i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                String MountainId = jsonChildNode.optString("Mountain_Id");
                String MountainName = jsonChildNode.optString("Mountain_Name");
                String MountainProvince = jsonChildNode.optString("Mountain_Province");
                String MountainCity = jsonChildNode.optString("Mountain_City");

                //Toast.makeText(getApplicationContext(),MountainName+" "+ MountainId +  MountainProvince + MountainCity, Toast.LENGTH_SHORT).show();

                SelectedIdGunungArray.add(MountainId);
                SelectedProvinceArray.add(MountainProvince);
                SelectedCityArray.add(MountainCity);

                gununglList.add(createGunung("result", MountainName));
            }
        }
        catch (JSONException e)
        {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        ListGunung = (ListView) findViewById(R.id.listView);
        SimpleAdapter simpleAdapter = new SimpleAdapter(peringatan_dini_gunung.this, gununglList, android.R.layout.simple_list_item_1, new String[] {"result"}, new int[]
                {android.R.id.text1});
        ListGunung.setAdapter(simpleAdapter);

        ListGunung.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                  positionChoiceList = position;
                 NamaGunungSelect = ((TextView)view).getText().toString();
                 if(NamaGunungSelect !=null)
                 {

                     buttonProses.setEnabled(true);

                 }
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
    }

    private HashMap<String, String>createGunung(String name, String name1)
    {
        HashMap<String, String> gunungName = new HashMap<String, String>();
        gunungName.put(name, name1);
        return gunungName;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

    String item = parent.getItemAtPosition(position).toString();
    //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0)
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_peringatan_dini_gunung, menu);
        return true;
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

        if (id == R.id.action_settings)
        {
            Intent i = new Intent(peringatan_dini_gunung.this, MainActivity.class);
            startActivity(i);
            peringatan_dini_gunung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Intent i = new Intent(peringatan_dini_gunung.this, peringatan_dini_peralatan_individu.class);
        startActivity(i);
        peringatan_dini_gunung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent i = new Intent(peringatan_dini_gunung.this, peringatan_dini_peralatan_individu.class);
        startActivity(i);
        peringatan_dini_gunung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
