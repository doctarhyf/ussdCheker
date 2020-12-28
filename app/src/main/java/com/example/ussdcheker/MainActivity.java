package com.example.ussdcheker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterUSSD.USSDItemListener {


    private static final String TAG = "TAG";
    RecyclerView rv;
    AdapterUSSD adapterUSSD;
    private List<USSDItem> ussdItemList = new ArrayList<>();
    String[] perms = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        askPermissions();

        setupGUI();



    }

    private void setupGUI() {
        rv = findViewById(R.id.rv);


        ussdItemList.addAll(USSDItem.GetDummyItems());
        adapterUSSD = new AdapterUSSD(this, ussdItemList, this);
        rv.setAdapter(adapterUSSD);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapterUSSD.notifyDataSetChanged();
    }

    private void askPermissions() {
        new RxPermissions(this)
                .request(perms)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M

                        if (granted) {
                            Log.e(TAG, "onCreate: perms granted");
                        }

                    } else {
                        Log.e(TAG, "onCreate: perms refused ");
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public void onUSSDItemListener(USSDItem ussdItem) {
        Log.e(TAG, "onUSSDItemListener: " );

        //TextView textView = (TextView) view;
        String ussd = ussdItem.getUssd();

        Uri phoneCallUri = Uri.parse("tel:" + ussd.replace("#", "%23"));

        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
        phoneCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phoneCallIntent.setData(phoneCallUri);

        startActivity(phoneCallIntent);

    }
}