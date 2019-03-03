package com.example.redmajesty.commandzone.AbstractClasses;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Red Majesty on 3/2/2019.
 */

public class Card {
    /**
     * Cards for my purposes, cards are made up of attributes
     */
    public String name = "";
    public ArrayList<String> aspects = new ArrayList<>();


    public Card(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName (String name){
        this.name = name;
    }

    public void addAspect(String element, String cost) {
        aspects.add(element + ": " + cost);
    }

    public ArrayList getCardDeats (){
        return aspects;
    }

    public String toString(){
        String tostring = "";
        tostring += name + "\n";
        for(int i = 0; i < aspects.size(); i++){
            tostring += aspects.get(i) + "\n";
        }
        return tostring;
    }
}

