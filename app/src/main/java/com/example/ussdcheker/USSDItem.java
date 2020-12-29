package com.example.ussdcheker;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class USSDItem {

    private String description = "ex: Africel Mega check";
    private String ussd = "*111*100#";
    private String id = "no_id";

    USSDItem(){

    }

    @Override
    public String toString() {
        return "USSDItem{" +
                "description='" + description + '\'' +
                ", ussd='" + ussd + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum USSD_ITEM_DIALOG_OPERATION {
        ADD, EDIT
    }

    public USSDItem(String description, String ussd) {
        this.description = description;
        this.ussd = ussd;
    }

    public static List<USSDItem> GetDummyItems() {
        List<USSDItem> ussdItems = new ArrayList<>();

        List<Pair<String, String>> pairs = new ArrayList<>();

        pairs.add(new Pair<>("Africell check mega", "*111*100#"));
        pairs.add(new Pair<>("Menu Mpesa", "*1122#"));
        pairs.add(new Pair<>("Africell check unite", "*1000#"));
        pairs.add(new Pair<>("Menu Orange", "*123#"));


        ///////////////////// todo Remove after testing

        pairs.add(new Pair<>("Africell check mega", "*111*100#"));
        pairs.add(new Pair<>("Menu Mpesa", "*1122#"));
        pairs.add(new Pair<>("Africell check unite", "*1000#"));
        pairs.add(new Pair<>("Menu Orange", "*123#"));
        pairs.add(new Pair<>("Africell check mega", "*111*100#"));
        pairs.add(new Pair<>("Menu Mpesa", "*1122#"));
        pairs.add(new Pair<>("Africell check unite", "*1000#"));
        pairs.add(new Pair<>("Menu Orange", "*123#"));
        pairs.add(new Pair<>("Africell check mega", "*111*100#"));
        pairs.add(new Pair<>("Menu Mpesa", "*1122#"));
        pairs.add(new Pair<>("Africell check unite", "*1000#"));
        pairs.add(new Pair<>("Menu Orange", "*123#"));
        pairs.add(new Pair<>("Africell check mega", "*111*100#"));
        pairs.add(new Pair<>("Menu Mpesa", "*1122#"));
        pairs.add(new Pair<>("Africell check unite", "*1000#"));
        pairs.add(new Pair<>("Menu Orange", "*123#"));

        /////////////////////


        int size = pairs.size();

        for (int i = 0; i < size; i++){


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
