package com.agung.ilham.monsilva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

/**
 * Created by ILHAM on 10/28/2015.
 */
public class peralatan_tim extends Fragment
{


    ListView listView;
    Button ButtonCobaLagi;
    ArrayList SelectedIdArray = new ArrayList();
    CircleProgressBar progressBar;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String TAG = "MyTag";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {   super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.activity_peralatan_tim, container, false);
        listView = (ListView)  v.findViewById(R.id.listViewPeralatanTim);
        progressBar = (CircleProgressBar) v.findViewById(R.id.progressBar);


        ButtonCobaLagi = (Button) v.findViewById(R.id.CobaLagi);
        ButtonCobaLagi.setVisibility(View.INVISIBLE);

        progressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);


        ButtonCobaLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ButtonCobaLagi.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);

                if(MainActivity.checkEquipmentPersonalTeam == 0)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    getListPeeralatanTim();
                }
                else if(MainActivity.checkEquipmentPersonalTeam !=0)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    readcache();

                }

            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {

            if(MainActivity.checkEquipmentPersonalTeam == 0)
            {

                progressBar.setVisibility(View.VISIBLE);
                getListPeeralatanTim();

            }

            else if(MainActivity.checkEquipmentPersonalTeam !=0)
            {

                progressBar.setVisibility(View.VISIBLE);
                readcache();

            }

        }
        else
        {


            if(MainActivity.checkEquipmentPersonalTeam !=0)
            {
                ToastTidakAdaKoneksi();
                progressBar.setVisibility(View.VISIBLE);
                readcache();

            }
            else
            {

                ToastTidakAdaKoneksi();
                ButtonCobaLagi.setVisibility(View.VISIBLE);

            }

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                String  PeralatanTimName = ((TextView)view).getText().toString();
                String  PeralatanTimIdName = (String) SelectedIdArray.get(position);

                Intent intent = new Intent(getActivity(), peralatan_tim_detail.class);
                getActivity().startActivity(intent);
                intent.putExtra("PeralatanName",PeralatanTimName);
                intent.putExtra("PeralatanId", PeralatanTimIdName);
                startActivity(intent);
            }
        });

        return v;
    }

   private void getListPeeralatanTim()
   {


       String REGISTER_URL = "http://tugas-akhir.hol.es/aplication/Judul_Peralatan_Tim.php";
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

                           file = new File(getActivity().getCacheDir(),"MyCacheTeamEquipment");

                           outputStream = new FileOutputStream(file);
                           outputStream.write(response.getBytes());
                           outputStream.close();
                       }
                       catch (IOException e)
                       {
                           e.printStackTrace();
                       }
                       MainActivity.checkEquipmentPersonalTeam=MainActivity.checkEquipmentPersonalTeam+1;
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
                       ButtonCobaLagi.setVisibility(View.VISIBLE);
                       //pDialog.dismiss();
                   }
               })
       {


       };

       stringRequest.setTag(TAG);
       requestQueue = Volley.newRequestQueue(getActivity());
       int socketTimeout = 30000;//30 seconds - change to what you want
       RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
       stringRequest.setRetryPolicy(policy);
       requestQueue.add(stringRequest);


   }

    private HashMap<String, String> createEquipmentTim(String name, String name1)
    {
        HashMap<String, String> EquipmentName = new HashMap<String, String>();
        EquipmentName.put(name, name1);
        return EquipmentName;
    }

    private void readcache ()
    {
        String line;
        BufferedReader input = null;
        File file = null;
        StringBuilder textfile = new StringBuilder();
        try
        {

            file = new File(getActivity().getCacheDir(), "MyCacheTeamEquipment");
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

        List<Map<String,String>> EquipmentList = new ArrayList<Map<String,String>>();
        JSONObject jsonResponse;

        try
        {
            jsonResponse = new JSONObject(textfile.toString());
            Log.d("RESPONSE----", jsonResponse.toString());

            JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

            for (int i = 0; i < jsonMainNode.length(); i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String id = jsonChildNode.optString("Equipment_Id");
                String name = jsonChildNode.optString("Equipment_Name");

                SelectedIdArray.add(id);
                EquipmentList.add(createEquipmentTim("result", name));

                //pDialog.dismiss();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        catch (JSONException e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), EquipmentList, android.R.layout.simple_list_item_1, new String[] {"result"}, new int[] {android.R.id.text1});
        listView.setAdapter(simpleAdapter);

    }

    private void ToastGagal()
    {
        Context context= getActivity().getApplicationContext();
        LayoutInflater inflater= getActivity().getLayoutInflater();
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
        Context context= getActivity().getApplicationContext();
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
    public void onStop () {
        super.onStop();
        if (requestQueue != null)
        {
            requestQueue.cancelAll(TAG);
        }

    }
}

