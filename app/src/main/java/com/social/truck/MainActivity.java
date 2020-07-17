package com.social.truck;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputBinding;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import  java.*;
import java.util.Stack;

public class MainActivity extends AppCompatActivity  {








    Spinner spinner;
    EditText editText;  int count1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final String usertype [] = { "Driver" , "Customer"};
        spinner = (Spinner) findViewById(R.id.loginuser);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, usertype));

        editText = findViewById(R.id.phone);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, int start, int count, int after) {
                          }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                DatabaseReference ref;

                if(s.toString().length()<10)
                    count1=0;
                ref = FirebaseDatabase.getInstance().getReference("Registered");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(s.toString())){
                            count1 =1;
                        }
                        else if(s.toString().length()==10)
                        {
                            Toast.makeText(getApplicationContext(),"Unregistered User",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = "91";//CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String user = usertype[spinner.getSelectedItemPosition()];
                DatabaseReference ref;

                ref = FirebaseDatabase.getInstance().getReference("Registered");
                // Read from the database

                final String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }
                String phoneNumber = "+" + code + number;

//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.hasChild(number)){
//                            count =1;
//                        }
//                        else
//                        {
//                            Toast.makeText(getApplicationContext(),"Unregistered User",Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                    }
//                });
                if(count1==1){
                    Intent intent = new Intent(MainActivity.this , Login.class);
                    intent.putExtra("phonenumber", phoneNumber);
                    startActivity(intent);
                }




            }
        });

        TextView textView = (TextView) findViewById(R.id.register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , Register.class);
                startActivity(intent);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


}