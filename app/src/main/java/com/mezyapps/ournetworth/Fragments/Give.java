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
import com.mezyapps.ournetworth.Adapters.AllGiveAdapter;
import com.mezyapps.ournetworth.MVP.AllGiveResponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.List;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Give extends Fragment {


    public Give() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView_give;
    private int mScrollY;
    public static List<AllGiveResponse> allGiveResponses;
    @SuppressLint("RestrictedApi")
    AllGiveAdapter usersAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.fab.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_give, container, false);
        recyclerView_give=view.findViewById(R.id.recyclerView_give_all);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        MainActivity.title.setText("ALL Give List");
        getAllMemberWisegive();

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void getAllMemberWisegive() {
        // getting news list data

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().getAllGive(new Callback<List<AllGiveResponse>>() {
                    @Override
                    public void success(List<AllGiveResponse> allGiveRespons, Response response) {

                        allGiveResponses = allGiveRespons;
                        if (allGiveRespons.get(0).getSuccess().equalsIgnoreCase("false")){
                            Toast.makeText(getActivity(), allGiveRespons.get(0).getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            setDataInRecyclerViewgiv();
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
    private void setDataInRecyclerViewgiv() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_give.setLayoutManager(linearLayoutManager);
        // call the constructor of UsersAdapter to send the reference and data to Adapter
         usersAdapter = new AllGiveAdapter(getActivity(), allGiveResponses);
        recyclerView_give.setAdapter(usersAdapter); // set the Adapter to RecyclerView


    }

}