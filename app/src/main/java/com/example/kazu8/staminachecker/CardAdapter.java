package com.example.kazu8.staminachecker;


import android.app.DialogFragment;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.app.DialogFragment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CardAdapter extends ArrayAdapter<card> {
    List<card> mCards;

    Context mContext;

    int hour[] = new int[98];
    int minute[] = new int[98];
    int second[] = new int[98];

    public Handler handler[] = new Handler[98];
    public Timer timer[] = new Timer[98];

    public ArrayAdapter<Integer> arrayAdapter[] = new ArrayAdapter[1000];

    public  CardAdapter(Context context, int  layoutResorseId, List<card> oblects){
        super(context, layoutResorseId, oblects);
        mCards = oblects;
        mContext = context;

    }



    @Override
    public int getCount(){
        return mCards.size();
    }

    @Override
    public card getItem(int position){
        return mCards.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder viewHolder;


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final card item =getItem(position);

        if (item != null){
            arrayAdapter[position] = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item);
            hour[position] = ((item.rTime * item.alertTime * 60) - item.sTime) / 3600;
            minute[position] = (((item.rTime * item.alertTime * 60) - item.sTime) / 60) % 60;
            second[position] = ((item.rTime * item.alertTime * 60) - item.sTime) % 60;
            viewHolder.titleView.setText(item.title);
            viewHolder.rtView.setText(item.alertTime + "　まで残り" + hour[position] + "時間 " + minute[position] + "分 " + second[position] + "秒");
            viewHolder.seekBar.setProgress(item.sTime / 60 / item.rTime);
            viewHolder.seekBar.setMax(item.maxNum);
            viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    item.sTime = progress * 60 * item.rTime;
                    hour[position] = ((item.rTime * item.alertTime * 60) - item.sTime) / 3600;
                    minute[position] = (((item.rTime * item.alertTime * 60) - item.sTime) / 60) % 60;
                    second[position] = ((item.rTime * item.alertTime * 60) - item.sTime) % 60;
                    viewHolder.countView.setText((item.sTime / 60 / item.rTime ) + "/" + item.maxNum);
                    viewHolder.rtView.setText(item.alertTime +"　まで残り" + hour[position] + "時間 " + minute[position] + "分 " + second[position] + "秒");
                    int sCheck = (item.maxNum * item.rTime * 60) - item.sTime;
                    if(sCheck <= 0){
                        item.sTime = item.maxNum * item.rTime * 60;
                        timer[position].cancel();
                    }
                    if(sCheck > 0){
                        timer[position].cancel();
                        timer[position] = new Timer();
                        handler[position] = new Handler();
                        timer[position].schedule(new TimerTask() {
                            @Override
                            public void run() {
                                int sCheck = (item.maxNum * item.rTime * 60) - item.sTime;
                                if(sCheck <= 0){
                                    item.sTime = item.maxNum * item.rTime * 60;
                                    timer[position].cancel();
                                }

                                item.sTime = item.sTime + 1;
                                hour[position] = ((item.rTime * item.alertTime * 60) - item.sTime) / 3600;
                                minute[position] = (((item.rTime * item.alertTime * 60) - item.sTime) / 60) % 60;
                                second[position] = ((item.rTime * item.alertTime * 60) - item.sTime) % 60;


                                handler[position].post(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewHolder.countView.setText((item.sTime / 60 / item.rTime ) + "/" + item.maxNum);
                                        viewHolder.rtView.setText(item.alertTime +"　まで残り" + hour[position] + "時間 " + minute[position] + "分 " + second[position] + "秒");
                                        viewHolder.seekBar.setProgress(item.sTime / 60 / item.rTime);
                                    }
                                });
                            }
                        },0,1000);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            viewHolder.countView.setText((item.sTime / 60 / item.rTime) + "/" + item.maxNum);
            viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            if(timer[position] == null){
                timer[position] = new Timer();
                handler[position] = new Handler();
                timer[position].schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int sCheck = (item.maxNum * item.rTime * 60) - item.sTime;
                        if(sCheck <= 0){
                            item.sTime = item.maxNum * item.rTime * 60;
                            timer[position].cancel();
                        }

                        item.sTime = item.sTime + 1;
                        hour[position] = ((item.rTime * item.alertTime * 60) - item.sTime) / 3600;
                        minute[position] = (((item.rTime * item.alertTime * 60) - item.sTime) / 60) % 60;
                        second[position] = ((item.rTime * item.alertTime * 60) - item.sTime) % 60;



                        handler[position].post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.countView.setText((item.sTime / 60 / item.rTime ) + "/" + item.maxNum);
                                viewHolder.rtView.setText(item.alertTime +"　まで残り" + hour[position] + "時間 " + minute[position] + "分 " + second[position] + "秒");
                                viewHolder.seekBar.setProgress(item.sTime / 60 / item.rTime);
                                viewHolder.spinner.setSelection(item.alertTime - 1);
                            }
                        });
                    }
                },0,1000);
            }
        }

        for (int i = 1; i <= item.maxNum; i++){
            arrayAdapter[position].add(i);
        }
        viewHolder.spinner.setAdapter(arrayAdapter[position]);
        viewHolder.spinner.setSelection(item.alertTime - 1);
        viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.alertTime = position + 1;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewHolder.swicher.setChecked(item.alertCheck);
        return convertView;
    }





    private class ViewHolder{
        TextView titleView;
        TextView rtView;
        SeekBar seekBar;
        TextView countView;
        Button editButton;
        Spinner spinner;
        Switch swicher;

        public ViewHolder(View v){
            titleView = (TextView)v.findViewById(R.id.titleText);
            rtView = (TextView)v.findViewById(R.id.rtText);
            seekBar = (SeekBar)v.findViewById(R.id.seekBar);
            countView = (TextView)v.findViewById(R.id.countText);
            editButton = (Button)v.findViewById(R.id.editButton);
            spinner = (Spinner)v.findViewById(R.id.spinner);
            swicher = (Switch)v.findViewById(R.id.switcher);
        }

    }
}
