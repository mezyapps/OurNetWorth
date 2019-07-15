package com.mezyapps.ournetworth.Fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Adapters.SpinnerBusinessdapter;
import com.mezyapps.ournetworth.MVP.SignUpResponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GiveAndAsk extends Fragment implements AdapterView.OnItemSelectedListener{


    public GiveAndAsk() {
        // Required empty public constructor
    }
    String[] businessNames={"--select--","Whole Seller","Manufacturer","Retailer","Distributor","Service Provider"};
    TextView date_give_tv,date_ask_tv;
    DatePickerDialog datePickerDialog;
    LinearLayout give_form_layout,ask_form_layout;
    private Button give_submit_button,ask_submit_button;
    TextInputEditText contactname_give_ed,category_give_ed,company_give_ed,location_give_ed,strength_give;
    TextInputEditText contactname_ask_ed,category_ask_ed,company_ask_ed;
    String st_business_type_give;
    Spinner business_type_give;
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_give_and_ask, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        MainActivity.title.setText("Add Give And Ask");
        MainActivity.fab.setVisibility(View.GONE);
        give_form_layout=view.findViewById(R.id.give_form_layout);
        ask_form_layout=view.findViewById(R.id.ask_form_layout);
       ask_form_layout.setVisibility(View.GONE);
        final TextView give_id_form=view.findViewById(R.id.give_id_form);
        final TextView ask_id_form=view.findViewById(R.id.ask_id_form);
        ask_id_form.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                ask_id_form.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                give_id_form.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                give_form_layout.setVisibility(View.GONE);
                ask_form_layout.setVisibility(View.VISIBLE);
               // Toast.makeText(getActivity(), "ask", Toast.LENGTH_SHORT).show();
            }
        });

        give_id_form.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                ask_id_form.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                give_id_form.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                ask_form_layout.setVisibility(View.GONE);
                give_form_layout.setVisibility(View.VISIBLE);
               // Toast.makeText(getActivity(), "give", Toast.LENGTH_SHORT).show();

            }
        });




//        get object of form give sstart

        date_give_tv = view.findViewById(R.id.date_give);
        contactname_give_ed=view.findViewById(R.id.contactname_give);
        category_give_ed=view.findViewById(R.id.category_give);
        company_give_ed=view.findViewById(R.id.comanyname_give);
        location_give_ed=view.findViewById(R.id.location_give);
        strength_give=view.findViewById(R.id.strength_give);
        business_type_give=view.findViewById(R.id.business_type_give);



//       end give object

 //        get object of form give sstart

        date_ask_tv = view.findViewById(R.id.date_ask);
        contactname_ask_ed=view.findViewById(R.id.contactname_ask);
        category_ask_ed=view.findViewById(R.id.category_ask);
        company_ask_ed=view.findViewById(R.id.comanyname_ask);


//       end give object





        // perform click event on edit text

        date_give_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        String  monthString=String.valueOf(month+1);
                        String dateString=String.valueOf(dayOfMonth);
                        if (monthString.length() == 1) {
                            monthString = "0" + monthString;
                        }
                        if (dateString.length() == 1) {
                            dateString = "0" + dateString;
                        }
                        strBuf.append(dateString);
                        strBuf.append("-");
                        strBuf.append(monthString);
                        strBuf.append("-");
                        strBuf.append(year);
                        date_give_tv.setText(strBuf.toString());

                    }
                };

                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH);
                int day = now.get(Calendar.DAY_OF_MONTH);
                // DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, onDateSetListener, year, month, day);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, onDateSetListener, year, month, day);
                // Set dialog icon and title.
