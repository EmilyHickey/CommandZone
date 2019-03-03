package com.example.redmajesty.commandzone.App;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.redmajesty.commandzone.AbstractClasses.Card;
import com.example.redmajesty.commandzone.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CommanderLookUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander_look_up);
        final EditText searchBar = (EditText) findViewById(R.id.searchBar);
        final TextView wizard = (TextView) findViewById(R.id.txtWizard);
        final TextView body = (TextView) findViewById(R.id.Commander);
        final Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            if (searchBar.getText().length() < 1){
                wizard.setText("I may be a great Wizard, but not that great!");
            }else{
                Card lookedUp = cardLookup(searchBar.getText().toString());
                body.setText(lookedUp.toString());
                wizard.setText("Behold! Your new champion!");
            }
        }
    });
    }

    public Card cardLookup(String name) {
        final String cardName = name;
        final Card foundCard = new Card("");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Boolean has_more = false;
                try {
                    URL currentURL = new URL("https://api.scryfall.com/cards/search?q=is%3Acommander&unique=cards+" + cardName);
                    HttpsURLConnection myConnection = (HttpsURLConnection) currentURL.openConnection();
                    myConnection.setRequestProperty("User-Agent", "command=zone-v0.1");
                    if (myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginObject(); // Start processing the JSON object
                        while (jsonReader.hasNext()) { // Loop through all keys
                            String key = jsonReader.nextName(); // Fetch the next key
                            if (key.equals("data")) {
                                jsonReader.beginArray();
                                jsonReader.beginObject();
                                    String name = jsonReader.nextName();
                                    if (name.equals("name")) {
                                            foundCard.setName(jsonReader.nextString());
                                            continue;
                                    }
                                    if (name.equals("mana_cost") || name.equals("cmc") || name.equals("type_line") || name.equals("oracle_text") ||
                                            name.equals("power") || name.equals("toughness")) {
                                        foundCard.addAspect(name, jsonReader.nextString());
                                        continue;
                                    }
                                    if (name.equals("color_identity")) {
                                        jsonReader.beginArray();
                                        String colorIdentity = "";
                                        while (jsonReader.hasNext()) {
                                            colorIdentity += jsonReader.nextString();
                                        }
                                        jsonReader.endArray();
                                        continue;
                                    }
                                    if (name.equals("purchase_uris")) {
                                        jsonReader.skipValue();
                                        jsonReader.endObject();
                                        if(jsonReader.peek().equals(JsonToken.END_ARRAY)) {
                                            jsonReader.endArray();
                                        } else if (jsonReader.peek().equals(JsonToken.BEGIN_ARRAY)){
                                            jsonReader.beginArray();
                                        } else if (jsonReader.peek().equals(JsonToken.BEGIN_OBJECT)){
                                            jsonReader.beginObject();
                                        } else if (jsonReader.peek().equals(JsonToken.END_OBJECT)){
                                            jsonReader.endObject();
                                        }
                                    } else {
                                        jsonReader.skipValue();
                                    }

                            } else {
                                jsonReader.skipValue(); // Skip values of other keys
                            }
                        }
                        jsonReader.close();
                        myConnection.disconnect();
                        Log.d("MyApp", "Done");
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        return foundCard;
    }
}
