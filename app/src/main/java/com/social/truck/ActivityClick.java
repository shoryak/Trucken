package com.social.truck;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

public class ActivityClick extends Fragment {







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_provider,container,false);

    }


    String uid ,count;
     ArrayList<String> vehicle = new ArrayList<>();
     ArrayList<String> materialtype = new ArrayList<>();
     ArrayList<String> weight = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid = FirebaseAuth.getInstance().getUid().toString();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Customer").child(uid).child("Requests").child(count);
        Query query = rootRef.orderByChild(count);
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                   if(ds.getKey().equals("MaterialType")||ds.getKey().equals("Status")||ds.getKey().equals("Vehicle")||ds.getKey().equals("Weight"));
                   else
                   {
                       String string = ds.getKey();
                       weight.add(ds.getValue(String.class));
                       Log.i("info",ds.getValue(String.class));
                       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(string);
                       Query q = databaseReference.orderByChild(string);
                       final ValueEventListener valueEventListener1 = new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot1) {
                               for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                   if(dataSnapshot2.getKey().equals("Name")){
                                       vehicle.add(dataSnapshot2.getValue(String.class));
                                       Log.i("info","HELLO");
                                   }

                                   if(dataSnapshot2.getKey().equals("OrganisationName"))
                                       materialtype.add(dataSnapshot2.getValue(String.class));

                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }


                       };
                       q.addListenerForSingleValueEvent(valueEventListener1);


    }


                    }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        {
                            Log.i("info",vehicle.size()+" "+materialtype.size()+" "+weight.size());
                            final ListView listView = (ListView) view.findViewById(R.id.providerlist);
                            Adapter arrayAdapter = new Adapter(getContext(),vehicle,materialtype,weight);
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                },5000);

                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        query.addListenerForSingleValueEvent(valueEventListener);


        }

    public ActivityClick( String uid, String count) {
        this.uid = uid;
        this.count = count;
    }

    class Adapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rvehicle ;
        ArrayList<String> rmaterialtype ;
        ArrayList<String> rweight ;



        Adapter (Context c, ArrayList<String> vehicle,ArrayList<String> materialtype,ArrayList<String> weight){
            super(c,R.layout.list,vehicle);
            this.context=c;
            this.rvehicle=vehicle;
            this.rmaterialtype = materialtype;
            this.rweight = weight;

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

            images.setImageResource(R.drawable.image);
            pv.setText(rvehicle.get(position));
            pm.setText(rmaterialtype.get(position));
            pw.setText(rweight.get(position));
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
