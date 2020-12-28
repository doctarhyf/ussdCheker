package com.example.ussdcheker;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class USSDItem {

    private String description;
    private String ussd;

    USSDItem(){

    }

    public USSDItem(String description, String ussd) {
        this.description = description;
        this.ussd = ussd;
    }

    public static List<USSDItem> GetDummyItems() {
        List<USSDItem> ussdItems = new ArrayList<>();

        List<Pair<String, String>> pairs = new ArrayList<>();

        pairs.add(0, new Pair<>("Africell check mega", "*111*100#"));
        pairs.add(0, new Pair<>("Menu Mpesa", "*1122#"));
        pairs.add(0, new Pair<>("Africell check unite", "*1000#"));
        pairs.add(0, new Pair<>("Menu Orange", "*123#"));


        for (int i = 0; i < pairs.size(); i++){

            Pair<String, String> pair = pairs.get(i);
            ussdItems.add(new USSDItem(pair.first, pair.second));
        }


        return ussdItems;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUssd() {
        return ussd;
    }

    public void setUssd(String ussd) {
        this.ussd = ussd;
    }
}
