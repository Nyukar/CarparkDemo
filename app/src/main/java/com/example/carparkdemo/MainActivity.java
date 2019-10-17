package com.example.carparkdemo;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//
public class MainActivity extends AppCompatActivity {

    private Spinner departSpinner;
    private Spinner arriveSpinner;

    private Button registerButton;
    private Button findButton;
    private Button goMap;

    private TextView resultsView;
    private TextView userView;
    private LocationManager locationManager;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest mLocationRequest;
    private ArrayList<LatLng> points;
    private boolean cReady;

    private LatLng[] locA = new LatLng[1];
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = (Button) findViewById(R.id.registerButton);
        findButton = (Button) findViewById(R.id.findButton);
        cReady = false;

        resultsView = (TextView) findViewById(R.id.FoundTextTV);
        userView = (TextView) findViewById(R.id.userNameText);
        goMap = (Button) findViewById(R.id.checkMap);
        goMap.setEnabled(false);




        //load up the spinners with time values

        String[] arraySpinner = new String[] {
                "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00"
        };

        departSpinner = (Spinner) findViewById(R.id.departSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departSpinner.setAdapter(adapter);

        arriveSpinner = (Spinner) findViewById(R.id.arrivalSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arriveSpinner.setAdapter(adapter2);

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Getting the toast ready/

                Context context = getApplicationContext();
                String str = userView.getText().toString();
                String text = "Hello, " + str + '\n' + "Thank-you for Registering";
                int duration = Toast.LENGTH_SHORT;


                //coordds stuff







                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();

                goMap.setEnabled(true);

                // Create a new user with a first and last name
                Map<String, Object> registration = new HashMap<>();
                registration.put("carpark", "1");


                //person details (you might dervive this from a saved state in the app

               if(userView.getText().toString().equalsIgnoreCase("Jane")) {

                   Map<String, Object> user = new HashMap<>();
                   user.put("Name", "Jane");
                   user.put("Points", "500");
                   user.put("Phone", "0433572342");

                   registration.put("User", user);

                   //car details (you might derive this from a saved state in the app
                   Map<String, Object> car = new HashMap<>();
                   car.put("Manufacturer", "Toyota");
                   car.put("Model", "Corolla");
                   car.put("Colour", "Grey");
                   car.put("Plate", "ABC123");

                   registration.put("car", car);

               }

                if(userView.getText().toString().equalsIgnoreCase("Bob")) {

                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", "Bob");
                    user.put("Points", "250");
                    user.put("Phone", "0433576531");

                    registration.put("User", user);


                    Map<String, Object> car = new HashMap<>();
                    car.put("Manufacturer", "Toyota");
                    car.put("Model", "Corolla");
                    car.put("Colour", "Red");
                    car.put("Plate", "ABC123");

                    registration.put("car", car);

                }


                // get the leave time from the UI
                String leaveVal = departSpinner.getSelectedItem().toString();


                registration.put("leaveTime", leaveVal);


                // Add a new document with a generated ID
                db.collection("availability")
                        .add(registration)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error adding document", e);
                            }
                        });
            }

        });


        findButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                // get the leave time from the UI
                String leaveVal = arriveSpinner.getSelectedItem().toString();

                db.collection("availability")
                        .whereEqualTo("leaveTime", leaveVal)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        resultsView.setText(document.getData().toString());
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });



        //button listener to go to maps//
        goMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { goToMaps();
            }
        });
    }

    //goes to maps activity//
    private void goToMaps()
    {

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);


    }




    //meant to send latlng to MapsActivity//
    public LatLng[] toMap()
    {
        final LatLng[] loc = new LatLng[1];
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                loc[0] = new LatLng(location.getLatitude(), location.getLongitude());


            }
        });

        //hold location readings//
        points = new ArrayList<LatLng>();

        return loc;


    }


}
