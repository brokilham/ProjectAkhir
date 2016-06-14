package com.agung.ilham.monsilva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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


public class  provinsi_gunung extends AppCompatActivity
{

    provinsi_expandable_list_adapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> Aceh = new ArrayList();
    List<String> Bali = new ArrayList();
    List<String> BangkaBelitung = new ArrayList();
    List<String> Banten = new ArrayList();
    List<String> Bengkulu = new ArrayList();
    List<String> DIYogyakarta = new ArrayList();
    List<String> DKIJakarta = new ArrayList();
    List<String> Gorontalo = new ArrayList();
    List<String> Jambi = new ArrayList();
    List<String> JawaBarat = new ArrayList();
    List<String> JawaTengah = new ArrayList();
    List<String> JawaTimur = new ArrayList();
    List<String> KalimantanBarat = new ArrayList();
    List<String> KalimantanSelatan = new ArrayList();
    List<String> KalimantanTengah = new ArrayList();
    List<String> KalimantanTimur = new ArrayList();
    List<String> Lampung = new ArrayList();
    List<String> Maluku = new ArrayList();
    List<String> MalukuUtara = new ArrayList();
    List<String> NTB= new ArrayList();
    List<String> NTT = new ArrayList();
    List<String> Papua = new ArrayList();
    List<String> PapuaBarat = new ArrayList();
    List<String> Riau = new ArrayList();
    List<String> RiauKepulauan = new ArrayList();
    List<String> SulawesiBarat= new ArrayList();
    List<String> SulawesiSelatan= new ArrayList();
    List<String> SulawesiTengah = new ArrayList();
    List<String> SulawesiTenggara = new ArrayList();
    List<String> SulawesiUtara = new ArrayList();
    List<String> SumateraBarat = new ArrayList();
    List<String> SumateraSelatan = new ArrayList();
    List<String> SumateraUtara = new ArrayList();
    Button ButtonCobaLagi;
    static int heightScreen;
    CircleProgressBar progressBar;
    String TAG = "MyTag";
    RequestQueue requestQueue;
    StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinsi_gunung);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        ButtonCobaLagi = (Button) findViewById(R.id.CobaLagi);

        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        heightScreen = displaymetrics.heightPixels;
        ButtonCobaLagi.setVisibility(View.GONE);
        expListView.setVisibility(View.GONE);
        ButtonCobaLagi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ButtonCobaLagi.setVisibility(View.GONE);
                if(MainActivity.checkMountainProvinceList == 0)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    GetDataListGunung();

                }
                else if(MainActivity.checkMountainProvinceList !=0)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    readcache();
                }


            }
        });

        prepareListData();
        listAdapter = new provinsi_expandable_list_adapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if(MainActivity.checkMountainProvinceList == 0)
            {
                progressBar.setVisibility(View.VISIBLE);
                GetDataListGunung();

            }
            else if(MainActivity.checkMountainProvinceList !=0)
            {
                progressBar.setVisibility(View.VISIBLE);
                readcache ();
            }

        }
        else
        {

            if(MainActivity.checkMountainProvinceList !=0)
            {
                ToastTidakAdaKoneksi();
                progressBar.setVisibility(View.VISIBLE);
                readcache ();
            }
            else
            {
                expListView.setVisibility(View.GONE);
                ToastTidakAdaKoneksi();
                ButtonCobaLagi.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }



        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id)
            {

                final String item = (String) listAdapter.getChild(groupPosition, childPosition);
                if(item != "Data kosong")
                {
                    Intent page = new Intent (provinsi_gunung.this, provinsi_menu_gunung_detail.class);
                    page.putExtra("item",item);
                    startActivity(page);

                }
                return true;
            }
        });

    }

    private void GetDataListGunung()
    {

        String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Judul_Gunung.php";
        stringRequest = new StringRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        File file;
                        FileOutputStream outputStream;

                        try
                        {
                            file = new File(getCacheDir(), "MyCacheMountain");

                            outputStream = new FileOutputStream(file);
                            outputStream.write(response.getBytes());
                            outputStream.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        readcache ();
                        MainActivity.checkMountainProvinceList = MainActivity.checkMountainProvinceList+1;

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
                }){
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

            file = new File(getCacheDir(), "MyCacheMountain");
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

            for(int i = 0; i<jsonMainNode.length();i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String MountainId = jsonChildNode.optString("Mountain_Id");
                String Mountain_Name = jsonChildNode.optString("Mountain_Name");
                String Mountain_Province = jsonChildNode.optString("Mountain_Province");

                if(Mountain_Province.equalsIgnoreCase("Aceh"))
                {

                    Aceh.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Bali"))
                {

                    Bali.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Bangka Belitung"))
                {

                    BangkaBelitung.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Banten"))
                {

                    Banten.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Bengkulu"))
                {

                    Bengkulu.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("DI Yogyakarta"))
                {

                    DIYogyakarta.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("DKI Jakarta"))
                {

                    DKIJakarta.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Gorontalo"))
                {

                    Gorontalo.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Jambi"))
                {

                    Jambi.add(Mountain_Name);

                }else  if(Mountain_Province.equalsIgnoreCase("Jawa Barat"))
                {

                    JawaBarat.add(Mountain_Name);

                }else  if(Mountain_Province.equalsIgnoreCase("Jawa Tengah"))
                {

                    JawaTengah.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Jawa Timur"))
                {

                    JawaTimur.add(Mountain_Name);

                }else  if(Mountain_Province.equalsIgnoreCase("Kalimantan Barat"))
                {

                    KalimantanBarat.add(Mountain_Name);

                }else  if(Mountain_Province.equalsIgnoreCase("Kalimantan Selatan"))
                {

                    KalimantanSelatan.add(Mountain_Name);

                }else  if(Mountain_Province.equalsIgnoreCase("Kalimantan Tengah"))
                {

                    KalimantanTengah.add(Mountain_Name);

                }else  if(Mountain_Province.equalsIgnoreCase("Kalimantan Timur"))
                {

                    KalimantanTimur.add(Mountain_Name);

                }else  if(Mountain_Province.equalsIgnoreCase("Lampung"))
                {

                    Lampung.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Maluku"))
                {

                    Maluku.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Maluku Utara"))
                {

                    MalukuUtara.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Nusa Tenggara Barat"))
                {

                    NTB.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Nusa Tenggara Timur"))
                {

                    NTT.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Papua"))
                {

                    Papua.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Papua Barat"))
                {

                    PapuaBarat.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Riau"))
                {

                    Riau.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Riau Kepulauan"))
                {

                    RiauKepulauan.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Sulawesi Barat"))
                {

                    SulawesiBarat.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Sulawesi Selatan"))
                {

                    SulawesiSelatan.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Sulawesi Tengah"))
                {

                    SulawesiTengah.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Sulawesi Tenggara"))
                {

                    SulawesiTenggara.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Sulawesi Utara"))
                {

                    SulawesiUtara.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Sumatera Barat"))
                {

                    SumateraBarat.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Sumatera Selatan"))
                {

                    SumateraSelatan.add(Mountain_Name);

                }
                else  if(Mountain_Province.equalsIgnoreCase("Sumatera Utara"))
                {

                    SumateraUtara.add(Mountain_Name);

                }


                if(i == jsonMainNode.length()-1)
                {

                     if(Aceh.isEmpty())
                     {

                         Aceh.add("Data kosong");
                     }
                     if(Bali.isEmpty())
                    {

                        Bali.add("Data kosong");
                    }
                     if(BangkaBelitung.isEmpty())
                     {

                         BangkaBelitung.add("Data kosong");
                     }
                     if(Banten.isEmpty())
                     {

                         Banten.add("Data kosong");
                     }
                     if(Bengkulu.isEmpty())
                     {

                         Bengkulu.add("Data kosong");
                     }
                     if(DIYogyakarta.isEmpty())
                     {

                         DIYogyakarta.add("Data kosong");
                     }
                     if(DKIJakarta.isEmpty())
                     {

                         DKIJakarta.add("Data kosong");
                     }
                     if(Gorontalo.isEmpty())
                     {

                         Gorontalo.add("Data kosong");
                     }
                     if(Jambi.isEmpty())
                     {

                         Jambi.add("Data kosong");
                     }
                     if(JawaBarat.isEmpty())
                     {

                         JawaBarat.add("Data kosong");
                     }
                     if(JawaTengah.isEmpty())
                     {

                         JawaTengah.add("Data kosong");
                     }
                     if(JawaTimur.isEmpty())
                     {

                         JawaTimur.add("Data kosong");
                     }

                     if(KalimantanBarat.isEmpty())
                     {

                         KalimantanBarat.add("Data kosong");
                     }


                     if(KalimantanSelatan.isEmpty())
                     {

                         KalimantanSelatan.add("Data kosong");
                     }

                     if(KalimantanTengah.isEmpty())
                     {

                         KalimantanTengah.add("Data kosong");
                     }
                     if(KalimantanTimur.isEmpty())
                     {

                         KalimantanTimur.add("Data kosong");
                     }
                     if(Lampung.isEmpty())
                     {

                         Lampung.add("Data kosong");
                     }
                     if(Maluku.isEmpty())
                     {

                         Maluku.add("Data kosong");
                     }
                     if(MalukuUtara.isEmpty())
                     {

                         MalukuUtara.add("Data kosong");
                     }
                     if(NTB.isEmpty())
                     {

                         NTB.add("Data kosong");
                     }
                     if(NTT.isEmpty())
                     {

                         NTT.add("Data kosong");
                     }
                     if(Papua.isEmpty())
                     {

                         Papua.add("Data kosong");
                     }
                     if(PapuaBarat.isEmpty())
                     {

                         PapuaBarat.add("Data kosong");
                     }
                     if(Riau.isEmpty())
                     {

                         Riau.add("Data kosong");
                     }
                     if(RiauKepulauan.isEmpty())
                     {

                         RiauKepulauan.add("Data kosong");
                     }
                     if(SulawesiBarat.isEmpty())
                     {

                         SulawesiBarat.add("Data kosong");
                     }
                     if(SulawesiSelatan.isEmpty())
                     {

                         SulawesiSelatan.add("Data kosong");
                     }
                     if(SulawesiTengah.isEmpty())
                     {

                         SulawesiTengah.add("Data kosong");
                     }
                     if(SulawesiTenggara.isEmpty())
                     {

                         SulawesiTenggara.add("Data kosong");
                     }
                     if(SulawesiUtara.isEmpty())
                     {

                         SulawesiUtara.add("Data kosong");
                     }
                     if(SumateraBarat.isEmpty())
                     {

                         SumateraBarat.add("Data kosong");
                     }
                     if(SumateraSelatan.isEmpty())
                     {

                         SumateraSelatan.add("Data kosong");
                     }
                     if(SumateraUtara.isEmpty())
                     {

                         SumateraUtara.add("Data kosong");
                     }
                }
                expListView.setVisibility(View.VISIBLE);

            }
        }
        catch(JSONException e)
        {
            expListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.INVISIBLE);
            ToastGagal();
            ButtonCobaLagi.setVisibility(View.VISIBLE);
         }

        progressBar.setVisibility(View.INVISIBLE);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Aceh");
        listDataHeader.add("Bali");
        listDataHeader.add("Bangka Belitung");
        listDataHeader.add("Banten");
        listDataHeader.add("Bengkulu");
        listDataHeader.add("DI Yogyakarta");
        listDataHeader.add("DKI Jakarta");
        listDataHeader.add("Gorontalo");
        listDataHeader.add("Jambi");
        listDataHeader.add("Jawa Barat");
        listDataHeader.add("Jawa Tengah");
        listDataHeader.add("Jawa Timur");
        listDataHeader.add("Kalimantan Barat");
        listDataHeader.add("Kalimantan Selatan");
        listDataHeader.add("Kalimantan Tengah");
        listDataHeader.add("Kalimantan Timur");
        listDataHeader.add("Lampung");
        listDataHeader.add("Maluku");
        listDataHeader.add("Maluku Utara");
        listDataHeader.add("Nusa Tenggara Barat");
        listDataHeader.add("Nusa Tenggara Timur");
        listDataHeader.add("Papua");
        listDataHeader.add("Papua Barat");
        listDataHeader.add("Riau");
        listDataHeader.add("Riau Kepulauan");
        listDataHeader.add("Sulawesi Barat");
        listDataHeader.add("Sulawesi Selatan");
        listDataHeader.add("Sulawesi Tengah");
        listDataHeader.add("Sulawesi Tenggara");
        listDataHeader.add("Sulawesi Utara");
        listDataHeader.add("Sumatera Barat");
        listDataHeader.add("Sumatera Selatan");
        listDataHeader.add("Sumatera Utara");

        listDataChild.put(listDataHeader.get(0), Aceh); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Bali);
        listDataChild.put(listDataHeader.get(2), BangkaBelitung);
        listDataChild.put(listDataHeader.get(3), Banten);
        listDataChild.put(listDataHeader.get(4), Bengkulu);
        listDataChild.put(listDataHeader.get(5), DIYogyakarta);
        listDataChild.put(listDataHeader.get(6), DKIJakarta);
        listDataChild.put(listDataHeader.get(7), Gorontalo);
        listDataChild.put(listDataHeader.get(8), Jambi);
        listDataChild.put(listDataHeader.get(9), JawaBarat); // Header, Child data
        listDataChild.put(listDataHeader.get(10), JawaTengah);
        listDataChild.put(listDataHeader.get(11), JawaTimur);
        listDataChild.put(listDataHeader.get(12), KalimantanBarat);
        listDataChild.put(listDataHeader.get(13), KalimantanSelatan);
        listDataChild.put(listDataHeader.get(14), KalimantanTengah);
        listDataChild.put(listDataHeader.get(15), KalimantanTimur);
        listDataChild.put(listDataHeader.get(16), Lampung);
        listDataChild.put(listDataHeader.get(17), Maluku);
        listDataChild.put(listDataHeader.get(18), MalukuUtara); // Header, Child data
        listDataChild.put(listDataHeader.get(19), NTB);
        listDataChild.put(listDataHeader.get(20), NTT);
        listDataChild.put(listDataHeader.get(21), Papua);
        listDataChild.put(listDataHeader.get(22), PapuaBarat);
        listDataChild.put(listDataHeader.get(23), Riau);
        listDataChild.put(listDataHeader.get(24), RiauKepulauan);
        listDataChild.put(listDataHeader.get(25), SulawesiBarat);
        listDataChild.put(listDataHeader.get(26), SulawesiSelatan);
        listDataChild.put(listDataHeader.get(27), SulawesiTengah);
        listDataChild.put(listDataHeader.get(28), SulawesiTenggara);
        listDataChild.put(listDataHeader.get(29), SulawesiUtara);
        listDataChild.put(listDataHeader.get(30), SumateraBarat);
        listDataChild.put(listDataHeader.get(31), SumateraSelatan);
        listDataChild.put(listDataHeader.get(32), SumateraUtara);

    }

    protected void onStop () {
        super.onStop();
        if (requestQueue != null)
        {
            requestQueue.cancelAll(TAG);
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_provinsi_gunung, menu);
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
        Intent i = new Intent(provinsi_gunung.this, MainActivity.class);
        startActivity(i);
        provinsi_gunung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent i = new Intent(provinsi_gunung.this, MainActivity.class);
        startActivity(i);
        provinsi_gunung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }
}
