package com.social.truck;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Verify_Phone_Activity extends AppCompatActivity {


    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;

    DatabaseReference ref;
    String user , mnumber , pname , pnumber , emailid , organisation , gst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);

        String phonenumber = getIntent().getStringExtra("phonenumber");
        user = getIntent().getStringExtra("user");
        mnumber = getIntent().getStringExtra("mnumber");
        pnumber = getIntent().getStringExtra("PanNumber");
        emailid = getIntent().getStringExtra("Email");
        organisation = getIntent().getStringExtra("OrganisationName");
        gst = getIntent().getStringExtra("GSTNumber");
        pname = getIntent().getStringExtra("Name");



        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String uid;

                                    uid= FirebaseAuth.getInstance().getUid().toString();
                            ref = FirebaseDatabase.getInstance().getReference(user).child(uid);
                            ref.child("OrganisationName").setValue(organisation);
                            ref.child("Name").setValue(pname);
                            ref.child("PanNumber").setValue(pnumber);
                            ref.child("GSTNumber").setValue(gst);
                            ref.child("OrganisationName").setValue(organisation);
                            ref.child("Email").setValue(emailid);
                            ref.child("Mobile").setValue(mnumber);
                            ref = FirebaseDatabase.getInstance().getReference("Registered");
                            ref.child(mnumber).setValue("user");
                            Intent intent = new Intent(Verify_Phone_Activity.this, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(Verify_Phone_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify_Phone_Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
