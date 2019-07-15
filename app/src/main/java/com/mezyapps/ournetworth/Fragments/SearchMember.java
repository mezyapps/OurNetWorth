package com.mezyapps.ournetworth.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Activities.SplashActivity;
import com.mezyapps.ournetworth.Adapters.MemberAdapter;
import com.mezyapps.ournetworth.MVP.Member;
import com.mezyapps.ournetworth.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchMember extends Fragment {
    @BindView(R.id.searchProductsRecyclerView)
    RecyclerView searchMemberRecyclerView;
    @BindView(R.id.searchEditText)
    EditText searchEditText;
    List<Member> memberList;

    @BindView(R.id.defaultMessage)
    TextView defaultMessage;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_search_member, container, false);
        ButterKnife.bind(this, view);
        defaultMessage.setText("Search Member by Name, Category, Keyword");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("text", editable.toString());
                searchProducts(editable.toString());
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.title.setText("Search");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchProducts(String s) {
        memberList = new ArrayList<>();
        if (s.length() > 0) {
            for (int i = 0; i < SplashActivity.allMembaerData.size(); i++)
                if (SplashActivity.allMembaerData.get(i).getFull_name().toLowerCase().contains(s.toLowerCase().trim()) || SplashActivity.allMembaerData.get(i).getCategory().toLowerCase().contains(s.toLowerCase().trim()) || SplashActivity.allMembaerData.get(i).getBusiness_keyword().toLowerCase().contains(s.toLowerCase().trim())) {
                    memberList.add(SplashActivity.allMembaerData.get(i));
                }
            if (memberList.size() < 1) {
                defaultMessage.setText("Record Not Found");
                defaultMessage.setVisibility(View.VISIBLE);
            } else {
                defaultMessage.setVisibility(View.GONE);
            }
            Log.d("size", memberList.size() + "" + SplashActivity.allMembaerData.size());
        } else {
            memberList = new ArrayList<>();
            defaultMessage.setText("Search Member by Name, Category, Keyword");
            defaultMessage.setVisibility(View.VISIBLE);
        }
        setProductsData();


    }

    private void setProductsData() {
        MemberAdapter memberAdapter;
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        searchMemberRecyclerView.setLayoutManager(gridLayoutManager);
        memberAdapter = new MemberAdapter(getActivity(), memberList);
        searchMemberRecyclerView.setAdapter(memberAdapter);

    }
}
