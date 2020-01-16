package com.example.navigationhaazrai;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MarkAttendance extends Fragment {
    private int[] p = new int[10];
    private int[] a = new int[10];
    private int[] addMth = {31,29,31,30,31,30,31,31,30,31,30,31};
    private String [] weekDay = {"TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY","MONDAY"};
    private RecyclerView mRecyclerView;
    private MarkAttendanceAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MarkAttendanceItem> exampleList;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button bt, submit;
    private String date, dayOfWeek;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private String email, message;
    private ArrayList<String> fakeSubject, realSubject;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mark_attendance, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait....");

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        createExampleList();
        buildRecyclerView(view);
        //here
        bt = (Button)view.findViewById(R.id.date);
        submit = (Button)view.findViewById(R.id.submit);
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        date = getDate(year,month,day);
        dayOfWeek = getDayOfWeek(month,day);
        bt.setText(date);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                progressDialog.show();
                date = getDate(year,month,day);
                dayOfWeek = getDayOfWeek(month,day);
                bt.setText(date);
                createExampleList();
                buildRecyclerView(view);
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realSubject = new ArrayList<>();
                for (int i = fakeSubject.size()-1; i >= 0 ; i--)
                    realSubject.add(fakeSubject.get(i));
                for (int i = 0; i < realSubject.size(); i++){
                    if (p[i]==1 && a[i]==0) {
                        markTheAttendance(realSubject.get(i), "P");
                    }
                    if (p[i]==0 && a[i]==1) {
                        markTheAttendance(realSubject.get(i), "A");
                    }
                }
                Toast.makeText(getContext(),"Attendance Marked",Toast.LENGTH_LONG).show();
                for (int i = 0; i < fakeSubject.size(); i++) {

                    p[i] = 0;
                    a[i] = 0;
                }
            }
        });

        //here
        return view;
    }

                                    /*      FUNCTIONS      */

    public String getDate(int year, int month, int day){
        month = month + 1;
        String d="",m="";
        if (day < 10)
            d = ""+0;
        if (month < 10)
            m = ""+0;
        String date = "" + year + "-" + m + month + "-" + d + day;
        return date;
    }

    public String getDayOfWeek(int month, int day){
        int sum = 0;
        String dayOfWeek;
        for(int k = 0; k < month; k++)
        {
            sum = sum + addMth[k];
        }
        sum = day + sum;
        dayOfWeek = weekDay[sum%7];
        return dayOfWeek;
    }

    public void markTheAttendance(final String course, final String value){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_MARK_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray;
                        progressDialog.dismiss();
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
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Something went Wrong. Try again!!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("course",course);
                params.put("date",date);
                params.put("value",value);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getContext());
        rq.add(stringRequest);
    }


    public void createExampleList(){
        exampleList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_TIME_TABLE_DAY_WISE,
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
                                    exampleList.add(0, new MarkAttendanceItem(sub));
                                    mAdapter.notifyItemInserted(0);
                                    fakeSubject.add(sub);
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
                params.put("dayOfWeek",dayOfWeek);
                params.put("email",email);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getContext());
        rq.add(stringRequest);
    }


    public void buildRecyclerView(View view){
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new MarkAttendanceAdapter(exampleList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MarkAttendanceAdapter.OnItemClickListener() {
            @Override
            public void onPresentClick(int position) {
                if(p[position]==0) {
                    p[position] = 1;
                }
                else {
                    p[position] = 0;
                }
            }
            public void onAbsentClick(int position) {
                if (a[position] == 0) {
                    a[position] = 1;
                } else {
                    a[position] = 0;
                }
            }
        });

    }
}
