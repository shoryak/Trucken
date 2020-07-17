package com.social.truck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Stack;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        list = new Stack<Fragment>();

        BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.bottomnavigationmenu);
        menu.setOnNavigationItemSelectedListener(navlistener);
        list.push(new Booking());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Booking()).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {


            case R.id.logout:
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

        }
        return true;
        }
        BottomNavigationMenu menu;

    @Override
    public void onBackPressed() {
        if(list.isEmpty())
        super.onBackPressed();
        else{
            AppCompatActivity appCompatActivity = (AppCompatActivity) this;
            Fragment favorites_fragment = list.pop();
            appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,favorites_fragment).commit();

        }
    }

    Stack<Fragment> list;

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            BottomNavigationView hello = (BottomNavigationView) findViewById(R.id.bottomnavigationmenu);
            Log.i("INFO","hello");
            hello.setVisibility(View.VISIBLE);
            switch (item.getItemId()){
                case R.id.provide :
                    selectedFragment = new Provider();
                    break;
                case R.id.Booking :
                    selectedFragment = new Booking();

                    break;
                case R.id.ongoing :
                    selectedFragment = new Activity();
                    break;


                case R.id.completed:
                    selectedFragment = new Completed();
            }

            if(selectedFragment!=null){
                list.push(selectedFragment);
                try {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,selectedFragment).commit();

                }
                catch (Exception e){

                }
            }


            return true;
        }
    };




}
