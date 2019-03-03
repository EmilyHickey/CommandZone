package com.example.redmajesty.commandzone.App;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.redmajesty.commandzone.AbstractClasses.Card;
import com.example.redmajesty.commandzone.AbstractClasses.CardList;
import com.example.redmajesty.commandzone.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandList extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    CardList commanders = new CardList();
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<String>> listDataChild;
    Boolean bollean = true;
    int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_list);
        final TextView title = (TextView) findViewById(R.id.txtTitle);
        title.setText("Loading Commanders...");
        expListView = (ExpandableListView) findViewById(R.id.Comm);
        final ArrayList<Card> cards = commanders.makeCommanders();
        Log.d("My App", "Commanders compiled");
        while(cards.size() < 40){

        }
        prepareListData(cards);
        listAdapter = new com.example.redmajesty.commandzone.AbstractClasses.ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

    }

    private void prepareListData(ArrayList<Card> cards) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<String>>();
        ArrayList<String> numberLoaded = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++){
            listDataHeader.add(cards.get(i).getName());
            listDataChild.put(listDataHeader.get(i), cards.get(i).getCardDeats());
        }

    }


}

