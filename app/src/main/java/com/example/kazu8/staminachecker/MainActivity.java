package com.example.kazu8.staminachecker;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<card> mCards;
    CardAdapter mCardAdapter;

    TreeSet<String> wordset;
    SharedPreferences pref;
    SharedPreferences.Editor editer;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;

        listView = (ListView)findViewById(R.id.listView);
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

        //DialogFragment newFragment = new NewDialogFlagment();
        //newFragment.show(getFragmentManager(),"新規追加");

        //DialogFragment editFragment = new EditDialogFlagment();
        //editFragment.show(getFragmentManager(),"編集");
    }
}
