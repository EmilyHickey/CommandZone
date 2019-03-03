package com.example.redmajesty.commandzone.AbstractClasses;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Red Majesty on 3/2/2019.
 */

public class CardList {
    /**
     * Contains every card in the game >n<
     */
    public ArrayList<Card> cards = new ArrayList<>();
    private URL previousPage = null;
    private URL currentURL = null;
    private URL nextPage = null;


    public CardList() {

    }

    public ArrayList<Card> makeCommanders(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Boolean has_more = false;
                try {
                    currentURL = new URL("https://api.scryfall.com/cards/search?q=is%3Acommander&unique=cards");
                    HttpsURLConnection myConnection = (HttpsURLConnection) currentURL.openConnection();
                    myConnection.setRequestProperty("User-Agent", "command=zone-v0.1");
                    if (myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginObject(); // Start processing the JSON object
                        while (jsonReader.hasNext()) { // Loop through all keys
                            String key = jsonReader.nextName(); // Fetch the next key
                            Card card = new Card("");
                            if (key.equals("has_more")) {
                                has_more = jsonReader.nextBoolean();
                                continue;
                            }
                            if (key.equals("next_page") && has_more) {
                                nextPage = new URL(jsonReader.nextString());
                                continue;
                            }
                            if (key.equals("data")) {
                                jsonReader.beginArray();
                                jsonReader.beginObject();
                                while (cards.size() < 40) {
                                    String name = jsonReader.nextName();
                                    if (name.equals("name")) {
                                        if(card == null){
                                            card = new Card(jsonReader.nextString());
                                            continue;
                                        }else {
                                            card.setName(jsonReader.nextString());
                                            continue;
                                        }
                                    }
                                    if (name.equals("mana_cost") || name.equals("cmc") || name.equals("type_line") || name.equals("oracle_text") ||
                                            name.equals("power") || name.equals("toughness")) {
                                        card.addAspect(name, jsonReader.nextString());
                                        continue;
                                    }
                                    if (name.equals("color_identity")) {
                                        jsonReader.beginArray();
                                        String colorIdentity = "";
                                        while (jsonReader.hasNext()) {
                                            colorIdentity += jsonReader.nextString();
                                        }
                                        jsonReader.endArray();
                                        cards.add(card);
                                        card = null;
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
        return cards;
    }

    public void NextPage() {

    }

    public void LastPage() {

    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}


