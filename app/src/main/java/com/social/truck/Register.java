package com.social.truck;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {




    Spinner spinner;
    EditText organisationname,name,pannumber,gstnumber,email,mobile;
    int count1=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        spinner = (Spinner) findViewById(R.id.usertype);
        final String usertype [] = { "Driver" , "Customer"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, usertype));
        organisationname = (EditText) findViewById(R.id.organisation);
        name = (EditText) findViewById(R.id.name);
        pannumber = (EditText) findViewById(R.id.pannumber);
        gstnumber = (EditText) findViewById(R.id.gstnumber);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.phonenumber);








        count1=1;
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Register");
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(s.toString())){
                            count1 =0;
                            Toast.makeText(getApplicationContext(),"User Already Registered",Toast.LENGTH_LONG).show();

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


        findViewById(R.id.buttonregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String organisation;
                final String pname;
                final String pnumber;
                String gst;
                String emailid;
                final String mnumber;
                final String user;
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
//                if (!isValidPanCardNo(pnumber)&&user.equals("Customer") ) {
//                    pannumber.setError("Invalid Pan Number");
//                    pannumber.requestFocus();
//                }
                if (mnumber.isEmpty() || mnumber.length() < 10) {
                    mobile.setError("Valid number is required");
                    mobile.requestFocus();
                }
                if(organisation.isEmpty())
                {
                    organisation="truck";
                }
                if(gst.isEmpty()){
                    gst="123456789";
                }
                if(emailid.isEmpty()){
                    emailid="truck@truck.com";

                }



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
                    if(count1==1)
                    startActivity(intent);

                }
            }
        });



    }
    public static boolean isValidPanCardNo(
            String panCardNo)
    {

        // Regex to check valid PAN Card number.
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the PAN Card number
        // is empty return false
        if (panCardNo == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given
        // PAN Card number using regular expression.
        Matcher m = p.matcher(panCardNo);

        // Return if the PAN Card number
        // matched the ReGex
        return m.matches();
    }

}
