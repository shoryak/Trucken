package com.social.truck;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MoneyBid extends Fragment {









    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.moneybid,container,false);

    }


    String vehicle,materialtype,weight,location, uid, count;

    public MoneyBid( String vehicle, String materialtype, String weight, String location,String count,String uid) {

     this.uid = uid;
        this.count=count;
        this.vehicle = vehicle;
        this.materialtype = materialtype;
        this.weight = weight;
        this.location = location;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("info",vehicle+" "+materialtype+" "+weight+" ");
        TextView pvehicle = (TextView) view.findViewById(R.id.moneyvehicle);
        TextView pmaterial = (TextView) view.findViewById(R.id.moneymaterial);
        TextView pweight = (TextView) view.findViewById(R.id.moneyweight);
        TextView plocation = (TextView) view.findViewById(R.id.moneylocation);
        pvehicle.setText(vehicle);
        pmaterial.setText(materialtype);
        pweight.setText(weight);
        plocation.setText("Location");
        final EditText editText = (EditText) view.findViewById(R.id.bidmoney);
        final String money = editText.getText().toString().trim();
        Button button = (Button) view.findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String money = editText.getText().toString().trim();
                String string = FirebaseAuth.getInstance().getUid().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer").child(uid).child("Requests").child(count);
                ref.child(string).setValue(money);
                ref.child("Vehicle").setValue(vehicle);
                ref.child("Weight").setValue(weight);
                ref.child("MaterialType").setValue(materialtype);
            }
        });

    }
}
