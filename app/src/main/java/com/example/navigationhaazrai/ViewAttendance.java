package com.example.navigationhaazrai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;
import static com.example.navigationhaazrai.Account.BRANCH;
import static com.example.navigationhaazrai.Account.EMAIL;
import static com.example.navigationhaazrai.Account.NAME;
import static com.example.navigationhaazrai.Account.ROLLNO;
import static com.example.navigationhaazrai.Account.SEMESTER;
import static com.example.navigationhaazrai.Account.SHARED_PREF;
import static com.example.navigationhaazrai.Account.YEAR;
import static com.example.navigationhaazrai.Account.userBranch;
import static com.example.navigationhaazrai.Account.userEmail;
import static com.example.navigationhaazrai.Account.userName;
import static com.example.navigationhaazrai.Account.userRollNo;
import static com.example.navigationhaazrai.Account.userSemester;
import static com.example.navigationhaazrai.Account.userYear;

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
        exampleList.add(0, new ViewAttendanceItem("COURSE", "TOTAL", "PRESENT", "ABSENT", "PERCENT" , ""));
        showAttendance();
        getAccount();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                exampleList = new ArrayList<>();
                buildRecyclerView(view);
                exampleList.add(0, new ViewAttendanceItem("COURSE", "TOTAL", "PRESENT", "ABSENT", "PERCENT", ""));
                showAttendance();
                getAccount();
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
                                    /* message */
                                    double x = 0;
                                    String message="";
                                    if(per<75)
                                    {
                                        x = (0.75*tot - pre)/(0.25);
                                        int w = (int)x;
                                        if(w==0)
                                            message = "Waiting for the first class";
                                        else if(w==1)
                                            message = "Need to attend the next class";
                                        else
                                            message = "Need to attend next " + w + " classes";
                                    }
                                    else
                                    {
                                        x = (pre-0.75*tot)/(0.75);
                                        int w = (int)x;
                                        if(w==0)
                                            message = "No leaves allowed";
                                        else if(w==1)
                                            message = "Could leave the next class";
                                        else
                                            message = "Could leave next " + w + " classes";
                                    }
                                    /* message */
                                    insertItem(sub,tot,pre,abs,per,message);
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


    public void saveData(){

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL,email);
        editor.putString(NAME,userName);
        editor.putString(ROLLNO, userRollNo);
        editor.putString(BRANCH, userBranch);
        editor.putString(SEMESTER, userSemester);
        editor.putString(YEAR,userYear);
        editor.apply();
    }

    public void getAccount(){
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
                                        userEmail = jsonObject.getString("email");
                                        userName = jsonObject.getString("name");
                                        userRollNo = jsonObject.getString("rollno");
                                        userBranch = jsonObject.getString("branch");
                                        userYear = jsonObject.getString("year");
                                        userSemester = jsonObject.getString("semester");
                                        saveData();
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
                    params.put("email", email);
                    return params;
                }
            };
            RequestQueue rq = Volley.newRequestQueue(getContext());
            rq.add(stringRequest);
    }

    public void insertItem(String subject, int total, int present, int absent, int percent, String message){
        exampleList.add(1, new ViewAttendanceItem(subject, ""+total, ""+present, ""+absent, ""+percent+"%", message));
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

