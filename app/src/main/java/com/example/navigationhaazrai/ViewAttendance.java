package com.example.navigationhaazrai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class ViewAttendance extends Fragment {
    private RecyclerView mRecyclerView;
    private ViewAttendanceAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ViewAttendanceItem> exampleList;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private String email;
    private ArrayList<String> fakeSubject, realSubject;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_attendance, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        exampleList = new ArrayList<>();
        buildRecyclerView(view);
        exampleList.add(0, new ViewAttendanceItem("       SUBJECTS", "T", "P", "A", "%"));
        showAttendance();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                exampleList = new ArrayList<>();
                buildRecyclerView(view);
                exampleList.add(0, new ViewAttendanceItem("SUBJECT", "T", "P", "A", "%"));
                showAttendance();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void showAttendance(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_VIEW_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray;
                        fakeSubject = new ArrayList<>();
                        progressDialog.dismiss();
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    jsonObject = jsonArray.getJSONObject(i);

                                    String sub = jsonObject.getString("title");
                                    fakeSubject.add(sub);
                                    int tot = jsonObject.getInt("total");
                                    int pre = jsonObject.getInt("present");
                                    int abs = jsonObject.getInt("absent");
                                    int per = 0;
                                    if (tot > 0)
                                        per = (pre*100)/tot;
                                    insertItem(sub,tot,pre,abs,per);
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
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Something went Wrong. Try again!!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getContext());
        rq.add(stringRequest);
    }

    public void insertItem(String subject, int total, int present, int absent, int percent){
        exampleList.add(1, new ViewAttendanceItem(subject, ""+total, ""+present, ""+absent, ""+percent));
        mAdapter.notifyItemInserted(1);
    }


    public void buildRecyclerView(View view){
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ViewAttendanceAdapter(exampleList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ViewAttendanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position>0) {
                    realSubject = new ArrayList<>();
                    for (int i = fakeSubject.size() - 1; i >= 0; i--)
                        realSubject.add(fakeSubject.get(i));
                    Intent intent = new Intent(getContext(), SubjectWise.class);
                    intent.putExtra("title", realSubject.get(position - 1));
                    startActivity(intent);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

