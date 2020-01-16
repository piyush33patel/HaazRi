package com.example.navigationhaazrai;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    private ProgressDialog progressDialog;
    public  static String userEmail, userName, userRollNo, userBranch, userYear, userSemester;

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

        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        getDetails();

    }


    public void getDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_USER_ACCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;
                        JSONArray jsonArray;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String Email = jsonObject.getString("email");
                                    userName = jsonObject.getString("name");

                                    userRollNo = jsonObject.getString("rollno");
                                    userBranch = jsonObject.getString("branch");
                                    userYear = jsonObject.getString("year");
                                    userSemester = jsonObject.getString("semester");

                                    tv1.setText(userName);
                                    tv2.setText(userEmail);
                                    tv3.setText(userBranch);
                                    tv4.setText(userYear);
                                    tv5.setText(userSemester);
                                    tv6.setText(userRollNo);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "Something went Wrong. Try again!!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(stringRequest);

    }
}