//                datePickerDialog.setIcon(R.drawable.appiconn);
                datePickerDialog.setTitle("Please select date.");

                // Popup the dialog.
                datePickerDialog.show();
            }
        });

        //  ask date  Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.

        // perform click event on edit text
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todate= dateFormat.format(currentdate());
        String  currentdate = todate.toString(); //here you get current dat
        date_ask_tv.setText(currentdate);
        date_give_tv.setText(currentdate);



        date_ask_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();

                        String  monthString=String.valueOf(month+1);
                        String dateString=String.valueOf(dayOfMonth);
                        if (monthString.length() == 1) {
                            monthString = "0" + monthString;
                        }
                        if (dateString.length() == 1) {
                            dateString = "0" + dateString;
                        }
                        strBuf.append(dateString);
                        strBuf.append("-");
                        strBuf.append(monthString);
                        strBuf.append("-");
                        strBuf.append(year);

                        date_ask_tv.setText(strBuf.toString());

                    }
                };

                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH);
                int day = now.get(Calendar.DAY_OF_MONTH);
                // DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, onDateSetListener, year, month, day);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, onDateSetListener, year, month, day);
                // Set dialog icon and title.
              //  datePickerDialog.setIcon(R.drawable.appiconn);
                datePickerDialog.setTitle("Please select date.");

                // Popup the dialog.
                datePickerDialog.show();
            }
        });





        give_submit_button=view.findViewById(R.id.give_submit_button);
        ask_submit_button=view.findViewById(R.id.ask_submit_button);
        give_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String st_givedate=date_give_tv.getText().toString().trim();
                String st_givecontactanme=contactname_give_ed.getText().toString().trim();
                String st_givecategory=category_give_ed.getText().toString().trim();
                String st_givecomapnyname=company_give_ed.getText().toString().trim();
                String st_givelocation=location_give_ed.getText().toString().trim();
                String st_strength_give=strength_give.getText().toString().trim();
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                // sending gcm token to server
                Api.getClient().givesubmit(MainActivity.userId, st_givedate,st_givecontactanme,st_givecategory,st_givecomapnyname,st_givelocation,st_strength_give,st_business_type_give,"111",
                        new Callback<SignUpResponse>() {
                            @Override
                            public void success(SignUpResponse signUpResponse, Response response) {
                                pDialog.dismiss();
                                if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                                    Toast.makeText(getActivity(), signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    contactname_give_ed.setText("");
                                    category_give_ed.setText("");
                                    company_give_ed.setText("");
                                    location_give_ed.setText("");
                                    strength_give.setText("");
                                    contactname_give_ed.requestFocus();

                                }else {
                                    Toast.makeText(getActivity(), signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                pDialog.dismiss();

                                Log.e("error", error.toString());
                            }
                        });


               // Toast.makeText(getActivity(), "give", Toast.LENGTH_SHORT).show();
            }
        });


//ask submit button action

        ask_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String st_askdate=date_ask_tv.getText().toString().trim();
                String st_askecontactanme=contactname_ask_ed.getText().toString().trim();
                String st_askcategory=category_ask_ed.getText().toString().trim();
                String st_askcomapnyname=company_ask_ed.getText().toString().trim();
                String st_askremarks="-";

                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                // sending gcm token to server
                Api.getClient().asksubmit(MainActivity.userId, st_askdate,st_askecontactanme,st_askcategory,st_askcomapnyname,st_askremarks,"222",
                        new Callback<SignUpResponse>() {
                            @Override
                            public void success(SignUpResponse signUpResponse, Response response) {
                                pDialog.dismiss();
                                if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                                    Toast.makeText(getActivity(), signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    contactname_ask_ed.setText("");
                                    category_ask_ed.setText("");
                                    company_ask_ed.setText("");
                                    contactname_ask_ed.requestFocus();

                                }else {
                                    Toast.makeText(getActivity(), signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                pDialog.dismiss();

                                Log.e("error", error.toString());
                            }
                        });
            }
        });


        //Getting the instance of Spinner and applying OnItemSelectedListener on it

        business_type_give.setOnItemSelectedListener(this);

        SpinnerBusinessdapter spinnerBusinessdapter=new SpinnerBusinessdapter(getActivity(),businessNames);
        business_type_give.setAdapter(spinnerBusinessdapter);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
      //  Toast.makeText(getActivity(), businessNames[position], Toast.LENGTH_LONG).show();
        st_business_type_give= businessNames[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private Date currentdate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }
}