package com.example.kazu8.staminachecker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class app_list_Fragment extends DialogFragment {


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
                Log.d("a", "a");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
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


