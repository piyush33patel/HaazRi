package com.example.navigationhaazrai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {
    EditText edit_name, edit_semester, edit_year;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_semester = (EditText)findViewById(R.id.edit_semester);
        edit_year = (EditText)findViewById(R.id.edit_year);
        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, Account.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }
}
