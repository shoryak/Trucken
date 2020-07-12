package com.social.truck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {




    Spinner spinner;
    EditText organisationname,name,pannumber,gstnumber,email,mobile;
    int count=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        spinner = (Spinner) findViewById(R.id.usertype);
        final String usertype [] = { "Driver" , "Customer"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, usertype));
        organisationname = (EditText) findViewById(R.id.organisation);
        name = (EditText) findViewById(R.id.name);
        pannumber = (EditText) findViewById(R.id.pannumber);
        gstnumber = (EditText) findViewById(R.id.gstnumber);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.phonenumber);

        findViewById(R.id.buttonregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String organisation , pname, pnumber , gst , emailid , mnumber , user;
                organisation = organisationname.getText().toString().trim();
                pname = name.getText().toString().trim();
                pnumber = pannumber.getText().toString().trim();
                gst = gstnumber.getText().toString().trim();
                emailid = email.getText().toString().trim();
                mnumber = mobile.getText().toString().trim();
                user = usertype[spinner.getSelectedItemPosition()];
                if (pname.isEmpty()) {
                    name.setError("Valid name is required");
                    name.requestFocus();
                }
                if (pnumber.isEmpty()&&user.equals("Customer") ) {
                    pannumber.setError("Valid number is required");
                    pannumber.requestFocus();
                }
                if (mnumber.isEmpty() || mnumber.length() < 10) {
                    mobile.setError("Valid number is required");
                    mobile.requestFocus();
                }
//                if(organisation.isEmpty())
//                {
//                    organisation="truck";
//                }
//                if(gst.isEmpty()){
//                    gst="123456789";
//                }
//                if(emailid.isEmpty()){
//                    emailid="truck@truck.com";
//
//                }


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(user);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(mnumber)){
                            count =0;
                            Toast.makeText(getApplicationContext(),"User Already Registered",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });

                if(pname.isEmpty()||(pnumber.isEmpty()&&user.equals("Customer")));
                else
                {

                    Intent intent = new Intent(Register.this , Verify_Phone_Activity.class);
                    String phoneNumber = "+91"+mnumber;
                    intent.putExtra("phonenumber", phoneNumber);
                    intent.putExtra("OrganisationName", organisation);
                    intent.putExtra("Name", pname);
                    intent.putExtra("PanNumber", pnumber);
                    intent.putExtra("GSTNumber", gst);
                    intent.putExtra("Email", emailid);
                    intent.putExtra("mnumber",mnumber);
                    intent.putExtra("user",user);
                    if(count==1)
                    startActivity(intent);

                }
            }
        });



    }
}
