package com.net.ddns.suyashbakshi.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.net.ddns.suyashbakshi.popularmovies.R;

import java.util.List;

/**
 * Created by Suyash on 3/28/2016.
 */
public class TrailerViewAdapter extends ArrayAdapter<String> {


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String valueString = getItem(position);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_item_trailer,parent,false);
        }

        TextView trailerNameView = (TextView)convertView.findViewById(R.id.trailer_name_view);

        String[] split = valueString.split("/");

        String trailerName = split[1];
        trailerNameView.setText(trailerName);



        return convertView;
    }

    public TrailerViewAdapter(Context context, List<String> trailerString) {
        super(context, 0, trailerString);
    }
}
