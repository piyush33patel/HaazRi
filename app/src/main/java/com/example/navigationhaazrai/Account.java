package com.example.navigationhaazrai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Account extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    Button bt;
    public  static String userEmail, userName, userRollNo, userBranch, userYear, userSemester;

    public static final String SHARED_PREF = "sharedPrefs";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String ROLLNO = "rollno";
    public static final String BRANCH = "branch";
    public static final String YEAR = "year";
    public static final String SEMESTER = "semester";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv1 = (TextView)findViewById(R.id.name);
        tv2 = (TextView)findViewById(R.id.email);
        tv3 = (TextView)findViewById(R.id.branch);
        tv4 = (TextView)findViewById(R.id.year);
        tv5 = (TextView)findViewById(R.id.semester);
        tv6 = (TextView)findViewById(R.id.rollNo);
        bt = (Button)findViewById(R.id.edit_profile);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        loadData();
    }

/*
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL,userEmail);
        editor.putString(NAME,userName);
        editor.putString(ROLLNO, userRollNo);
        editor.putString(BRANCH, userBranch);
        editor.putString(SEMESTER, userSemester);
        editor.putString(YEAR,userYear);
        editor.apply();
    }
    */

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        tv1.setText(sharedPreferences.getString(NAME,"USER NAME"));
        tv2.setText(sharedPreferences.getString(EMAIL,"USER EMAIL"));
        tv3.setText(sharedPreferences.getString(BRANCH,"USER BRANCH"));
        tv4.setText(sharedPreferences.getString(YEAR,"USER YEAR"));
        tv5.setText(sharedPreferences.getString(SEMESTER,"USER SEMESTER"));
        tv6.setText(sharedPreferences.getString(ROLLNO,"USER ROLL NUMBER"));
    }

}
