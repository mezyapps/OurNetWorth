package com.mezyapps.ournetworth.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezyapps.ournetworth.Activities.MainActivity;
import com.mezyapps.ournetworth.Config;
import com.mezyapps.ournetworth.Fragments.Home;
import com.mezyapps.ournetworth.Fragments.LikeListView;
import com.mezyapps.ournetworth.MVP.LikeResponse;
import com.mezyapps.ournetworth.MVP.MemberWiseGiveReponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MemberwiseGiveAdapter  extends RecyclerView.Adapter<MemberwiseGiveAdapter.MemberGivViewHolder> {

    Context context;
    List<MemberWiseGiveReponse> memberWiseGiveReponses;

    public MemberwiseGiveAdapter(Context context, List<MemberWiseGiveReponse> memberList) {
        this.memberWiseGiveReponses = memberList;
        this.context = context;
    }

    @Override
    public MemberwiseGiveAdapter.MemberGivViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.member_wise_give_item, null);
        MemberwiseGiveAdapter.MemberGivViewHolder memberViewHolder = new MemberwiseGiveAdapter.MemberGivViewHolder(view);
        return memberViewHolder;
    }

    @Override
    public void onBindViewHolder(final MemberwiseGiveAdapter.MemberGivViewHolder holder, final int position) {
        // set the data

            holder.contact_name.setText(memberWiseGiveReponses.get(position).getContact_name());
            holder.category.setText(memberWiseGiveReponses.get(position).getCategory());
            holder.comanyaneme.setText(memberWiseGiveReponses.get(position).getCompany_name());
            holder.contactby.setText("By: " + memberWiseGiveReponses.get(position).getFull_name());
            holder.tiame_date_give.setText(memberWiseGiveReponses.get(position).getDay().substring(0, 3) + " " + memberWiseGiveReponses.get(position).getDate_time());
            holder.likecount_give_w.setText(String.valueOf(memberWiseGiveReponses.get(position).getLike_count()));
            holder.like_button_give.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // display a toast with user name
                    if (!Home.swipeRefreshLayout.isRefreshing()) {
                        updateLike(holder, position);
                    }
                }
            });
            holder.likecount_give_w.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Home.swipeRefreshLayout.isRefreshing()) {

                        LikeListView.allGiveResponses1.clear();
                        LikeListView.allGiveResponses1.addAll(memberWiseGiveReponses);

                        LikeListView likeListView = new LikeListView();
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        bundle.putString("tag", "W");
                        likeListView.setArguments(bundle);
                        ((MainActivity) context).loadFragment(likeListView, true);
                    }
                }
            });

        }

    @Override
    public int getItemCount() {

        return memberWiseGiveReponses.size(); // size of the list items
    }

    class MemberGivViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
       private TextView contact_name, category,comanyaneme,contactby,tiame_date_give,likecount_give_w;
       ImageView like_button_give;
        public MemberGivViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            contact_name =itemView.findViewById(R.id.contact_name_member_wise_give);
            comanyaneme =itemView.findViewById(R.id.comany_name_member_wise_give);
            category =itemView.findViewById(R.id.category_member_wise_give);
            contactby =itemView.findViewById(R.id.contact_by_member_wise_give);
            tiame_date_give=itemView.findViewById(R.id.tiame_date_give);
            likecount_give_w=itemView.findViewById(R.id.likecount_give_w);
            like_button_give=itemView.findViewById(R.id.like_button_give);
        }
    }
    public void updateLike(final MemberwiseGiveAdapter.MemberGivViewHolder holder, final int position) {


        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient().likeGiveProcess(String.valueOf(memberWiseGiveReponses.get(position).getG_a_id()),
                String.valueOf(memberWiseGiveReponses.get(position).getUser_id()),
                MainActivity.userId,
                new Callback<LikeResponse>() {
                    @Override
                    public void success(LikeResponse addToWishlistResponse, Response response) {

                        progressDialog.dismiss();
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
//
                            //     ((MainActivity) context).loadFragment(new Give(), false);

                            holder.likecount_give_w.setText(String.valueOf(addToWishlistResponse.getLike_count()));
                            Config.showCustomAlertDialog(context,
                                    "You Liked "+memberWiseGiveReponses.get(position).getContact_name(),
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.SUCCESS_TYPE);
                        }else if (addToWishlistResponse.getSuccess().equalsIgnoreCase("false")){
                            holder.likecount_give_w.setText(String.valueOf(addToWishlistResponse.getLike_count()));
                            //    ((MainActivity) context).loadFragment(new Give(), false);
                            Config.showCustomAlertDialog(context,
                                    "You Unliked "+memberWiseGiveReponses.get(position).getContact_name(),
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }
                        else {
                            Config.showCustomAlertDialog(context,
                                    "Your Give",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });

    }
}
