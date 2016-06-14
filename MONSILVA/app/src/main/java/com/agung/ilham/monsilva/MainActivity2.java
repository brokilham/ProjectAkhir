package com.agung.ilham.monsilva;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class MainActivity2 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);
       /* ArrayList<LatLng> coordinates = new ArrayList<>();
        coordinates.add(new LatLng(66,55));
        coordinates.add(new LatLng(44,77));
        coordinates.add(new LatLng(11,99));
        // Put the coordinates

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("coordinates", coordinates);

        // Get the coordinates
       // coordinates = bundle.getParcelableArrayList("coordinates");

        Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
        intent.putExtras(bundle);
        startActivity(intent);*/

        //tenda();
    }

    private void tenda()
    {

        int JumlahPendaki = 10,JumlahTenda4 = 0,JumlahTenda3 = 0,JumlahTenda2 = 0,JumlahTenda1 = 0;
        do
        {
            if(JumlahPendaki>=4)
            {
                JumlahPendaki=JumlahPendaki-4;
                JumlahTenda4 = JumlahTenda4 + 1;


            }
            if(JumlahPendaki==3)
            {
                JumlahPendaki=JumlahPendaki-3;
                JumlahTenda3 = JumlahTenda3 + 1;


            }
            if(JumlahPendaki==2)
            {

                JumlahPendaki=JumlahPendaki-2;
                JumlahTenda2 = JumlahTenda2 + 1;


            }
            if(JumlahPendaki==1)
            {

                JumlahTenda1 = JumlahTenda1 + 1;

            }

        }
        while(JumlahPendaki>=1);
       // while(JumlahPendaki>=0);

        Toast.makeText(getBaseContext(), Integer.toString(JumlahTenda4) + " & " + Integer.toString(JumlahTenda3), Toast.LENGTH_LONG).show();
        Toast.makeText(getBaseContext(),Integer.toString(JumlahTenda2) +" & "+ Integer.toString(JumlahTenda1),Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
