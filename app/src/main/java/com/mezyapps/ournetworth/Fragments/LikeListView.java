package com.mezyapps.ournetworth.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Adapters.LikeListAdapter;
import com.mezyapps.ournetworth.MVP.AllGiveResponse;
import com.mezyapps.ournetworth.MVP.LikeListResponse;
import com.mezyapps.ournetworth.MVP.MemberWiseGiveReponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeListView extends Fragment {


    public LikeListView() {
        // Required empty public constructor
    }
    public static List<LikeListResponse> likeListResponses;
    public static List<AllGiveResponse> allGiveResponses = new ArrayList<>();
    public static List<MemberWiseGiveReponse> allGiveResponses1 = new ArrayList<>();
    int position;
    TextView no_Like_found;
    RecyclerView recyclerView_Like_list;
    String tag;
    private   Integer gaid = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_like_list_view, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText("Like List");

        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        tag=bundle.getString("tag");
        recyclerView_Like_list=view.findViewById(R.id.recyclerView_Like_list);
        no_Like_found=view.findViewById(R.id.no_Like_found);
        if ( tag == "W"){
            gaid= allGiveResponses1.get(position).getG_a_id();
            getAllLikeList();
        }else {
            gaid= allGiveResponses.get(position).getG_a_id();
        }
        getAllLikeList();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    public void getAllLikeList() {
        // getting news list data
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
              Api.getClient().getLikeListView(gaid,
                new Callback<List<LikeListResponse>>() {
                    @Override
                    public void success(List<LikeListResponse> likeListRespons, Response response) {
                        likeListResponses = likeListRespons;
                        if (likeListResponses.get(0).getSuccess().equalsIgnoreCase("false")){
                            no_Like_found.setText(likeListResponses.get(0).getMessage());
                            pDialog.dismiss();
                        }else {
                            setDataInRecyclerViewAsk();
                            pDialog.dismiss();
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "connection problem", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void setDataInRecyclerViewAsk() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_Like_list.setLayoutManager(linearLayoutManager);

        LikeListAdapter memberwiseAskAdapter = new LikeListAdapter(getActivity(), likeListResponses);
        recyclerView_Like_list.setAdapter(memberwiseAskAdapter); // set the Adapter to RecyclerView

    }
}
