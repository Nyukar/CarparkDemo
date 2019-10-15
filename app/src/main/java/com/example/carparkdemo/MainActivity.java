package com.example.carparkdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
//
public class MainActivity extends AppCompatActivity {

    Spinner departSpinner;
    Spinner arriveSpinner;

    Button registerButton;
    Button findButton;

    TextView resultsView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = (Button) findViewById(R.id.registerButton);
        findButton = (Button) findViewById(R.id.findButton);


        resultsView = (TextView) findViewById(R.id.FoundTextTV);

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
                // Create a new user with a first and last name
                Map<String, Object> registration = new HashMap<>();
                registration.put("carpark", "1");

                //person details (you might dervive this from a saved state in the app
                Map<String, Object> user = new HashMap<>();
                user.put("Name", "Jane");
                user.put("Points", "500");
                user.put("Phone", "0433572342");

                registration.put("User", user);

                //car details (you might derive this from a saved state in the app
                Map<String, Object> car = new HashMap<>();
                car.put("Manufacturer", "Toyota");
                car.put("Model", "Corolla");
                car.put("Colour", "Red");
                car.put("Plate", "ABC123");

                registration.put("car", car);


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

    }
}
