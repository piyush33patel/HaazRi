package com.example.navigationhaazrai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NewAccountLogin extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText etName,etRollNo,etEmail,etPassword;
    Button bt;
    Spinner sSemester,sBranch,sYear;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(NewAccountLogin.this);
        etName = (EditText) findViewById(R.id.name);
        etRollNo = (EditText) findViewById(R.id.rollno);
        sSemester = (Spinner)findViewById(R.id.department);
        sBranch = (Spinner) findViewById(R.id.branch);
        sYear = (Spinner) findViewById(R.id.year);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        bt = (Button) findViewById(R.id.submit);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(NewAccountLogin.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.branches));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sBranch.setAdapter(arrayAdapter1);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(NewAccountLogin.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.semester));
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSemester.setAdapter(arrayAdapter2);

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(NewAccountLogin.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.year));
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sYear.setAdapter(arrayAdapter3);

        mAuth = FirebaseAuth.getInstance();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Registering User.....");
                progressDialog.show();
                try {
                    final String name = etName.getText().toString().trim();
                    final String rollNo = etRollNo.getText().toString().trim();
                    final String branch = sBranch.getSelectedItem().toString().trim();
                    final String year = sYear.getSelectedItem().toString().trim();
                    final String semester = sSemester.getSelectedItem().toString().trim();
                    final String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(NewAccountLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (!task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
                                registerUser(name, rollNo, branch, year, email, semester);
                            }
                        }
                    });
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter Email and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(final String name, final String rollNo, final String branch, final String year, final String email, final String semester) {
        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_NEW_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            Toast.makeText(getApplicationContext(), "Happening", Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            //changed
                            Intent intent = new Intent(getApplicationContext(),Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            //changed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", name);
                params.put("rollno", rollNo);
                params.put("year", year);
                params.put("branch", branch);
                params.put("semester",semester);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}