package com.social.truck;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity extends Fragment {







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_provider,container,false);

    }


    String uid ;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      uid=  FirebaseAuth.getInstance().getUid().toString();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Customer").child(uid).child("Requests");
        Query query = rootRef.orderByChild("Requests");
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> vehicle = new ArrayList<>();
                final ArrayList<String> materialtype = new ArrayList<>();
                final ArrayList<String> weight = new ArrayList<>();
                final ArrayList<String> count = new ArrayList<>();
                final ArrayList<String> location = new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {


                        String name = ds.child("Vehicle").getValue(String.class);
                        vehicle.add(name);
                        name =  ds.child("MaterialType").getValue(String.class);
                        materialtype.add(name);
                        weight.add(ds.child("Weight").getValue(String.class));
                        count.add(ds.getKey());
                        location.add(ds.child("Location").getValue(String.class));



//                        count.add(ds.child("Number").getValue(String.class));


                }

                final ListView listView = (ListView) view.findViewById(R.id.providerlist);
              Adapter arrayAdapter = new Adapter(getContext(),vehicle,materialtype,weight,location);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AppCompatActivity appCompatActivity = (AppCompatActivity) getContext();
                        Fragment favorites_fragment = new ActivityClick(uid,count.get(position));
                                //ActivityClick(vehicle.get(position),materialtype.get(position),weight.get(position),"Location",count.get(position),FirebaseAuth.getInstance().getUid().toString());
                        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,favorites_fragment).commit();




                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("INFO", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }


    class Adapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rvehicle ;
        ArrayList<String> rmaterialtype ;
        ArrayList<String> rweight ;
        ArrayList<String> rlocation;



        Adapter (Context c, ArrayList<String> vehicle,ArrayList<String> materialtype,ArrayList<String> weight, ArrayList<String> location){
            super(c,R.layout.list,vehicle);
            this.context=c;
            this.rvehicle=vehicle;
            this.rmaterialtype = materialtype;
            this.rweight = weight;
            this.rlocation = location;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list,parent,false);
            ImageView images = row.findViewById(R.id.searchimage);
            TextView pv = row.findViewById(R.id.providervehicle);
            TextView pm = row.findViewById(R.id.providermaterialtype);
            TextView pw = row.findViewById(R.id.providerweight);
            TextView pl = row.findViewById(R.id.location);

            images.setImageResource(R.drawable.image);
            pv.setText(rvehicle.get(position));
            pm.setText(rmaterialtype.get(position));
            pw.setText(rweight.get(position));
            pl.setText(rlocation.get(position));
//            if(rImgs[position] != null && rImgs[position].length()>0)
//            {
//                Picasso.get().load(rImgs[position]).placeholder(R.drawable.common_full_open_on_phone).into(images);
//            }else
//            {
//                Picasso.get().load(R.drawable.common_full_open_on_phone).into(images);
//            }



            return row;
        }
    }


}
