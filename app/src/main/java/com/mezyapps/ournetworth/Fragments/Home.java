package com.mezyapps.ournetworth.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Activities.SplashActivity;
import com.mezyapps.ournetworth.Adapters.MemberAdapter;
import com.mezyapps.ournetworth.DetectConnection;
import com.mezyapps.ournetworth.MVP.Member;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.ContentValues.TAG;

public class Home extends Fragment {
    @BindString(R.string.app_name)
    String app_name;
    View view;
    public static NestedScrollView nestedScrollView;
    public static SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    List<Member> userListResponseData;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText(app_name);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        MainActivity.fab.setVisibility(View.VISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);

        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                 //   MainActivity.searchLayout.setVisibility(View.GONE);


                    getMemberList();


                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView =view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

       setDataInRecyclerView();


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainActivity.profilelogo.setVisibility(View.VISIBLE);

        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainActivity.drawerLayout.closeDrawers();
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (MainActivity.searchLayout.getVisibility() == View.VISIBLE) {
                    if (scrollY > oldScrollY) {
                        Log.i(TAG, "Scroll DOWN");
                       // hideToolbar();
                       // showToolbar();
                    }
                    if (scrollY < oldScrollY) {
                        Log.i(TAG, "Scroll UP");
                        showToolbar();
                    }

                    if (scrollY == 0) {
                        Log.i(TAG, "TOP SCROLL");
                        showToolbar();

                    }
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        Log.i(TAG, "BOTTOM SCROLL");
                       // hideToolbar();
                        //showToolbar();
                    }
                } else
                    nestedScrollView.setNestedScrollingEnabled(false);
            }
        });

    }

    public void showToolbar() {
        MainActivity.toolbarContainer.clearAnimation();
        MainActivity.toolbarContainer
                .animate()
                .translationY(0)
                .start();

    }

    private void hideToolbar() {
        MainActivity.toolbarContainer.clearAnimation();
        MainActivity.toolbarContainer
                .animate()
                .translationY(-MainActivity.toolbar.getBottom())
                .alpha(1.0f)
                .start();
    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of UsersAdapter to send the reference and data to Adapter
        MemberAdapter memberAdapter = new MemberAdapter(getActivity(), SplashActivity.allMembaerData);
        recyclerView.setAdapter(memberAdapter); // set the Adapter to RecyclerView


    }
    private void getMemberList() {
        // getting news list data
        Api.getClient().getAllMember(new Callback<List<Member>>() {
            @Override
            public void success(List<Member> allMember, Response response) {
                SplashActivity.allMembaerData.clear();
                SplashActivity.allMembaerData.addAll(allMember);
                swipeRefreshLayout.setRefreshing(false);
               // MainActivity.searchLayout.setVisibility(View.VISIBLE);
                if (allMember.get(0).getSuccess().equalsIgnoreCase("false")){
                    Toast.makeText(getActivity(), allMember.get(0).getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    setDataInRecyclerView();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
                MainActivity.searchLayout.setVisibility(View.VISIBLE);
            }
        });
    }

}
