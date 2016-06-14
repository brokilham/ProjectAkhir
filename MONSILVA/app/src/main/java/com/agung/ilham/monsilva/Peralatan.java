package com.agung.ilham.monsilva;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class Peralatan extends AppCompatActivity
{
    public static FragmentTabHost mTabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peralatan);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("Tim").setIndicator("Tim", null),
                peralatan_tim.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Individu").setIndicator("Individu", null),
                peralatan_individu.class, null);

        mTabHost.setCurrentTab(0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        if (id == R.id.hitungPeralatan)
        {
            Intent i = new Intent (Peralatan.this, peralatan_kalkulator_peralatan.class);
            startActivity(i);
            return true;
        }
        else
        {
            onBackPressed();

        }
        return super.onOptionsItemSelected(item);

    }



    public void onBackPressed ()
    {
        if(mTabHost.getCurrentTab() == 1)
        {
            mTabHost.setCurrentTab(0);
        }
        else
        {
            Intent page = new Intent(Peralatan.this, MainActivity.class);
            startActivity(page);
            Peralatan.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }


    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        if(mTabHost.getCurrentTab() == 1)
        {
            mTabHost.setCurrentTab(0);
        }
        else
        {
            Intent page = new Intent(Peralatan.this, MainActivity.class);
            startActivity(page);
            Peralatan.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } return  true;
    }
}
