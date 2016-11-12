package com.example.kazu8.staminachecker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditDialogFragment extends DialogFragment {

    Button button;
    EditText rEdit;
    EditText mEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.editdialog_layout, null);

        builder.setView(content);

        button = (Button)content.findViewById(R.id.iconbutton);
        rEdit = (EditText)content.findViewById(R.id.rEdit);
        mEdit = (EditText)content.findViewById(R.id.mEdit);




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