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
 * Created by Suyash on 3/29/2016.
 */
public class ReviewViewAdapter extends ArrayAdapter<String> {


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String valueString = getItem(position);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_item_review,parent,false);
        }

        TextView reviewAuthorNameView = (TextView)convertView.findViewById(R.id.review_author_name);
        TextView reviewContentview = (TextView)convertView.findViewById(R.id.review_content);

        String[] split = valueString.split("/");

        reviewAuthorNameView.setText(split[0]);
        reviewContentview.setText(split[1]);



        return convertView;
    }

    public ReviewViewAdapter(Context context, List<String> reviewString) {
        super(context, 0, reviewString);
    }
}
