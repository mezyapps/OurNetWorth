package com.mezyapps.ournetworth.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Adapters.AllAskAdapter;
import com.mezyapps.ournetworth.MVP.AllAskResponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.List;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Ask extends Fragment {


    public Ask() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView_ask;

    public static List<AllAskResponse> allAskResponses;
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.fab.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ask, container, false);
        recyclerView_ask=view.findViewById(R.id.recyclerView_ask_all);
        getAllMemberWiseAsk();

        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        MainActivity.title.setText("Ask");



        RecyclerView.LayoutManager layoutManager = new  LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_ask.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void getAllMemberWiseAsk() {
        // getting news list data
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().getAllAsk(new Callback<List<AllAskResponse>>() {
            @Override
            public void success(List<AllAskResponse> allGiveRespons, Response response) {

                allAskResponses = allGiveRespons;
                if (allGiveRespons.get(0).getSuccess().equalsIgnoreCase("false")){
                    Toast.makeText(getActivity(), allGiveRespons.get(0).getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    setDataInRecyclerViewAsk();
                }
                pDialog.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });

    }
    private void setDataInRecyclerViewAsk() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_ask.setLayoutManager(linearLayoutManager);
        // call the constructor of UsersAdapter to send the reference and data to Adapter
        AllAskAdapter usersAdapter = new AllAskAdapter(getActivity(), allAskResponses);
        recyclerView_ask.setAdapter(usersAdapter); // set the Adapter to RecyclerView
    }
}