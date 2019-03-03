package com.example.redmajesty.commandzone.App;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.redmajesty.commandzone.R;

public class HomeScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final Button CommList = (Button) findViewById(R.id.CommandList);
        CommList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeScreen.this, CommandList.class);
                HomeScreen.this.startActivity(myIntent);
            }
        });

        final Button CommSearch = (Button) findViewById(R.id.CommanderSearch);
        CommSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeScreen.this, CommanderLookUp.class);
                HomeScreen.this.startActivity(myIntent);
            }
        });
    }
}
