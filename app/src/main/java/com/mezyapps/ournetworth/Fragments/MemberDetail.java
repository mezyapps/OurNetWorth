package com.mezyapps.ournetworth.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Adapters.MemberwiseAskAdapter;
import com.mezyapps.ournetworth.Adapters.MemberwiseGiveAdapter;
import com.mezyapps.ournetworth.MVP.Member;
import com.mezyapps.ournetworth.MVP.MemberWiseAskReponse;
import com.mezyapps.ournetworth.MVP.MemberWiseGiveReponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;
import com.squareup.picasso.Picasso;

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
public class MemberDetail extends Fragment {


    public MemberDetail() {
        // Required empty public constructor
    }
    public static List<MemberWiseGiveReponse> memberWiseGiveReponses;
    public static List<MemberWiseAskReponse> memberWiseAskReponses;
    public static List<Member> memberList = new ArrayList<>();
    Activity activity;
    int position;
    private TextView name_detail,category_detail,email_detail,mobile_detail,no_give_added,no_ask_added,company_detail;
    ImageView profileimage_detail;
    LinearLayout give_form_layout_detail,ask_form_layout_detail;
    private RecyclerView recyclerView_memeber_give,recyclerView_give_detail_ask;
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_member_detail, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        MainActivity.title.setText("Member Details");
        activity = (Activity) view.getContext();
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        MainActivity.fab.setVisibility(View.GONE);

//        xml object start
        name_detail=view.findViewById(R.id.name_detail);
        category_detail=view.findViewById(R.id.category_detail);
        company_detail=view.findViewById(R.id.company_detail);
        email_detail=view.findViewById(R.id.email_detail);
        mobile_detail=view.findViewById(R.id.mobile_no_detail);
        profileimage_detail=view.findViewById(R.id.profileimage_detail);
        recyclerView_memeber_give=view.findViewById(R.id.recyclerView_give_detail_give);
        recyclerView_give_detail_ask=view.findViewById(R.id.recyclerView_give_detail_ask);
        no_give_added=view.findViewById(R.id.no_give_added);
        no_ask_added=view.findViewById(R.id.no_ask_added);
        give_form_layout_detail=view.findViewById(R.id.give_form_layout_detail);
        ask_form_layout_detail=view.findViewById(R.id.ask_form_layout_detail);
        ask_form_layout_detail.setVisibility(View.GONE);

//                xml object end
        getAllMemberWisegive();

//        set data start
        name_detail.setText(memberList.get(position).getFull_name());
        category_detail.setText(memberList.get(position).getCategory());
        email_detail.setText(memberList.get(position).getEmail());
        mobile_detail.setText(memberList.get(position).getMobile());
        company_detail.setText(memberList.get(position).getCompany_name());
        try {
            Picasso.with(getContext())
                    .load(memberList.get(position).getImage())
                    // .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(profileimage_detail);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
//        end set data
        getAllMemberWiseAsk();


        final TextView give_id_form_detail=view.findViewById(R.id.give_id_form_detail);
        final TextView ask_id_form_detail=view.findViewById(R.id.ask_id_form_detail);
        ask_id_form_detail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                ask_id_form_detail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                give_id_form_detail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                give_form_layout_detail.setVisibility(View.GONE);
                ask_form_layout_detail.setVisibility(View.VISIBLE);
                // Toast.makeText(getActivity(), "ask", Toast.LENGTH_SHORT).show();
            }
        });

        give_id_form_detail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                ask_id_form_detail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                give_id_form_detail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                ask_form_layout_detail.setVisibility(View.GONE);
                give_form_layout_detail.setVisibility(View.VISIBLE);
                // Toast.makeText(getActivity(), "give", Toast.LENGTH_SHORT).show();

            }
        });




        profileimage_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileImageFull profileImageFull = new ProfileImageFull();
                Bundle bundle = new Bundle();
                bundle.putString("url_image", memberList.get(position).getImage());
                profileImageFull.setArguments(bundle);
                ((MainActivity) getActivity()).loadFragment(profileImageFull, true);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }



    public void getAllMemberWisegive() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().getUserWiseGive(memberList.get(position).getUser_id(),
                new Callback<List<MemberWiseGiveReponse>>() {
                    @Override
                    public void success(List<MemberWiseGiveReponse> memberWiseGiveRepons, Response response) {
                        memberWiseGiveReponses = memberWiseGiveRepons;
                        if (memberWiseGiveRepons.get(0).getSuccess().equalsIgnoreCase("false")){
                            no_give_added.setText(memberWiseGiveRepons.get(0).getMessage());
                        }else {
                            setDataInRecyclerView();
                        }
                    pDialog.dismiss();

                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(activity, "connection error", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

    }

    public void getAllMemberWiseAsk() {
        // getting news list data
        Api.getClient().getUserAskGive(memberList.get(position).getUser_id(),
                new Callback<List<MemberWiseAskReponse>>() {
                    @Override
                    public void success(List<MemberWiseAskReponse> memberWiseAskRepons, Response response) {
                        memberWiseAskReponses = memberWiseAskRepons;
                        if (memberWiseAskReponses.get(0).getSuccess().equalsIgnoreCase("false")){
                            no_ask_added.setText(memberWiseAskReponses.get(0).getMessage());
                        }else {
                            setDataInRecyclerViewAsk();
                        }

                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(activity, "connection error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setDataInRecyclerViewAsk() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_give_detail_ask.setLayoutManager(linearLayoutManager);

            MemberwiseAskAdapter memberwiseAskAdapter = new MemberwiseAskAdapter(getActivity(), memberWiseAskReponses);
            recyclerView_give_detail_ask.setAdapter(memberwiseAskAdapter); // set the Adapter to RecyclerView
    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_memeber_give.setLayoutManager(linearLayoutManager);
        // call the constructor of UsersAdapter to send the reference and data to Adapter


            MemberwiseGiveAdapter memberwiseGiveAdapter = new MemberwiseGiveAdapter(getActivity(), memberWiseGiveReponses);

            recyclerView_memeber_give.setAdapter(memberwiseGiveAdapter); // set the Adapter to RecyclerView

      // set the Adapter to RecyclerView
    }
}