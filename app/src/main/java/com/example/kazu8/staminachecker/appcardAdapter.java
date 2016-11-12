package com.example.kazu8.staminachecker;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class appcardAdapter extends ArrayAdapter<appcard>{
    List<appcard> mCards;

    public appcardAdapter(Context context, int  layoutResorseId, List<appcard> oblects){
        super(context, layoutResorseId, oblects);
        mCards = oblects;
    }

    @Override
    public int getCount(){
        return mCards.size();
    }

    @Override
    public appcard getItem(int position){
        return mCards.get(position);
    }

    public Drawable getIcon(int position){

        final appcard item = getItem(position);
        Drawable iconView = item.icon;
        return iconView;
    }

    public String getName(int position){
        final appcard item = getItem(position);
        String appname = item.appname;
        return appname;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.appcard,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final appcard mitem =getItem(position);

        viewHolder.iconImageView.setImageDrawable(mitem.icon);
        viewHolder.appnameTextView.setText(mitem.appname);

        return convertView;
    }

    private class ViewHolder {
        ImageView iconImageView;
        TextView appnameTextView;

        public ViewHolder(View view) {
            iconImageView = (ImageView)view.findViewById(R.id.appiconImage);
            appnameTextView = (TextView)view.findViewById(R.id.apptitleText);
        }
    }
}
