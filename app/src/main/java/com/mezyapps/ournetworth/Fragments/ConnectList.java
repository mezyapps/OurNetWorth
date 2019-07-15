package com.mezyapps.ournetworth.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.MVP.AllAskResponse;
import com.mezyapps.ournetworth.MVP.MemberWiseAskReponse;
import com.mezyapps.ournetworth.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectList extends Fragment {


    public ConnectList() {
        // Required empty public constructor
    }
    int position;
    RecyclerView recyclerView_connect_list;
    public static List<AllAskResponse> allAskResponses = new ArrayList<>();
    public static List<MemberWiseAskReponse> allAskResponses1 = new ArrayList<>();
   private TextView name, category,mobileno_re,company_re;
    private ImageView imageView_re;
    String tag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_connect_list, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText("Like List");

        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        tag=bundle.getString("tag");

            // get the reference of item view's
            name =view.findViewById(R.id.name_re_connect);
            category =view.findViewById(R.id.category_re_connect);
            mobileno_re =view.findViewById(R.id.mobile_no_re_connect);
            imageView_re =view.findViewById(R.id.profileimage_re_connect);
            company_re=view.findViewById(R.id.company_re_connect);
            if (tag == "W"){
                name.setText(allAskResponses1.get(position).getCb_name());
                category.setText(allAskResponses1.get(position).getCb_category());
                mobileno_re.setText(allAskResponses1.get(position).getCb_mobile());
                company_re.setText(allAskResponses1.get(position).getCb_email());
                try {
                    Picasso.with(getActivity())
                            .load(allAskResponses1.get(position).getCb_image())
                            // .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                            .placeholder(R.drawable.defaultimage)
                            .into(imageView_re);
                } catch (Exception e) {
                    Log.d("exception", e.toString());
                }
            }else {
                name.setText(allAskResponses.get(position).getCb_name());
                category.setText(allAskResponses.get(position).getCb_category());
                mobileno_re.setText(allAskResponses.get(position).getCb_mobile());
                company_re.setText(allAskResponses.get(position).getCb_email());
                try {
                    Picasso.with(getActivity())
                            .load(allAskResponses.get(position).getCb_image())
                            // .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                            .placeholder(R.drawable.defaultimage)
                            .into(imageView_re);
                } catch (Exception e) {
                    Log.d("exception", e.toString());
                }
            }



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }



}
