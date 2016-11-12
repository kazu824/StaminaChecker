package com.example.kazu8.staminachecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    public static ListView listView;
    public static List<card> mCards;
    public  static CardAdapter mCardAdapter;

    public static TreeSet<String> wordset;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editer;

    public static Drawable mainicon;
    public static String mainname;
    public static Boolean listCheck;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainicon = null;
        mainname = null;
        listCheck = false;

        count = 0;


        listView = (ListView)findViewById(R.id.mainlist);
        mCards = new ArrayList<card>();

        wordset = new TreeSet<String>();
        pref = getSharedPreferences("stamina",MODE_PRIVATE);
        editer = pref.edit();


        wordset.addAll(pref.getStringSet("wordSet",wordset));
        for(String word : wordset){
            //mCards.add(new card());
            count++;
        }

        mCards.add(new card("パズドラ",100,5,0,true,50));
        mCards.add(new card("モンスト",134,5,5000,false,40));
        mCards.add(new card("クラロワ",3,120,7999,true,2));
        mCards.add(new card("デレステ",76,5,4888,false,50));


        mCardAdapter = new CardAdapter(this,R.layout.card, mCards);
        listView.setAdapter(mCardAdapter);


    }

    public void newbutton(View v){
        DialogFragment dialogFragment = new newCreate_Fragment();
        dialogFragment.show(getFragmentManager(),"選択肢表示");
    }

    public static class app_list_Fragment extends DialogFragment {


        ListView applist;
        List<appcard> appmCards;
        appcardAdapter appmCardAdapter;
        Drawable iconView;
        String appnameText;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.appdialog_layout, null);

            builder.setView(content);

            applist = (ListView) content.findViewById(R.id.applist);

            appmCards = new ArrayList<appcard>();

            PackageManager pm = this.getActivity().getPackageManager();
            final List<ApplicationInfo> appInfoList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            for (ApplicationInfo ai : appInfoList) {

                Drawable icon = ai.loadIcon(pm);
                String appname = ai.loadLabel(pm).toString();
                appmCards.add(new appcard(icon, appname));

            }

            appmCardAdapter = new appcardAdapter(this.getActivity(), R.layout.card, appmCards);
            applist.setAdapter(appmCardAdapter);
            Log.d("確認", "mae");
            applist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    iconView = appmCardAdapter.getIcon(position);
                    appnameText = appmCardAdapter.getName(position);
                    MainActivity.mainicon = iconView;
                    MainActivity.mainname = appnameText;
                    MainActivity.listCheck = true;
                    Log.d("a", "a");
                    onDestroyView();
                    DialogFragment dialogFragment = new NewDialogFragment();
                    dialogFragment.show(getFragmentManager(),"新規作成");
                }
            });

            builder.setTitle("新規追加");
            builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            return builder.create();
        }
    }

    public static class NewDialogFragment extends DialogFragment {

        ImageButton nbutton;
        EditText nrEdit;
        EditText nmEdit;
        EditText nmtitleEdit;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.editdialog_layout, null);

            builder.setView(content);

            nbutton = (ImageButton) content.findViewById(R.id.iconButton);
            nmtitleEdit = (EditText)content.findViewById(R.id.titleEdit);
            nrEdit = (EditText) content.findViewById(R.id.rEdit);
            nmEdit = (EditText) content.findViewById(R.id.mEdit);

            if(MainActivity.listCheck == true){
                nbutton.setImageDrawable(MainActivity.mainicon);
                nmtitleEdit.setText(MainActivity.mainname + "");
            }
            MainActivity.listCheck = false;
            MainActivity.mainname = null;
            MainActivity.mainicon = null;

            builder.setTitle("新規作成");
            builder.setPositiveButton("完了", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            return builder.create();

        }
    }

    public static class newCreate_Fragment extends DialogFragment {

        Button applistButton;
        Button selfButoon;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.createlayout, null);

            applistButton = (Button)content.findViewById(R.id.button3);
            applistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDestroyView();
                    DialogFragment dialogFragment = new MainActivity.app_list_Fragment();
                    dialogFragment.show(getFragmentManager(),"アプリ取得");
                }
            });
            selfButoon = (Button)content.findViewById(R.id.button2);
            selfButoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDestroyView();
                    DialogFragment dialogFragment = new NewDialogFragment();
                    dialogFragment.show(getFragmentManager(),"新規作成");
                }
            });
            builder.setView(content);


            return builder.create();
        }
    }

    public static class EditDialogFragment extends DialogFragment {

        ImageButton button;
        EditText rEdit;
        EditText mEdit;
        EditText mtitleEdit;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.editdialog_layout, null);

            builder.setView(content);

            button = (ImageButton)content.findViewById(R.id.iconButton);
            mtitleEdit =(EditText)content.findViewById(R.id.titleEdit);
            rEdit = (EditText)content.findViewById(R.id.rEdit);
            mEdit = (EditText)content.findViewById(R.id.mEdit);

            if(MainActivity.listCheck == true){
                button.setImageDrawable(MainActivity.mainicon);
                mtitleEdit.setText(MainActivity.mainname + "");
            }
            MainActivity.listCheck = false;
            MainActivity.mainname = null;
            MainActivity.mainicon = null;




            builder.setTitle("編集");
            builder.setPositiveButton("完了", new DialogInterface.OnClickListener(){
                public  void onClick(DialogInterface dialog,int id){

                }
            });
            builder.setNegativeButton("削除", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            return builder.create();

        }
    }

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
                        DialogFragment dialogFragment = new MainActivity.EditDialogFragment();
                        dialogFragment.show(getFragmentManager(),"設定");
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

}
