package com.agung.ilham.monsilva;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class MainActivity3 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3);


        ArrayList<LatLng> coordinates = getIntent().getParcelableArrayListExtra("coordinates");
        if(coordinates.isEmpty())
        {
            for (int i = 0; i < coordinates.size(); i++) {
                LatLng point = coordinates.get(i);
                Toast.makeText(getBaseContext(),String.valueOf(point) ,Toast.LENGTH_LONG).show();
            }


        }
        else
        {
            for (int i = 0; i < coordinates.size(); i++) {
                LatLng point = coordinates.get(i);
                Toast.makeText(getBaseContext(),String.valueOf(point) ,Toast.LENGTH_LONG).show();


            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
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
