package com.example.ussdcheker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterUSSD.USSDItemListener {


    private String uid = "auth id";
    private AdView mAdView;
    private static final String TAG = "TAG";
    RecyclerView rv;
    AdapterUSSD adapterUSSD;
    private List<USSDItem> ussdItemList = new ArrayList<>();
    String[] perms = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.CALL_PHONE};
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
TextView tvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        askPermissions();
        setupGUI();
        loadData();
        loadAdMob();

    }

    private void loadAdMob() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setupGUI() {

        tvLoading = findViewById(R.id.tvLoading);
        rv = findViewById(R.id.rv);
        adapterUSSD = new AdapterUSSD(this, ussdItemList, this);
        rv.setAdapter(adapterUSSD);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }

    private void loadData() {


        Task<QuerySnapshot> ref = db.collection(uid).get();


        ref.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                parseUSSDData(querySnapshot);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        /*
        ussdItemList.addAll(USSDItem.GetDummyItems());
        adapterUSSD.notifyDataSetChanged();
        */

    }

    private void parseUSSDData(QuerySnapshot querySnapshot) {
        if (querySnapshot.size() > 0) {


            tvLoading.setVisibility(View.GONE);

            ussdItemList.clear();
            for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {

                USSDItem ussdItem = queryDocumentSnapshot.toObject(USSDItem.class);
                ussdItemList.add(ussdItem);

            }

            adapterUSSD.notifyDataSetChanged();

        } else {
            tvLoading.setText("NO USSD");
            Log.e(TAG, "onSuccess: NO USSD");
            Toast.makeText(this, "NO USSD", Toast.LENGTH_LONG).show();
        }
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
        Log.e(TAG, "onUSSDItemListener: ");

        //TextView textView = (TextView) view;
        String ussd = ussdItem.getUssd();

        Uri phoneCallUri = Uri.parse("tel:" + ussd.replace("#", "%23"));

        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
        phoneCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phoneCallIntent.setData(phoneCallUri);

        startActivity(phoneCallIntent);

    }

    @Override
    public void onUSSDItemEditClicked(USSDItem mItem, USSDItem.USSD_ITEM_DIALOG_OPERATION operation) {


        showUSSDAddEditDialog(mItem, operation);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_new_ussd:
                showUSSDAddEditDialog(new USSDItem(), USSDItem.USSD_ITEM_DIALOG_OPERATION.ADD);
                break;
        }

        return true;
    }

    private void showUSSDAddEditDialog(USSDItem mItem, USSDItem.USSD_ITEM_DIALOG_OPERATION operation) {


        View view = getLayoutInflater().inflate(R.layout.dialog_add_delete_ussd, null);

        EditText etDesc = view.findViewById(R.id.etDesc);
        EditText etUSSD = view.findViewById(R.id.etUSSD);

        String descriptionOld = mItem.getDescription();
        String ussdOld = mItem.getUssd();
        //String id = mItem.getId();

        String title = "ADD NEW USSD";

        if (operation == USSDItem.USSD_ITEM_DIALOG_OPERATION.EDIT) {
            title = "EDIT USSD";
            etDesc.setText(descriptionOld);
            etUSSD.setText(ussdOld);
        } else {
            mItem.setId(db.collection(uid).document().getId());
            etDesc.setHint(descriptionOld);
            etUSSD.setHint(ussdOld);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setView(view);

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "onClick: save ussd");


                String descriptionNew = etDesc.getText().toString();
                String ussdNew = etUSSD.getText().toString();


                if (descriptionNew.isEmpty() && ussdNew.isEmpty()) {

                    Toast.makeText(MainActivity.this, "Fields cant be empty", Toast.LENGTH_SHORT).show();

                } else {

                    mItem.setDescription(descriptionNew);
                    mItem.setUssd(ussdNew);

                    updateUSSDItem(mItem);

                }

            }
        });

        builder.setNeutralButton("CANCEL", null);

        builder.show();

    }

    private void updateUSSDItem(USSDItem mItem) {
        Log.e(TAG, "updateUSSDItem: " + mItem.toString());

        db.collection(uid).document(mItem.getId()).set(mItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Added succesfully", Toast.LENGTH_LONG).show();
                loadData();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }
}