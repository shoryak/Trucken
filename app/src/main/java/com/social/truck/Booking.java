package com.social.truck;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.LastOwnerException;
import java.util.Arrays;
import java.util.List;

public class Booking extends Fragment implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    // private static final int RESULT_OK = ;
    long count = -1;
    String uid;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
     LocationRequest mLocationRequest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String uid = FirebaseAuth.getInstance().getUid().toString();

        ref = FirebaseDatabase.getInstance().getReference("Booking").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = snapshot.getChildrenCount();
                Log.i("infocount",count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return inflater.inflate(R.layout.fragment_booking, container, false);

    }


    Spinner type, details;
    String vehicle[] = {"Trailer Empty 20 Feet", "Trailer Empty 40 Feet", " Trailer Loaded 20 Feet", "Trailer Loaded 40 Feet", "Full Body ", " Half Body", "Other"};
    EditText weight, materialtype, otype, fromlocation, tolocation;
    String w, m, ot, vehicletype, from, to;
    DatabaseReference ref;
Fragment fragment;
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = (Spinner) view.findViewById(R.id.vehicletype);
        otype = (EditText) view.findViewById(R.id.type);


        type.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, vehicle));
        weight = (EditText) view.findViewById(R.id.Weight);
        materialtype = (EditText) view.findViewById(R.id.materialtype);
        w = "";
        m = "";


        uid = FirebaseAuth.getInstance().getUid();

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weight.setVisibility(View.VISIBLE);
                materialtype.setVisibility(View.VISIBLE);
                otype.setVisibility(View.INVISIBLE);
                vehicletype = vehicle[position];
                if (position == 0 || position == 1) {
                    weight.setVisibility(View.INVISIBLE);
                    materialtype.setVisibility(View.INVISIBLE);
                } else {

                    w = weight.getText().toString();
                    m = materialtype.getText().toString();

                }
                if (position == 6) {
                    otype.setVisibility(View.VISIBLE);
                    ot = otype.getText().toString();

                }

                Log.i("INFO", "" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Button button = (Button) view.findViewById(R.id.next);
        {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(count>-1)
{

                    w = weight.getText().toString();
                    m = materialtype.getText().toString();
                    if (vehicletype.equals("Other"))
                        vehicletype = otype.getText().toString();
    AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new MapLocation(vehicletype,w,m,count)).commit();







                }else
                    {
                        Toast.makeText(getContext(),"Loading",1).show();
                    }
                }

            });
        }


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

    public Booking() {
    }

    public Booking(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
