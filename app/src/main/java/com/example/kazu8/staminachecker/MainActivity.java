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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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

    public static String bitmapStr;
    public static String titletext;

    public static Boolean nullcheck;

    public static int nrint;
    public static int nmint;

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
        nullcheck = false;

        count = 0;

        listView = (ListView)findViewById(R.id.mainlist);
        mCards = new ArrayList<card>();

        wordset = new TreeSet<String>();
        pref = getSharedPreferences("stamina",MODE_PRIVATE);
        editer = pref.edit();


        wordset.addAll(pref.getStringSet("wordSet",wordset));
        for(String word : wordset){
            mCards.add(new card(pref.getString(word + "1357icon",""),word,pref.getInt(word + "2468max",0),pref.getInt(word + "3579re",0),pref.getInt(word + "4680se",0),pref.getBoolean(word + "5791al",false),pref.getInt(word + "6802alt",0)));
            count++;
        }



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
            final View content = inflater.inflate(R.layout.editdialog_layout, null);

            builder.setView(content);

            nbutton = (ImageButton) content.findViewById(R.id.iconButton);
            nmtitleEdit = (EditText)content.findViewById(R.id.titleEdit);
            nrEdit = (EditText) content.findViewById(R.id.rEdit);
            nmEdit = (EditText) content.findViewById(R.id.mEdit);

            if(MainActivity.listCheck == true){
                nbutton.setImageDrawable(MainActivity.mainicon);
                nmtitleEdit.setText(MainActivity.mainname + "");
            }
            else {
                nbutton.setImageResource(R.mipmap.ic_launcher);
            }


            builder.setTitle("新規作成");
            builder.setPositiveButton("完了", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    titletext = nmtitleEdit.getText().toString();
                    if(titletext.equals("")){
                        nullcheck = true;
                    }
                    String nrst = nrEdit.getText().toString();
                    try {
                        nrint = Integer.parseInt(nrst);
                    }catch (Exception e){
                        nullcheck = true;
                    }
                    String nmst = nmEdit.getText().toString();
                    try {
                        nmint = Integer.parseInt(nmst);
                    }catch (Exception e){
                        nullcheck = true;
                    }
                    if(nrint > 1440){
                        nullcheck = true;
                    }
                    if(nmint > 999){
                        nullcheck = true;
                    }
                    if(nullcheck == true){
                        Toast.makeText(getActivity(),"正しい値が入力されていません", Toast.LENGTH_SHORT).show();
                        DialogFragment dialogFragment = new NewDialogFragment();
                        dialogFragment.show(getFragmentManager(),"選択肢表示");
                    }
                    if(nullcheck == false){
                        MainActivity.listCheck = false;
                        MainActivity.mainname = null;
                        MainActivity.mainicon = null;
                        nullcheck = false;
                        Drawable drawable = nbutton.getDrawable();
                        Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        bitmapStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                        editer.putString(titletext + "1357icon",bitmapStr);
                        wordset.add(titletext);
                        editer.putStringSet("wordSet",wordset);
                        editer.putInt(titletext + "2468max",nmint);
                        editer.putInt(titletext + "3579re",nrint);
                        editer.putInt(titletext + "4680se",0);
                        editer.putBoolean(titletext + "5791al",false);
                        editer.putInt(titletext + "6802alt",nmint);
                        editer.commit();
                        mCards.add(new card(bitmapStr,titletext,nmint,nrint,0,false,nmint));

                    }
                    nullcheck = false;
                }
            });
            builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.listCheck = false;
                    MainActivity.mainname = null;
                    MainActivity.mainicon = null;
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
                if (!item.iconstr.equals("")) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    byte[] b = Base64.decode(item.iconstr, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length).copy(Bitmap.Config.ARGB_8888, true);
                    viewHolder.iconView.setImageBitmap(bmp);
                }
                else{
                    viewHolder.iconView.setImageResource(R.mipmap.ic_launcher);
                }
                viewHolder.titleView.setText(item.title);
                viewHolder.rtView.setText(item.alertTime + "　まで残り" + hour[position] + "時間 " + minute[position] + "分 " + second[position] + "秒");
                viewHolder.seekBar.setProgress(item.sTime / 60 / item.rTime);
                viewHolder.seekBar.setMax(item.maxNum);
                viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        item.sTime = progress * 60 * item.rTime;
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

                                    int Spinernum = item.sTime - (item.alertTime*60*item.rTime);
                                    if(Spinernum > 0){
                                        item.alertTime = item.maxNum;
                                        handler[position].post(new Runnable() {
                                            @Override
                                            public void run() {
                                                viewHolder.spinner.setSelection(item.alertTime - 1);
                                            }
                                        });
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

                            int Spinernum = item.sTime - (item.alertTime*60*item.rTime);
                            if(Spinernum > 0){
                                item.alertTime = item.maxNum;
                                handler[position].post(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewHolder.spinner.setSelection(item.alertTime - 1);
                                    }
                                });
                            }

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
                for (int i = 1; i <= item.maxNum; i++){
                    arrayAdapter[position].add(i);
                }
                viewHolder.spinner.setAdapter(arrayAdapter[position]);
                viewHolder.spinner.setSelection(item.alertTime - 1);
                viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int Spinernum = (item.sTime / 60 / item.rTime) - (position + 1);
                        if(Spinernum > 0){
                            Toast.makeText(getContext(),(item.sTime / 60 / item.rTime) + "以上を入力してください", Toast.LENGTH_SHORT).show();
                            item.alertTime = item.maxNum;
                            viewHolder.spinner.setSelection(item.alertTime - 1);
                        }
                        if(Spinernum <= 0){
                            item.alertTime = position + 1;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                viewHolder.swicher.setChecked(item.alertCheck);

            }

            return convertView;
        }





        private class ViewHolder{
            ImageView iconView;
            TextView titleView;
            TextView rtView;
            SeekBar seekBar;
            TextView countView;
            Button editButton;
            Spinner spinner;
            Switch swicher;

            public ViewHolder(View v){
                iconView = (ImageView)v.findViewById(R.id.iconView);
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
