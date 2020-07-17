package com.social.truck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MapLocation extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.maplocation,container,false);

    }


    EditText fromlocation , tolocation;
    String  from , to;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Places.initialize(getContext(), "AIzaSyCOjQp-gFJyxEAhxKbYV1L7tL5Sxpzgq-c");
        fromlocation = (EditText) view.findViewById(R.id.fromlocation);
        tolocation = (EditText) view.findViewById(R.id.toloaction);

        fromlocation.setFocusable(false);
        fromlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                startActivityForResult(intent, 100);
            }
        });
        tolocation.setFocusable(false);

        tolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                startActivityForResult(intent, 100);
            }
        });

        Button request = (Button) view.findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getUid();
                DatabaseReference  ref = FirebaseDatabase.getInstance().getReference("Booking").child(uid);

                ref.child(count+"").child("Vehicle").setValue(vehicle);
                ref.child(count+"").child("Weight").setValue(w);
                ref.child(count+"").child("MaterialType").setValue(m);
                ref.child(count+"").child("User").setValue(uid);

                ref.child(count+"").child("Location").setValue(from+" TO "+to);

                ref.child(count+"").child("Number").setValue(count+"");


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(uid);
                databaseReference.child("Requests").child(""+count).child("Status").setValue("0");
                databaseReference.child("Requests").child(count+"").child("Vehicle").setValue(vehicle);
                databaseReference.child("Requests").child(count+"").child("Weight").setValue(w);
                databaseReference.child("Requests").child(count+"").child("MaterialType").setValue(m);
                databaseReference.child("Requests").child(count+"").child("Location").setValue(from+" TO "+to);

                count++;

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == -1) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            fromlocation.setText(place.getAddress());

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(), status.getStatusMessage(), 1).show();
        }
    }

    String vehicle,w,m;
    long  count;

    public MapLocation(String vehicle, String w, String m ,long count) {
        this.vehicle = vehicle;
        this.w = w;
        this.m = m;
        this.count = count;
    }

    public MapLocation(int contentLayoutId, String vehicle, String w, String m ,long count) {
        super(contentLayoutId);
        this.vehicle = vehicle;
        this.w = w;
        this.m = m;
        this.count=count;
    }
}
