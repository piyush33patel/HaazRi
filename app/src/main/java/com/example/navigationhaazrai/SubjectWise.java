package com.example.navigationhaazrai;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectWise extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SubjectWiseAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SubjectWiseItem> exampleList;
    private JSONObject jsonObject;
    private String email, title;
    private ArrayList<String> fakeDate, realDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_wise);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        exampleList = new ArrayList<>();
        title = getIntent().getStringExtra("title");
        toolbar.setSubtitle(title);
        createExampleList();
        buildRecyclerView();
        showSubjectWise();
    }

    public void removeItem(int position){
        exampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
        Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
    }

    public void createExampleList(){
        exampleList.add(new SubjectWiseItem("    DATE", "VALUE", "TIME"));
    }
    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SubjectWise.this);
        mAdapter = new SubjectWiseAdapter(exampleList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SubjectWiseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                if(position>0) {
                    removeItem(position);
                    deleteAttendanceTuple(position);
                }
            }
        });

    }


    public void deleteAttendanceTuple(final int position){
        realDate = new ArrayList<>();
        for (int i = fakeDate.size()-1; i >= 0 ; i--)
            realDate.add(fakeDate.get(i));
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_DELETE_ATTENDANCE_TUPLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    jsonObject = jsonArray.getJSONObject(i);
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
                        Toast.makeText(getApplicationContext(), "Something went Wrong. Try again!!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("title", title);
                params.put("date", realDate.get(position-1));
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(stringRequest);
    }


    public void showSubjectWise(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_VIEW_ATTENDANCE_SUBJECT_WISE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray;
                        fakeDate = new ArrayList<>();
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String date = jsonObject.getString("date");
                                    fakeDate.add(date);
                                    String value = jsonObject.getString("value");
                                    String time = jsonObject.getString("time");
                                    exampleList.add(1, new SubjectWiseItem(date,value,time));
                                    mAdapter.notifyItemInserted(1);

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
                        Toast.makeText(getApplicationContext(), "Something went Wrong. Try again!!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("title", title);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(stringRequest);
    }
}