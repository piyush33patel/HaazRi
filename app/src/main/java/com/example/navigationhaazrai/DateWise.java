package com.example.navigationhaazrai;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

public class DateWise extends AppCompatActivity {

    private TextView tv;
    private String ans = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise);
        tv = (TextView)findViewById(R.id.result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_DATE_WISE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject,jsonObject2;
                        JSONArray jsonArray;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String date = jsonObject.getString("date");
                                ans = ans + "-" + date + "-" + "\n\n";
                                JSONArray subject = jsonObject.getJSONArray("subject");
                                for(int j = 0; j < subject.length(); j++){
                                    jsonObject2 = subject.getJSONObject(j);
                                    String title = jsonObject2.getString("title");
                                    String value = jsonObject2.getString("value");
                                    ans = ans + "      " + title + "       " + value + "\n\n";
                                }
                                ans = ans + "\n\n";
                            }
                            tv.setText(ans);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), ""+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(stringRequest);

    }
}
