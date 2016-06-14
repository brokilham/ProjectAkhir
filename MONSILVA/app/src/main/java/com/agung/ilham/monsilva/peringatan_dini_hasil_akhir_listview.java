package com.agung.ilham.monsilva;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ILHAM on 12/30/2015.
 */
public class peringatan_dini_hasil_akhir_listview extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public peringatan_dini_hasil_akhir_listview(Activity context,String[] web, Integer[] imageId)
    {
        super(context, R.layout.activity_peringatan_dini_hasil_akhir_listview_item, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_peringatan_dini_hasil_akhir_listview_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.CuacaHariIni);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
